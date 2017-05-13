/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

/**
 * @author Bureau
 *
 */
public interface JSOverlay extends JSClass {
	Object speed = null;
	Object opacity = null;
	
	default Object constructor(Object speed, Object opacity)
	{
		set(this.speed, speed)
		.set(this.opacity, opacity)
		;
		return null;
	}
	
	
	default Object doShow(Object phase)
	{
		var("overlay", "$('.active .black_overlay')" )
		._if(phase, "==1")
			.__("overlay.css('display','block')")
			.__("overlay.css('opacity','0')")
		._elseif(phase, "==2")
			.__("overlay.css('transition','opacity '+", speed ,"+'ms linear')")
			.__("overlay.css('opacity',",opacity,")")
		.endif()
		;
		return null;
	}
	
	default Object doHide(Object phase)
	{
		var("overlay", "$('.active .black_overlay')" )
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
