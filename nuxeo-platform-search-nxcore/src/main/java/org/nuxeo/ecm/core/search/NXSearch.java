/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 */

package org.nuxeo.ecm.core.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.search.api.client.SearchService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentName;

/**
 * Facade for services provided by NXSearch module.
 *
 * @author <a href="mailto:eionica@nuxeo.com">Eugen Ionica</a>
 */
public final class NXSearch {

    private static final Log log = LogFactory.getLog(NXSearch.class);

    public static final ComponentName NAME = new ComponentName( "org.nuxeo.ecm.core.search.service.SearchServiceCoreImpl");
    // Utility class.
    private NXSearch() {
    }

    /**
     * Returns the local search service.
     * <p>
     * Here, the search service is expected to be on the same node. We need this
     * because of the bean wrapping.
     *
     * @return the search service
     */
    public static SearchService getSearchService() {
        try {
            return (SearchService) Framework.getRuntime().getComponent(NAME);
//            return (SearchService) Framework.getService(SearchService.class);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

}
