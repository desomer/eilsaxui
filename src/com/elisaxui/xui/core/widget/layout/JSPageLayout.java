/**
 * 
 */
package com.elisaxui.xui.core.widget.layout;

import static com.elisaxui.xui.core.toolkit.json.JXui.$xui;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSWindow.*;
import com.elisaxui.xui.core.toolkit.JQuery;
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
	
	
	default JSVoid hideOnScroll(Object idActivity) {
				
		JSInt interval = let(JSInt.class, "interval", "1000/30");	// 60 image seconde
		
		JSInt now = let(JSInt.class, "now", 0);	
		JSInt then = let(JSInt.class, "then", "Date.now()");	
		JSInt deltaTime = let(JSInt.class, "deltaTime", 0);	
		JQuery $header = let(JQuery.class, "$header", JQuery.$("body>header"));
		JQuery $window = let(JQuery.class, "$window", JQuery.$(jsvar("window")));
		JSInt lastScrollTop = let(JSInt.class, "lastScrollTop", $window.scrollTop());	
		JSInt scrollTop = let(JSInt.class, "scrollTop", $window.scrollTop());
		
		//_if(true).then(()->{})._else(()->{});
		
		Object fctdraw = fragmentIf(true).__(()->{
							
				window().requestAnimationFrame("draw");	
				
				now.set("Date.now()");
				deltaTime.set(now.substact(then));
				
				_if( deltaTime,">", interval);
					scrollTop.set($window.scrollTop());
						
					_if(lastScrollTop.isNotEqual(scrollTop)); 
					
						JSInt deltas = let(JSInt.class, "deltas", lastScrollTop.substact(scrollTop));
						JSInt currentDelta = let(JSInt.class, "currentDelta", $header.data("deltaY"));
						
						set( currentDelta, currentDelta,"==null?0:",currentDelta);
						
						JSInt h = let(JSInt.class, "h", $header.outerHeight());
						JSInt deltaHeader = let(JSInt.class, "deltaHeader", currentDelta.add(deltas) );
						
					    _if(deltas, "<0", "&&", "-",currentDelta, "<=", h);
							deltaHeader.set("deltaHeader<-h?-h:deltaHeader");
							$header.data("deltaY",  deltaHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaHeader , "px, 0px)") );
							
						_elseif("deltas>0 && currentDelta<0");
							deltaHeader.set("deltaHeader>0?0:deltaHeader");
							$header.data("deltaY", deltaHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaHeader , "px, 0px)"));
						endif();	

						lastScrollTop.set(scrollTop);
					endif();
					
					then.set(now.substact(deltaTime.modulo(interval)));
				endif();
			});
		
		__("function draw() {", fctdraw , "}");
		__("draw()");  // start
		
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
