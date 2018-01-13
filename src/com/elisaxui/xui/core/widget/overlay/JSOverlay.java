/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.xui.core.toolkit.JQuery;

import static com.elisaxui.xui.core.widget.overlay.ViewOverlay.*;
import static com.elisaxui.xui.core.toolkit.JQuery.*;
import static com.elisaxui.xui.core.transition.CssTransition.*;
/**
 * @author Bureau
 *
 */
public interface JSOverlay extends JSClass {
	
	JSInt speed = null;
	JSFloat opacity = null;
	
	default Object constructor(Object speed, Object opacity)
	{
		set(JSOverlay.speed, speed);
		set(JSOverlay.opacity, opacity);
		return _void();
	}
	
	
	default Object doShowOverlay(JQuery act, Object phase)
	{
		
		JQuery overlay = let(JQuery.class, "overlay", act.find(cBlackOverlay) );
		_if(phase, "==1");
			__(overlay.css("display","block"));
			__(overlay.css("opacity",0));
		_elseif_(phase, "==2");
			__(overlay.css("transition", txt("opacity ", speed, "ms linear" )));
			__(overlay.css("opacity",opacity));
		endif()
		;
		return _void();
	}
	
	default Object doHideOverlay(Object phase)
	{

		JQuery overlay = let(JQuery.class, "overlay", $(active.descendant(cBlackOverlay)) );
		
		_if(phase, "==1")
			.__("overlay.css('opacity','0')")
		._elseif_(phase, "==2")
			.__("overlay.css('display','none')")
			.__("overlay.css('transition','')")
		.endif()
		;
		return null;
	}
	
}
