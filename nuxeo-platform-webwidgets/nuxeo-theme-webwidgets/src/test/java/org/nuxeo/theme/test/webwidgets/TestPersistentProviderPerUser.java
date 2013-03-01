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
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.ArrayList;

import org.junit.Test;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.impl.UserPrincipal;
import org.nuxeo.theme.webwidgets.ProviderException;
import org.nuxeo.theme.webwidgets.Widget;
import org.nuxeo.theme.webwidgets.providers.PersistentProvider;
import org.nuxeo.theme.webwidgets.providers.PersistentProviderPerUser;

public class TestPersistentProviderPerUser extends AbstractTestPersistentProvider {


    class MockPersistentProvider extends PersistentProviderPerUser {

        Principal currentNuxeoPrincipal;

        public MockPersistentProvider(String name, boolean anonymous) {
            NuxeoPrincipal currentNuxeoPrincipal = new UserPrincipal(name, new ArrayList<String>(), anonymous, false);
            this.currentNuxeoPrincipal = currentNuxeoPrincipal;
        }

        @Override
        public Principal getCurrentPrincipal() {
            return currentNuxeoPrincipal;
        }
    }

    @Override
    protected PersistentProvider newProvider() {
        return new MockPersistentProvider("user1", false);
    }



    @Test
    public void testCreateWidgetUser1() throws ProviderException {
        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget 2");

        assertEquals("test widget", widget1.getName());
        assertEquals("test widget 2", widget2.getName());
    }

    @Test
    public void testAddAndGetWidgetsUser1() throws ProviderException {
        Widget widget1 = provider.createWidget("test widget");
        Widget widget2 = provider.createWidget("test widget 2");
        provider.addWidget(widget1, "region A", 0);
        provider.addWidget(widget2, "region B", 0);
        assertEquals("region A", provider.getRegionOfWidget(widget1));
        assertEquals("region B", provider.getRegionOfWidget(widget2));
        assertTrue(provider.getWidgets("region A").contains(widget1));
        assertTrue(provider.getWidgets("region B").contains(widget2));
        assertFalse(provider.getWidgets("region A").contains(widget2));
        assertFalse(provider.getWidgets("region B").contains(widget1));

        assertEquals(0, provider.getWidgets("region A").indexOf(widget1));
        assertEquals(0, provider.getWidgets("region B").indexOf(widget2));

        Widget widget3 = provider.createWidget("test widget 2");
        provider.addWidget(widget3, "region A", 1);
        assertEquals(1, provider.getWidgets("region A").indexOf(widget3));
    }



}
