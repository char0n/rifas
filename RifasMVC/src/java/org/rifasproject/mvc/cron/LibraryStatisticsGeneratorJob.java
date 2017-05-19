/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rifasproject.services.ServiceException;
import org.rifasproject.services.StatsService;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author char0n
 */
public class LibraryStatisticsGeneratorJob extends QuartzJobBean {

    private StatsService statsService;
    private static final Logger log = Logger.getLogger(LibraryStatisticsGeneratorJob.class);

    public void setStatsService(StatsService service) {
        this.statsService = service;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Generating Library statistics");
            this.statsService.generateLibraryStatistics(true);
        } catch (ServiceException ex) {
            log.warn("Error while running "+this.getClass().getName(), ex);
            throw new JobExecutionException("Error while running "+this.getClass().getName(), ex);
        }
    }
}