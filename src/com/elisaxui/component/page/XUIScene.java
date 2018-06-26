package com.elisaxui.component.page;

import static com.elisaxui.component.toolkit.jquery.JQuery.$;
import static com.elisaxui.component.toolkit.json.JXui.$xui;
import static com.elisaxui.component.toolkit.transition.ConstTransition.NEXT_FRAME;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_RIPPLE_EFFECT;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_MENU;
import static com.elisaxui.component.toolkit.transition.CssTransition.activity;
import static com.elisaxui.component.widget.button.CssRippleEffect.cRippleEffect;
import static com.elisaxui.component.widget.button.CssRippleEffect.cRippleEffectShow;

import com.elisaxui.app.elisys.xui.asset.AssetHandler;
import com.elisaxui.component.config.TKCoreConfig;
import com.elisaxui.component.toolkit.TKActivity;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.TKRouterEvent;
import com.elisaxui.component.toolkit.datadriven.JSDataCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.toolkit.jquery.JQuery;
import com.elisaxui.component.toolkit.transition.ConstTransition;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.toolkit.transition.JSTransition;
import com.elisaxui.component.widget.button.ViewBtnCircle;
import com.elisaxui.component.widget.container.JSContainer;
import com.elisaxui.component.widget.container.JSViewCard;
import com.elisaxui.component.widget.layout.JSPageLayout;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.loader.ViewLoader;
import com.elisaxui.component.widget.log.ViewLog;
import com.elisaxui.component.widget.menu.JSMenu;
import com.elisaxui.component.widget.menu.ViewMenu;
import com.elisaxui.component.widget.navbar.JSNavBar;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.overlay.JSOverlay;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

@Deprecated
public abstract class XUIScene extends XHTMLPart {

	public static final String heightTabBar = 3.5 + "rem";
	public static final String heightNavBar = 12 + "rem"; // 7
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

	// public static final String PREF_3D= ""; //"backface-visibility: hidden;"
	// + " transform-style:preserve-3d;"
	// transform: translate3d(0px,0px,0px); =bug : les header et footer ne se fixe
	// plus au body

	public static final String PERFORM_3D = "backface-visibility: hidden; transform: translate3d(0px,0px,0px); will-change:transform;";

	public static final String PERFORM_CHANGE_OPACITY = "will-change:opacity, display;";

	public static CSSClass scene;
	public static CSSClass cShell;

	public abstract JSContentInterface createScene();

	public abstract ConfigScene getConfigScene();

	@xTarget(AFTER_BODY.class)
	@xResource
	@xPriority(1)
	public XMLElement xImportCssXUIScene() {
		return xListNode(
				xLinkCssAsync("https://fonts.googleapis.com/icon?family=Material+Icons"),
				xLinkCssAsync("https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css"),
				xLinkCssAsync("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css"));
	}

