Ajax.Responders.register({
  onCreate: function() {
    if($('busy_indicator') && Ajax.activeRequestCount>0)
      Element.show('busy_indicator');
  },
  onComplete: function() {
    if($('busy_indicator') && Ajax.activeRequestCount==0)
      Element.hide('busy_indicator');
  }
});
function send() {
   var form   = document.forms.contactForm;
   var params = $(form).serialize();

   new Ajax.Request(contextPath+'/contact/sendemail', {
                            method: 'post',
                            parameters: params,
                            onSuccess: function(transport)
                            {
                                var response = eval('('+transport.responseText+')');
                                var buffer   = document.getElementById("buffer");
                                if (response.Status == 'OK') {
                                    buffer.innerHTML = "Email sent. Thank You.";
                                } else {
                                    buffer.innerHTML = response.Error;
                                }
                            },
                            onFailure: function(){ alert('Something went wrong...') }
                   });

}