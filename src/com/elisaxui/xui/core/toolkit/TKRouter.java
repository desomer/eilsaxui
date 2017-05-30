/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;

/**
 * @author Bureau
 *
 */
public interface TKRouter extends JSClass {

	TKAnimation tkAnimation = null;
	TKRouter _this = null;
	TKRouter _self = null;
	Object navigo = null;
	TKActivity activityMgr= null;
	Object historyIntent = null;
	Object _historyIntent = null;
	
	default Object constructor(Object nav) {
		__()
		.set(tkAnimation, _new())
	    .set(activityMgr, _new())
	    
//	.__("router.resolve()")
		.set(navigo, nav)
		.var(_self, _this)
		
		.var("h", fct("params","query")   // ecoute l'history back
				._if(_self,".navigo.nextenable")
				
				    .var("toRoute", _self,".navigo._lastRouteResolved")
					.var("backFromIntent", _self.isBack("toRoute.url"))
					.var("fromIntent", _self.geCurrentIntention())
					
					.consoleDebug("'pushState ENABLE action=', this.toString() , ' toRoute =',toRoute,' backFromIntent =', backFromIntent, 'fromIntent=', fromIntent")
					
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
						.consoleDebug("'intent='", "intent")
						.__(_self.doAction(txt("toggleMenu")))
					.endif()
				
					/**************** gestion route activity  **********************/
					._if("backFromIntent!=null && this.toString()=='route'")
					
						.set("$xui.intent.prevActivity", "params.url")     //TODO a changer
					    .set("$xui.intent.activity", "tkActivity.idCurrentActivity")
					    .set("$xui.intent.action", "'route'")
					    .set("$xui.intent.nextActivityAnim", "backFromIntent.nextActivityAnim")
						.__(_self.doAction(txt("backRoute")))
						
					._elseif("this.toString()=='route'") // is next
					
						.set("$xui.intent.prevActivity", "tkActivity.idCurrentActivity")
					    .set("$xui.intent.activity", "params.url")
					    .set("$xui.intent.action", "'route'")
					    .set("$xui.intent.url", "toRoute.url")
					    .var("intent", "{}")
					    .__("$.extend(intent, $xui.intent)")
						.__(_self,".",_historyIntent,".push(intent)")   // ajoute a l historique interne
					 //   .consoleDebug("'intent='", "intent")
						.__(_self.doAction(txt("route")))
					.endif()
					/******************************************/
				._else()
					.__("console.debug(",txt("pushState DISABLE param=<"), ",params, '> query =<', query, '> action=', this, ' nextEnable=',", _self,".navigo.nextenable)")
					.__(_self,".navigo.nextenable=", true)
				.endif()
			)

		.__("nav.on("
				+ "{"
				+ " 'route/:url': { as: 'route', uses: h.bind('route') },"  
				+ " 'menu': { as: 'menu', uses: h.bind('menu') },"  
			//	+ " 'home' : { as: 'home', uses: h.bind('home') }"   
				+ "})")
		
		.__("nav.notFound(", fct("query").consoleDebug("'notFound navigo ='", "query") ,")")
		
		.set(historyIntent,"[]")
		.__(navigo,".nextenable=", false)
		.__(_this.doNavigate(txt("route/Activity1")))   // premier etat
		.var("intent", "{ url:'route/Activity1', nextActivityAnim : 'fromBottom' }")
		.__(historyIntent,".push(intent)")   // ajoute a l historique interne)
		.__(navigo,".resolve()")
		;
		return null;
	}
	
	default Object doEvent(Object event)
	{
		 __()   // action lancer au click
		._if("!window.animInProgess && event!=null")
		   .var("currentIntent",   historyIntent, "[", historyIntent, ".length-1]")
		   .consoleDebug(txt("do event"), "event","' currentIntent ='", "currentIntent")
		   .__("if (navigator.vibrate) { navigator.vibrate(30); }")    // je vibre
		   
		   
		   .var("jsonAct", activityMgr.getCurrentActivity())
		   .consoleDebug("'jsonAct'", "jsonAct")
		   .var("jsonAction", "jsonAct.events[event]")
		   
		   ._if("jsonAction!=null &&jsonAction.action=='route' ")
		   		.__(_this.doNavigate("jsonAction.url"))  
		   .endif()
		   
		   ._if("jsonAction!=null &&jsonAction.action=='back' ")
		   		.__(_this.doBack())  
	   		.endif()
		   
		   
//		   ._if("event=='BtnFloatMain' || event=='more' || event=='HeaderSwipeDown' ")
//		   			.__(navigo,".nextenable=", true)
//					.__(_this.doNavigate(txt("open/Activity2?p=1"))) // ajoute history pour le back   /test/act1/read?p=1
//			.endif()
			
			._if("event=='burger' || event=='Overlay'")
				._if(historyIntent, "[",historyIntent,".length-1].url=='menu'")  // a ameliorer
					.__(_this.doBack()) 
				._else()
					.__(_this.doNavigate(txt("menu")))
				.endif()
			.endif()
		.endif();
		 return null;
	}	
	
	default Object doNavigate(Object uri)
	{
		__()
		.consoleDebug(txt("doNavigate"), "uri")
		.__(navigo,".navigate(uri)")
		;
		return null;
	}
	
	
	default Object isBack(Object change)
	{
		__()
		._if(historyIntent,".length>1 && ",historyIntent, "[",historyIntent,".length-2].url==", change )
			.__("return ",historyIntent,".pop()")
		.endif()
		;
		return null;
	}

	default Object geCurrentIntention()
	{ 
		__("return ", historyIntent, "[", historyIntent ,".length-1]")
		;
		return null;
	}
	
	
	default Object doBack()
	{
		__("history.go(-1)")
		;
		return null;
	}
	
	default Object doAction(Object action)
	{
		 __()
		 .var(_self, _this)   //  self.geCurrentIntention())
		 
		 ._if("action=='route'")
		 	.var("actAnim", geCurrentIntention(), ".nextActivityAnim")
		 	.__(activityMgr.setCurrentActivity("$xui.intent.activity"))  
			 .consoleDebug(txt("doAction"), "action", "'anim='", "actAnim")
		 	
		    ._if("actAnim=='fromBottom'")
		    	.__(tkAnimation.doOpenActivityFromBottom())
		    ._else()
		    	.__(tkAnimation.doOpenActivityFromOpacity())
		    .endif()
	     .endif()
	     
		 ._if("action=='backRoute'")  
		 	.var("actAnim", "$xui.intent.nextActivityAnim")
		 	.__(activityMgr.setCurrentActivity("$xui.intent.prevActivity"))   
			.consoleDebug(txt("doAction"), "action", "'anim='", "actAnim")
		    ._if("actAnim=='fromBottom'")
		    	.__(tkAnimation.doOpenActivityFromBottom())
		    ._else()
		    	.__(tkAnimation.doOpenActivityFromOpacity())
		    .endif()
	     .endif()
	     
		 ._if("action=='toggleMenu'")
		 	.__(tkAnimation.doToggleBurgerMenu())
		 .endif();
		 return null;
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
