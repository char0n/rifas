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
public class PeriodicLinkSetCheckerJob extends QuartzJobBean {

    private static Logger log = Logger.getLogger(PeriodicLinkSetCheckerJob.class);
    private static final int LINKSETS_TO_CHECK = 1;

    private MaintenanceService maintenanceService;

    public void setMaintenanceService(MaintenanceService service) {
        this.maintenanceService = service;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Checking unchecked LinkSet item");
            this.maintenanceService.checkUncheckedLinkSets(0, LINKSETS_TO_CHECK);
        } catch (ServiceException ex) {
            log.warn("Error while running "+this.getClass().getName(), ex);
            throw new JobExecutionException("Error while running "+this.getClass().getName(), ex);
        }            
    }
}