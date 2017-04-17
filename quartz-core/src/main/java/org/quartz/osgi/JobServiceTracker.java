/*
 * Copyright Terracotta, Inc.
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
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>JobServiceTracker</code> is a {@link org.osgi.util.tracker.ServiceTracker} that
 * to track whole registered {@link org.quartz.Job} services.
 *
 * <p>
 * The jobServices is the concurrent hash map facilitates to get an instance of requested job service.
 * </p>
 *
 *  @author S.T
 */

public class JobServiceTracker extends ServiceTracker<Object, Job> {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final BundleContext context;
    private volatile ConcurrentHashMap<String, Job> jobServices = new ConcurrentHashMap<String, Job>();

    public JobServiceTracker(BundleContext context, Filter filter,
                             ServiceTrackerCustomizer<Object, Job> customizer) {
        super(context, filter, customizer);
        this.context = context;
    }

    /**
     *
     * @param clazz is a canonical name class of requested job service
     * @return instance of job service
     */
    Job getJobService(String clazz) {
       return jobServices.get(clazz);
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the added service.
     *
     * @param reference The service reference of the added service.
     * @return The service object to be used by the tracker.
     */
    @Override
    public Job addingService(ServiceReference<Object> reference) {
        Object obj = context.getService(reference);
        if(obj != null) {
            if(obj instanceof Job) {
                log.debug("Service of " + obj.getClass().getCanonicalName() +
                        "add to JobServiceTracker.");
                jobServices.put(obj.getClass().getCanonicalName(), (Job)obj);
                return (Job)obj;
            }
        }

        return null;
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the modified service.
     *
     * @param reference The service reference of the modified service.
     * @param job The service object of the modified service.
     **/
    @Override
    public void modifiedService(ServiceReference<Object> reference, Job job) {
    }

    /**
     * Overrides the <tt>ServiceTracker</tt> functionality to inform the
     * application object about the removed service.
     *
     * @param reference The service reference of the removed service.
     * @param job The service object of the removed service.
     **/
    @Override
    public void removedService(ServiceReference<Object> reference, Job job) {
        log.debug("Service of " + job.getClass().getCanonicalName() +
                "removed from JobServiceTracker.");
        jobServices.remove(job.getClass().getCanonicalName());
    }
}
