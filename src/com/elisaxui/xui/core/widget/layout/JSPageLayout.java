/**
 * 
 */
package com.elisaxui.xui.core.widget.layout;

import static com.elisaxui.xui.core.toolkit.json.JXui.$xui;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSWindow.*;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.transition.CssTransition;
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
	
	
	default JSVoid hideOnScroll(JSString idActivity) {
		
		int nbImageParSecond = 30;
		JSInt interval = let(JSInt.class, "interval", "1000/"+nbImageParSecond);	// 60 image seconde
		
		JSInt now = let(JSInt.class, "now", 0);	
		JSInt then = let(JSInt.class, "then", "Date.now()");	
		JSInt deltaTime = let(JSInt.class, "deltaTime", 0);	
		
		JQuery $activity = let(JQuery.class, "$activity", JQuery.$(idActivity));
		
		JQuery $header = let(JQuery.class, "$header", $activity.find("header"));
		JQuery $footer = let(JQuery.class, "$footer", $activity.find("footer"));
		
		JQuery $window = let(JQuery.class, "$window", JQuery.$(jsvar("window")));
		JSInt lastScrollTop = let(JSInt.class, "lastScrollTop", $window.scrollTop());	
		JSInt scrollTop = let(JSInt.class, "scrollTop", $window.scrollTop());

		
		//_if(true).then(()->{})._else(()->{});
		
		Object fctdraw = fragmentIf(true).__(()->{
							
				window().requestAnimationFrame("draw");	
				
				now.set("Date.now()");
				deltaTime.set(now.substact(then));
				
				_if( deltaTime,">", interval);
					scrollTop.set($window.scrollTop()); //position du scroll
						
					_if(lastScrollTop.isNotEqual(scrollTop), "&&", $activity.hasClass(CssTransition.active) ); 
					
						JSInt deltas = let(JSInt.class, "deltas", lastScrollTop.substact(scrollTop));
						JSInt currentHeaderDelta = let(JSInt.class, "currentHeaderDelta", $header.data("deltaY"));
						JSInt currentFooterDelta = let(JSInt.class, "currentFooterDelta", $footer.data("deltaY"));
						
						currentHeaderDelta.set(currentHeaderDelta,"==null?0:",currentHeaderDelta);
						currentFooterDelta.set(currentFooterDelta,"==null?0:",currentFooterDelta);
						
						JSInt deltaHeader = let(JSInt.class, "deltaHeader", currentHeaderDelta.add(deltas) );
						JSInt deltaFooter = let(JSInt.class, "deltaFooter", currentFooterDelta.add(deltas) );
						
						JSInt hHeader = let(JSInt.class, "hHeader", $header.outerHeight());
						hHeader.set(hHeader.add(3));  // hauteur de l'hombre 
						
						JSInt hFooter = let(JSInt.class, "hFooter", $footer.outerHeight());
		
					    _if(deltas, "<0", "&&", "-",currentHeaderDelta, "<=", hHeader);
					   // _if(deltas, "<0", "&&", hHeader, ">0");
					    	//ferme
							deltaHeader.set(deltaHeader,"< -",hHeader,"?-",hHeader,":", deltaHeader);
							$header.data("deltaY",  deltaHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaHeader , "px, 0px)") );
							//__($header, ".css( {'max-height':", jsvar(192, "+", deltaHeader, "-3") ,"+'px'})");
							
						_elseif(deltas, ">0","&&", currentHeaderDelta, "<0");
						//_elseif(deltas, ">0","&&", hHeader, "<200");
							//ouvre
							deltaHeader.set(deltaHeader,">0?0:",deltaHeader);
							$header.data("deltaY", deltaHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaHeader , "px, 0px)"));
							//__($header, ".css( {'max-height':", jsvar(192, "+", deltaHeader, "-3") ,"+'px'})");
						endif();	
						
						_if(deltas, "<0", "&&", "-",currentFooterDelta, "<=", hFooter);
							deltaFooter.set(deltaFooter,">",hFooter,"?",hFooter,":", deltaFooter);
							$footer.data("deltaY",  deltaFooter);
							deltaFooter.set("-",deltaFooter);
							$footer.css("transform", txt("translate3d(0px, " , deltaFooter , "px, 0px)") );
							
						_elseif(deltas, ">0","&&", currentFooterDelta, "<0");
							deltaFooter.set(deltaFooter,">0?0:",deltaFooter);
							$footer.data("deltaY", deltaFooter);
							deltaFooter.set("-",deltaFooter);
							$footer.css("transform", txt("translate3d(0px, " , deltaFooter , "px, 0px)"));
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
