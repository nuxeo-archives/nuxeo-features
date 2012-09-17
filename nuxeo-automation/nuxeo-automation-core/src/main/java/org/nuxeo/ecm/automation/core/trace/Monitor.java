/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 */
package org.nuxeo.ecm.automation.core.trace;

import java.util.Map;
import java.util.Stack;

import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.nuxeo.ecm.automation.OperationCallback;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.automation.core.impl.InvokableMethod;

public class Monitor implements OperationCallback {

    Simon root = SimonManager.getRootSimon();
    
    protected final Stack<Split> splits = new Stack<Split>();
    
    @Override
    public void onChain(OperationContext context, OperationChain chain) {
        Stopwatch sw = SimonManager.getStopwatch("org.nuxeo.automation.chain."+chain.getId());
        sw.setAttribute("chain", chain);
        splits.push(sw.start());
    }

    @Override
    public void onOperation(OperationContext context, OperationType type,
            InvokableMethod method, Map<String, Object> parms) {
        Split parent = splits.peek();
        String name = parent.getStopwatch().getName().concat(type.getId());
        Stopwatch watch = SimonManager.getStopwatch(name);
        splits.push(watch.start());
    }

    @Override
    public void onError(OperationException error) {
        Split split = splits.pop();
        split.stop();
        split.getStopwatch().setAttribute("error", error);
    }

    @Override
    public void onOutput(Object output) {
        Split split = splits.pop();
        split.stop();
        split.getStopwatch().setAttribute("output", output);
    }

}
