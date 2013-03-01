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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.theme.webwidgets.DefaultWidget;
import org.nuxeo.theme.webwidgets.Provider;
import org.nuxeo.theme.webwidgets.ProviderException;
import org.nuxeo.theme.webwidgets.Widget;
import org.nuxeo.theme.webwidgets.WidgetData;
import org.nuxeo.theme.webwidgets.WidgetState;

public class DefaultProvider implements Provider {

    private static final Log log = LogFactory.getLog(DefaultProvider.class);

    private static final String PROVIDER_SESSION_ID = "org.nuxeo.theme.webwidgets.default_provider_session";

    @Override
    public void activate() {
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void destroy() {
        HttpSession httpSession = WebEngine.getActiveContext().getRequest().getSession();
        DefaultProviderSession session = (DefaultProviderSession) httpSession.getAttribute(PROVIDER_SESSION_ID);
        if (session != null) {
            session.clear();
            httpSession.removeAttribute(PROVIDER_SESSION_ID);
        }
    }

    public DefaultProviderSession getDefaultProviderSession() {
        HttpSession httpSession = WebEngine.getActiveContext().getRequest().getSession();
        DefaultProviderSession session = (DefaultProviderSession) httpSession.getAttribute(PROVIDER_SESSION_ID);
        if (session == null) {
            session = new DefaultProviderSession();
            httpSession.setAttribute(PROVIDER_SESSION_ID, session);
        }
        return session;
    }

    @Override
    public Widget createWidget(String widgetTypeName) throws ProviderException {
        if (widgetTypeName == null) {
            throw new ProviderException("Widget type name is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        int counter = session.getCounter();
        final String uid = Integer.toString(counter);
        counter++;
        session.setCounter(counter);
        final Widget widget = new DefaultWidget(widgetTypeName, uid);
        session.getWidgetsByUid().put(uid, widget);
        log.debug("Created web widget '" + widgetTypeName + "' (uid " + uid
                + ")");
        return widget;
    }

    @Override
    public Widget getWidgetByUid(String uid) throws ProviderException {
        DefaultProviderSession session = getDefaultProviderSession();
        Widget widget = session.getWidgetsByUid().get(uid);
        if (widget == null) {
            throw new ProviderException("Widget not found: " + uid);
        }
        return widget;
    }

    @Override
    public List<Widget> getWidgets(String regionName) throws ProviderException {
        if (regionName == null) {
            throw new ProviderException("Region name is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        return session.getWidgetsByRegion().get(regionName);
    }

    @Override
    public List<Widget> addWidget(Widget widget, String regionName, int order)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (regionName == null) {
            throw new ProviderException("Region name is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        Map<String, List<Widget>> widgetsByRegion = session.getWidgetsByRegion();
        if (!widgetsByRegion.containsKey(regionName)) {
            widgetsByRegion.put(regionName, new ArrayList<Widget>());
        }
        List<Widget> region = widgetsByRegion.get(regionName);
        region.add(order, widget);
        session.getRegionsByUid().put(widget.getUid(), regionName);
        log.debug("Added web widget '" + widget.getName() + "' (uid "
                + widget.getUid() + ") into region '" + regionName
                + "' at position " + order);
        return region;
    }

    @Override
    public List<Widget> moveWidget(Widget widget, String destRegionName, int order)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (destRegionName == null) {
            throw new ProviderException("Destination region name is undefined.");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        final String srcRegionName = getRegionOfWidget(widget);
        Map<String, List<Widget>> widgetsByRegion = session.getWidgetsByRegion();
        List<Widget> region = widgetsByRegion.get(srcRegionName);
        region.remove(widget);
        if (!widgetsByRegion.containsKey(destRegionName)) {
            widgetsByRegion.put(destRegionName, new ArrayList<Widget>());
        }
        widgetsByRegion.get(destRegionName).add(order, widget);
        session.getRegionsByUid().put(widget.getUid(), destRegionName);
        log.debug("Moved web widget '" + widget.getName() + "' (uid "
                + widget.getUid() + ") from region '" + srcRegionName
                + "' to '" + destRegionName + "' at position " + order);
        return region;
    }

    @Override
    public List<Widget> reorderWidget(Widget widget, int order)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        final String regionName = getRegionOfWidget(widget);
        Map<String, List<Widget>> widgetsByRegion = session.getWidgetsByRegion();
        final List<Widget> widgets = widgetsByRegion.get(regionName);
        widgets.remove(widget);
        widgets.add(order, widget);
        log.debug("Reordered web widget '" + widget.getName() + "' (uid "
                + widget.getUid() + ") in region '" + regionName
                + "' to position " + order);
        return widgets;
    }

    @Override
    public List<Widget> removeWidget(Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        final String uid = widget.getUid();
        final String regionName = getRegionOfWidget(widget);
        Map<String, List<Widget>> widgetsByRegion = session.getWidgetsByRegion();
        List<Widget> region = widgetsByRegion.get(regionName);
        region.remove(widget);
        session.getWidgetsByUid().remove(uid);
        log.debug("Removed web widget '" + widget.getName() + "' (uid " + uid
                + ") from region '" + regionName + "'");
        return region;
    }

    @Override
    public String getRegionOfWidget(Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        return session.getRegionsByUid().get(widget.getUid());
    }

    @Override
    public Map<String, String> getWidgetPreferences(Widget widget)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        return session.getPreferencesByWidget().get(widget);
    }

    @Override
    public void setWidgetPreferences(Widget widget,
            Map<String, String> preferences) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (preferences == null) {
            throw new ProviderException("Widget preferences are undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        session.getPreferencesByWidget().put(widget, preferences);
    }

    @Override
    public void setWidgetState(Widget widget, WidgetState state)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (state == null) {
            throw new ProviderException("Widget state is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        session.getStatesByWidget().put(widget, state);
    }

    @Override
    public WidgetState getWidgetState(Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        return session.getStatesByWidget().get(widget);
    }

    @Override
    public WidgetData getWidgetData(Widget widget, String dataName)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (dataName == null || "".equals(dataName)) {
            throw new ProviderException("Data name is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        Map<Widget, Map<String, WidgetData>> dataByWidget = session.getDataByWidget();
        if (dataByWidget.containsKey(widget)) {
            return dataByWidget.get(widget).get(dataName);
        }
        return null;
    }

    @Override
    public void setWidgetData(Widget widget, String dataName, WidgetData data)
            throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        if (dataName == null || "".equals(dataName)) {
            throw new ProviderException("Data name is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        Map<Widget, Map<String, WidgetData>> dataByWidget = session.getDataByWidget();
        if (!dataByWidget.containsKey(widget)) {
            dataByWidget.put(widget, new HashMap<String, WidgetData>());
        }
        dataByWidget.get(widget).put(dataName, data);
    }

    @Override
    public void deleteWidgetData(Widget widget) throws ProviderException {
        if (widget == null) {
            throw new ProviderException("Widget is undefined");
        }
        DefaultProviderSession session = getDefaultProviderSession();
        Map<Widget, Map<String, WidgetData>> dataByWidget = session.getDataByWidget();
        if (dataByWidget.containsKey(widget)) {
            dataByWidget.remove(widget);
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
        return true;
    }

}
