/*	CheckPlayer 1.0.1 <http://checkplayer.flensed.com/>
	Copyright (c) 2008 Kyle Simpson, Getify Solutions, Inc.
	This software is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>

	====================================================================================================
	Portions of this code were extracted and/or derived from:

	SWFObject v2.1 & 2.2a8 <http://code.google.com/p/swfobject/>
	Copyright (c) 2007-2008 Geoff Stearns, Michael Williams, and Bobby van der Sluis
	This software is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>
*/
(function(r){var e=r,v=r.document,n="undefined",g=true,x=false,w="",h="object",o="function",t="string",m="div",d="onunload",j="none",u=null,p=null,i=null,l=null,k="flensed.js",f="checkplayer.js",b="swfobject.js",c=r.setTimeout,a=r.clearTimeout,s=r.setInterval,q=r.clearInterval;if(typeof r.flensed===n){r.flensed={}}if(typeof r.flensed.checkplayer!==n){return}p=r.flensed;c(function(){var y=x,I=v.getElementsByTagName("script"),D=I.length;try{p.base_path.toLowerCase();y=g}catch(B){p.base_path=""}function G(N,M,O){for(var L=0;L<D;L++){if(typeof I[L].src!==n){if(I[L].src.indexOf(N)>=0){break}}}var K=v.createElement("script");K.setAttribute("src",p.base_path+N);if(typeof M!==n){K.setAttribute("type",M)}if(typeof O!==n){K.setAttribute("language",O)}v.getElementsByTagName("head")[0].appendChild(K)}if((typeof I!==n)&&(I!==null)){if(!y){var J=0;for(var C=0;C<D;C++){if(typeof I[C].src!==n){if(((J=I[C].src.indexOf(k))>=0)||((J=I[C].src.indexOf(f))>=0)){p.base_path=I[C].src.substr(0,J);break}}}}}try{r.swfobject.getObjectById("a")}catch(H){G(b,"text/javascript")}try{p.ua.pv.join(".")}catch(F){G(k,"text/javascript")}function z(){a(A);try{e.detachEvent(d,arguments.callee)}catch(K){}}try{e.attachEvent(d,z)}catch(E){}var A=c(function(){z();try{r.swfobject.getObjectById("a");p.ua.pv.join(".")}catch(K){throw new r.Error("CheckPlayer dependencies failed to load.")}},20000)},0);p.checkplayer=function(X,ai,O,ab){if(typeof i._ins!==n){if(i._ins.ready()){setTimeout(function(){ai(i._ins)},0)}return i._ins}var A="6.0.65",Z=[],I=null,F=x,G=null,ak=null,S=w,D=x,L=null,B=[],R={},aa=[],E=null,ag=null,af=null,M=null,H=x,ah=null,K=x,T=x,P=x,ae=null;var z=function(){if((typeof X!==n)&&(X!==null)&&(X!==x)){ag=X+w}else{ag="0.0.0"}if(typeof ai===o){af=ai}if(typeof O!==n){H=!(!O)}if(typeof ab===o){M=ab}function am(){a(G);try{e.detachEvent(d,am)}catch(ap){}}try{e.attachEvent(d,am)}catch(an){}(function ao(){try{p.bindEvent(e,d,Y)}catch(ap){G=c(arguments.callee,25);return}am();ah=p.ua.pv.join(".");G=c(ad,1)})()}();function ad(){try{E=v.getElementsByTagName("body")[0]}catch(an){}if((typeof E===n)||(E===null)){G=c(ad,25);return}try{r.swfobject.getObjectById("a");l=r.swfobject}catch(am){G=c(ad,25);return}T=l.hasFlashPlayerVersion(A);K=l.hasFlashPlayerVersion(ag);aj();if(typeof af===o){af(J)}D=g;if(K){U()}else{if(H){V()}}}function Y(){if(typeof e.detachEvent!==n){e.detachEvent(d,Y)}i._ins=null;if((typeof L!==n)&&(L!==null)){try{L.updateSWFCallback=null;ac=null}catch(ap){}L=null}try{for(var ao in J){if(J[ao]!==Object.prototype[ao]){try{J[ao]=null}catch(an){}}}}catch(am){}J=null;E=null;y();aa=null;af=null;M=null;try{for(var at in i){if(i[at]!==Object.prototype[at]){try{i[at]=null}catch(ar){}}}}catch(aq){}i=null;p.checkplayer=null;p=null;r=null}function al(an,ao,am){aa[aa.length]={func:an,funcName:ao,args:am}}function U(){if(!D){I=c(U,25);return}var ao=0;try{ao=aa.length}catch(ap){}for(var an=0;an<ao;an++){try{aa[an].func.apply(J,aa[an].args);aa[an]=x}catch(am){K=x;aj();if(typeof af===o){af(J)}else{throw new r.Error("checkplayer::"+aa[an].funcName+"() call failed.")}}}aa=null}function y(){a(G);G=null;a(I);I=null;for(var an in R){if(R[an]!==Object.prototype[an]){q(R[an]);R[an]=x}}for(var am in Z){if(Z[am]!==Object.prototype[am]){a(Z[am]);Z[am]=x}}}function aj(){try{J.playerVersionDetected=ah;J.checkPassed=K;J.updateable=T;J.updateStatus=P;J.updateControlsContainer=ae}catch(am){}}function N(at,an){var ap=an?"visible":"hidden";var ar=p.getObjectById(at);try{if(ar!==null&&(typeof ar.style!==n)&&(ar.style!==null)){ar.style.visibility=ap}else{try{p.createCSS("#"+at,"visibility:"+ap)}catch(aq){}}}catch(ao){try{p.createCSS("#"+at,"visibility:"+ap)}catch(am){}}}function V(){var ar=E;if((typeof ar===n)||(ar===null)){Z[Z.length]=c(V,25);return}try{l.getObjectById("a")}catch(aq){Z[Z.length]=c(V,25);return}if(!F){F=g;y();if(T){S="CheckPlayerUpdate";ak=S+"SWF";p.createCSS("#"+S,"width:221px;height:145px;position:absolute;left:5px;top:5px;border:none;background-color:#000000;display:block;");p.createCSS("#"+ak,"display:inline;position:absolute;left:1px;top:1px;");ae=v.createElement(m);ae.id=S;ar.appendChild(ae);N(ae.id,x);aj();var au=null;try{au=e.top.location.toString()}catch(am){au=e.location.toString()}var ao={swfId:ak,MMredirectURL:au.replace(/&/g,"%26"),MMplayerType:(p.ua.ie&&p.ua.win?"ActiveX":"PlugIn"),MMdoctitle:v.title.slice(0,47)+" - Flash Player Installation"};var at={allowScriptAccess:"always"};var ap={id:ak,name:ak};try{Q(p.base_path+"updateplayer.swf",{appendToId:S},"219","143",ao,at,ap,{swfTimeout:3000,swfCB:C},g)}catch(an){W();return}}else{W()}}}function W(am){if(typeof am===n){am="Flash Player not detected or not updateable."}P=i.UPDATE_FAILED;aj();if(typeof M===o){M(J)}else{throw new r.Error("checkplayer::UpdatePlayer(): "+am)}}function C(am){if(am.status===i.SWF_LOADED){a(R["continueUpdate_"+ak]);R["continueUpdate_"+ak]=x;L=am.srcElem;L.updateSWFCallback=ac;P=i.UPDATE_INIT;aj();if(typeof M===o){M(J)}N(ae.id,g)}else{if(am.status===i.SWF_FAILED||am.status===i.SWF_TIMEOUT){W()}}}function ac(an){try{if(an===0){P=i.UPDATE_SUCCESSFUL;ae.style.display=j;try{e.open(w,"_self",w);e.close();r.self.opener=e;r.self.close()}catch(ao){}}else{if(an===1){P=i.UPDATE_CANCELED;ae.style.display=j}else{if(an===2){ae.style.display=j;W("The Flash Player update failed.");return}else{if(an===3){ae.style.display=j;W("The Flash Player update timed out.");return}}}}}catch(am){}aj();if(typeof M===o){M(J)}}function Q(aG,at,av,an,ap,ar,ax,aE,aC){if(at!==null&&(typeof at===t||typeof at.replaceId===t)){N(((typeof at===t)?at:at.replaceId),x)}if(!D){al(Q,"DoSWF",arguments);return}if(K||aC){av+=w;an+=w;var aA=(typeof ax===h)?ax:{};aA.data=aG;aA.width=av;aA.height=an;var az=(typeof ar===h)?ar:{};if(typeof ap===h){for(var aD in ap){if(ap[aD]!==Object.prototype[aD]){if(typeof az.flashvars!==n){az.flashvars+="&"+aD+"="+ap[aD]}else{az.flashvars=aD+"="+ap[aD]}}}}var aF=null;if(typeof ax.id!==n){aF=ax.id}else{if(at!==null&&(typeof at===t||typeof at.replaceId===t)){aF=((typeof at===t)?at:at.replaceId)}else{aF="swf_"+B.length}}var aH=null;if(at===null||at===x||typeof at.appendToId===t){var ao=null;if(at!==null&&at!==x&&typeof at.appendToId===t){ao=p.getObjectById(at.appendToId)}else{ao=E}var au=v.createElement(m);aH=(au.id=aF);ao.appendChild(au)}else{aH=((typeof at.replaceId===t)?at.replaceId:at)}var ay=function(){},aB=0,am=w,aw=null;if(typeof aE!==n&&aE!==null){if(typeof aE===h){if(typeof aE.swfCB!==n&&aE.swfCB!==null){ay=aE.swfCB}if(typeof aE.swfTimeout!==n&&(r.parseInt(aE.swfTimeout,10)>0)){aB=aE.swfTimeout}if(typeof aE.swfEICheck!==n&&aE.swfEICheck!==null&&aE.swfEICheck!==w){am=aE.swfEICheck}}else{if(typeof aE===o){ay=aE}}}try{aw=l.createSWF(aA,az,aH)}catch(aq){}if(aw!==null){B[B.length]=aF;if(typeof ay===o){ay({status:i.SWF_INIT,srcId:aF,srcElem:aw});R[aF]=s(function(){var aJ=p.getObjectById(aF);if((typeof aJ!==n)&&(aJ!==null)&&(aJ.nodeName==="OBJECT"||aJ.nodeName==="EMBED")){var aI=0;try{aI=aJ.PercentLoaded()}catch(aK){}if(aI>0){if(aB>0){a(R["DoSWFtimeout_"+aF]);R["DoSWFtimeout_"+aF]=x}if(aI<100){c(function(){ay({status:i.SWF_LOADING,srcId:aF,srcElem:aJ})},1)}else{q(R[aF]);R[aF]=x;c(function(){ay({status:i.SWF_LOADED,srcId:aF,srcElem:aJ})},1);if(am!==w){var aL=x;R[aF]=s(function(){if(!aL&&typeof aJ[am]===o){aL=g;try{aJ[am]();q(R[aF]);R[aF]=x;ay({status:i.SWF_EI_READY,srcId:aF,srcElem:aJ})}catch(aM){}aL=x}},25)}}}}},50);if(aB>0){R["DoSWFtimeout_"+aF]=c(function(){var aJ=p.getObjectById(aF);if((typeof aJ!==n)&&(aJ!==null)&&(aJ.nodeName==="OBJECT"||aJ.nodeName==="EMBED")){var aI=0;try{aI=aJ.PercentLoaded()}catch(aK){}if(aI<=0){q(R[aF]);R[aF]=x;if(p.ua.ie&&p.ua.win&&aJ.readyState!==4){aJ.id="removeSWF_"+aJ.id;aJ.style.display=j;R[aJ.id]=s(function(){if(aJ.readyState===4){q(R[aJ.id]);R[aJ.id]=x;l.removeSWF(aJ.id)}},500)}else{l.removeSWF(aJ.id)}R[aF]=x;R["DoSWFtimeout_"+aF]=x;ay({status:i.SWF_TIMEOUT,srcId:aF,srcElem:aJ})}}},aB)}}}else{if(typeof ay===o){ay({status:i.SWF_FAILED,srcId:aF,srcElem:null})}else{throw new r.Error("checkplayer::DoSWF(): SWF could not be loaded.")}}}else{if(typeof ay===o){ay({status:i.SWF_FAILED,srcId:aF,srcElem:null})}else{throw new r.Error("checkplayer::DoSWF(): Minimum Flash Version not detected.")}}}var J={playerVersionDetected:ah,versionChecked:ag,checkPassed:K,UpdatePlayer:V,DoSWF:function(ar,at,ap,aq,an,am,ao,au){Q(ar,at,ap,aq,an,am,ao,au,x)},ready:function(){return D},updateable:T,updateStatus:P,updateControlsContainer:ae};i._ins=J;return J};i=p.checkplayer;i.UPDATE_INIT=1;i.UPDATE_SUCCESSFUL=2;i.UPDATE_CANCELED=3;i.UPDATE_FAILED=4;i.SWF_INIT=5;i.SWF_LOADING=6;i.SWF_LOADED=7;i.SWF_FAILED=8;i.SWF_TIMEOUT=9;i.SWF_EI_READY=10;i.module_ready=function(){}})(window);