/*
Created By: QuidProQuo inc.
Website: http://rifasproject.org
Date: 7/3/2009
Version: 1.0b EXPERIMENTAL

Inspired by the lightbox implementation found at http://www.huddletogether.com/projects/lightbox/
and prototype framework implementation found at http://www.prototypejs.org/
*/

var RrcEvent = {

    observe: function(obj, evType, fn, useCapture) {
              if (obj.addEventListener){
                obj.addEventListener(evType, fn, useCapture);
                return true;
              } else if (obj.attachEvent){
                var r = obj.attachEvent("on"+evType, fn);
                return r;
              } else {
                alert("Handler could not be attached");
                return false;
              }
    }
};
Function.prototype.bindAsRrcEventListener = function(object) {
  var __method = this;
  return function(event) {
    return __method.call(object, event || window.event);
  };
};
function getElementsByClassName(className, tag, elm){
	var testClass = new RegExp("(^|\\s)" + className + "(\\s|$)");
	var tag = tag || "*";
	var elm = elm || document;
	var elements = (tag == "*" && elm.all)? elm.all : elm.getElementsByTagName(tag);
	var returnElements = [];
	var current;
	var length = elements.length;
	for(var i=0; i<length; i++){
		current = elements[i];
		if(testClass.test(current.className)){
			returnElements.push(current);
		}
	}
	return returnElements;
};

/*-------------------------------GLOBAL VARIABLES------------------------------------*/

var rrcDetect = navigator.userAgent.toLowerCase();
var rrcOs,rrcBrowser,rrcVersion,rrcTotal,rrcTheString;
var rrcExportShowed = false;
var rrcBuffer       = null;
var rrcPolicy       = 'http://rifasproject.org/crossdomain.xml';
var rrcModalWindow  = 'http://rifasproject.org/rrc/modalwindow';
var rrcServiceUrl   = 'http://rifasproject.org/rrc/check';
var rrcLibVersion   = '1.0b EXPERIMENTAL';

/*-----------------------------------------------------------------------------------------------*/

//Browser detect script origionally created by Peter Paul Koch at http://www.quirksmode.org/

function rrcGetBrowserInfo() {
	if (rrcCheckIt('konqueror')) {
		rrcBrowser = "Konqueror";
		rrcOs = "Linux";
	}
	else if (rrcCheckIt('safari')) rrcBrowser 	= "Safari";
	else if (rrcCheckIt('omniweb')) rrcBrowser 	= "OmniWeb";
	else if (rrcCheckIt('opera')) rrcBrowser 		= "Opera";
	else if (rrcCheckIt('webtv')) rrcBrowser 		= "WebTV";
	else if (rrcCheckIt('icab')) rrcBrowser 		= "iCab";
	else if (rrcCheckIt('msie')) rrcBrowser 		= "Internet Explorer";
	else if (!rrcCheckIt('compatible')) {
		rrcBrowser = "Netscape Navigator";
		rrcVersion = rrcDetect.charAt(8);
	}
	else rrcBrowser = "An unknown browser";

	if (!rrcVersion) rrcVersion = rrcDetect.charAt(place + rrcTheString.length);

	if (!rrcOs) {
		if (rrcCheckIt('linux')) rrcOs 		= "Linux";
		else if (rrcCheckIt('x11')) rrcOs 	= "Unix";
		else if (rrcCheckIt('mac')) rrcOs 	= "Mac";
		else if (rrcCheckIt('win')) rrcOs 	= "Windows";
		else rrcOs 								= "an unknown operating system";
	}
};

function rrcCheckIt(string) {
	place = rrcDetect.indexOf(string) + 1;
	rrcTheString = string;
	return place;
};

/*-----------------------------------------------------------------------------------------------*/

RrcEvent.observe(window, 'load', rrcLigtboxinitialize, false);
RrcEvent.observe(window, 'load', rrcGetBrowserInfo, false);

function RrcLightbox(ctrl) {
    this.initialize(ctrl);
};

