function hideFilterDefs() {
    var list   = $('filterList');
    var active = $('activeFilterDef');
    list.style.display = 'none';
    for (var i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active.innerHTML = '<input type="radio" checked="checked" /> '+list.options[i].text;
            break;
        }
    }
}
function selectActiveFilterInModalWindow() {
    var list   = $('filterList');
    var list1  = document.forms.modalFilterDefs;
    var active;
    var i;
    for (i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active = list.options[i].value;
            break;
        }
    }

    for (i = 0; i < list1.length; i++) {
        if (list1.filter[i].value == active) {
            list1.filter[i].checked = true;
            break;
        }
    }
}
function selectFilterDef() {
    var list   = $('filterList');
    var list1  = document.forms.modalFilterDefs;
    var active;
    var activeOption;
    var i;
    for (i = 0; i < list1.length; i++) {
        if (list1.filter[i].checked == true) {
            active = list1.filter[i].value;
            break;
        }
    }

    for (i = 0; i < list.length; i++) {
       list.options[i].selected = false;
       if (list.options[i].value == active) {
           activeOption = list.options[i];
       }
    }
    activeOption.selected = true;
    $('activeFilterDef').innerHTML = '<input type="radio" checked="checked" /> '+activeOption.text;
    lightbox.prototype.deactivate();
}

function hideStorageDefs() {
    var list   = $('storageList');
    var active = $('activeStorageDef');
    list.style.display = 'none';
    for (var i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active.innerHTML = '<input type="radio" checked="checked" /> '+list.options[i].text;
            break;
        }
    }
}
function selectActiveStorageInModalWindow() {
    var list   = $('storageList');
    var list1  = document.forms.modalStorageDefs;
    var active;
    var i;
    for (i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active = list.options[i].value;
            break;
        }
    }

    for (i = 0; i < list1.length; i++) {
        if (list1.filter[i].value == active) {
            list1.filter[i].checked = true;
            break;
        }
    }
}
function selectStorageDef() {
    var list   = $('storageList');
    var list1  = document.forms.modalStorageDefs;
    var active;
    var activeOption;
    var i;
    for (i = 0; i < list1.length; i++) {
        if (list1.filter[i].checked == true) {
            active = list1.filter[i].value;
            break;
        }
    }

    for (i = 0; i < list.length; i++) {
       list.options[i].selected = false;
       if (list.options[i].value == active) {
           activeOption = list.options[i];
       }
    }
    activeOption.selected = true;
    $('activeStorageDef').innerHTML = '<input type="radio" checked="checked" /> '+activeOption.text;
    lightbox.prototype.deactivate();
}

function hideSearchEngineDefs() {
    var list   = $('searchEngineList');
    var active = $('activeSearchEngineDef');
    list.style.display = 'none';
    for (var i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active.innerHTML = '<input type="radio" checked="checked" /> '+list.options[i].text;
            break;
        }
    }
}
function selectActiveSearchEngineInModalWindow() {
    var list   = $('searchEngineList');
    var list1  = document.forms.modalSearchEnginesDefs;
    var active;
    var i;
    for (i = 0; i < list.length; i++) {
        if (list.options[i].selected == true) {
            active = list.options[i].value;
            break;
        }
    }

    for (i = 0; i < list1.length; i++) {
        if (list1.searchEngine[i].value == active) {
            list1.searchEngine[i].checked = true;
            break;
        }
    }
}

function focusKeywordInput() {
    if (document.forms.searchBar.keyword) {
        document.forms.searchBar.keyword.focus();
    }
}

hideFilterDefs();
hideStorageDefs();
hideSearchEngineDefs();