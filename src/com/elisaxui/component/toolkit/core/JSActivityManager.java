/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.page.ScnPage;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
@xExport
public interface JSActivityManager extends JSClass {

	JSNodeElement currentActivity();

	JSNodeElement activity = JSClass.declareType();
	JSNodeElement activityDest = JSClass.declareType();
	JSActivityStateManager animMgr = JSClass.declareType();


	@xStatic()
	default JSNodeElement doRouteToActivity(TIntent intent) {
		let(animMgr, newJS(JSActivityStateManager.class));
		let(activity, getCurrentActivity());
		let(activityDest, document().querySelector(txt(CSSSelector.onPath("#", intent.idActivity()))));
	//	animMgr.doActivityNoDisplay(activity);
	//	animMgr.doActivityActive(activityDest);
	//	animMgr.doActivityInactive(activity);
		animMgr.doOpenActivityFromBottom(activity, activityDest);
		
		return activityDest;
	}

	@xStatic()
	default JSNodeElement getCurrentActivity() {
		currentActivity().set(document().querySelector(ScnPage.getcMain()).querySelector(CssTransition.activity, CssTransition.active));
		return currentActivity();
	}
	
	public interface TIntent extends JSType {
		JSString idActivity();
		JSString url();	
		JSString action();
		
		JSString nextAnim();
	}

}
