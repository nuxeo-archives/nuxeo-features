/*
 * (C) Copyright 2009 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Sun Seng David TAN (a.k.a. sunix) <stan@nuxeo.com>
 */
package org.nuxeo.opensocial.container.factory.utils;

import java.io.InputStream;
import java.util.List;

import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.UserPref;
import org.nuxeo.opensocial.gadgets.service.api.GadgetDeclaration;
import org.nuxeo.opensocial.gadgets.service.api.GadgetService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.NXRuntimeTestCase;

/**
 * @author Sun Seng David TAN (a.k.a. sunix) <stan@nuxeo.com>
 * 
 */
public class TestGadgetUtils extends NXRuntimeTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        deployBundle("org.nuxeo.opensocial.gadgets.core");
        // deploying some gadget definition copied from the dashboard
        deployBundle("org.nuxeo.opensocial.dashboard.test");
    }

    /**
     * Testing that we have the gadgets registered for the other tests
     * 
     * @throws Exception
     */
    public void testRegisteredGadget() throws Exception {
        GadgetService gadgetservice = Framework.getService(GadgetService.class);
        GadgetDeclaration gadgetbbcnews = gadgetservice.getGadget("bbcnews");
        assertNotNull(gadgetbbcnews);
        GadgetDeclaration gadgetuserdocuments = gadgetservice.getGadget("userdocuments");
        assertNotNull(gadgetuserdocuments);
        GadgetDeclaration gadgetuserworkspaces = gadgetservice.getGadget("userworkspaces");
        assertNotNull(gadgetuserworkspaces);

    }

    /**
     * Testing the gadget title retrieval from the GadgetSpec used when we add a
     * new gadget in a dashboard for example.
     */
    public void testGadgetGetTitle() throws Exception {
        String titleBbc = GadgetsUtils.getGadgetTitle("bbcnews", "defaultValue");
        assertEquals("BBC News Front Page", titleBbc);
        String titleuserdocuments = GadgetsUtils.getGadgetTitle(
                "userdocuments", "defaultValue");
        assertEquals("My Documents", titleuserdocuments);
        String titleuserworkspace = GadgetsUtils.getGadgetTitle(
                "userworkspaces", "defaultValue");
        assertEquals("My Workspaces", titleuserworkspace);
    }

    /**
     * Testing a gadget spec creation from a dummy spec
     * 
     * @throws Exception
     */
    public void testCreateGadgetSpec() throws Exception {
        InputStream input = getClass().getResourceAsStream("testGadgetSpec.xml");
        GadgetSpec test = GadgetsUtils.createGadgetSpec(input);
        List<UserPref> userPrefs = test.getUserPrefs();
        assertEquals("My Documents", userPrefs.get(0).getDefaultValue());
        assertEquals("title", userPrefs.get(0).getName());
        assertEquals("User documents", test.getModulePrefs().getTitle());
    }

}
