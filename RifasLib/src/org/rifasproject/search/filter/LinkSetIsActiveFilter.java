/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.filter;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.RangeFilter;
import org.apache.lucene.util.OpenBitSet;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

/**
 *
 * @author char0n
 */
public class LinkSetIsActiveFilter extends Filter {

    private Boolean active;

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter(this.active);
        return key;
    }

    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
        OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());

        TermDocs termDocs = reader.termDocs(new Term("isChecked", "1"));
        while ( termDocs.next() ) {
            bitSet.set( termDocs.doc() );
        }

        if (this.active == true) {
            RangeFilter range = null;
            range    = RangeFilter.More("linkSetSizeBytes", "0");
            bitSet.and((OpenBitSet) range.getDocIdSet(reader));
        } else {
            TermDocs termDocs1 = reader.termDocs(new Term("linkSetSizeBytes", "0"));
            OpenBitSet set1 = new OpenBitSet(reader.maxDoc());
            while ( termDocs1.next() ) {
                set1.set(termDocs1.doc());
            }
            bitSet.and(set1);
        }

        return bitSet;
    }
}