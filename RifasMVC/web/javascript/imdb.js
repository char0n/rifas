/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function lazyLoadSubtitlesByImdbId(imdbId) {

   var params = 'imdbId='+imdbId;

   new Ajax.Request(contextPath+'/imdb/searchasync/subtitles', {

                            method: 'post',
                            parameters: params,
                            onSuccess: function(transport)
                            {
                                var response = transport.responseText;
                                $('subtitles').innerHTML = response;
                            },
                            onFailure: function(){ alert('Something went wrong...') }
                        });
}
