/*
 * (C) Copyright 2006-2009 Nuxeo SAS <http://nuxeo.com> and others
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

package org.nuxeo.theme.editor;

import java.util.ArrayList;
import java.util.List;

public class UndoBuffer {
    
    private String themeSrc;
    
    private List<ThemeVersion> versions;

    public UndoBuffer(String themeSrc) {
        this.themeSrc = themeSrc;
        this.versions = new ArrayList<ThemeVersion>();
    }
    
    public String getThemeSrc() {
        return themeSrc;
    }

    public void setThemeSrc(String themeSrc) {
        this.themeSrc = themeSrc;
    }

    public List<ThemeVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<ThemeVersion> versions) {
        this.versions = versions;
    }
    
    public void save(String source) {
        versions.add(new ThemeVersion(source));
    }

}
