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
		
		var("fps",30);
		var("now",0);
		var("then","Date.now()");
		var("interval", "1000/fps");
		var("delta",0);
		var("$window","$(window)");
		var("lastScrollTop","$window.scrollTop()");
		
		Object fct = fragmentIf(true)
				.__("requestAnimationFrame(draw)")
				.set("now", "Date.now()")
				.set("delta", "now - then")
				._if("delta > interval")
					.var("scrollTop", "$window.scrollTop()")
					._if("lastScrollTop!=scrollTop") 
//						.consoleDebug(txt("anim "), "scrollTop")
//						.__(JQuery.$(ViewLog.cLog, " textArea").val( JQuery.$(ViewLog.cLog, " textArea").val() , "+ scrollTop + '\\n'"  ))
						.set("lastScrollTop", "scrollTop")
					.endif()
					
					.set("then", "now - (delta % interval)")
				.endif()
				;
		
		__("function draw() {", fct , "}");
		__("draw()")
		;
		
		

  /*
function draw() {
     
    requestAnimationFrame(draw);
     
    now = Date.now();
    delta = now - then;
     
    if (delta > interval) {
        // update time stuffs
         
        // Just `then = now` is not enough.
        // Lets say we set fps at 10 which means
        // each frame must take 100ms
        // Now frame executes in 16ms (60fps) so
        // the loop iterates 7 times (16*7 = 112ms) until
        // delta > interval === true
        // Eventually this lowers down the FPS as
        // 112*10 = 1120ms (NOT 1000ms).
        // So we have to get rid of that extra 12ms
        // by subtracting delta (112) % interval (100).
        // Hope that makes sense.
         
        then = now - (delta % interval);
         
        // ... Code for Drawing the Frame ...
    }
}
 
draw();
		 
		 * 
		 * */
		

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
