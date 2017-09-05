/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import static com.elisaxui.xui.core.toolkit.json.JXui.$xui;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.xui.core.transition.ConstTransition;
import com.elisaxui.xui.core.transition.TKTransition;

/**
 * @author Bureau
 *
 *
 *
 *    doEvent -> doNavigate (ou doBack)  -> doPushState ->  doIntent -> doAction  (route ou backRoute)
 *
 *			  -> doAction  (a faire)  	
 *
 */
public interface TKRouterEvent extends JSClass {

	public static final String ACTION_PREV_ROUTE = "ACTION_PREV_ROUTE";
	public static final String ACTION_NEXT_ROUTE = "ACTION_NEXT_ROUTE";
	public static final String STATE_ROUTE = "STATE_ROUTE";
	
	TKTransition tkAnimation = null;
	JSVariable navigo = null;
	TKActivity activityMgr= null;
	
	JSVariable historyIntent = null;  //   gestion des historique d'intention
	
	JSVariable _historyIntent = null;
	TKRouterEvent _self = null;
	

	default Object constructor(Object nav) {
		__()
		.set(tkAnimation, _new())
	    .set(activityMgr, _new())
	    
//	.__("router.resolve()")
		.set(navigo, nav)
		.var(_self, _this())
		
		.var("doPushState", fct("params","query")   // ecoute l'history back
				._if(_self,".navigo.nextenable")
				
				    .var("toRoute", _self,".navigo._lastRouteResolved")
				    ._if("toRoute.url.substring(0, 1) == '!'")
				    	.__("toRoute.url = toRoute.url.substring(1)")
				    .endif()
	
					.var("backFromIntent", _self.isBack("toRoute.url"))
					.var("fromIntent", _self.geCurrentIntention())
					.systemDebugIf(TKConfig.debugPushState, "'pushState ENABLE action=', this.toString() , ' toRoute =',toRoute,' backFromIntent =', backFromIntent, 'fromIntent=', fromIntent")					
					/**************** gestion toogle menu  **********************/
					._if("backFromIntent!=null && backFromIntent.url=='menu'")
						.__(_self.doAction(txt("toggleMenu")))
						.__("return")    // exit 
					._elseif("this.toString()=='menu'") // is next
					
						.set("$xui.intent.action", "'toggleMenu'")
						.set("$xui.intent.url", "'menu'")
						.var("intent", "{}")
						.__("$.extend(intent, $xui.intent)")
						
						.__(_self,".",_historyIntent,".push(intent)")   // ajoute a l historique interne
//						.consoleDebug("'intent='", "intent")
						.__(_self.doAction(txt("toggleMenu")))
					.endif()
				
					/**************** gestion route activity  **********************/
					._if("backFromIntent!=null && this.toString()==", txt(STATE_ROUTE))
						 // to Prev Route
						.set("$xui.intent.prevActivity", "params.url")     //TODO a changer
					    .set("$xui.intent.activity", "$xui.tkrouter.activityMgr.idCurrentActivity")
					    .set("$xui.intent.nextActivityAnim", "backFromIntent.nextActivityAnim")
					    .set("$xui.intent.url", txt("?"))
					    
					    .set("$xui.intent.action", txt(ACTION_PREV_ROUTE))
						.__(_self.doAction(txt(ACTION_PREV_ROUTE)))
						
					._elseif("this.toString()==", txt(STATE_ROUTE)) // is next
						// to Next Route
						.set("$xui.intent.prevActivity", "$xui.tkrouter.activityMgr.idCurrentActivity")
					    .set("$xui.intent.activity", "params.url")
					    .set("$xui.intent.url", "toRoute.url")
					    
					    .set("$xui.intent.action", txt(ACTION_NEXT_ROUTE))
					    .var("intent", "{}")
					    .__("$.extend(intent, $xui.intent)")
//						.consoleDebug("'intent='", "intent")
					    
						.__(_self,".",_historyIntent,".push(intent)")   // ajoute a l historique interne
						.__(_self.doAction(txt(ACTION_NEXT_ROUTE)))
					.endif()
					/******************************************/
				._else()
					.systemDebugIf(TKConfig.debugPushState,txt("pushState DISABLE param=<"), "params, '> query =<', query, '> action=', this, ' nextEnable='", _self+".navigo.nextenable")
					.__(_self,".navigo.nextenable=", true)
				.endif()
			)

		/**TODO change la ligne menu */
		.__("nav.on("
				+ "{"
				+ " 'route/:url': { as: 'route', uses: doPushState.bind('"+STATE_ROUTE+"') },"  
				+ " 'menu': { as: 'menu', uses: doPushState.bind('menu') },"  
			//	+ " 'home' : { as: 'home', uses: doPushState.bind('home') }"   
				+ "})")
		
		/**TODO change par un affichage user */
		.__("nav.notFound(", fct("query").consoleDebug("'notFound navigo ='", "query") ,")")
		
		.set(historyIntent,"[]")
		.__(_self.doInitialize())
		;
		return _void();
	}
	
