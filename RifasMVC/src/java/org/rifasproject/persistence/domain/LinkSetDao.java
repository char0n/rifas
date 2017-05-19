/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.persistence.domain;

import java.util.List;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.services.HibernateResult;
import org.rifasproject.util.LinkSetSortOrder;
import org.rifasproject.util.ResultSetFilter;
import org.rifasproject.util.SearchSortUtil;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class LinkSetDao extends AbstractHibernateSpringDao<LinkSet, Long> {

    private FullTextQuery prepareFullTextQuery(String phrase, LinkSetType type, InternetStorage storage, ResultSetFilter filter, int sortBy, LinkSetSortOrder order) throws DaoException {
        FullTextSession fulltextSession;
        FullTextQuery fulltextQuery;
        Query query;

        try {
            fulltextSession = this.getCurrentFulltextSession();
            String[] fields = new String[]{"name", "description", "tags.binder", "links.url", "pages.title", "pages.url"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, fulltextSession.getSearchFactory().getAnalyzer("defaultAnalyzer"));
            query = parser.parse(phrase);

            fulltextQuery = fulltextSession.createFullTextQuery(query, LinkSet.class);
            fulltextQuery.setCacheable(true);
            // LinkSetType filter
            if (!type.equals(LinkSetType.UNSPECIFIED)) {
                fulltextQuery.enableFullTextFilter("linkSetType").setParameter("type", type);
            }
            // Checked filter
            //fulltextQuery.enableFullTextFilter("linkSetIsChecked").setParameter("checked", checked);

            // Results filter
            if (filter.equals(ResultSetFilter.CHECKED)) {
                fulltextQuery.enableFullTextFilter("linkSetIsChecked").setParameter("checked", true);
            } else if (filter.equals(ResultSetFilter.UNCHECKED)) {
                fulltextQuery.enableFullTextFilter("linkSetIsChecked").setParameter("checked", false);
            } else if (filter.equals(ResultSetFilter.ACTIVE)) {
                fulltextQuery.enableFullTextFilter("linkSetIsActive").setParameter("active", true);
            } else if (filter.equals(ResultSetFilter.DELETED)) {
                fulltextQuery.enableFullTextFilter("linkSetIsActive").setParameter("active", false);
            }

            // Sorting
            if (sortBy != SearchSortUtil.getDefaultSortId()) {
                Sort sort = new Sort(new SortField(SearchSortUtil.getFieldNameById(sortBy), SearchSortUtil.getSortTypeById(sortBy), order.value()));
                fulltextQuery.setSort(sort);
            }            

            fulltextQuery.enableFullTextFilter("linkSetStorage").setParameter("storage", storage);
        } catch (ParseException ex) {
            throw new DaoException("Error searching for LinkSet objects", ex);
        } catch (Exception ex) {
            throw new DaoException("Error searching for LinkSet objects", ex);
        }


        return fulltextQuery;
    }

    public HibernateResult search(String phrase, LinkSetType type, InternetStorage storage, ResultSetFilter filter, int sortBy, LinkSetSortOrder order, int firstResult, int maxResult) throws DaoException
    {
        List<LinkSet> linkSets = null;
        int resultSize         = 0;

        try {
            FullTextQuery fulltextQuery = this.prepareFullTextQuery(phrase, type, storage, filter, sortBy, order);
            fulltextQuery.setFirstResult(firstResult);
            fulltextQuery.setMaxResults(maxResult);
            linkSets   = fulltextQuery.list();
            resultSize = fulltextQuery.getResultSize();
        } catch (Exception ex) { 
            throw new DaoException("Error searching LinkSets in lucene index", ex);
        }


        return new HibernateResult<LinkSet, Integer>(linkSets, resultSize);
    }

    public LinkSet getLinkSetByUuid(String UUID) throws DaoException {
        org.hibernate.Query query = null;
        try {
            Session session = this.getCurrentSession();
            query = session.createQuery("from LinkSet l WHERE l.uuid=:uuid");
            query.setString("uuid", UUID);
            query.setCacheable(true);
        } catch (Exception ex) {
            throw new DaoException("Error getting LinkSet object by uuid", ex);
        }

        return (LinkSet) query.uniqueResult();
    }

    public void delete(LinkSet l, boolean deleteCommentsBefore) {
        if (deleteCommentsBefore == true) {
            this.getCurrentSession().createQuery("delete from Comment where itemId=:linkSetId").setParameter("linkSetId", l.getId()).executeUpdate();            
        }
        this.getCurrentSession().delete(l);        
    }

    @Override
    public void delete(LinkSet l) {
        this.delete(l, true);
    }

    public void deleteById(Long id, boolean deleteCommentsBefore) {
        if (deleteCommentsBefore == true) {
            this.getCurrentSession().createQuery("delete from Comment where itemId=:linkSetId").setParameter("linkSetId", id).executeUpdate();
        }
        LinkSet l = this.load(id);
        this.delete(l, false);
    }

    @Override
    public void deleteById(Long id) {
        this.deleteById(id, true);
    }
}
