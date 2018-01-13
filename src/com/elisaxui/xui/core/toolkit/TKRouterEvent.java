/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import static com.elisaxui.xui.core.toolkit.json.JXui.$xui;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.xui.core.config.TKCoreConfig;
import com.elisaxui.xui.core.toolkit.json.JIntent;
import com.elisaxui.xui.core.toolkit.json.JRoute;
import com.elisaxui.xui.core.transition.ConstTransition;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.transition.JSTransition;

/**
 * @author Bureau
 *
 *
 *
 *    doEvent -> doNavigate (ou doBack)  -> doPushState ->  doIntent -> doAction  (route ou backRoute)
 *
 *			  -> doAction 
 *
 */
public interface TKRouterEvent extends JSClass {

	/**
	 * 
	 */
	public static final String ACTION_TOGGLE_MENU = "ACTION_TOGGLE_MENU";
	public static final String ACTION_PREV_ROUTE = "ACTION_PREV_ROUTE";
	public static final String ACTION_NEXT_ROUTE = "ACTION_NEXT_ROUTE";
	public static final String STATE_ROUTE = "STATE_ROUTE";
	public static final String STATE_MENU = "STATE_MENU";
	public static final String URL_MENU = "menu";
	
	JSTransition tkAnimation = null;
	TKActivity activityMgr= null;
	
	JSArray<JIntent> historyIntent();    //   gestion des historique d'intention
	JSon navigo();

	default Object constructor(Object nav) {
		set(tkAnimation, _new());
	    set(activityMgr, _new());
	    
//	.__("nav.resolve()")
		navigo().set(nav);
		TKRouterEvent self = let(TKRouterEvent.class, "self", "this");
		
		// this est un string (bind)
		// ecoute l'history back
		JSCallBack doPushState = let(JSCallBack.class, "doPushState", fct("params","query").__(()->{   
				_if(self.navigo().get("nextenable"));
				
				    JRoute toRoute = let(JRoute.class, "toRoute", self.navigo().get("_lastRouteResolved"));
				    _if(toRoute.url().substring(0, 1).isEqual("!"));
				    	toRoute.url().set(toRoute.url().substring(1));
				    endif();
	
					JIntent fromIntent =  let("fromIntent", self.geCurrentIntention());
				    JIntent backFromIntent = let("backFromIntent", self.isBackIntention(toRoute.url()));
					
					systemDebugIf(TKCoreConfig.debugPushState, "'pushState ENABLE action=', this.toString() , ' toRoute =',toRoute,' backFromIntent =', backFromIntent, 'fromIntent='", fromIntent)	;				
					/**************** gestion toogle menu  **********************/
					_if(backFromIntent,"!=null &&", backFromIntent.url().isEqual(URL_MENU));
						self.doAction(txt(ACTION_TOGGLE_MENU));
						_return() ;   // exit 
					_elseif_("this.toString()==",txt(STATE_MENU)); // is next
					
						set("$xui.intent.action", txt(ACTION_TOGGLE_MENU));
						set("$xui.intent.url", txt(URL_MENU));
						_var("intent", "{}");
						__("$.extend(intent, $xui.intent)");
						
						__(self.historyIntent().push(cast(JIntent.class, "intent"))) ;  // ajoute a l historique interne
//						.consoleDebug("'intent='", "intent")
						__(self.doAction(txt(ACTION_TOGGLE_MENU)));
					endif();
				
					/**************** gestion route activity  **********************/
					_if(backFromIntent,"!=null && this.toString()==", txt(STATE_ROUTE));
						 // to Prev Route
						set("$xui.intent.prevActivity", "params.url") ;    //TODO a changer
					    set("$xui.intent.activity", "$xui.tkrouter.activityMgr.idCurrentActivity");
					    set("$xui.intent.nextActivityAnim", "backFromIntent.nextActivityAnim");
					    set("$xui.intent.url", txt("?"));
					    
					    set("$xui.intent.action", txt(ACTION_PREV_ROUTE));
						__(self.doAction(txt(ACTION_PREV_ROUTE)));
						
					_elseif_("this.toString()==", txt(STATE_ROUTE)); // is next
						// to Next Route
						set("$xui.intent.prevActivity", "$xui.tkrouter.activityMgr.idCurrentActivity");
					    set("$xui.intent.activity", "params.url");
					    set("$xui.intent.url", "toRoute.url");
					    
					    set("$xui.intent.action", txt(ACTION_NEXT_ROUTE));
					    _var("intent", "{}");
					    __("$.extend(intent, $xui.intent)");
//						.consoleDebug("'intent='", "intent")
					    
						__(self.historyIntent().push(cast(JIntent.class, "intent")));   // ajoute a l historique interne
						__(self.doAction(txt(ACTION_NEXT_ROUTE)));
					endif();
					/******************************************/
				_else();
					systemDebugIf(TKCoreConfig.debugPushState,txt("pushState DISABLE param=<"), "params, '> query =<', query, '> action=', this, ' nextEnable='", self+".navigo.nextenable");
					__(self.navigo(),".nextenable=", true);
				endif();

				})
			);


		__("nav.on("
				+ "{"
				+ " 'route/:url': { as: 'route', uses: "+doPushState+".bind('"+STATE_ROUTE+"') },"  
				+ " 'menu': { as: '"+URL_MENU+"', uses: "+doPushState+".bind('"+STATE_MENU+"') },"	/**TODO change la ligne menu */
			//	+ " 'home' : { as: 'home', uses: doPushState.bind('home') }"   
				+ "})");
		
		/**TODO change par un affichage user */
		__("nav.notFound(", fct("query").consoleDebug("'notFound navigo ='", "query") ,")");
		
		// set(historyIntent(),"[]");
		historyIntent().set("[]");
		self.doInitialize();
		;
		return _void();
	}
	
