/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;

/**
 * @author Bureau
 *
 */
public interface TKRouter extends JSClass {

	JSNavBar jsNavBar = null;
	TKRouter _this = null;
	TKRouter _self = null;
	Object navigo = null;
	
	default Object constructor(Object nav) {
		__()
		.set(jsNavBar, _new())	

//	.__("router.resolve()")
		.set(navigo, nav)
		.var(_self, _this)
		
		.var("h", fct("params","query")
				.__("console.debug(params,query)")
				._if(_self,".navigo.nextenable")
				    .var("lastRoute", _self,".navigo._lastRouteResolved")
					.consoleDebug("lastRoute")
					.consoleDebug("this.toString()", "History.length")
					.var("backEvent", _self.isBack("this.toString()"))
					
					._if("backEvent=='menu'")
						.__(_self.doAction(txt("menu")))
					._elseif("this.toString()=='menu'") // is next
					//	.__("history.replaceState(null,null,'#home')")
					.endif()
					
					._if("backEvent=='open'")
						.__(_self.doAction(txt("open")))
					._elseif("this.toString()=='open'") // is next
					//	.__("history.replaceState(null,null,'#home')")
					.endif()
				._else()
					.__(_self,".navigo.nextenable=", true)
				.endif()
			)

		.__("nav.on("
				+ "{"
				+ " 'open': { as: 'open', uses: h.bind('open') },"
				+ " 'menu': { as: 'menu', uses: h.bind('menu') },"
				+ " 'home' : { as: 'home', uses: h.bind('home') }" 
				+ "})")
		
		.__(navigo,".history=[]")
		.__(navigo,".nextenable=", false)
		.__(_this.doNavigate(txt("home")))
		;
		return null;
	}
	
	default Object doEvent(Object event)
	{
		 __()
		._if("!window.animInProgess && event!=null")
//		   .__("alert(event)")
		   .__("if (navigator.vibrate) { navigator.vibrate(30); }")    // je vibre

		   ._if("event=='BtnFloatMain' || event=='more' ")
				._if(navigo,".history[",navigo,".history.length-1]=='open'")
					.__(navigo,".nextenable=", false)
					.__(_this.doBack())
				._else()
					.__(navigo,".nextenable=", false)
					.__(_this.doNavigate(txt("open")))
				.endif()
		   		.__(_this.doAction(txt("open")))	
			.endif()
			
			._if("event=='burger' || event=='Overlay'")
				._if(navigo,".history[",navigo,".history.length-1]=='menu'")
					.__(navigo,".nextenable=", false)
					.__(_this.doBack())
				._else()
					.__(navigo,".nextenable=", false)
					.__(_this.doNavigate(txt("menu")))
				.endif()
				.__(_this.doAction(txt("menu")))	
			.endif()
			
		.endif();
		 return null;
	}	
	
	default Object doNavigate(Object uri)
	{
		__(navigo,".history.push(uri)")
		.__(navigo,".navigate(uri)")
		;
		return null;
	}
	
	
	default Object isBack(Object change)
	{
		__()
		._if(navigo,".history.length>1 && ",navigo,".history[",navigo,".history.length-2]==", change )
			.__("return ",navigo,".history.pop()")
		.endif()
		;
		return null;
	}
	
	default Object doBack()
	{
		__(navigo,".history.pop()")
		.__("history.go(-1)")
		;
		return null;
	}
	
	default Object doAction(Object action)
	{
		 __()
		 ._if("action=='open' ")
		    ._if("$('#activity1').hasClass('active')")
			     .__(TKQueue.start(200, fct()
					   		.__("$('#activity1').toggleClass('toback')")
					   		.__("$('#activity2').toggleClass('tofront')"),
					   		ScnStandard.activitySpeed, fct()
					   		.__("$('#activity1').toggleClass('active')")
			    		 	.__("$('#activity2').toggleClass('inactive active tofront')")
					   		, 100, fct().consoleDebug("'end activity anim'")
					   ))
			._else()
				.__(TKQueue.start(200, fct()
				   		.__("$('#activity1').toggleClass('active toback backToFront')")
				   		.__("$('#activity2').toggleClass('inactive')"),
				   		ScnStandard.activitySpeed, fct()
				   		.__("$('#activity1').toggleClass('backToFront')")
		    		 	.__("$('#activity2').toggleClass('active')"),
				   		 100, fct().consoleDebug("'end activity anim'")
				   ))	
		    .endif()
	     .endif()
		 ._if("action=='menu'")
		 		.__(jsNavBar.doBurger())
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
Do different actions depending on if state uid was greater or less than last state uid.
*
*/	
}