RrcLightbox.prototype = {

	yPos : 0,
	xPos : 0,

	initialize: function(ctrl) {
        if (ctrl != null) {
            this.content = ctrl.href;
            RrcEvent.observe(ctrl, 'click', this.activate.bindAsEventListener(this), false);
            ctrl.onclick = function(){return false;};
        }
	},

	// Turn everything on - mainly the IE fixes
	activate: function(){
		if (rrcBrowser == 'Internet Explorer'){
			this.getScroll();
			this.prepareIE('100%', 'hidden');
			this.setScroll(0,0);
			this.hideSelects('hidden');
		}
		this.displayLightbox("block");
	},

	// Ie requires height to 100% and overflow hidden or else you can scroll down past the lightbox
	prepareIE: function(height, overflow){
		bod = document.getElementsByTagName('body')[0];
		bod.style.height = height;
		bod.style.overflow = overflow;

		htm = document.getElementsByTagName('html')[0];
		htm.style.height = height;
		htm.style.overflow = overflow;
	},

	// In IE, select elements hover on top of the lightbox
	hideSelects: function(visibility){
		selects = document.getElementsByTagName('select');
		for(i = 0; i < selects.length; i++) {
			selects[i].style.visibility = visibility;
		}
	},

	// Taken from lightbox implementation found at http://www.huddletogether.com/projects/lightbox/
	getScroll: function(){
		if (self.pageYOffset) {
			this.yPos = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){
			this.yPos = document.documentElement.scrollTop;
		} else if (document.body) {
			this.yPos = document.body.scrollTop;
		}
	},

	setScroll: function(x, y){
		window.scrollTo(x, y);
	},

	displayLightbox: function(display){
		document.getElementById('overlay').style.display = display;
		document.getElementById('lightbox').style.display = display;
		if(display != 'none') this.loadInfo();
	},

	// Begin Ajax request based off of the href of the clicked linked
	loadInfo: function() {
        var flproxy = new flensed.flXHR({autoUpdatePlayer:true});
        flproxy.instanceId = "RRC";
        flproxy.xmlResponseText = false;
        flproxy.onreadystatechange = this.processInfo.bindAsRrcEventListener(this);
        flproxy.noCacheHeader = false;
        flproxy.loadPolicyURL = rrcPolicy;
        flproxy.open("POST", rrcModalWindow);
        flproxy.send("empty=");
	},

	// Display Ajax response
	processInfo: function(XHRobj){
        if (XHRobj.readyState == 4) {
          var info = document.createElement("div");
          info.id = "lbContent";
          info.innerHTML = XHRobj.responseText;

          var buffer = document.getElementById("lbLoadMessage");
          buffer.parentNode.appendChild(info);
          buffer.parentNode.insertBefore(buffer, info);
          document.getElementById('lightbox').className = "done";
          this.actions();
          rrcCheck(this.rel);
        }
	},


	// Search through new links within the lightbox, and attach click event
	actions: function(){
		lbActions = getElementsByClassName('lbAction');

		for(i = 0; i < lbActions.length; i++) {
			RrcEvent.observe(lbActions[i], 'click', this[lbActions[i].rel].bindAsRrcEventListener(this), false);
			lbActions[i].onclick = function(){return false;};
		}
	},

	// Example of creating your own functionality once lightbox is initiated
	deactivate: function(){
        var content = document.getElementById('lbContent');
		content.parentNode.removeChild(content);

		if (rrcBrowser == "Internet Explorer"){
			this.setScroll(0,this.yPos);
			this.prepareIE("auto", "auto");
			this.hideSelects("visible");
		}

		this.displayLightbox("none");
	}
};

/*-----------------------------------------------------------------------------------------------*/

// Onload, make all links that need to trigger a lightbox active
function rrcLigtboxinitialize(){
	addRrcLightboxMarkup();

    var rrcLinks = getElementsByClassName('rrcOn');
	for(i = 0; i < rrcLinks.length; i++) {
        rrcLinks[i].onclick    = new Function("rrcRegister(this)");
	}
};

// Add in markup necessary to make this work. Basically two divs:
// Overlay holds the shadow
// Lightbox is the centered square that the content is put into.
function addRrcLightboxMarkup() {
	bod 				= document.getElementsByTagName('body')[0];
	overlay 			= document.createElement('div');
	overlay.id		    = 'overlay';
	lb					= document.createElement('div');
	lb.id				= 'lightbox';
	lb.className 	= 'loading';
	lb.innerHTML	= '<div id="lbLoadMessage">' +
						  '<p>Loading</p>' +
						  '</div>';
	bod.appendChild(overlay);
	bod.appendChild(lb);
};

function rrcRegister(link) {
    if (document.getElementById(link.rel)) {
        var popup = new RrcLightbox();
        popup.content = rrcModalWindow;
        popup.rel     = link.rel;
        popup.activate();
        return true;
    } else {
        return false;
    }
};

function rrcCheck(rel) {
   var content = document.getElementById(rel).innerHTML;
   var params  = "content="+encodeURI(content);
       params += "&referer="+encodeURI(window.location);

    var flproxy = new flensed.flXHR({autoUpdatePlayer:true});
    flproxy.instanceId = "RRC";
    flproxy.xmlResponseText = false;
    flproxy.onreadystatechange = function handleLoading(XHRobj) {
        if (XHRobj.readyState == 4) {
            document.getElementById("rrcBox").style.display = "none";
            document.getElementById("rrcLinkCheckedBox").innerHTML = XHRobj.responseText;
            document.getElementById("rrcLinkCheckedBox").style.display = "block";
        }
    };
    flproxy.noCacheHeader = false;
    flproxy.loadPolicyURL = rrcPolicy;
    flproxy.open("POST", rrcServiceUrl);
    flproxy.send(params);
};
function rrcShowExport() {
    if (rrcExportShowed == true) {
        document.getElementById("rrcLinkReport").innerHTML = rrcBuffer;
        rrcBuffer = null;
        rrcExportShowed = false;
    } else {
        rrcBuffer = document.getElementById("rrcLinkReport").innerHTML;
        document.getElementById("rrcLinkReport").innerHTML = document.getElementById("rrcExport").innerHTML;
        rrcExportShowed = true;
    }
};