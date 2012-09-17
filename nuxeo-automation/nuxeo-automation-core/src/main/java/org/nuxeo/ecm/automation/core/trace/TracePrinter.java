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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.nuxeo.ecm.automation.OperationException;

public class TracePrinter {
    
    protected final BufferedWriter writer;
    
    protected String preamble = "";
    
    public TracePrinter(Writer writer) {
        this.writer = new BufferedWriter(writer);
    }
    
    public TracePrinter(OutputStream out) {
        this(new OutputStreamWriter(out));
    }
    
    protected void printLine(String line) throws IOException {
        writer.write(preamble + line);
    }
    
    protected void printHeading(String heading) throws IOException {
        printLine("****** " + heading + " ******");
    }
    
    public void print(Trace trace) throws IOException {
        printHeading("chain");
        if (trace.error != null) {
            printLine("caught error of type " + trace.error.getClass().getSimpleName());
            printError(trace.error);
        } else {
            printLine("produced output of type " + trace.output.getClass().getSimpleName());
            printObject(trace.output);
        }
        print(trace.operations);
        writer.flush();
    }
    
    public void printError(OperationException error) throws IOException {
        
    }

    public void print(List<Call> calls) throws IOException {
        for (Call call:calls) { 
            print(call);
        }
    }

    public void print(Call call) throws IOException {
        printHeading("operation");
        printObject(call);
    }
    
    public void printObject(Object object) {
        
    }

}