	default TKActivity activityMgr()
	{
		_return(activityMgr);
		
		//attr("activityMgr");
		return null;
	}
	
	
	default Object doInitialize()
	{
		 __()
		.__(navigo,".nextenable=", false)  // DISABLE PUSH STATE
		.__(doNavigate(txt("!route/Activity1")))   // premier etat
		.var("intent", "{ url:'route/Activity1', nextActivityAnim : 'fromBottom' }")
		.__(historyIntent,".push(intent)")   // ajoute a l historique interne)
		.__(navigo,".resolve()")
		;
		
		return _void();
	}
		
	default Object doEvent(Object event)
	{
		var("retEvent", "{ cancelRipple:false }")
		
		._if("!window.animInProgess && event!=null")
		
			   .var("currentIntent",   historyIntent, "[", historyIntent, ".length-1]")
			   
			   .var("jsonAct", activityMgr.getCurrentActivity())
			   .systemDebugIf(TKConfig.debugDoEvent, txt("do event <", jsvar("event"), "> on"), "jsonAct", txt(" intent is"), "currentIntent") 
			   .var("jsonAction", "jsonAct.events[event]")
			   
			   ._if("jsonAction!=null &&jsonAction.action=='route' ")
			   		.__("if (navigator.vibrate) { navigator.vibrate(30); }")    // je vibre
//					.__("$.notify('doNavigate to '+jsonAction.url, {globalPosition: 'bottom left', className:'info', autoHideDelay: 2000})")
			   		.__(doNavigate("jsonAction.url"))  
			   		.__("retEvent.cancelRipple=true")
			   .endif()
			   
			   ._if("jsonAction!=null &&jsonAction.action=='back' ")
			   		.__("if (navigator.vibrate) { navigator.vibrate(30); }")    // je vibre
			   		.__( doBack())  
			   		.__("retEvent.cancelRipple=true")
			   .endif()
	   		
		   	   ._if("jsonAction!=null &&jsonAction.action=='callback' ")
			   		.__("window[jsonAction.fct].call(this, jsonAction, currentIntent)")  
			   .endif()
		   
		   
//		   ._if("event=='BtnFloatMain' || event=='more' || event=='HeaderSwipeDown' ")
//		   			.__(navigo,".nextenable=", true)
//					.__(_this.doNavigate(txt("open/Activity2?p=1"))) // ajoute history pour le back   /test/act1/read?p=1
//			.endif()
			
			._if("event=='burger' || event=='Overlay'")
				.__("if (navigator.vibrate) { navigator.vibrate(30); }")    // je vibre
				._if(historyIntent, "[",historyIntent,".length-1].url=='menu'")  // a ameliorer
					.__(doBack()) 
				._else()
					.__(doNavigate(txt("menu")))
				.endif()
			.endif()
		._else()
		    ._if("event!=null")
//				.__("$.notify('pb event anim prog '+window.animInProgess+' event='+event, {globalPosition: 'bottom left', className:'error', autoHideDelay: 2000})")
		    .endif()
		.endif();
		
		 return "retEvent";
	}	
	
