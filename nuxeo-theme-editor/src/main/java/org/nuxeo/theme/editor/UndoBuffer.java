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

import org.nuxeo.theme.themes.ThemeException;

public class UndoBuffer {

    private String themeSrc;

    private List<ThemeVersion> versions;

    private int historyLength;

    private static int HISTORY_LENGTH = 10;

    public UndoBuffer(String themeSrc) {
        this.themeSrc = themeSrc;
        this.versions = new ArrayList<ThemeVersion>();
        this.historyLength = HISTORY_LENGTH;
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
        versions.add(0, new ThemeVersion(source));
        int size = size();
        int historyLength = getHistoryLength();
        if (size > historyLength) {
            versions.subList(historyLength, size).clear();
        }
    }
    
    public int getHistoryLength() {
        return historyLength;
    }

    public void setHistoryLength(int historyLength) {
        this.historyLength = historyLength;
    }

    public ThemeVersion getHistory(int position) {
        if (position < 1 || position >  size()) {
            return null;
        }
        return versions.get(position-1);
    }
    
    public ThemeVersion getPreviousVersion() {
        return getHistory(1);
    }
    
    public int size() {
        return versions.size();
    }

}
