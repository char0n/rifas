/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;

/**
 *
 * @author root
 */
public interface LinkChecker {

    public Link check(Link link) throws LinkCheckerException;
    public LinkSet check(LinkSet linkSet) throws LinkCheckerException;
    public InternetStorage getAssignedStorage();
}
