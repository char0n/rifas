/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.services.ArchiveService;
import org.rifasproject.services.CommentsService;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/archive/*")
public class ArchiveController {

    @Autowired private ArchiveService archiveService;
    @Autowired private LinkSetService linkSetService;
    @Autowired private CommentsService commentsService;

    @RequestMapping("/archive/show.htm")
    public ModelAndView show(@RequestParam(value="uuid", required=true) String uuid) throws ServiceException {

        ArchivedLinkSet al = this.archiveService.getArchivedLinkSetByUUID(uuid);

        BaseModelAndView mav = new BaseModelAndView();
        String title    = (al.getName() != null) ? al.getName() : al.getUuid();
        title    = title+" - Archived";
        title    = title+" "+al.getStorage().toString().substring(0, 1).toUpperCase()+al.getStorage().toString().substring(1).toLowerCase();
        title    = title+" LinkSet";
        String desc     = title;
        String keywords = (al.getName() != null) ? al.getName().toLowerCase().replaceAll("\\s+", ", ")+", archived linkset" : "archived linkset";
        mav.addObject("archivedLinkSet", al);
        mav.setPageTitle(title);
        mav.setPageDescription(desc);
        mav.setPageKeywords(keywords);


        // Related LinkSets
        List<LinkSet> related;
        Long[] relatedIds;
        Map<Long, List<Link>> relatedLinks = null;

        related    = this.linkSetService.getRelatedLinkSets(al, 0, 10);
        if (related.size() > 0) {
            relatedIds = new Long[related.size()];
            int index = 0;
            for (LinkSet l : related) {
                relatedIds[index++] = l.getId();
            }
            relatedLinks = this.linkSetService.getLinksByLinkSetIds(relatedIds);
        }

        Map<String, Object> result                  = this.commentsService.getComments(al.getId(), CommentOwner.ARCHIVE);
        Set<Comment> comments                       = (Set<Comment>) result.get("comments");
        Map<Comment, Integer> commentsRefs          = (Map<Comment, Integer>) result.get("commentsRefs");
        Map<Comment, List<Integer>> commentsReplies = (Map<Comment, List<Integer>>) result.get("commentsReplies");


        mav.addObject("comments"       , comments);
        mav.addObject("commentsRefs"   , commentsRefs);
        mav.addObject("commentsReplies", commentsReplies);        
        mav.addObject("relatedLinkSets", related);
        mav.addObject("relatedLinks"   , relatedLinks);
        mav.addObject("itemId"         , al.getId());
        mav.addObject("owner"          , CommentOwner.ARCHIVE.ordinal());

        return mav;
    }
}
