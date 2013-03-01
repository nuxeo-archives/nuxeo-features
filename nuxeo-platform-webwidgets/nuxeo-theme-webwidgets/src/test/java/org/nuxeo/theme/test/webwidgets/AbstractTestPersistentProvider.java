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

package org.nuxeo.theme.test.webwidgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.runtime.test.NXRuntimeTestCase;
import org.nuxeo.theme.webwidgets.ProviderException;
import org.nuxeo.theme.webwidgets.Widget;
import org.nuxeo.theme.webwidgets.WidgetData;
import org.nuxeo.theme.webwidgets.WidgetState;
import org.nuxeo.theme.webwidgets.providers.PersistentProvider;

public abstract class AbstractTestPersistentProvider extends NXRuntimeTestCase {

    protected PersistentProvider provider;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        deployContrib("org.nuxeo.ecm.core.persistence",
                "OSGI-INF/persistence-service.xml");
        deployContrib("org.nuxeo.theme.test.webwidgets",
                "webwidgets-contrib.xml");

        provider = newProvider();
    }

    protected abstract PersistentProvider newProvider();

    @Override
    @After
    public void tearDown() throws Exception {
        try {
            provider.destroy();
            provider.deactivate();
            provider = null;
        } finally {
            super.tearDown();
        }
    }

    @Test
    public void testCreateWidget() throws ProviderException {
        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget 2");

        assertEquals("test widget", widget1.getName());
        assertEquals("test widget 2", widget2.getName());
    }

    @Test
    public void testGetWidgetByUid() throws ProviderException {
        String name1 = "test widget";
        String name2 = "test widget 2";

        Widget widget1 = provider.createWidget(name1);
        Widget widget2 = provider.createWidget(name2);
        String uid1 = widget1.getUid();
        String uid2 = widget2.getUid();

        assertEquals(widget1, provider.getWidgetByUid(uid1));
        assertEquals(widget2, provider.getWidgetByUid(uid2));
    }

    @Test
    public void testAddAndGetWidgets() throws ProviderException {
        List<Widget> regionA = Collections.emptyList();
        List<Widget> regionB = Collections.emptyList();
        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget 2");
        regionA = provider.addWidget(widget1, "region A", 0);
        regionB = provider.addWidget(widget2, "region B", 0);
        assertEquals("region A", provider.getRegionOfWidget(widget1));
        assertEquals("region B", provider.getRegionOfWidget(widget2));
        assertTrue(regionA.contains(widget1));
        assertTrue(regionB.contains(widget2));
        assertFalse(regionA.contains(widget2));
        assertFalse(regionB.contains(widget1));

        assertEquals(0, regionA.indexOf(widget1));
        assertEquals(0, regionB.indexOf(widget2));

        Widget widget3 = provider.createWidget("test widget 2");
        regionA = provider.addWidget(widget3, "region A", 1);
        assertEquals(1, regionA.indexOf(widget3));
    }

    @Test
    public void testReorderWidget() throws ProviderException {
        List<Widget> regionA = Collections.emptyList();
        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget");
        Widget widget3 = provider.createWidget("test widget");
        regionA = provider.addWidget(widget1, "region A", 0);
        assertEquals(0, regionA.indexOf(widget1));
        regionA = provider.addWidget(widget2, "region A", 1);
        assertEquals(1, regionA.indexOf(widget2));
        regionA = provider.addWidget(widget3, "region A", 2);
        assertEquals(2, regionA.indexOf(widget3));

        regionA = provider.reorderWidget(widget2, 0);
        assertEquals(0, regionA.indexOf(widget2));
        assertEquals(1, regionA.indexOf(widget1));
        assertEquals(2, regionA.indexOf(widget3));

        regionA = provider.reorderWidget(widget3, 1);
        assertEquals(0, regionA.indexOf(widget2));
        assertEquals(1, regionA.indexOf(widget3));
        assertEquals(2, regionA.indexOf(widget1));

        regionA = provider.reorderWidget(widget2, 2);
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(1, regionA.indexOf(widget1));
        assertEquals(2, regionA.indexOf(widget2));

        regionA = provider.reorderWidget(widget3, 2);
        assertEquals(0, regionA.indexOf(widget1));
        assertEquals(1, regionA.indexOf(widget2));
        assertEquals(2, regionA.indexOf(widget3));
    }

    @Test
    public void testRemoveWidget() throws ProviderException {
        List<Widget> regionA = Collections.emptyList();
        Widget widget1 = provider.createWidget("remove test widget");
        Widget widget2 = provider.createWidget("remove test widget");
        Widget widget3 = provider.createWidget("remove test widget");
        regionA = provider.addWidget(widget1, "remove region A", 0);
        assertEquals(0, regionA.indexOf(widget1));
        regionA = provider.addWidget(widget2, "remove region A", 1);
        assertEquals(1, regionA.indexOf(widget2));
        regionA = provider.addWidget(widget3, "remove region A", 2);
        assertEquals(2, regionA.indexOf(widget3));


        regionA = provider.removeWidget(widget2);
        assertEquals(0, regionA.indexOf(widget1));
        assertEquals(1, regionA.indexOf(widget3));

        regionA = provider.removeWidget(widget1);
        assertEquals(0, regionA.indexOf(widget3));

        regionA = provider.removeWidget(widget3);
        assertTrue(regionA.isEmpty());
    }

    @Test
    public void testMoveWidget() throws ProviderException {
        List<Widget> regionA = Collections.emptyList();
        List<Widget> regionB = Collections.emptyList();

        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget");
        Widget widget3 = provider.createWidget("test widget");
        regionA = provider.addWidget(widget1, "region A", 0);
        regionA = provider.addWidget(widget2, "region A", 1);
        regionA = provider.addWidget(widget3, "region A", 2);

        assertEquals(0, regionA.indexOf(widget1));
        assertEquals(1, regionA.indexOf(widget2));
        assertEquals(2, regionA.indexOf(widget3));

        regionA = provider.moveWidget(widget1, "region A", 1);
        assertEquals(0, regionA.indexOf(widget2));
        assertEquals(1, regionA.indexOf(widget1));
        assertEquals(2, regionA.indexOf(widget3));

        regionA = provider.moveWidget(widget3, "region A", 0);
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(1, regionA.indexOf(widget2));
        assertEquals(2, regionA.indexOf(widget1));

        regionA = provider.moveWidget(widget3, "region A", 0);
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(1, regionA.indexOf(widget2));
        assertEquals(2, regionA.indexOf(widget1));

        regionA = provider.moveWidget(widget2, "region A", 2);
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(1, regionA.indexOf(widget1));
        assertEquals(2, regionA.indexOf(widget2));

        regionB = provider.moveWidget(widget1, "region B", 0);
        regionA = provider.getWidgets("region A");
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(1, regionA.indexOf(widget2));
        assertEquals(0, regionB.indexOf(widget1));

        regionB = provider.moveWidget(widget2, "region B", 0);
        assertEquals(0, regionA.indexOf(widget3));
        assertEquals(0, regionB.indexOf(widget2));
        assertEquals(1, regionB.indexOf(widget1));
    }

    @Test
    public void testState() throws ProviderException {
        Widget widget = provider.createWidget("test widget");
        provider.setWidgetState(widget, WidgetState.DEFAULT);
        assertEquals(WidgetState.DEFAULT, provider.getWidgetState(widget));
        provider.setWidgetState(widget, WidgetState.SHADED);
        assertEquals(WidgetState.SHADED, provider.getWidgetState(widget));
    }

    @Test
    public void testPreferences() throws ProviderException {
        Widget widget = provider.createWidget("test widget");
        Map<String, String> preferences = new HashMap<String, String>();
        preferences.put("key1", "value 1");
        preferences.put("key2", "value 2");
        preferences.put("key3", "value 3");
        provider.setWidgetPreferences(widget, preferences);
        Map<String, String> retrievedPreferences = provider.getWidgetPreferences(widget);
        assertEquals("value 1", retrievedPreferences.get("key1"));
        assertEquals("value 2", retrievedPreferences.get("key2"));
        assertEquals("value 3", retrievedPreferences.get("key3"));
    }

    @Test
    public void testWidgetData() throws ProviderException {
        Widget widget = provider.createWidget("test widget");

        String content = "<FILE CONTENT>";

        WidgetData data = new WidgetData("image/png", "image.png",
                content.getBytes());
        String dataName = "src";
        provider.setWidgetData(widget, dataName, data);

        WidgetData retrievedData = provider.getWidgetData(widget, dataName);
        assertEquals("image/png", retrievedData.getContentType());
        assertEquals("image.png", retrievedData.getFilename());
        assertEquals(content, new String(retrievedData.getContent()));

        assertNull(provider.getWidgetData(widget, "unknown"));

        provider.deleteWidgetData(widget);
        assertNull(provider.getWidgetData(widget, dataName));
    }

    @Test
    public void testReorderWidgets() throws ProviderException {
        List<Widget> region = Collections.emptyList();
        Widget widget1 = provider.createWidget("test widget 1");
        Widget widget2 = provider.createWidget("test widget 2");
        Widget widget3 = provider.createWidget("test widget 3");
        region = provider.addWidget(widget1, "region A", 0);
        assertEquals(0, region.indexOf(widget1));
        region = provider.addWidget(widget2, "region A", 1);
        assertEquals(1, region.indexOf(widget2));
        region = provider.addWidget(widget3, "region A", 2);
        assertEquals(2, region.indexOf(widget3));

        region = provider.reorderWidget(widget1, 1);

        assertEquals(0, region.indexOf(widget2));
        assertEquals(1, region.indexOf(widget1));
        assertEquals(2, region.indexOf(widget3));

    }
}
