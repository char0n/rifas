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
public class PeriodicDeletedLinkSetArchiverJob extends QuartzJobBean {

    private static Logger log = Logger.getLogger(PeriodicDeletedLinkSetArchiverJob.class);
    private MaintenanceService maintenanceService;

    public void setMaintenanceService(MaintenanceService service) {
        this.maintenanceService = service;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Archiving deleted LinkSets");
            final int BATCH_SIZE = 100;
            int firstResult = 0;
            int processed   = 0;
            while (true) {
                processed = this.maintenanceService.archiveDeletedLinkSets(firstResult, BATCH_SIZE);
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
