/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.services.CommentsService;
import org.rifasproject.services.ServiceException;
import org.rifasproject.util.validation.CommentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/comments/*")
public class CommentsController {

    @Autowired private CommentsService commentsService;
    @Autowired private CommentValidator commentValidator;

    @RequestMapping(value="/comments/commentForm.htm", method=RequestMethod.POST)
    public ModelAndView commentForm(@RequestParam("itemId") Long itemId, @RequestParam("owner") int owner) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemId", itemId);
        mav.addObject("owner" , owner);
        return mav;
    }

    @RequestMapping(value="/comments/replyForm.htm", method=RequestMethod.POST)
    public ModelAndView replyForm(@RequestParam("itemId") Long itemId, @RequestParam("owner") int owner, @RequestParam("commentId") Long commentId) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemId"     , itemId);
        mav.addObject("owner"      , owner);
        mav.addObject("commentId"  , commentId);
        return mav;
    }

    @RequestMapping(value="/comments/addComment.htm", method=RequestMethod.POST)
    public ModelAndView addComment(@RequestParam("author") String author,
                                   @RequestParam("email") String email,
                                   @RequestParam("web") String web,
                                   @RequestParam("message") String message,
                                   @RequestParam("itemId") Long itemId,
                                   @RequestParam("owner") int owner,
                                   HttpServletRequest request) throws ServiceException, JSONException {
                                   
        
        email         = (email.trim().equals("")) ? null : email.trim();
        web           = (web.trim().equals(""))   ? null : web.trim();
        Long parentId = (request.getParameter("parentId") != null) ? Long.valueOf(request.getParameter("parentId")) : null;

        // Getting comment owner
        CommentOwner cOwner = null;
        for (CommentOwner o : CommentOwner.values()) {
            if (o.ordinal() == owner) {
                cOwner = o;
            }
        } if (cOwner == null) throw new ServiceException("Unknown comment owner");

        Comment c = new Comment();
        c.setAuthor(author);
        c.setEmail(email);
        c.setWeb(web);
        c.setMessage(message);
        c.setOwner(cOwner);
        c.setCreated(new Date());
        c.setItemId(itemId);

        ValidationResult commentValResult = this.commentValidator.validateForSave(c);

        if (commentValResult.isValid() == true) {
            if (parentId != null) {
                this.commentsService.addParentToComment(c, parentId);
            }
            this.commentsService.addCommentForItem(c, itemId, cOwner);            
        }

        ModelAndView mav = new ModelAndView();
        JSONObject json  = new JSONObject();
        try {            
            if (commentValResult.isValid() == true) {
                json.append("status", "OK");
            } else {
                json.append("status", "KO");
                json.append("error" , commentValResult.getBrokenRules().get(0).getErrorDescription());
            }
            mav.addObject("json", json.toString(1));
        } catch (JSONException ex) { }

        return mav;
    }

    @RequestMapping(value="/comments/getComments.htm", method=RequestMethod.POST)
    public ModelAndView getComments(@RequestParam(value="itemId", required=true) Long itemId, @RequestParam(value="owner", required=true) int owner) throws ServiceException {
        ModelAndView mav           = new ModelAndView();

        // Getting comment owner
        CommentOwner cOwner = null;
        for (CommentOwner o : CommentOwner.values()) {
            if (o.ordinal() == owner) {
                cOwner = o;
            }
        } if (cOwner == null) throw new ServiceException("Unknown comment owner");

        Map<String, Object> result                  = this.commentsService.getComments(itemId, cOwner);
        Set<Comment> comments                       = (Set<Comment>) result.get("comments");
        Map<Comment, Integer> commentsRefs          = (Map<Comment, Integer>) result.get("commentsRefs");
        Map<Comment, List<Integer>> commentsReplies = (Map<Comment, List<Integer>>) result.get("commentsReplies");


        mav.addObject("comments"       , comments);
        mav.addObject("commentsRefs"   , commentsRefs);
        mav.addObject("commentsReplies", commentsReplies);
        mav.addObject("itemId"         , itemId);
        mav.addObject("owner"          , owner);

        return mav;
    }
}