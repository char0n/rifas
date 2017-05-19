/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import java.util.Calendar;
import java.util.Date;
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
public class PeriodicLinkSetRecheckerJob extends QuartzJobBean {

    private static Logger log = Logger.getLogger(PeriodicLinkSetRecheckerJob.class);
    private static final int LINKSETS_TO_RECHECK     = 1;
    private static final int RECHECK_PERIOD_MONTHS   = 2;

    private MaintenanceService maintenanceService;

    public void setMaintenanceService(MaintenanceService service) {
        this.maintenanceService = service;
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.MONTH, -(RECHECK_PERIOD_MONTHS));
       Date maxModDate = cal.getTime();

       try {
            log.info("Rechecking LinkSet items older than "+RECHECK_PERIOD_MONTHS+" months");
            this.maintenanceService.recheckOldLinkSets(maxModDate, 0, LINKSETS_TO_RECHECK);            
       } catch (ServiceException ex) {
           log.warn("Error while running "+this.getClass().getName(), ex);
           throw new JobExecutionException("Error while running "+this.getClass().getName(), ex);
       }
    }
}
