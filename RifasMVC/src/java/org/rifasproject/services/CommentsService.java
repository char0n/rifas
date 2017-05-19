/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Map;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author root
 */
public interface CommentsService {

    public void setCommentsDao(CommentsDao dao);
    public void setLinkSetService(LinkSetService service);
    public void setLinkSetDao(LinkSetDao dao);
    public void setArchiveService(ArchiveService service);
    public void setArchiveDao(ArchiveDao dao);

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public boolean saveComment(Comment c) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Comment getCommentById(Long id) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public void addCommentForItem(Comment c, Long itemId, CommentOwner owner) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Comment addParentToComment(Comment c, Long parentId) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<String, Object> getComments(Long itemId, CommentOwner owner) throws ServiceException;
}