	@xTarget(HEADER.class)
	@xResource
	@xPriority(1)
	public XMLElement xImportXUIScene() {
		return xListNode(

				xTitle(getConfigScene().getTitle()),
				xMeta(xAttr("name", xTxt("theme-color")), xAttr("content", xTxt(getConfigScene().getBgColorTheme()))),
				xLinkIcon(AssetHandler.getIconUri(getConfigScene().getIdIcon(), 32, 32)),
				xLinkManifest(getConfigScene().getAppManifest()),

				xScriptJS(js()
						._set("window.doOnResLoadWait", fct("res", "id")
								._if("window.doOnResLoad==undefined")
								.__("setTimeout(", fct().__("doOnResLoadWait(res,id)"), ",100)")
								._else()
								.__("doOnResLoad(res, id)") // attente fin chargement js
								.endif())

						._set("window.resLoaded", fct("res", "id").__("doOnResLoadWait(res, id)"))
						._set("window.resLoadedCss", fct("res", "media").__("res.media=media"))

						._set("window.onerror", fct("msg", "url", "noLigne", "noColonne", "erreur")
								._var("chaine", "msg.toLowerCase()")
								._var("souschaine", txt("script error"))
								._if("chaine.indexOf(souschaine) > -1")
								.__("alert('Script Error : voir la Console du Navigateur pour les Détails')")
								._else()
								._var("message", "["
										+ "'Message : ' + msg,"
										+ "'URL : ' + url,"
										+ "'Ligne : ' + noLigne,"
										+ "'Colonne : ' + noColonne,"
										+ "'Objet Error : ' + JSON.stringify(erreur)"
										+ "].join(' - ')")
								.__("alert(message)")
								.endif()
								._return(false) // false = trace dans le log de la console
				)),

				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js",
						"resLoaded(this, " + XUICstRessource.ID_RES_FASTDOM + ");"),
				xScriptSrcAsync("/asset/?url=http://work.krasimirtsonev.com/git/navigo/navigo.js",
						"resLoaded(this, " + XUICstRessource.ID_RES_NAVIGO + ");"),
				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/hammer.js/2.0.8/hammer.min.js",
						"resLoaded(this, " + XUICstRessource.ID_RES_HAMMER + ");"),
				xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js",
						"resLoaded(this, " + XUICstRessource.ID_RES_JQUERY + ");")

		// xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js",
		// "resLoaded(this, "+XUICstRessource.ID_RES_CHART+");"),
		// xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/json-editor/0.7.28/jsoneditor.min.js"),
		// xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js"),
		// xScriptSrcAsync("https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js")
		// + "<link rel='alternate stylesheet' title='main'
		// href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>"
		);
	}

	
	@Deprecated
	public static XMLElement getTemplateAction(Object name, Object action) {
		
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{		
				return xNode("button", xAttr("data-x-action", xTxt(xVar(action))), ViewNavBar.cActionBtnContainer, cRippleEffect , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
			}
		};
		
		return template.getTemplate();
	}
	
	@xTarget(AFTER_CONTENT.class)
	@xResource
	public XMLElement xImportAfterXUIScene() {
		
		/******************* AJOUT BTN DU TABBAR *******************/
		XMLElement cont = xListNode(
					xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter,
							getTemplateAction("'schedule'", "''")),
					xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, getTemplateAction("'today'", "''")),
					xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, getTemplateAction("'mic'", "''")),
					xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, getTemplateAction("'apps'", "''")));
		
		vProp(ViewTabBar.pChildren, cont);	
		vProp(ViewTabBar.pHeight, "height: "+heightTabBar);
		vProp(ViewTabBar.pStyle, "background:"+((XUIScene)XUIFactoryXHtml.getXMLFile().getMainXMLPart()).getConfigScene().getBgColorNavBar());
		
		vProp(ViewNavBar.pHeight, "height:" + XUIScene.heightNavBar);
		vProp(ViewNavBar.pStyle, "background:"+((XUIScene)XUIFactoryXHtml.getXMLFile().getMainXMLPart()).getConfigScene().getBgColorNavBar());

		
		
		/************************************************************/
		return xListNode(
				vPart(new TKQueue()), // TODO Remplacer par une class js
				xIncludeJS(JSXHTMLPart.class),
				xIncludeJS(TKPubSub.class),
				xIncludeJS(JSDataDriven.class),
				xIncludeJS(JSDataSet.class),
				xIncludeJS(JSDataCtx.class),
				xIncludeJS(JSOverlay.class),
				xIncludeJS(JSContainer.class),
				xIncludeJS(JSNavBar.class),
				xIncludeJS(JSPageLayout.class),
				xIncludeJS(TKRouterEvent.class),
				vPart(new CssReset()), 
				vPart(new CssTransition()), // TODO Remplacer
				xIncludeJS(JSTransition.class),
				xIncludeJS(JSMenu.class),
				xIncludeJS(TKActivity.class),
				xIncludeJS(JSViewCard.class));
	}

	@xTarget(HEADER.class)
	@xResource
	@xPriority(2)
	public XMLElement xStyleXUIScene() {

		return cStyle()
				.path("html").set("font-size: 16px;")

				// //+"overflow-y: scroll;"
				// //+ "overflow-x: hidden;" // pour que le 100vw ne prennent pas en compte la
				// largueur du scrollbar
				// + " line-height: 1.5;"
				// + "font-family: 'Roboto', sans-serif;font-weight: normal;")

				.path("body").set("background-color: " + getConfigScene().getBgColorScene() + ";"
						+ "font-family: 'Roboto', sans-serif;"
						+ "font-weight: normal;")

				// ----------------------------------------------------------------
				.path(scene).set("overflow-x: hidden; background-color: " + getConfigScene().getBgColorScene() + ";" 
						+ "min-height: 100vh; ")
				.andChild(cStyle(cShell).set("background-color: " + getConfigScene().getBgColorContent() + ";"
						+ "width: " + widthScene + ";"
						+ "  min-height: 100vh; "))
				.andChild(cStyle("#NavBarShell h1")
						.set("text-align:center;color: inherit;  font-size: 2.1rem; margin-top: 50px"))
				// ----------------------------------------------------------------
				.path(activity)
				.set("background-color: " + getConfigScene().getBgColorContent() + ";"
						+ " width:" + widthScene + ";"
						+ " will-change:overflow,z-index;") // will-change:transform =bug : les header et footer ne se
															// fixe plus au body
				.andChild(cStyle(ViewPageLayout.getcContent())
						.set(" min-height: 100vh; "
								+ "min-width: " + widthScene + "; "
								+ "background-color:" + getConfigScene().getBgColorContent() + ";will-change:contents") 
						)

				// ----------------------------------------------------------------
				.path(ViewPageLayout.getcContent()).set( // ne pas ajouter a cActivity
						"padding-top: " + heightNavBar + "; "
								+ "padding-bottom: " + heightTabBar)

		;
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenuXUIScene() {
		return xListNode(
				vPart(new ViewMenu()),
				xDiv(scene, xDiv(cShell,
						vPart(new ViewNavBar().vProp(ViewNavBar.pId, "NavBarShell"), xH1("Loading...")),
						vPart(new ViewLoader()))));
	}

	@xTarget(AFTER_CONTENT.class)
	@xPriority(500)
	@xResource
	public XMLElement xImportStartXUIScene() {
		return xListNode(
				xScriptJS(js()
						// a mettre dans TKConfig
						._set($xui(),
								"{ intent: { nextActivityAnim : 'fromBottom' }, tkrouter:'', config:{ }, resourceLoading:{} }")
						._set("$xui.resourceLoading.query", true)
						._set("$xui.resourceLoading.hammer", true)
						._set("$xui.resourceLoading.navigo", true)
						._set("$xui.resourceLoading.fastdom", true)
						._set("$xui.resourceLoading.chart", false)

						._set("window.doOnResLoad", fct("res", "id")
								.systemDebugIf(TKCoreConfig.debugAsyncResource, txt("ressource loaded ="), "id") // res.src.split('/').pop()
								.__()
								._if("id==" + XUICstRessource.ID_RES_JQUERY)
								.__(getIntializeJSFct())
								.__("(", getEventManager(), ")()")
								._set("$xui.resourceLoading.query", false)
								.endif()
								._if("id==" + XUICstRessource.ID_RES_NAVIGO)
								._set("$xui.resourceLoading.navigo", false)
								.__("(", getStateManager(), ")()")
								.endif()
								._if("id==" + XUICstRessource.ID_RES_HAMMER)
								._set("$xui.resourceLoading.hammer", false)
								.endif()
								._if("id==" + XUICstRessource.ID_RES_FASTDOM)
								._set("$xui.resourceLoading.fastdom", false)
								.endif()
								._if("id==" + XUICstRessource.ID_RES_CHART)
								._set("$xui.resourceLoading.chart", false)
								.endif()

								._if("!$xui.resourceLoading.hammer && !$xui.resourceLoading.query")
								// .__("(",getMoveManager(),")()")
								.endif()

								._if("!$xui.resourceLoading.hammer && !$xui.resourceLoading.query&& !$xui.resourceLoading.navigo&& !$xui.resourceLoading.fastdom&& !$xui.resourceLoading.chart")
								.__("(", getMoveManager(), ")()")
								/***************************************************************/
								.__(initializeScene())
								.__(createScene())
								/***************************************************************/
								.endif()))

		);
	}

	public JSContentInterface removeAppShell() {
		return fragment()
				.__(TKQueue.startProcessQueued(200, fct() // TODO a gerer dans un event appli ready
						.__($(XUIScene.scene).children(XUIScene.cShell).remove())));
	}

	public JSContentInterface registerServiceWorker() {
		return fragment()
				._if("'serviceWorker' in navigator")
				.__("navigator.serviceWorker.register('sw.js', { scope: '/'} ).then(", // rest/page/
						fct("registration")
								.consoleDebug(txt("ok registration scope"), "registration.scope")
								._set("navigator.serviceWorker.onmessage", fct("e")
										// .consoleDebug(txt("onEventSW"), "e.data")
										._var("v", JQuery.$(ViewLog.cLog, " textArea").val(), jsvar("+ e.data"))
										.__(JQuery.$(ViewLog.cLog, " textArea").val(jsvar("v")))),
						",",
						fct("err").consoleDebug(txt("ServiceWorker registration failed"), "err"), ")")
				.endif();
	}

	static JSMenu jsMenu;
	protected static JSNavBar jsNavBar;
	protected static TKActivity tkActivity;
	protected static JSPageLayout jsPageLayout;

	public JSContentInterface initializeScene() {
		return fragment()
				// ******************** construction du menu
				// ****************************************************
				._var(jsMenu, _new())
				._var("jsonMenu", jsMenu.getData())
				// TODO a changer par un data sur le menu
				._set("window.jsonMainMenu", "jsonMenu") // liste des nemu pour animation dans tkAnimation
				._var(tkActivity, "$xui.tkrouter.activityMgr")

				/***********************************************************************************************/

				/**************************************************************/
				.__(TKQueue.startProcessQueued(100, fct()
						.__(loadPage())
						.__(removeAppShell()), 200,
						fct()
								.__(loadExtendScript())
								.__(searchMenu()),
						500, fct()
								.__(registerServiceWorker())

		))
		/*************************************************************/
		;
	}

	public JSContentInterface searchMenu() {
		return fragment();
	}

	public JSContentInterface loadPage() {
		return fragment();
	}

	public JSContentInterface loadExtendScript() { // TODO a changer : mettre dans une queue avec priorité (avec image)
													// et gestion de promise d'attente
		return fragment()
				// load script notify
				.__(" $.getScript({url:'https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js',  cache: true})");
	}

	public JSContentInterface getIntializeJSFct() {
		return js()

				._set("navigator.vibrate",
						"navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate")

				.__("$.fn.insertAt = function(elements, index){" + // utiliser dans le core
						"var children = this.children();" +
						"if(index >= children.length){" +
						"this.append(elements);" +
						"return this;" +
						"}" +
						"var before = children.eq(index);" +
						"$(elements).insertBefore(before);" +
						"return this;" +
						"}");
	}

	JQuery ripple;
	JQuery btn;

	public JSContentInterface searchRipple() {
		return fragment()
				// recherche le ripple btn
				._var(ripple, btn)
				._if("!", ripple.hasClass(cRippleEffect))
					._set(ripple, btn.closest(cRippleEffect))
					._if("!", ripple.hasClass(cRippleEffect))
						._set(ripple, btn.children(cRippleEffect))
					.endif()
				.endif();
	}

	public JSContentInterface getEventManager() {
		return fct()
				.consoleDebug("'ok EventManager'")
				.__($("body").on("touchstop", fct('e')
						._set("window.stopClientY", "e.originalEvent.touches[0].clientY")))
				.__($("body").on("touchstart", fct('e')
						._set("window.startClientY", "e.originalEvent.touches[0].clientY")
						._set("window.stopClientY", -1)
						._set("window.startScrollTop", $(jsvar("document")).scrollTop())

						._var(btn, $(jsvar("e.target")).closest("[data-x-action]"))

						.__(searchRipple())

						.__("$xui.intent.nextActivityAnim= '" + ConstTransition.ANIM_FROM_BOTTOM + "' ")

						._if("ripple.length>0")
						// TODO A Changer par action sur description
						._if(ripple.hasClass(ViewBtnCircle.cBtnCircle))
						// pas animation ripple
							.__("$xui.intent.nextActivityAnim= '" + ConstTransition.ANIM_FROM_RIPPLE + "'")
						._else()
						// animation ripple
						.__(TKQueue.startProcessQueued(fct(), NEXT_FRAME, fct() // attente ripple effect
								.__(ripple.addClass(cRippleEffectShow)), 
								SPEED_RIPPLE_EFFECT,
								fct() // attente ripple effect
										.__(ripple.removeClass(cRippleEffectShow))

						))
						.endif()
						.endif()));
	}

	public JSContentInterface getMoveManager() {
		// gestion deplacement menu et fermeture par gesture
		return fct().consoleDebug("'ok MoveManager by HAMMER'")
				// .__("$.notify('ok MoveManager', {globalPosition: 'bottom left',
				// className:'warn', autoHideDelay: 2000})")

				// hammer sur le body
				.__("var mc = new Hammer($('body')[0])") // {touchAction: 'auto'}
				// .__("mc.get('pinch').set({ enable: true })")
				.__("mc.get('pan').set({ enable: true, direction: Hammer.DIRECTION_HORIZONTAL })") // DIRECTION_ALL
				.__("mc.get('tap').set({ enable: true, time: 1000 })")

				._var("anim", true)

				.__("mc.on('tap',", fct("e")

						._var("btn", $(jsvar("e.target")).closest("[data-x-action]"))
						._var("event", "btn.data('x-action')")
						._set("window.lastBtn", btn) // TODO a virer

						// .__("$.notify('do Event '+event, {globalPosition: 'bottom left',
						// className:'warn', autoHideDelay: 2000})")

						.__(searchRipple())

						// TODO passer un context d'event au doEvent (ripple, btn, e, event)
						.__(TKQueue.startProcessQueued(fct()
								
								._var("retEvent", $xui().tkrouter().doEvent("event"))
								
								._if("retEvent.cancelRipple")
								._if("ripple.length>0")
								.__("ripple.removeClass('", cRippleEffectShow.getId(), "')")
								.endif()
								.endif())),
						")")

				.__("mc.on('hammer.input',", fct("ev")
						// .consoleDebug("ev.deltaX", "ev")
						// .set("window.velocityY", "ev.velocityY")
						// .set("window.deltaY", "ev.deltaY")

						// .__("$('#content')[0].innerHTML = [ev.srcEvent.type, ev.pointers.length,
						// ev.isFirst, ev.isFinal, ev.deltaX, ev.deltaY, ev.distance, ev.velocity,
						// ev.deltaTime, ev.offsetDirection, ev.target].join('<br>');")
						// **************************** gestion swipe anim du menu
						// *************************/
						._if("$(ev.target).closest('.menu').length > 0")
						// JSInt sct = let( JSInt.class, "sct", $(jsvar("document")).scrollTop() );
						._var("sct", $(jsvar("document")).scrollTop())
						._if("ev.deltaX>-100 && ev.offsetDirection==2 && ev.velocity>-1 ")
						._if("anim==true")
						// bouge en fonction delta
						.__("$('.menu').css('transition', '' )")
						.__("$('.menu').css('transform', 'translate3d('+ev.deltaX+'px,'+sct+'px,0px)' )")
						.endif()
						._elseif_("anim==true && ev.offsetDirection==2 ")
						._set("anim", "false")
						// ferme le menu par le doOverlay avec une animation
						.__("$('.menu').css('transition', 'transform " + (SPEED_SHOW_MENU + 50) + "ms ease-out' )")
						.__($xui().tkrouter().doEvent("'Overlay'"))
						.endif()

						._if("ev.isFinal")
						// lance l'animation de retour a l'ouverture
						._if("anim==true")
						.__("$('.menu').css('transition', 'transform " + (SPEED_SHOW_MENU + 50) + "ms ease-out' )")
						.__("$('.menu').css('transform', 'translate3d(0px,'+sct+'px,0px)' )")
						.endif()
						._set("anim", "true")
						.endif()
						.endif()
		// ***************************************************************************************/
						, ")");
	}

	TKRouterEvent tkrouter;

	public JSContentInterface getStateManager() {
		return fct()
				// .consoleDebug("'ok StateManager'")
				._set("nav", "new Navigo(null,true,'#!')") // null,true,'!#')")
				._var(tkrouter, _new("nav"))
				._set($xui().tkrouter(), tkrouter);

	}

}
