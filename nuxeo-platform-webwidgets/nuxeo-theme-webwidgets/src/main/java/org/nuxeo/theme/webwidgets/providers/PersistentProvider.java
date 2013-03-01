/*
 * (C) Copyright 2006-2007 Nuxeo SAS <http://nuxeo.com> and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jean-Marc Orliaguet, Chalmers
 *
 * $Id$
 */

package org.nuxeo.theme.webwidgets.providers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.theme.webwidgets.DefaultWidget;
import org.nuxeo.theme.webwidgets.Provider;
import org.nuxeo.theme.webwidgets.ProviderException;
import org.nuxeo.theme.webwidgets.Widget;
import org.nuxeo.theme.webwidgets.WidgetData;
import org.nuxeo.theme.webwidgets.WidgetState;

public class PersistentProvider implements Provider {

    private static final Log log = LogFactory.getLog(PersistentProvider.class);

    protected PersistenceProvider persistenceProvider;

    @Override
    public void activate() {
        PersistenceProviderFactory persistenceProviderFactory = Framework.getLocalService(PersistenceProviderFactory.class);
        persistenceProvider = persistenceProviderFactory.newProvider("nxwebwidgets");
        persistenceProvider.openPersistenceUnit();
    }

    @Override
    public void deactivate() {
        if (persistenceProvider != null) {
            persistenceProvider.closePersistenceUnit();
            persistenceProvider = null;
        }
    }

