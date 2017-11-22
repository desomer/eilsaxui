package com.elisaxui.xui.core.page;

import static com.elisaxui.xui.core.toolkit.JQuery.$;
import static com.elisaxui.xui.core.toolkit.json.JXui.$xui;
import static com.elisaxui.xui.core.transition.ConstTransition.NEXT_FRAME;
import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_RIPPLE_EFFECT;
import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_SHOW_MENU;
import static com.elisaxui.xui.core.transition.CssTransition.activity;
import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffect;
import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffectShow;

import com.elisaxui.app.elisys.xui.asset.AssetHandler;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.config.TKCoreConfig;
import com.elisaxui.xui.core.datadriven.JSDataCtx;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouterEvent;
import com.elisaxui.xui.core.transition.ConstTransition;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.transition.TKTransition;
import com.elisaxui.xui.core.widget.button.ViewBtnCircle;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.container.JSViewCard;
import com.elisaxui.xui.core.widget.layout.JSPageLayout;
import com.elisaxui.xui.core.widget.layout.ViewPageLayout;
import com.elisaxui.xui.core.widget.loader.ViewLoader;
import com.elisaxui.xui.core.widget.log.ViewLog;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;

public abstract class XUIScene extends XHTMLPart {

	public static final String heightTabBar = 3.5 + "rem";
	public static final String heightNavBar = 7 + "rem";
	public static final int widthMenu = 250;
	public static final String widthScene = "100vw";
	
	public static final double OVERLAY_OPACITY_MENU = 0.5;
	public static final double OVERLAY_OPACITY_BACK = 0.7;
	
	public static final int ZINDEX_ANIM_FRONT = 1;
	public static final int ZINDEX_ANIM_BACK = 0;
	
	public static final int ZINDEX_NAV_BAR = 1;
	public static final int ZINDEX_MENU = 2;
	public static final int ZINDEX_FLOAT = 3;
	public static final int ZINDEX_OVERLAY = 4;

    public static final String PREF_3D= "backface-visibility: hidden;"
    		//+ " transform-style:preserve-3d;"
    		;
    
	public static XClass scene;
	public static XClass cShell;
	
	public abstract JSMethodInterface createScene();
	public abstract ConfigScene getConfigScene();
	   
	@xTarget(AFTER_BODY.class)
	@xRessource
	@xPriority(1)
	public XMLElement xImportCssXUIScene() {
		return xListElement(
				xLinkCssAsync("https://fonts.googleapis.com/icon?family=Material+Icons"),
				xLinkCssAsync("https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css"),
				xLinkCssAsync("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css")
				);
	}
	