	default TKActivity activityMgr()
	{
		_return(activityMgr);
		return null;
	}
	
	
	//JIntent _intent = defVar();
	default Object doInitialize()
	{
		__( navigo(),".nextenable=", false);  // DISABLE PUSH STATE
		__(doNavigate(txt("!route/Activity1")));   // premier etat
		_var("intent", "{ url:'route/Activity1', nextActivityAnim : 'fromBottom' }");
		__(historyIntent().push(cast(JIntent.class, "intent")));   // ajoute a l historique interne)
		__( navigo(),".resolve()");
		
//		var(_intent,   historyIntent, "[", historyIntent, ".length-1]");
//		var("urlinent", _intent.url());
//		var("urlinent2", _intent.activity().name());
		return _void();
	}
		
	default Object doEvent(Object event)
	{
		_var("retEvent", "{ cancelRipple:false }");
		
		_if("!window.animInProgess && event!=null");
		
			   _var("currentIntent",   historyIntent(), "[", historyIntent(), ".length-1]");
			   
			   _var("jsonAct", activityMgr.getCurrentActivity());
			   systemDebugIf(TKCoreConfig.debugDoEvent, txt("do event <", var("event"), "> on"), "jsonAct", txt(" intent is"), "currentIntent"); 
			   _var("jsonAction", "jsonAct.events[event]");
			   
			   _if("jsonAction!=null &&jsonAction.action=='route' ");
			   		__("if (navigator.vibrate) { navigator.vibrate(30); }")  ;  // je vibre
//					__("$.notify('doNavigate to '+jsonAction.url, {globalPosition: 'bottom left', className:'info', autoHideDelay: 2000})")
			   		__(doNavigate("jsonAction.url")) ; 
			   		__("retEvent.cancelRipple=true");
			   endif();
			   
			   _if("jsonAction!=null &&jsonAction.action=='back' ");
			   		__("if (navigator.vibrate) { navigator.vibrate(30); }") ;   // je vibre
			   		 doBack() ; 
			   		__("retEvent.cancelRipple=true");
			   endif();
	   		
		   	   _if("jsonAction!=null &&jsonAction.action=='callback' ");
			   		__("window[jsonAction.fct].call(this, jsonAction, currentIntent)")  ;
			   endif();
		   
		   
//		   ._if("event=='BtnFloatMain' || event=='more' || event=='HeaderSwipeDown' ")
//		   			.__(navigo,".nextenable=", true)
//					.__(_this.doNavigate(txt("open/Activity2?p=1"))) // ajoute history pour le back   /test/act1/read?p=1
//			.endif()
			
			_if("event=='burger' || event=='Overlay'");
				__("if (navigator.vibrate) { navigator.vibrate(30); }") ;   // je vibre
				_if(historyIntent(), "[",historyIntent(),".length-1].url=='menu'");  // a ameliorer
					doBack(); 
				_else();
					__(doNavigate(txt("menu")));
				endif();
			endif();
			
		_else();
		    _if("event!=null");
//				.__("$.notify('pb event anim prog '+window.animInProgess+' event='+event, {globalPosition: 'bottom left', className:'error', autoHideDelay: 2000})")
		    endif();
		endif();
		
		return "retEvent";
	}	
	
	default Object doNavigate(Object uri)
	{
		systemDebugIf(TKCoreConfig.debugDoNavigate , txt("doNavigate"), "uri");
		__( navigo(),".navigate(uri)");
		return _void();
	}
	
	
	default JIntent isBackIntention(Object change)
	{
		_if(historyIntent().length(),">1 && ",historyIntent().at(calc(historyIntent().length() ,"-2")),".url==", change ); //TODO retirer le calc
			_return(historyIntent().pop());
		endif();
		;
		return cast(JIntent.class, "null");  //TODO a faire marcher return null 
	}

	default JIntent geCurrentIntention()
	{ 
		return cast(JIntent.class, historyIntent().at( calc(historyIntent().length() ,"-1")));   //TODO retirer le calc
	}
	
	
	default void doBack()
	{
		__("history.go(-1)")
		;
	}
	
	default void doCancel()
	{
 		systemDebugIf(TKCoreConfig.debugDoAction, txt("REMOVE HISTORY"), "$xui.intent");
 		__( navigo(),".nextenable=false");
 		historyIntent().pop();
 		__("history.go(-1)");
		;
	}
	
