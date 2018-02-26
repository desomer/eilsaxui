/**
 * 
 */
package com.elisaxui.component.widget.layout;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

import static com.elisaxui.component.toolkit.json.JXui.$xui;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSWindow.*;
import static com.elisaxui.component.toolkit.JQuery.*;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.component.transition.CssTransition;
import com.elisaxui.component.widget.navbar.ViewNavBar;
/**
 * @author Bureau
 *
 */
public interface JSPageLayout extends JSClass {
	
	/**
	 * lance event HeaderSwipeDown
	 * @param idActivity
	 */
	default void setEnableCloseGesture(JSString idActivity) {
	  // gesture bas pour fermer l'activity
	  __("var mcheader = new Hammer(", $(idActivity).get(0) ,")");
	  __("mcheader.get('pan').set({ enable: true, direction: Hammer.DIRECTION_VERTICAL})");
	  __("mcheader.on('hammer.input',", funct("ev") 
			  		._if("ev.deltaY>20 && ev.offsetDirection==16 && ev.velocity>1 ")
			  			.__($xui().tkrouter().doEvent("'HeaderSwipeDown'"))
			  		.endif()
			  ,")")	;
	}	
	
	
	default void hideOnScroll(JSString idActivity) {
	
		int nbImageParSecond = 30;
		JSInt interval = let(JSInt.class, "interval", "1000/"+nbImageParSecond);	// 60 image seconde
		
		JSInt now = let(JSInt.class, "now", 0);	
		JSInt then = let(JSInt.class, "then", "Date.now()");	
		JSInt deltaTime = let(JSInt.class, "deltaTime", 0);	
		
		JQuery $activity = let(JQuery.class, "$activity", $(idActivity));
		
		JQuery $header = let(JQuery.class, "$header", $activity.find("header"));
		JQuery $headerDesc = let(JQuery.class, "$headerDesc", $activity.find("header").find(ViewNavBar.descBar));
		JQuery $headerLogo = let(JQuery.class, "$headerLogo", $activity.find("header").find(ViewNavBar.logo));
		JQuery $footer = let(JQuery.class, "$footer", $activity.find("footer"));
		
		JQuery $window = let(JQuery.class, "$window", $(var("window")));
		JSInt lastScrollTop = let(JSInt.class, "lastScrollTop", $window.scrollTop());	
		JSInt scrollTop = let(JSInt.class, "scrollTop", $window.scrollTop());

		
		JSFunction fctdraw = fragmentIf(true).__(()->{
							
				window().requestAnimationFrame(var("fctdraw"));	  // restart
				
				now.set(var("Date.now()"));
				deltaTime.set(now.substact(then));
				
				_if( deltaTime,">", interval, "&&", "window.disableScrollEvent==null", "&&", "window.animInProgess==false");
					scrollTop.set($window.scrollTop()); //position du scroll
						
					_if(lastScrollTop.isNotEqual(scrollTop), "&&", $activity.hasClass(CssTransition.active) ); 
					
						JSInt deltas = let(JSInt.class, "deltas", lastScrollTop.substact(scrollTop));
						
						JSInt currentHeaderDescDelta = let(JSInt.class, "currentHeaderDescDelta", $headerDesc.data("deltaY"));
						JSInt currentHeaderDelta = let(JSInt.class, "currentHeaderDelta", $header.data("deltaY"));
						JSInt currentFooterDelta = let(JSInt.class, "currentFooterDelta", $footer.data("deltaY"));
						
						currentHeaderDescDelta.set(currentHeaderDescDelta,"==null?0:",currentHeaderDescDelta);
						currentHeaderDelta.set(currentHeaderDelta,"==null?0:",currentHeaderDelta);
						currentFooterDelta.set(currentFooterDelta,"==null?0:",currentFooterDelta);
						
						JSInt deltaDescHeader = let(JSInt.class, "deltaDescHeader", currentHeaderDescDelta.add(deltas) );
						JSInt deltaTopHeader= let(JSInt.class, "deltaTopHeader", currentHeaderDelta.add(deltas) );
						JSInt deltaFooter = let(JSInt.class, "deltaFooter", currentFooterDelta.add(deltas) );
						
						JSInt hHeaderDesc = let(JSInt.class, "hHeaderDesc", $headerDesc.outerHeight());
						hHeaderDesc.set(hHeaderDesc.add(3));  // hauteur de l'hombre 
						
						JSInt hHeaderTop = let(JSInt.class, "hHeaderTop", $header.find(ViewNavBar.topBar).outerHeight());
						
						JSInt hFooter = let(JSInt.class, "hFooter", $footer.outerHeight());
		
						/************************* HEADER DESC + IMAGE et TITRE**************************/
					    _if(deltas, "<0", "&&", "-",currentHeaderDescDelta, "<=", hHeaderDesc);
					    	//cache
					    	deltaDescHeader.set(deltaDescHeader,"< -",hHeaderDesc,"?-",hHeaderDesc,":", deltaDescHeader);
							$headerDesc.data("deltaY",  deltaDescHeader);
							$headerDesc.css("transform", txt("translate3d(0px, " , deltaDescHeader , "px, 0px)") );
							JSFloat opacityHeaderDesc = let(JSFloat.class, "opacityHeaderDesc", calc("1+(", deltaDescHeader, "/", hHeaderDesc ,")"  ));
							$headerDesc.css("opacity", opacityHeaderDesc);
							
							JSFloat scale = let(JSFloat.class, "scale", calc("1+0.3*(", deltaDescHeader, "/", hHeaderDesc ,")"  ));
							$headerLogo.css("transform", txt("scale(",scale,")"));
							
							 
						_elseif_(deltas, ">0","&&", currentHeaderDescDelta, "<0", "&&", scrollTop, "<", hHeaderDesc);
							//affiche
							deltaDescHeader.set(deltaDescHeader,">0?0:",deltaDescHeader);
							$headerDesc.data("deltaY", deltaDescHeader);
							$headerDesc.css("transform", txt("translate3d(0px, " , deltaDescHeader , "px, 0px)"));
							opacityHeaderDesc = let(JSFloat.class, "opacityHeaderDesc", calc("1+(", deltaDescHeader, "/", hHeaderDesc ,")"  ));
							$headerDesc.css("opacity", opacityHeaderDesc);
							
							scale = let(JSFloat.class, "scale", calc("1+0.3*(", deltaDescHeader, "/", hHeaderDesc ,")"  ));
							$headerLogo.css("transform", txt("scale(",scale,")"));
						endif();	
						
						/**************  HEADER ACTION ***********/
						 _if(deltas, "<0", "&&", "-",currentHeaderDelta, "<=", hHeaderTop, "&&", scrollTop, ">", hHeaderDesc, "+50");
					    	//cache
						 	deltaTopHeader.set(deltaTopHeader,"< -",hHeaderTop,"?-",hHeaderTop,":", deltaTopHeader);
							$header.data("deltaY",  deltaTopHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaTopHeader , "px, 0px)") );
							
						_elseif_(deltas, ">0","&&", currentHeaderDelta, "<0");
							//affiche
							deltaTopHeader.set(deltaTopHeader,">0?0:",deltaTopHeader);
							$header.data("deltaY", deltaTopHeader);
							$header.css("transform", txt("translate3d(0px, " , deltaTopHeader , "px, 0px)"));
						endif();	
						
						/**************** FOOTER ACTION ********************/
						_if(deltas, "<0", "&&", "-",currentFooterDelta, "<=", hFooter);
							//cache
							deltaFooter.set(deltaFooter,">",hFooter,"?",hFooter,":", deltaFooter);
							$footer.data("deltaY",  deltaFooter);
							deltaFooter.set("-",deltaFooter);
							$footer.css("transform", txt("translate3d(0px, " , deltaFooter , "px, 0px)") );
							
						_elseif_(deltas, ">0","&&", currentFooterDelta, "<0");
							//affiche
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
		
		__("function fctdraw() {", fctdraw , "}");
		__("fctdraw()");  // start
		
	}	
}
