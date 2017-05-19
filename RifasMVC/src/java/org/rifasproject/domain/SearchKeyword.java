/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.util.Date;

/**
 *
 * @author char0n
 */
public class SearchKeyword {

    private Long id;
    private String keyword;
    private Date firstSeen;
    private Date lastSeen;
    private int searchCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(Date firstSeen) {
        this.firstSeen = firstSeen;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }
}