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

package org.nuxeo.theme.test.editor.filters;

import junit.framework.TestCase;

import org.nuxeo.theme.editor.UndoBuffer;

public class TestUndoBuffer extends TestCase {

    @Override
    public void setUp() {
    }

    public void testUndoBuffer() {
        final String themeSrc = "file:////some/file.xml";
        UndoBuffer    undoBuffer = new UndoBuffer(themeSrc);
        undoBuffer.setHistoryLength(2);
        undoBuffer.save("<theme>version 1</theme>");
        
        assertEquals(undoBuffer.size(), 1);
        assertEquals("<theme>version 1</theme>", undoBuffer.getHistory(1).getSource());
        assertNull(undoBuffer.getHistory(2));
        undoBuffer.save("<theme>version 2</theme>");
        
        assertEquals(undoBuffer.size(), 2);
        assertEquals("<theme>version 2</theme>", undoBuffer.getHistory(1).getSource());
        assertEquals("<theme>version 1</theme>", undoBuffer.getHistory(2).getSource());
        assertNull(undoBuffer.getHistory(3));
        
        undoBuffer.save("<theme>version 3</theme>");
        assertEquals(undoBuffer.size(), 2);
        assertEquals("<theme>version 3</theme>", undoBuffer.getHistory(1).getSource());
        assertEquals("<theme>version 2</theme>", undoBuffer.getHistory(2).getSource());
        assertNull(undoBuffer.getHistory(3));
    }

}
