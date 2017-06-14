/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.toolkit.TKRouter;

/**
 * @author Bureau
 *
 */
public interface JSPageLayout extends JSClass {

	TKRouter _tkrouter =null;
	
	
	default Object setEnableCloseGesture(Object name) {
	  // gesture bas pour fermer l'activity
	  __("var mcheader = new Hammer($(name)[0])")
	  .__("mcheader.get('pan').set({ enable: true, direction: Hammer.DIRECTION_VERTICAL})")
	  .__("mcheader.on('hammer.input',", fct("ev") 
			  		._if("ev.deltaY>20 && ev.offsetDirection==16 && ev.velocity>1 ")
			  		.__(_tkrouter.doEvent("'HeaderSwipeDown'"))
			  		.endif()
			  ,")")	
	   ;
	   return null;
	}	
	
	
	/*
	 *    push to reload
	 *    
	 *     touch-action: pan-x pan-y;
    user-select: none;
    -webkit-user-drag: none;
	 * */
}
