/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.List;

/**
 *
 * @author char0n
 */
public class HibernateResult<T, ID> {

    private List<T> results;
    private ID resultSize;

    public HibernateResult(List<T> results, ID resultSize) {
        this.setResults(results);
        this.setResultSize(resultSize);
    }

    public ID getResultSize() {
        return resultSize;
    }

    public void setResultSize(ID resultSize) {
        this.resultSize = resultSize;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