	@xTarget(HEADER.class)
	@xRessource
	@xPriority(1)
	public XMLElement xImportXUIScene() {
		return xListElement(
				
				xTitle(getConfigScene().getTitle()),
				xMeta(xAttr("name", xTxt("theme-color")), xAttr("content", xTxt(getConfigScene().getBgColorTheme()))),
				xLinkIcon(AssetHandler.getIconUri(getConfigScene().getIdIcon(), 32, 32)), 
				xLinkManifest(getConfigScene().getAppManifest()),
				
				xScriptJS(js()
						.set("window.doOnResLoadWait", fct("res", "id")
								._if("window.doOnResLoad==undefined")
									.__("setTimeout(", fct().__("doOnResLoadWait(res,id)") ,",100)")
								._else()
									.__("doOnResLoad(res, id)")   // attente fin chargement js
								.endif()
								)
								
						.set("window.resLoaded", fct("res", "id").__("doOnResLoadWait(res, id)"))  
						.set("window.resLoadedCss", fct("res", "media").__("res.media=media"))  
						
						.set("window.onerror", fct("msg", "url", "noLigne", "noColonne", "erreur") 
								.var("chaine", "msg.toLowerCase()")
								.var("souschaine", txt("script error"))
								._if("chaine.indexOf(souschaine) > -1")
									.__("alert('Script Error : voir la Console du Navigateur pour les Détails')")
								._else()
									.var("message", "["
											+ "'Message : ' + msg,"
											+ "'URL : ' + url,"
											+ "'Ligne : ' + noLigne,"
											+ "'Colonne : ' + noColonne,"
											+ "'Objet Error : ' + JSON.stringify(erreur)"
											+ "].join(' - ')")
									.__("alert(message)")
								.endif()
								._return(false)  // false = trace dans le log de la console
						)
						),
				
				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js", "resLoaded(this, "+XUICstRessource.ID_RES_FASTDOM+");"),
				xScriptSrcAsync("/asset/?url=http://work.krasimirtsonev.com/git/navigo/navigo.js", "resLoaded(this, "+XUICstRessource.ID_RES_NAVIGO+");"),
				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/hammer.js/2.0.8/hammer.min.js", "resLoaded(this, "+XUICstRessource.ID_RES_HAMMER+");"),
				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js", "resLoaded(this, "+XUICstRessource.ID_RES_JQUERY+");")

				//xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js", "resLoaded(this, "+XUICstRessource.ID_RES_CHART+");"),
				//xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/json-editor/0.7.28/jsoneditor.min.js"),
				//xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js"),
				//xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js")
				//+ "<link rel='alternate stylesheet' title='main' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>"				
				);
	}
		
	
	@xTarget(AFTER_CONTENT.class)
	@xRessource
	public XMLElement xImportAfterXUIScene() {
		
		return xListElement(
				xPart(new TKQueue()),   //TODO Remplacer par une class js
				xImport(JSXHTMLPart.class),
				xImport(JSDataDriven.class),
				xImport(JSDataSet.class),
				xImport(JSDataCtx.class),
				xImport(JSOverlay.class),
				xImport(JSContainer.class),
				xImport(JSPageLayout.class),
				xImport(TKRouterEvent.class),
				xPart(new CssTransition()),  //TODO Remplacer
				xImport(TKTransition.class),
				xImport(JSMenu.class),
				xImport(TKActivity.class),
				xImport(JSViewCard.class)			
				);
	}
				
