/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.xui.core.toolkit.JQuery;

import static com.elisaxui.xui.core.widget.overlay.ViewOverlay.*;
import static com.elisaxui.xui.core.toolkit.JQuery.*;
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
	
	
	default Object doShow(Object act, Object phase)
	{
		JQuery overlay = new  JQuery().setName("overlay");
		
		var(overlay, $( jsvar(act), " ", cBlackOverlay) );
		_if(phase, "==1");
			__(overlay.css("display","block"));
			__(overlay.css("opacity",0));
		_elseif(phase, "==2");
			__(overlay.css("transition", jsvar(txt("opacity ", speed, "ms linear" ))));
			__(overlay.css("opacity",opacity));
		endif()
		;
		return _void();
	}
	
	default Object doHide(Object phase)
	{
		var("overlay", "$('.active ."+cBlackOverlay.getId()+"')" )
		._if(phase, "==1")
			.__("overlay.css('opacity','0')")
//			.__("overlay.one('transitionend',", fct()
//					.__("overlay.css('display','none')")
//					.__("overlay.css('transition','')") ,")")
		._elseif(phase, "==2")
			.__("overlay.css('display','none')")
			.__("overlay.css('transition','')")
		.endif()
		;
		return null;
	}
	
}
