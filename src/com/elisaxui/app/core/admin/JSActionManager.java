/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.app.core.admin.JSTouchManager.TTouchInfo;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface JSActionManager extends JSClass {

	public static final String DATA_X_ACTION = "data-x-action";
	JSNodeElement target = JSClass.defVar();
	JSNodeElement activity =JSClass.defVar();
	JSInt scrollp = JSClass.defVar(); 
			
	@xStatic()
	default void searchStart(TTouchInfo info, JSEventTouch event) {
		let(target, event.target().closest("[" +DATA_X_ACTION+ "]"));
		
		_if(target.notEqualsJS(null)).then(() -> {
			let(scrollp, window().pageYOffset());			
			let(activity, target.closest(CssTransition.activity));
			
			JSPageAnimation animMgr = let("animMgr", newJS(JSPageAnimation.class));
			animMgr.doActivityFreeze(activity, scrollp);
			animMgr.doFixedElemToAbsolute(activity);
		});	
	}
	
	@xStatic()
	default void searchMove(TTouchInfo info, JSEventTouch event) {
		let(target, event.target().closest("[" +DATA_X_ACTION+ "]"));
		
		_if(target.notEqualsJS(null)).then(() -> {			
			let(activity, target.closest(CssTransition.activity));
			activity.style().attr("transform").set(txt("translate3d(",info.deltaX(),"px,",info.deltaY(),"px,0px)"));
		});	
	}
	
	@xStatic() 
	default void searchStop(TTouchInfo info, JSEventTouch event) {
		let(target, event.target().closest("[" +DATA_X_ACTION+ "]"));
		
		_if(target.notEqualsJS(null)).then(() -> {
			JSPageAnimation animMgr = let("animMgr", newJS(JSPageAnimation.class));
			let(activity, target.closest(CssTransition.activity));
			animMgr.doActivityDeFreeze(activity);
			animMgr.doInitScrollTo(activity);
			animMgr.doFixedElemToFixe(activity);
			activity.style().attr("transform").set(txt());
		});	
	}
}
