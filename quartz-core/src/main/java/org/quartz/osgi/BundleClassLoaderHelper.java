/*
 * Copyright 2001-2016 Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package org.quartz.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.quartz.Job;
import org.quartz.osgi.activator.Activator;
import org.quartz.spi.ClassLoadHelper;

import java.io.InputStream;
import java.net.URL;

/**
 * A <code>BundleClassLoaderHelper</code> that uses either the current bundle
 * class loader (<code>context.getBundle().adapt(BundleWiring.class).getClassLoader()</code>),
 * or bundle class loader of requested class
 *
 *
 * @see org.quartz.spi.ClassLoadHelper
 *
 *
 * @author S.T
 */

public class BundleClassLoaderHelper implements ClassLoadHelper {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private BundleContext context = null;
    private JobServiceTracker jobServiceTracker = null;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Called to give the ClassLoadHelper a chance to initialize itself,
     * including the opportunity to "steal" the class loader off of the calling
     * thread, which is the thread that is initializing Quartz.
     */
    public void initialize() {
        context = Activator.getContext();
        jobServiceTracker = Activator.getJobServiceTracker();
    }

    /**
     * Return the class with the given name.
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Job job;
        if(jobServiceTracker != null)
            if ((job = jobServiceTracker.getJobService(name)) != null)
                return job.getClass();

        throw new ClassNotFoundException();
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> loadClass(String name, Class<T> clazz)
            throws ClassNotFoundException {
        return (Class<? extends T>) loadClass(name);
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.net.URL object
     */
    public URL getResource(String name) {
        return getClassLoader().getResource(name);
    }

    /**
     * Finds a resource with a given name. This method returns null if no
     * resource with this name is found.
     * @param name name of the desired resource
     * @return a java.io.InputStream object
     */
    public InputStream getResourceAsStream(String name) {
        return getClassLoader().getResourceAsStream(name);
    }

    /**
     * Enable sharing of the class-loader with 3rd party.
     *
     * @return the class-loader of quartz bundle
     *      or <code>Thread.currentThread().getContextClassLoader()</code>
     */
    public ClassLoader getClassLoader() {
        if(context != null)
            return context.getBundle().adapt(BundleWiring.class).getClassLoader();

        return Thread.currentThread().getContextClassLoader();
    }
}
