var exportLink;

function showProgressIndicator(level) {

    var linkSetLevel = document.getElementById("deepScanLevel");
    linkSetLevel.innerHTML = level;

    Effect.toggle("deepScanProgress", "appear");
    Effect.toggle("deepScanLevels"  , "appear");

    doDIS(level);
}
function doDIS(level) {

   var form   = document.forms.searchBar;
   var params = $(form).serialize();

   new Ajax.Request(contextPath+'/linksets/dis/'+level.toString().toLowerCase(), {

                            method: 'post',
                            parameters: params,
                            onSuccess: function(transport)
                            {
                                var response = eval('('+transport.responseText+')');
                                if (response.status == 'OK') {
                                    var url = window.location.href;
                                    url = url.replace(new RegExp("(\/page\/[0-9]+|#)$", "g"), "");
                                    window.location.href = url;
                                } else {
                                    alert("DSC was unsucesfull to scan the internet");
                                }
                            },                            
                            onFailure: function(){ alert('Something went wrong...') }
                   }); 

}
function showHiddenLinks(id) {
    var moreLinksRow = document.getElementById("moreLinksRow"+id);
    var linkTable    = document.getElementById(id);
    var rows         = linkTable.getElementsByTagName('tr');

    for(var i=0; i < rows.length; i++) {
        rows[i].style.display = "table-row";
    }
    moreLinksRow.style.display = "none";
}
function checkLinkSet(uuid) {    
    var params = {uuid: uuid};
    new Ajax.Request(contextPath+'/linksets/check/', {
                            method: 'post',
                            parameters: params,
                            onSuccess: function(transport)
                            {
                                var response = eval('('+transport.responseText+')');
                                if (response.status == 'OK') {
                                    window.location.href = window.location;
                                } else {
                                    alert("RIFAS was unsuccesfull to check the LinkSet");
                                }
                            },
                            onFailure: function(){ alert('Something went wrong...') }
                   });
}
function switchTab(action, param1, param2) {
    var rows         = document.getElementById("linkSetTabs").getElementsByTagName('li');
    var current      = document.getElementById(action);

    for(var i=0; i < rows.length; i++) {
        rows[i].className = "";
    }
    current.className = "active";

    var container = document.getElementById("container");
    var buffer = document.getElementById("buffer");
    if (action == 'export') {
        if (buffer.innerHTML != '') {
            container.innerHTML = buffer.innerHTML;
            buffer.innerHTML = '';
        }
    } else if (action == 'comments') {
        if (buffer.innerHTML == '') {
            buffer.innerHTML = container.innerHTML;
        }
        getComments(container, param1, param2);
    } else if (action == 'checker') {
        if (buffer.innerHTML == '') {
            buffer.innerHTML = container.innerHTML;
        }

        container.innerHTML  = '<h2>Checking LinkSet</h2><br />';
        container.appendChild($('linkSetCheck'));
        Effect.toggle('linkSetCheck', 'appear');
        checkLinkSet(param1);
    }
}
function automaticDIS() {
    showProgressIndicator('Level1');
}
function exportOpenpaste(uuid) {
    new Ajax.Request(contextPath+'/linksets/export/'+uuid+'/to/openpaste', {
                                method: 'post',
                                onLoading: function() {
                                    $('progress').show();
                                },
                                onSuccess: function(transport) {
                                    $('progress').hide();
                                    $('exportForm').show();
                                    document.forms.exportForm.exportURL.value = transport.responseText;
                                    document.forms.exportButtonForm.exportButton.disabled = true;
                                },
                                onFailure: function(){ alert('Something went wrong...') }
                       });
}
function openExportLink() {
    window.open(document.forms.exportForm.exportURL.value,'_blank').blur();
}