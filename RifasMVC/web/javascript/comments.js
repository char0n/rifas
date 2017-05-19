/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var addCommentLink;
var replyCommentsLinks = new Array();

function getComments(container, itemId, owner) {
    new Ajax.Request(contextPath+'/comments/getcomments/for/'+itemId+'/'+owner, {
                            method: 'post',
                            onSuccess: function(transport)
                            {
                                container.innerHTML = transport.responseText;
                            },
                            onFailure: function(){ alert('Something went wrong...') }
                   });
}
function showAddCommentModalWindow() {
    if (!addCommentLink) {
        addCommentLink = new lightbox($('addCommentLink'));
    }
    addCommentLink.activate();
}
function showReplyCommentModalWindow(commentId) {
    if (!replyCommentsLinks[commentId]) {
        replyCommentsLinks[commentId] = new lightbox($('addReplyLink'+commentId));
    }
    replyCommentsLinks[commentId].activate();
}
function addComment(itemId, owner) {
   var form   = document.forms.commentForm;
   var params = $(form).serialize();


    new Ajax.Request(contextPath+'/comments/addcomment/for/'+itemId+'/'+owner, {
                                method: 'post',
                                parameters: params,
                                onSuccess: function(transport)
                                {
                                    var response = eval('('+transport.responseText+')');
                                    if (response.status == "OK") {
                                        lightbox.prototype.deactivate();
                                        if (owner == 0) {
                                            var url = window.document.location.href.replace(/\/comments$/, '');
                                            url = url + "/comments";
                                            window.location.href = url;
                                        } else if (owner == 1) {
                                            window.location.reload();
                                        }
                                    } else {
                                        document.getElementById("commentAddErrors").innerHTML = response.error
                                    }

                                },
                                onFailure: function(){ alert('Something went wrong...') }
                       });

}