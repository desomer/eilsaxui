/**
 * 
 */
package com.elisaxui.xui.core.widget.layout;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKRouterEvent;
import com.elisaxui.xui.core.widget.log.ViewLog;

import static  com.elisaxui.xui.core.toolkit.json.JXui.*;
/**
 * @author Bureau
 *
 */
public interface JSPageLayout extends JSClass {

	//TKRouter _tkrouter =null;
	
	
	default Object setEnableCloseGesture(Object idActivity) {
	  // gesture bas pour fermer l'activity
	  __("var mcheader = new Hammer($(idActivity)[0])");
	  __("mcheader.get('pan').set({ enable: true, direction: Hammer.DIRECTION_VERTICAL})");
	  __("mcheader.on('hammer.input',", fct("ev") 
			  		._if("ev.deltaY>20 && ev.offsetDirection==16 && ev.velocity>1 ")
			  			.__($xui().tkrouter().doEvent("'HeaderSwipeDown'"))
			  		.endif()
			  ,")")	;
	   ;
	   return _void();
	}	
	
	
	
	default Object hideOnScroll(Object idActivity) {
		
		var("fps",60);
		var("now",0);
		var("then","Date.now()");
		var("interval", "1000/fps");
		var("delta",0);
		var("$window","$(window)");
		var("lastScrollTop","$window.scrollTop()");

		JQuery $hearder = let(JQuery.class, "$hearder", JQuery.$("body>header"));
		
		Object fct = fragmentIf(true)
				.__("requestAnimationFrame(draw)")
				.set("now", "Date.now()")
				.set("delta", "now - then")
				._if("delta > interval")
					.var("scrollTop", "$window.scrollTop()")
					._if("lastScrollTop!=scrollTop") 
					
						.var("deltas", "lastScrollTop-scrollTop")
						.var("currentDelta",  $hearder.data("deltaY"))
						.set("currentDelta", "currentDelta==null?0:currentDelta")
					    .var("h", $hearder.outerHeight())
						.var("deltaHeader", "currentDelta+deltas")
						
					    ._if("deltas<0 && -currentDelta<=h")
							.consoleDebug(txt("deltaY "), "deltaHeader")
							.set("deltaHeader", "deltaHeader<-h?-h:deltaHeader")
							.__( $hearder.data("deltaY",  jsvar("deltaHeader")))
							.__( $hearder.css("transform", txt("translate3d(0px, " , jsvar("deltaHeader") , "px, 0px)")) )
						._elseif("deltas>0 && currentDelta<0")
							.set("deltaHeader", "deltaHeader>0?0:deltaHeader")
							.consoleDebug(txt("deltaY "), "deltaHeader")
							.__( $hearder.data("deltaY",  jsvar("deltaHeader")))
							.__( $hearder.css("transform", txt("translate3d(0px, " , jsvar("deltaHeader") , "px, 0px)")) )
						.endif()	

						.set("lastScrollTop", "scrollTop")
					.endif()
					
					.set("then", "now - (delta % interval)")
				.endif()
				;
		
		__("function draw() {", fct , "}");
		__("draw()");
		
		//__(JQuery.$(jsvar("window")).on(txt("scroll"), ",", fct().consoleDebug(txt("onscroll")) ));
		return _void();
	}	
	
	/*
	 *    push to reload
	 *    
	 *     touch-action: pan-x pan-y;
    user-select: none;
    -webkit-user-drag: none;
	 * */
}
