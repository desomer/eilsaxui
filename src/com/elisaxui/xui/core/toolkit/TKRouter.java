/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;

/**
 * @author Bureau
 *
 */
public interface TKRouter extends JSClass {

	JSNavBar jsNavBar = null;
	TKRouter _this = null;
	
	default Object constructor(Object val) {
		__()
		.set(jsNavBar, _new())	
		;
		return null;
	}
	
	default Object doEvent(Object event)
	{
		 __()
		._if("!window.animInProgess && event!=null")
		   .consoleDebug(event)
		   .__("if (navigator.vibrate) { navigator.vibrate(30); }")
		   //.__("router.navigate(action)")
		   ._if("event=='BtnFloatMain' || event=='more' ")
				._if("router.history[router.history.length-1]=='open'")
					.set("router.nextenable", false)
					.__(_this.doBack())
				._else()
					.set("router.nextenable", false)
					.__(_this.doNavigate(txt("open")))
				.endif()
		   		.__(_this.doAction(txt("open")))	
			.endif()
			._if("event=='burger' || event=='Overlay'")
				._if("router.history[router.history.length-1]=='menu'")
					.set("router.nextenable", false)
					.__(_this.doBack())
				._else()
					.set("router.nextenable", false)
					.__(_this.doNavigate(txt("menu")))
				.endif()
				.__(_this.doAction(txt("menu")))	
			.endif()
		.endif();
		 return null;
	}	
	
	default Object doNavigate(Object uri)
	{
		__("router.history.push(uri)")
		.__("router.navigate(uri)")
		;
		return null;
	}
	
	
	default Object isBack(Object change)
	{
		__()
		._if("router.history.length>1 && router.history[router.history.length-2]==", change )
			.__("return router.history.pop()")
		.endif()
		;
		return null;
	}
	
	default Object doBack()
	{
		__("router.history.pop()")
		.__("history.go(-1)")
		;
		return null;
	}
	
	default Object doAction(Object action)
	{
		 __()
		 ._if("action=='open' ")
		     .__(TKQueue.start(200, fct().__("$('#activity2').toggleClass('inactive active')")
				   		.__("$('#activity1').toggleClass('toback')")
				   		.__("$('#activity1').toggleClass('active')")
				   		, 100, fct().consoleDebug("'end activity anim'")
				   ))
	     .endif()
		 ._if("action=='menu'")
		 		.__(jsNavBar.doBurger())
		 .endif();
		 return null;
	}
	
}
