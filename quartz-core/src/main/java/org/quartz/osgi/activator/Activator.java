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

package org.quartz.osgi.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceRegistration;
import org.quartz.osgi.JobServiceTracker;


/**
 *
 * The <code>Activator</code> is the Quartz bundle activator which register
 * {@link JobServiceTracker} for tracking whole registered Job services
 *
 * @author S.T
 */
public class Activator implements BundleActivator {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private static BundleContext bundleContext = null;
    private static JobServiceTracker jobServiceTracker;
    private ServiceRegistration<JobServiceTracker> serviceRegistration;


    /**
     *
     * @return bundleContext of Quartz bundle
     */
    public static BundleContext getContext() {
        return bundleContext;
    }

    /**
     *
     * @return JobServiceTracker is the tracker of registered Job services
     */
    public static JobServiceTracker getJobServiceTracker() {
        return jobServiceTracker;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Called when Quartz bundle is started so the OSGi Framework can perform the
     * bundle-specific activities necessary to start Quartz bundle.
     *
     * <p>
     * This method must complete and return to its caller in a timely manner.
     *
     * @param context The execution context of the bundle being started.
     * @throws Exception If this method throws an exception, this bundle is
     *         marked as stopped and the Framework will remove this bundle's
     *         listeners, unregister all services registered by this bundle, and
     *         release all services used by this bundle.
     */
    @Override
    public void start(BundleContext context) throws Exception {
        Activator.bundleContext = context;
        Filter filter = context.createFilter("(objectClass=*)");
        jobServiceTracker = new JobServiceTracker(context, filter, null);

        // tracking all services
        jobServiceTracker.open(true);
        serviceRegistration = bundleContext.registerService(JobServiceTracker.class, jobServiceTracker, null);
    }

    /**
     * Called when Quartz bundle is stopped so the OSGi Framework can perform the
     * bundle-specific activities necessary to stop the bundle. In general, this
     * method should undo the work that the {@code BundleActivator.start} method
     * started. There should be no active threads that were started by this
     * bundle when this bundle returns. A stopped bundle must not call any
     * OSGi Framework objects.
     *
     * <p>
     * This method must complete and return to its caller in a timely manner.
     *
     * @param context The execution context of the bundle being stopped.
     * @throws Exception If this method throws an exception, the bundle is still
     *         marked as stopped, and the Framework will remove the bundle's
     *         listeners, unregister all services registered by the bundle, and
     *         release all services used by the bundle.
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        if(serviceRegistration != null) {
            serviceRegistration.unregister();
            serviceRegistration = null;
            jobServiceTracker.close();
            jobServiceTracker = null;
        }

        Activator.bundleContext = null;
    }
}
