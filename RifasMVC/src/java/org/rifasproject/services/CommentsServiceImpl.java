/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.domain.LinkSet;

/**
 *
 * @author char0n
 */
public class CommentsServiceImpl extends AbstractCommentsService {

    @Override
    public boolean saveComment(Comment c) throws ServiceException {
        try {
            this.commentsDao.saveOrUpdate(c);
        } catch (Exception ex) {
            throw new ServiceException("Error saving Comment object", ex);
        }

        return true;
    }

    @Override
    public Comment getCommentById(Long id) throws ServiceException {
        Comment c = null;
        try {
            c = this.commentsDao.load(id);
        } catch (Exception ex) {
            throw new ServiceException("Error loading Comment object by Id", ex);
        }

        return c;
    }

    @Override
    public void addCommentForItem(Comment c, Long itemId, CommentOwner owner) throws ServiceException {
        try {
            switch (owner) {
                case LINKSET:
                    LinkSet l = this.linkSetDao.get(itemId);
                    this.linkSetService.addCommentToLinkSet(c, l);
                    break;
                case ARCHIVE:
                    ArchivedLinkSet al = this.archiveDao.get(itemId);
                    this.archiveService.addCommentToArchivedLinkSet(c, al);
                    break;
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public Comment addParentToComment(Comment c, Long parentId) throws ServiceException {
        Comment c1;
        try {
            c1 = this.commentsDao.load(parentId);
            c.setParent(c1);
        } catch (Exception ex) {
            throw new ServiceException("Error adding parent for Comment", ex);
        }

        return c;
    }

    @Override
    public Map<String, Object> getComments(Long itemId, CommentOwner owner) throws ServiceException {
        Map<String, Object> toReturn = new HashMap<String, Object>();
        Set<Comment> comments = null;
        try {
            switch (owner) {
                case LINKSET:
                    LinkSet l = this.linkSetDao.get(itemId);
                    comments  = l.getComments();
                    break;
                case ARCHIVE:
                    ArchivedLinkSet al = this.archiveDao.get(itemId);
                    comments = al.getComments();
                    break;
            }
            Map<Comment, Integer> commentsRefs          = new HashMap<Comment, Integer>();
            Map<Comment, List<Integer>> commentsReplies = new HashMap<Comment, List<Integer>>();
            int i = 1;
            for (Comment comment : comments) {
                commentsRefs.put(comment, i++);
                if (comment.getParent() != null) {
                    if (!commentsReplies.containsKey(comment.getParent())) commentsReplies.put(comment.getParent(), new ArrayList<Integer>());
                    commentsReplies.get(comment.getParent()).add(commentsRefs.get(comment));
                }
            }

            toReturn.put("comments"       , comments);
            toReturn.put("commentsRefs"   , commentsRefs);
            toReturn.put("commentsReplies", commentsReplies);

        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

        return toReturn;
    }
}
