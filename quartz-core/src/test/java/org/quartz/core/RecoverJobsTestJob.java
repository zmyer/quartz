/*
 * Copyright 2013 Terracotta, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.quartz.core;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author https://github.com/eugene-goroschenya
 */
@DisallowConcurrentExecution
public class RecoverJobsTestJob implements Job {
    private static Logger _log = LoggerFactory.getLogger(RecoverJobsTestJob.class);
    static boolean runForever = true;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long now = System.currentTimeMillis();
        int tic = 0;
        _log.info("Started - " + now);
        try {
            while (runForever) {
                Thread.sleep(1000);
                _log.info("Tic " + (++tic) + "- " + now);
            }
            _log.info("Stopped - " + now);
        } catch (InterruptedException e) {
            _log.info("Interrupted - " + now);
        }
    }
}
