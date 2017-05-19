/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.persistence.domain;

import org.hibernate.Query;
import org.rifasproject.domain.SearchKeyword;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class SearchKeywordDao extends AbstractHibernateSpringDao<SearchKeyword, Long> {

    public SearchKeyword getByKeyword(String keyword) throws DaoException {

        Query query;
        try {
            query = this.getCurrentSession().createQuery("from SearchKeyword as sk where sk.keyword=:keyword");
            query.setString("keyword", keyword);
            query.setCacheable(true);
        } catch (Exception ex) {
            throw new DaoException("Error loading SearchKeyword object by keyword", ex);
        }

        return (SearchKeyword) query.uniqueResult();
    }

}