	default Object doTraceHisto()
	{
		_var("currentActivity", $xui().tkrouter().activityMgr().getCurrentActivity());
 		systemDebugIf(TKCoreConfig.debugPushState, txt("DO TRACE HISTORY'"), "currentActivity", txt(" histo intent "), historyIntent() );
		return _void();
	}
	
	default Object doAction(Object action)
	{
		 /*******************************************************************/
		 _if("action==",txt(ACTION_NEXT_ROUTE));

		 	JIntent currentIntent = let("currentIntent", geCurrentIntention());
//		 	var("act1", "'#'+$xui.intent.activity");
		 	JQuery jqAct1 = let(JQuery.class, "jqAct1", JQuery.$(calc("'#'+",currentIntent.activity())));
		 
		 	_if(jqAct1.hasClass(CssTransition.cStateBackActivity));
		 		systemDebugIf(TKCoreConfig.debugDoAction, txt("PB doAction Activity used to intent "), currentIntent);
				__("$.notify('pb activity is used '+currentIntent.activity, {globalPosition: 'bottom left', className:'error', autoHideDelay: 2000})");
		 		doCancel();
	 			_return();
		 	endif();
		 	
//		    .__(_self.doEvent(TKActivity.ON_ACTIVITY_RESUME))
		 	
		 	_var("actAnim", currentIntent.nextActivityAnim());
			 systemDebugIf(TKCoreConfig.debugDoAction, txt("doAction anim=<"), "actAnim", "'> to intent '", currentIntent);
			 __(activityMgr.setCurrentActivity(new JSString()._setContent("$xui.intent.activity")))  ;
			 
		    _if("actAnim=='"+ConstTransition.ANIM_FROM_BOTTOM+"'");
		    	__(tkAnimation.doOpenActivityFromBottom());
		    _elseif_("actAnim=='"+ConstTransition.ANIM_FROM_RIPPLE+"'");
		    	__(tkAnimation.doOpenActivityFromRipple());
		    endif();
	     endif();
	     /**************************************************************/
		 _if("action==",txt(ACTION_PREV_ROUTE))  ;
		 	_var("actAnim", "$xui.intent.nextActivityAnim");
		 	
		 	systemDebugIf(TKCoreConfig.debugDoAction, txt("doAction "), "action", "' to intent '", "$xui.intent");
		 	__(activityMgr.setCurrentActivity(new JSString()._setContent("$xui.intent.prevActivity")))   ;
//		    __(_self.doEvent(TKActivity.ON_ACTIVITY_RESUME))
			
		 	_if("actAnim=='"+ConstTransition.ANIM_FROM_BOTTOM+"'");
		    	__(tkAnimation.doOpenActivityFromBottom());
		    _elseif_("actAnim=='"+ConstTransition.ANIM_FROM_RIPPLE+"'");
		    	__(tkAnimation.doOpenActivityFromRipple());
		    endif();
	     endif();
	     /**************************************************************/
		 _if("action==",txt(ACTION_TOGGLE_MENU));
		 	systemDebugIf(TKCoreConfig.debugDoAction, txt("doAction"), "action");
		 	__(tkAnimation.doToggleBurgerMenu());
		 endif();
		 
		 return _void();
	}

	/*****************************************************************************/
	
//	.var("handler", fct("params","query")
//	.__("console.debug(params,query)")
//	.consoleDebug("router._lastRouteResolved")
//	.consoleDebug("this.toString()", "History.length")
//	)
//
//.__("router=new Navigo(null,true)")   //   null,true,'!#')")
//.__("router.on("
//	+ "{'/trip/:tripId/edit': { as: 'trip.edit', uses: handler.bind('trip.edit') },"
//	+ " '/trip/save': { as: 'trip.save', uses: handler.bind('trip.save') },"
//	+ " '/trip/:action/:tripId': { as: 'trip.action', uses: handler.bind('trip.action') },"
//	+ " '*' : { as: 'home', uses: handler.bind('home') }" 
//	+ "})")
//.__("router.resolve()")
//.var("viewHistory", "[]")
////	.__("router.navigate('*');")
//.__("setTimeout(", fct().__("router.navigate('/trip/save?p=1')") ,",2000)")
//.__("setTimeout(", fct().__("router.navigate('/trip/12/edit?p=2')") ,",4000)")
//.__("setTimeout(", fct().__("router.navigate('?p=3')") ,",6000)")
////	.__("router.navigate('/trip/12/edit');")	

	
	/*if (viewHistory.indexOf(nextView) > 0) {
    // *** Back Button Clicked ***
    // this logic assumes that there is never a recipeList nested
    // under another recipeList in the view hierarchy
    animateBack(nextView);

    // don't forget to remove 'recipeList' from the history
    viewHistory.splice(viewHistory.indexOf(nextView), viewHistory.length);
} else {
    // *** They arrived some other way ***
    animateForward(nextView);
} 

When invoking pushState give the data object a unique incrementing id (uid).
When onpopstate handler is invoked; check the state uid against a persistent variable containing the last state uid.
Update the persistent variable with the current state uid.
Do different events depending on if state uid was greater or less than last state uid.
*
*/	
}
