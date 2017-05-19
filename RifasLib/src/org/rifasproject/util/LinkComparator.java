/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rifasproject.domain.Link;

/**
 *
 * @author char0n
 */
public class LinkComparator implements Comparator<Link>
{
    private RegexRepository regexRepository;
    private static final int EQUAL  = 0;
    private static final int BEFORE = -1;
    private static final int AFTER  = 1;

    public LinkComparator(RegexRepository repository)
    {
        this.regexRepository = repository;
    }

    @Override
    public int compare(Link o1, Link o2) {

        if (o1 == o2) return LinkComparator.EQUAL;

        Pattern p = Pattern.compile(regexRepository.regex());
        Matcher m = p.matcher("");

        m.reset(o1.getUrl().toString());
        m.find();
        String linkDesc1 = m.group(2);
        m.reset(o2.getUrl().toString());
        m.find();
        String linkDesc2 = m.group(2);

        return linkDesc1.compareTo(linkDesc2);
    }
}