	@xTarget(HEADER.class)
	@xRessource
	@xPriority(2)
	public XMLElement xStyleXUIScene() {
		
		return xCss()
				.select("html").set("font-size: 16px;"
						//+"overflow-y: scroll;"
						//+ "overflow-x: hidden;" // pour que le 100vw ne prennent pas en compte la largueur du scrollbar
						+ " line-height: 1.5;"
						+ "font-family: 'Roboto', sans-serif;font-weight: normal;")    //color: rgba(0,0,0,0.87)
				
				.select("body").set("background-color: "+getConfigScene().getBgColorScene()+"; margin: 0; ")
				.select("*").set("-webkit-tap-highlight-color: rgba(0,0,0,0);")  // pas de coulour au click => ripple a la place

				//----------------------------------------------------------------
				.select(scene).set("overflow-x: hidden; background-color: "+getConfigScene().getBgColorScene()+";"   // overflow: auto; -webkit-overflow-scrolling: auto
						//+ "min-width: 100vw;  "
						+ "min-height: 100vh; " 
						)
				    .path(xCss(cShell).set("background-color: "+getConfigScene().getBgColorContent()+ ";"
				    		+ "width: "+widthScene+";"
				    		+ "  min-height: 100vh; "))
					.path(xCss("#NavBarShell h1").set("text-align:center;color: inherit;  font-size: 2.1rem; margin-top: 50px"))
				//----------------------------------------------------------------
				.select(activity).set("background-color: "+getConfigScene().getBgColorContent()+";"+ PREF_3D+ 
					"width:"+widthScene+"; will-change:overflow,z-index;") //will-change:transform
					.path(xCss(ViewPageLayout.content)
								.set(" min-height: 100vh; "
									+ "min-width: "+widthScene+"; "
									+ "background-color:"+getConfigScene().getBgColorContent()+";will-change:contents")  // changement durant le freeze du contenu de l'activity
								)
				
				//----------------------------------------------------------------
				.select(ViewPageLayout.content).set("box-sizing: border-box;"  // ne pas ajouter a cActivity
						//+ "min-height:100%;"
						//+ " min-width: 100%;"
						//+ " max-width: 100%; "
						+ "padding-top: " + heightNavBar + "; "
						+ "padding-bottom: " + heightTabBar)	 
				
				;
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenuXUIScene() {
		return 
			xListElement(
				xPart(new ViewMenu()),
				xDiv(scene, xDiv(cShell, 
						xPart(new ViewNavBar().addProperty(ViewNavBar.PROPERTY_NAME, "NavBarShell"), xH1("Loading...")),  
						xPart(new ViewLoader())))                      
		);
	}
	
	
	@xTarget(AFTER_CONTENT.class)
	@xPriority(500)
	@xRessource
	public XMLElement xImportStartXUIScene() {
		return xListElement(
				xScriptJS(js()
						// a mettre dans TKConfig
						.set($xui(), "{ intent: { nextActivityAnim : 'fromBottom' }, tkrouter:'', config:{ }, resourceLoading:{} }")
						.set("$xui.resourceLoading.query", true)
						.set("$xui.resourceLoading.hammer", true)
						.set("$xui.resourceLoading.navigo", true)
						.set("$xui.resourceLoading.fastdom", true)
						.set("$xui.resourceLoading.chart", false)
						
						.set("window.doOnResLoad", fct("res", "id")
								.systemDebugIf(TKCoreConfig.debugAsyncResource, txt("ressource loaded ="), "id")   //res.src.split('/').pop()
								.__()
								._if("id=="+XUICstRessource.ID_RES_JQUERY)
									.__(getIntializeJSFct())
									.__("(",getEventManager(),")()")									
									.set("$xui.resourceLoading.query", false)	
								.endif()
								._if("id=="+XUICstRessource.ID_RES_NAVIGO)
									.set("$xui.resourceLoading.navigo", false)	
									.__("(",getStateManager(),")()")
								.endif()
								._if("id=="+XUICstRessource.ID_RES_HAMMER)
									.set("$xui.resourceLoading.hammer", false) 
								.endif()	
								._if("id=="+XUICstRessource.ID_RES_FASTDOM)
									.set("$xui.resourceLoading.fastdom", false) 
								.endif()
								._if("id=="+XUICstRessource.ID_RES_CHART)
									.set("$xui.resourceLoading.chart", false) 
								.endif()
								
								._if("!$xui.resourceLoading.hammer && !$xui.resourceLoading.query")
									//.__("(",getMoveManager(),")()")
								.endif()	
								
								._if("!$xui.resourceLoading.hammer && !$xui.resourceLoading.query&& !$xui.resourceLoading.navigo&& !$xui.resourceLoading.fastdom&& !$xui.resourceLoading.chart")
									.__("(",getMoveManager(),")()")	
									/***************************************************************/
									.__(initializeScene())
									.__(createScene())
									/***************************************************************/
								.endif()
							)						
					)	
				
				);
	}

	

	public JSMethodInterface removeAppShell()
	{
	  return fragment()
			  .__(TKQueue.startProcessQueued( 200,  fct()  //TODO a gerer dans un event appli ready
			  			 .__($(XUIScene.scene).children(XUIScene.cShell).remove())
			  		))
				;
	}
	
	public JSMethodInterface registerServiceWorker()
	{
	  return fragment()
			._if("'serviceWorker' in navigator")
				.__("navigator.serviceWorker.register('sw.js', { scope: '/'} ).then(",  //rest/page/
						fct("registration")
							.consoleDebug(txt("ok registration scope"), "registration.scope")
							.set("navigator.serviceWorker.onmessage", fct("e")
									//.consoleDebug(txt("onEventSW"), "e.data")
									.__(JQuery.$(ViewLog.cLog, " textArea").val( JQuery.$(ViewLog.cLog, " textArea").val() , "+ e.data"  ))
									)
							,",",
						fct("err").consoleDebug(txt("ServiceWorker registration failed"), "err") 
					,")")
			.endif();
	}
		
	static JSMenu jsMenu;
	protected static JSNavBar jsNavBar;
	protected static TKActivity tkActivity;
	protected static JSPageLayout jsPageLayout;
	
	public JSMethodInterface initializeScene()
	{
	  return fragment()   
			//******************** construction du menu ****************************************************
				.var(jsMenu, _new())
				.var("jsonMenu", jsMenu.getData())
				// TODO a changer par un data sur le menu
				.set("window.jsonMainMenu", "jsonMenu")  // liste des nemu pour animation dans tkAnimation
				.var(tkActivity, "$xui.tkrouter.activityMgr")
				
				/***********************************************************************************************/		

				/**************************************************************/
				.__(TKQueue.startProcessQueued( 100,  fct()
							.__(loadPage())	
							.__(removeAppShell())
					 , 200, fct()
					 		.__(loadExtendScript()) 
							.__(searchMenu()) 
					 , 500,  fct()
							.__(registerServiceWorker())

					))
				/*************************************************************/
			  ;
	}
	
	
	public JSMethodInterface searchMenu()
	{
	  return fragment();
	}
	

	public JSMethodInterface loadPage()
	{
	  return fragment();
	}
	
	public JSMethodInterface loadExtendScript()
	{  	// TODO a changer : mettre dans une queue avec priorité (avec image) et gestion de promise d'attente 
	  return fragment()   
		 		// load script notify
				.__(" $.getScript({url:'https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js',  cache: true})") 
			  ;
	}
	
	public JSMethodInterface getIntializeJSFct()
	{
		return js()
		
	   	.set("navigator.vibrate", "navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate")
	   	
		.__("$.fn.insertAt = function(elements, index){"+   // utiliser dans le core 
				"var children = this.children();"+
				"if(index >= children.length){"+
				"this.append(elements);"+
				"return this;"+
				"}"+
				"var before = children.eq(index);"+
				"$(elements).insertBefore(before);"+
				"return this;"+
				"}")
		;
	}
	
	JQuery ripple;
	JQuery btn;
	
	public JSMethodInterface searchRipple()
	{
	  return fragment()
			 // recherche le ripple btn
			   .var(ripple, btn)
			   ._if( "!", ripple.hasClass(cRippleEffect))
			   		.set(ripple, btn.closest(cRippleEffect))
					._if( "!", ripple.hasClass(cRippleEffect))
			   			.set(ripple, btn.children(cRippleEffect))
			   		.endif()	
			   .endif();
	}
	
	public JSMethodInterface getEventManager()
	{
	  return fct()
			   .consoleDebug("'ok EventManager'") 
			   .__($("body").on(txt("touchstop"), ",", fct('e')
					   .set("window.stopClientY", "e.originalEvent.touches[0].clientY")
			   ))
			   .__($("body").on(txt("touchstart"), ",", fct('e')
					   .set("window.startClientY", "e.originalEvent.touches[0].clientY")
					   .set("window.stopClientY", -1)
					   .set("window.startScrollTop", $(jsvar("window")).scrollTop())
					   
					   .var(btn, $(jsvar("e.target")).closest("[data-x-action]"))
					   
					   .__(searchRipple())
					   					   
					   .__("$xui.intent.nextActivityAnim= '"+ConstTransition.ANIM_FROM_BOTTOM+"' ")	
					   				   	
					   ._if( "ripple.length>0") 
					   	  //TODO A Changer par action sur description 		
					   	   ._if(ripple.hasClass(ViewBtnCircle.cBtnCircle))
					   	   		// pas animation ripple
					   	   	    .__("$xui.intent.nextActivityAnim= '"+ConstTransition.ANIM_FROM_RIPPLE+"'")
						   ._else()
						   	   // animation ripple 
							   .__(TKQueue.startProcessQueued(fct()
						   		      , NEXT_FRAME	, fct()  // attente ripple effect
							   				.__(ripple.addClass(cRippleEffectShow))
									  ,SPEED_RIPPLE_EFFECT, fct()  // attente ripple effect
								   		    .__(ripple.removeClass(cRippleEffectShow))     	
    
									   )
								)  
						   .endif()
					   .endif()
				))
			;
	}
	
	public JSMethodInterface getMoveManager()
	{
		// gestion deplacement menu et fermeture par gesture 
	  return fct().consoleDebug("'ok MoveManager by HAMMER'") 
				//.__("$.notify('ok MoveManager', {globalPosition: 'bottom left', className:'warn', autoHideDelay: 2000})")

			     // hammer sur le body
				.__("var mc = new Hammer($('body')[0])")    // {touchAction: 'auto'}
			//	.__("mc.get('pinch').set({ enable: true })")
				.__("mc.get('pan').set({ enable: true, direction: Hammer.DIRECTION_HORIZONTAL })")  //DIRECTION_ALL
				.__("mc.get('tap').set({ enable: true, time: 1000 })")
				
				.var("anim", true)
				
				.__("mc.on('tap',", fct("e")

							.var("btn", $(jsvar("e.target")).closest("[data-x-action]"))
							.var("event", "btn.data('x-action')")
							.set("window.lastBtn", btn)  // TODO a virer
							
							//.__("$.notify('do Event '+event, {globalPosition: 'bottom left', className:'warn', autoHideDelay: 2000})")
							
							 .__(searchRipple())
							
							 // TODO passer un context d'event au doEvent  (ripple, btn, e,  event)
							 .__(TKQueue.startProcessQueued(fct()
									 .var("retEvent", $xui().tkrouter().doEvent("event"))
									 ._if("retEvent.cancelRipple")
										 ._if( "ripple.length>0") 
										 	.__("ripple.removeClass('", cRippleEffectShow.getId() ,"')")   
										 .endif()
									 .endif()
									 ))
						, ")")
				
				.__("mc.on('hammer.input',", fct("ev")
//						.consoleDebug("ev.deltaX", "ev")
//						.set("window.velocityY", "ev.velocityY")
//						.set("window.deltaY", "ev.deltaY")
						
						
//						.__("$('#content')[0].innerHTML = [ev.srcEvent.type, ev.pointers.length, ev.isFirst, ev.isFinal, ev.deltaX, ev.deltaY, ev.distance, ev.velocity, ev.deltaTime, ev.offsetDirection, ev.target].join('<br>');")
						//**************************** gestion swipe anim du menu *************************/
						._if("$(ev.target).closest('.menu').length > 0")
							._if("ev.deltaX>-100 && ev.offsetDirection==2 && ev.velocity>-1 ")
								._if("anim==true")
									// bouge en fonction delta
									.__("$('.menu').css('transition', '' )")
									.__("$('.menu').css('transform', 'translate3d('+ev.deltaX+'px,'+$('body').scrollTop()+'px,0px)' )")
								.endif()
							._elseif("anim==true && ev.offsetDirection==2 ")
								.set("anim", "false")
								// ferme le menu par le doOverlay avec une animation
								.__("$('.menu').css('transition', 'transform "+(SPEED_SHOW_MENU+50)+"ms ease-out' )")
								.__($xui().tkrouter() .doEvent("'Overlay'"))
							.endif()
							
							._if("ev.isFinal")
								// lance l'animation de retour a l'ouverture
								._if("anim==true")
									.__("$('.menu').css('transition', 'transform "+(SPEED_SHOW_MENU+50)+"ms ease-out' )")
									.__("$('.menu').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
								.endif()
								.set("anim", "true")
							.endif()
						.endif()
						//***************************************************************************************/
					,")")
			;
	}
 
	TKRouterEvent tkrouter;
	public JSMethodInterface getStateManager()
	{
	  return fct()
//		.consoleDebug("'ok StateManager'") 
		.set("nav", "new Navigo(null,true,'#!')")   //   null,true,'!#')")
		.var(tkrouter , _new("nav"))
		.set($xui().tkrouter(), tkrouter) 
		;
			  
	}
	
}
