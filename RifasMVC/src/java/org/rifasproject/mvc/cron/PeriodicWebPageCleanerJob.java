/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rifasproject.services.MaintenanceService;
import org.rifasproject.services.ServiceException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author char0n
 */
public class PeriodicWebPageCleanerJob extends QuartzJobBean {

    private static final Logger log = Logger.getLogger(PeriodicWebPageCleanerJob.class);
    private MaintenanceService maintenanceService;

    public void setMaintenanceService(MaintenanceService service) {
        this.maintenanceService = service;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        int BATCH_SIZE = 100;
        int firstResult = 0;
        int processed = 0;
        try {
            log.info("Cleaning unused WebPage objects");
            while (true) {
                processed = this.maintenanceService.cleanUnusedWebpages(firstResult, BATCH_SIZE);
                if (processed == 0) {
                    break;
                }                
            }
        } catch (ServiceException ex) {
            log.warn("Error while running "+this.getClass().getName(), ex);
            throw new JobExecutionException("Error while running "+this.getClass().getName(), ex);
        }
    }
}