    @Override
    public void destroy() throws ProviderException {
        try {
            getPersistenceProvider().run(true, new RunVoid() {
                @Override
                public void runWith(EntityManager em) {
                    em.createQuery("DELETE FROM DataEntity data").executeUpdate();
                    for (Object w : em.createQuery("FROM WidgetEntity widget").getResultList()) {
                        em.remove(w);
                    }
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    public PersistenceProvider getPersistenceProvider() {
        if (persistenceProvider == null) {
            activate();
        }
        return persistenceProvider;
    }

    public Principal getCurrentPrincipal() {
        WebContext ctx = WebEngine.getActiveContext();
        if (ctx != null) {
            return ctx.getPrincipal();
        }
        return null;
    }

    @Override
    public List<Widget> addWidget(final Widget widget, final String regionName,
            final int order) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (regionName == null) {
            throw new ProviderException("Region name is undefined");
        }

        try {
            return toDefaults(getPersistenceProvider().run(true, new RunCallback<List<WidgetEntity>>() {
                @Override
                public List<WidgetEntity> runWith(EntityManager em) {
                    return addWidget(em, widget.getUid(), regionName, order);
                }
            }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    protected List<WidgetEntity> addWidget(EntityManager em, String uuid, String dest, int order) {
        WidgetEntity widget = em.find(WidgetEntity.class, Integer.parseInt(uuid));
        List<WidgetEntity> widgets = getWidgets(em, dest);
        widget.setRegion(dest);
        widgets.add(order, widget);
        return reorderWidgets(em, dest, widgets);
    }

    @Override
    public synchronized Widget createWidget(final String widgetTypeName)
            throws ProviderException {
        if (widgetTypeName == null) {
            throw new ProviderException("Widget type name is undefined");
        }

        try {
            return toDefault(getPersistenceProvider().run(true,
                    new RunCallback<WidgetEntity>() {
                        @Override
                        public WidgetEntity runWith(EntityManager em) {
                            final WidgetEntity widget = new WidgetEntity(
                                    widgetTypeName);
                            em.persist(widget);
                            return widget;
                        }
                    }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public String getRegionOfWidget(final Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        try {
            return getPersistenceProvider().run(true,
                    new RunCallback<String>() {
                        @Override
                        public String runWith(EntityManager em) {
                            return em.find(WidgetEntity.class, Integer.parseInt(widget.getUid())).getRegion();
                        }
                    });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public WidgetState getWidgetState(final Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        try {
            return getPersistenceProvider().run(true,
                    new RunCallback<WidgetState>() {
                        @Override
                        public WidgetState runWith(EntityManager em) {
                            return em.find(WidgetEntity.class, Integer.parseInt(widget.getUid())).getState();
                        }
                    });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public synchronized Widget getWidgetByUid(final String uid)
            throws ProviderException {
        WidgetEntity entity;
        try {
            entity = getPersistenceProvider().run(false,
                    new RunCallback<WidgetEntity>() {
                        @Override
                        public WidgetEntity runWith(EntityManager em) {
                            WidgetEntity widget = em.find(WidgetEntity.class,
                                    Integer.valueOf(uid));
                            return widget;
                        }
                    });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

        if (entity == null) {
            throw new ProviderException("Widget not found: " + uid);
        }
        return toDefault(entity);
    }

    @Override
    public synchronized List<Widget> getWidgets(final String regionName)
            throws ProviderException {
        if (regionName == null) {
            throw new ProviderException("Region name is undefined.");
        }

        try {
            return toDefaults(getPersistenceProvider().run(true,
                    new RunCallback<List<WidgetEntity>>() {
                        @Override
                        public List<WidgetEntity> runWith(EntityManager em) {
                            return getWidgets(em, regionName);
                        }
                    }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    @SuppressWarnings("unchecked")
    protected List<WidgetEntity> getWidgets(EntityManager em, String regionName) {
        Query query = em.createNamedQuery("Widget.findAll");
        query.setParameter("region", regionName);
        return query.getResultList();
    }

    @Override
    public List<Widget> moveWidget(final Widget widget, final String destRegionName, final int order)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (destRegionName == null) {
            throw new ProviderException("Destination region name is undefined.");
        }

        try {
            return toDefaults(getPersistenceProvider().run(true,
                    new RunCallback<List<WidgetEntity>>() {
                        @Override
                        public List<WidgetEntity> runWith(EntityManager em) throws ClientException {
                            return moveWidget(em, widget.getUid(), destRegionName, order);
                        }
                    }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
     }

    protected List<WidgetEntity> moveWidget(EntityManager em, String uuid, String dest, int order) {
        WidgetEntity widget = em.find(WidgetEntity.class, Integer.parseInt(uuid));
        String src = widget.getRegion();
        List<WidgetEntity> region = getWidgets(em, src);
        region.remove(widget);
        if (!dest.equals(src)) {
            region = reorderWidgets(em, src, region);
            region = getWidgets(em, dest);
        }
        region.add(order, widget);
        return reorderWidgets(em, dest, region);
    }

    @Override
    public synchronized List<Widget> removeWidget(final Widget widget)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        try {
            return toDefaults(getPersistenceProvider().run(true, new RunCallback<List<WidgetEntity>>() {
                @Override
                public List<WidgetEntity> runWith(EntityManager em) {
                    return removeWidget(em, widget.getUid());
                }
            }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    protected List<WidgetEntity> removeWidget(EntityManager em, String uuid) {
        WidgetEntity widget = em.find(WidgetEntity.class, Integer.parseInt(uuid));
        String name = widget.getRegion();
        List<WidgetEntity> widgets = getWidgets(em, name);
        widgets.remove(widget);
        em.remove(widget);
        return reorderWidgets(em, name, widgets);
    }


    @Override
    public List<Widget> reorderWidget(final Widget widget, final int order)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        try {
            return toDefaults(getPersistenceProvider().run(true, new RunCallback<List<WidgetEntity>>() {
                @Override
                public List<WidgetEntity> runWith(EntityManager em) {
                    return reorderWidget(em, widget.getUid(), order);
                }
            }));
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    protected List<WidgetEntity> reorderWidget(EntityManager em, String uuid, int order) {
        WidgetEntity widget = em.find(WidgetEntity.class, Integer.parseInt(uuid));
        String name = widget.getRegion();
        List<WidgetEntity> widgets = getWidgets(em, name);
        widgets.remove(widget);
        widgets.add(order, widget);
        return reorderWidgets(em, name, widgets);
    }

    protected List<WidgetEntity> reorderWidgets(EntityManager em, String name, List<WidgetEntity> widgets) {
        int order = 1;
        for (WidgetEntity widget:widgets) {
            widget.setOrder(order++);
            widget.setRegion(name);
        }
        return widgets;
    }

    @Override
    public Map<String, String> getWidgetPreferences(final Widget widget)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        try {
            return getPersistenceProvider().run(true, new RunCallback<Map<String,String>>() {
                @Override
                public Map<String,String> runWith(EntityManager em) {
                    WidgetEntity entity = em.find(WidgetEntity.class, Integer.parseInt(widget.getUid()));
                    return entity.getPreferences();
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public void setWidgetPreferences(final Widget widget,
            final Map<String, String> preferences) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (preferences == null) {
            throw new ProviderException("Widget preferences are undefined");
        }

        try {
            getPersistenceProvider().run(true, new RunVoid() {
                @Override
                public void runWith(EntityManager em) {
                    WidgetEntity entity = em.find(WidgetEntity.class, Integer.parseInt(widget.getUid()));
                    entity.setPreferences(preferences);
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    @Override
    public void setWidgetState(final Widget widget, final WidgetState state)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (state == null) {
            throw new ProviderException("Widget state is undefined");
        }

        try {
            getPersistenceProvider().run(true, new RunVoid() {
                @Override
                public void runWith(EntityManager em) {
                    setWidgetState(em, widget.getUid(), state);
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    protected void setWidgetState(EntityManager em, String uid, WidgetState state) {
        WidgetEntity widget = em.find(WidgetEntity.class, Integer.parseInt(uid));
        widget.setState(state);
    }

    @Override
    public synchronized WidgetData getWidgetData(final Widget widget,
            final String dataName) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (dataName == null || "".equals(dataName)) {
            throw new ProviderException("Data name is undefined");
        }

        try {
            return getPersistenceProvider().run(true,
                    new RunCallback<WidgetData>() {
                        @Override
                        public WidgetData runWith(EntityManager em) {
                            return getWidgetData(em, widget.getUid(), dataName);
                        }


                    });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    protected WidgetData getWidgetData(EntityManager em,
            final String uid, final String dataName) {
        Query query = em.createNamedQuery("Data.findByWidgetAndName");
        query.setParameter("widgetUid", uid);
        query.setParameter("dataName", dataName);
        List<?> results = query.getResultList();
        if (results.size() > 0) {
            DataEntity dataEntity = (DataEntity) results.get(0);
            return dataEntity.getData();
        }
        return null;
    }

    @Override
    public synchronized void setWidgetData(final Widget widget,
            final String dataName, final WidgetData data)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (dataName == null || "".equals(dataName)) {
            throw new ProviderException("Data name is undefined");
        }

        try {
            getPersistenceProvider().run(true, new RunVoid() {
                @Override
                public void runWith(EntityManager em) {
                    setWidgetData(em, widget.getUid(), dataName, data);
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    protected void setWidgetData(EntityManager em, String uid, String name, WidgetData data) {
        Query query = em.createNamedQuery("Data.findByWidgetAndName");
        query.setParameter("widgetUid", uid);
        query.setParameter("dataName", name);
        @SuppressWarnings("unchecked")
        List<DataEntity> results = query.getResultList();
        DataEntity entity;
        if (results.size() == 0) {
            entity = new DataEntity(uid, name);
            em.persist(entity);
        }  else {
            entity = results.get(0);
        }
        entity.setData(data);
    }

    @Override
    public synchronized void deleteWidgetData(final Widget widget)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }

        try {
            getPersistenceProvider().run(true, new RunVoid() {
                @Override
                public void runWith(EntityManager em) {
                    deleteWidgetData(em, widget.getUid());
                }
            });
        } catch (ClientException e) {
            throw new ProviderException(e);
        }

    }

    protected void deleteWidgetData(EntityManager em, String uid) {
        Query query = em.createNamedQuery("Data.findByWidget");
        query.setParameter("widgetUid", uid);
        for (Object dataEntity : query.getResultList()) {
            em.remove(dataEntity);
        }
    }

    /*
     * Security
     */
    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public boolean canWrite() {
        Principal currentNuxeoPrincipal = getCurrentPrincipal();
        if (currentNuxeoPrincipal == null) {
            log.warn("Could not get the current user from the context.");
            return false;
        }
        return ((NuxeoPrincipal) currentNuxeoPrincipal).isMemberOf(SecurityConstants.ADMINISTRATORS);
    }

    protected Widget toDefault(WidgetEntity widget) {
        return new DefaultWidget(widget.getName(), widget.getUid());
    }

    protected List<Widget> toDefaults(List<WidgetEntity> widgets) {
        List<Widget> defaults = new ArrayList<Widget>(widgets.size());
        for (WidgetEntity widget:widgets) {
            defaults.add(toDefault(widget));
        }
        return defaults;
    }
}
