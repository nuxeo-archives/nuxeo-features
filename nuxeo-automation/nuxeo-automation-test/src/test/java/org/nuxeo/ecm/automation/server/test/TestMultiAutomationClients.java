/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Julien Carsique <jcarsique@nuxeo.com>
 *     Vladimir Pasquier <vpasquier@nuxeo.com>
 *
 */

package org.nuxeo.ecm.automation.server.test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;
import org.nuxeo.ecm.automation.test.RestFeature;
import org.nuxeo.runtime.test.runner.ParameterizedSuite;
import org.nuxeo.runtime.test.runner.ParameterizedSuite.ParameterizedFeature;

/**
 * Multi Version Automation Clients tests
 * 
 * @since 5.7
 */
@RunWith(ParameterizedSuite.class)
@SuiteClasses(value = { RestTest.class })
@ParameterizedFeature(RestFeature.class)
public class TestMultiAutomationClients {

    protected static final String[] AUTOMATION_CLIENT_VERSIONS_TO_TEST = new String[] { "5.7-SNAPSHOT" };

    private static final String M2_REPO_NUXEO = "/org/nuxeo/";

    private static String m2Repository = null;

    @Parameters
    public static Collection<Object[]> connectClientVersions() throws Exception {
        List<Object[]> testData = new ArrayList<Object[]>();
        for (String version : AUTOMATION_CLIENT_VERSIONS_TO_TEST) {
            URLClassLoader cl = new URLClassLoader(
                    new URL[] { getJAR(version) });
            testData.add(new Object[] { version, cl });
        }
        return testData;
    }

    private static URL getJAR(String version) throws Exception {
        File jar = null;
        if (m2Repository == null) {
            List<String> clf = getClassLoaderFiles();
            for (String f : clf) {
                if (f.contains(M2_REPO_NUXEO)) {
                    m2Repository = f.substring(0, f.indexOf(M2_REPO_NUXEO)
                            + M2_REPO_NUXEO.length());
                    break;
                }
            }
        }
        jar = new File(
                m2Repository,
                String.format(
                        "ecm/automation/nuxeo-automation-client/%s/nuxeo-automation-client-%<s.jar",
                        version));
        if (!jar.exists()) {
            throw new RuntimeException(version
                    + " not found in classloader or local M2 repository");
        }
        return jar.toURI().toURL();
    }

    /**
     * Introspects the classpath and returns the list of files in it. Copied
     * from org.nuxeo.functionaltests.AbstractTest#getClassLoaderFiles()
     * 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws java.net.MalformedURLException
     * @throws java.net.URISyntaxException
     * 
     */
    protected static List<String> getClassLoaderFiles()
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, MalformedURLException,
            URISyntaxException {
        ClassLoader cl = TestMultiAutomationClients.class.getClassLoader();
        URL[] urls = null;
        if (cl instanceof URLClassLoader) {
            urls = ((URLClassLoader) cl).getURLs();
        } else if (cl.getClass().getName().equals(
                "org.apache.tools.ant.AntClassLoader")) {
            Method method = cl.getClass().getMethod("getClasspath");
            String cp = (String) method.invoke(cl);
            String[] paths = cp.split(File.pathSeparator);
            urls = new URL[paths.length];
            for (int i = 0; i < paths.length; i++) {
                urls[i] = new URL("file:" + paths[i]);
            }
        } else {
            System.err.println("Unknown classloader type: "
                    + cl.getClass().getName());
            return null;
        }
        // special case for maven surefire with useManifestOnlyJar
        if (urls.length == 1) {
            try {
                URI uri = urls[0].toURI();
                if (uri.getScheme().equals("file")
                        && uri.getPath().contains("surefirebooter")) {
                    JarFile jar = new JarFile(new File(uri));
                    try {
                        String cp = jar.getManifest().getMainAttributes().getValue(
                                Attributes.Name.CLASS_PATH);
                        if (cp != null) {
                            String[] cpe = cp.split(" ");
                            URL[] newUrls = new URL[cpe.length];
                            for (int i = 0; i < cpe.length; i++) {
                                // Don't need to add 'file:' with maven
                                // surefire >= 2.4.2
                                String newUrl = cpe[i].startsWith("file:") ? cpe[i]
                                        : "file:" + cpe[i];
                                newUrls[i] = new URL(newUrl);
                            }
                            urls = newUrls;
                        }
                    } finally {
                        jar.close();
                    }
                }
            } catch (Exception e) {
                // skip
            }
        }
        List<String> files = new ArrayList<String>(urls.length);
        for (URL url : urls) {
            files.add(url.toURI().getPath());
        }
        return files;
    }

}
