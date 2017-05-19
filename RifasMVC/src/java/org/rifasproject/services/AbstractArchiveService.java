/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;


import org.rifasproject.persistence.domain.ArchiveDao;

/**
 *
 * @author char0n
 */
abstract public class AbstractArchiveService implements ArchiveService {

    protected ArchiveDao archiveDao;

    @Override
    public void setArchiveDao(ArchiveDao dao) {
        this.archiveDao = dao;
    }
}
