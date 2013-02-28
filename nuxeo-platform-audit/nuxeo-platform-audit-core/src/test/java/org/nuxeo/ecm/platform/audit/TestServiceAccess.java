/*
 * (C) Copyright 2006-2009 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.audit.api.AuditLogger;
import org.nuxeo.ecm.platform.audit.api.AuditReader;
import org.nuxeo.ecm.platform.audit.api.NXAuditEvents;
import org.nuxeo.ecm.platform.audit.service.NXAuditEventsService;
import org.nuxeo.ecm.platform.audit.service.extension.AdapterDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

@SuppressWarnings("deprecation")
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({ "org.nuxeo.ecm.core.persistence", "org.nuxeo.ecm.platform.audit.api", "org.nuxeo.ecm.platform.audit" })
@LocalDeploy({ "org.nuxeo.ecm.platform.audit:nxaudit-tests.xml", "org.nuxeo.ecm.platform.audit:test-audit-contrib.xml"})
public class TestServiceAccess  {


    @Test
    public void testFullAccess() {
        NXAuditEvents fullService = Framework.getLocalService(NXAuditEvents.class);
        assertNotNull(fullService);

        if (!(fullService instanceof NXAuditEventsService)) {
            fail("");
        }
    }

    @Test
    public void testReadAccess() {
        AuditReader reader = Framework.getLocalService(AuditReader.class);
        assertNotNull(reader);

        if (!(reader instanceof NXAuditEventsService)) {
            fail("");
        }
    }

    @Test
    public void testWriteAccess() {
        AuditLogger writer = Framework.getLocalService(AuditLogger.class);
        assertNotNull(writer);

        if (!(writer instanceof NXAuditEventsService)) {
            fail("");
        }
    }

    @Test
    public void testAuditContribution() throws Exception {
        NXAuditEventsService auditService = (NXAuditEventsService) Framework.getLocalService(NXAuditEvents.class);
        assertNotNull(auditService);
        AdapterDescriptor[] registeredAdapters = auditService.getRegisteredAdapters();
        assertEquals(1, registeredAdapters.length);

        AdapterDescriptor ad = registeredAdapters[0];
        assertEquals("myadapter", ad.getName());

    }
}
