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
 * Contributors:
 *     ${user}
 */

package org.nuxeo.ecm.automation.core.trace;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationException;

public class Trace {

    public Call getParent() {
        return parent;
    }

    public OperationChain getChain() {
        return chain;
    }

    protected final Call parent;

    protected final OperationChain chain;

    protected final OperationException error;

    protected final Object output;

    protected final List<Call> operations;

    Trace(Call parent, OperationChain chain,
            List<Call> operations) {
        this.parent = parent;
        this.chain = chain;
        this.operations = new ArrayList<Call>(operations);
        this.output = null;
        this.error = null;
    }

    Trace(Call parent, OperationChain chain,
            List<Call> calls, OperationException error) {
        this.parent = parent;
        this.chain = chain;
        this.operations = new ArrayList<Call>(calls);
        this.output = null;
        this.error = error;
    }

    Trace(Call parent, OperationChain chain,
            List<Call> calls, Object output) {
        this.parent = parent;
        this.chain = chain;
        this.operations = new ArrayList<Call>(calls);
        this.output = output;
        this.error = null;
    }

    public OperationException getError() {
        return error;
    }

    public Object getOutput() {
        return output;
    }
    
    public List<Call> getCalls() {
        return operations;
    }
    
    public String getFormattedText() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new TracePrinter(out).print(this);
        } catch (IOException e) {
            LogFactory.getLog(Trace.class).error("Cannot print trace of " + chain.getId(), e);
            return chain.getId();
        }
        return out.toString();
    }
}