	default Object doNavigate(Object uri)
	{
		systemDebugIf(TKConfig.debugDoNavigate , txt("doNavigate"), "uri")
		.__(navigo,".navigate(uri)")
		;
		return _void();
	}
	
	
	default Object isBack(Object change)
	{
		__()
		._if(historyIntent,".length>1 && ",historyIntent, "[",historyIntent,".length-2].url==", change )
			.__("return ",historyIntent,".pop()")
		.endif()
		;
		return _null();
	}

	default Object geCurrentIntention()
	{ 
		/** TODO A changer double return */
		return __("return ", historyIntent, "[", historyIntent ,".length-1]");
	}
	
	
	default Object doBack()
	{
		__("history.go(-1)")
		;
		return _void();
	}
	
	default Object doCancel()
	{
 		systemDebugIf(TKConfig.debugDoAction, txt("REMOVE HISTORY'", "$xui.intent"))
		.__(_this(),".navigo.nextenable=false")
 		.__(historyIntent,".pop()")
 		.__("history.go(-1)")
		;
		return _void();
	}
	
	default Object doTraceHisto()
	{
		var("currentActivity", $xui().tkrouter().activityMgr().getCurrentActivity())
 		.systemDebugIf(TKConfig.debugPushState, txt("DO TRACE HISTORY'"), "currentActivity", txt(" histo intent "), historyIntent )
		;
		return _void();
	}
	
	default Object doAction(Object action)
	{
		 var(_self, _this())   //TODO Bug si je retire dans le geCurrentIntention
		 
		 /*******************************************************************/
		 ._if("action==",txt(ACTION_NEXT_ROUTE))
		 	.var("actAnim", geCurrentIntention(), ".nextActivityAnim")
		 	
		 	.var("act1", "'#'+$xui.intent.activity")
		 	.var("jqAct1", "$(act1)")
		 	._if("jqAct1.hasClass('backActivity')")
		 		.systemDebugIf(TKConfig.debugDoAction, txt("PB doAction Activity used to intent '", "$xui.intent"))
				.__("$.notify('pb activity is used '+act1, {globalPosition: 'bottom left', className:'error', autoHideDelay: 2000})")
		 		.__(doCancel())
	 			.__("return")
		 	.endif()
		 	
//		    .__(_self.doEvent(TKActivity.ON_ACTIVITY_RESUME))
		 	
			 .systemDebugIf(TKConfig.debugDoAction, txt("doAction anim=<"), "actAnim", "'> to intent '", "$xui.intent")
			 .__(activityMgr.setCurrentActivity("$xui.intent.activity"))  
			 
			 
		    ._if("actAnim=='"+ConstTransition.ANIM_FROM_BOTTOM+"'")
		    	.__(tkAnimation.doOpenActivityFromBottom())
		    ._elseif("actAnim=='"+ConstTransition.ANIM_FROM_RIPPLE+"'")
		    	.__(tkAnimation.doOpenActivityFromRipple())
		    .endif()
	     .endif()
	     /**************************************************************/
		 ._if("action==",txt(ACTION_PREV_ROUTE))  
		 	.var("actAnim", "$xui.intent.nextActivityAnim")
		 	
		 	.systemDebugIf(TKConfig.debugDoAction, txt("doAction "), "action", "' to intent '", "$xui.intent")
		 	.__(activityMgr.setCurrentActivity("$xui.intent.prevActivity"))   
//		    .__(_self.doEvent(TKActivity.ON_ACTIVITY_RESUME))
			
		 	._if("actAnim=='"+ConstTransition.ANIM_FROM_BOTTOM+"'")
		    	.__(tkAnimation.doOpenActivityFromBottom())
		    ._elseif("actAnim=='"+ConstTransition.ANIM_FROM_RIPPLE+"'")
		    	.__(tkAnimation.doOpenActivityFromRipple())
		    .endif()
	     .endif()
	     /**************************************************************/
		 ._if("action=='toggleMenu'")
		 	.systemDebugIf(TKConfig.debugDoAction, txt("doAction"), "action")
		 	.__(tkAnimation.doToggleBurgerMenu())
		 .endif();
		 
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