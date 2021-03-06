/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Alexandre Russel
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.annotations.descriptors;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.platform.annotations.service.PermissionManager;

/**
 * @author Alexandre Russel
 *
 */
@XObject("permissionManager")
public class PermissionManagerDescriptor {

    @XNode("@class")
    private Class<? extends PermissionManager> klass;

    public Class<? extends PermissionManager> getKlass() {
        return klass;
    }

    public void setKlass(Class<? extends PermissionManager> klass) {
        this.klass = klass;
    }

}
