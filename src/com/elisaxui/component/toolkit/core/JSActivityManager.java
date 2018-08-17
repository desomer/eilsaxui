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
	JSPageAnimation animMgr = JSClass.declareType();

	@xStatic(autoCall = true)
	default void init() {

	}

	@xStatic()
	default JSNodeElement doRouteToActivity(JSString idActivity) {
		let(animMgr, newJS(JSPageAnimation.class));
		let(activity, getCurrentActivity());
		let(activityDest, document().querySelector(txt(CSSSelector.onPath("#", idActivity))));
		animMgr.doActivityNoDisplay(activity);
		animMgr.doActivityActive(activityDest);
		animMgr.doActivityInactive(activity);
		return activityDest;
	}

	@xStatic()
	default JSNodeElement getCurrentActivity() {
		currentActivity().set(document().querySelector(ScnPage.getcMain()).querySelector(CssTransition.activity, CssTransition.active));
		return currentActivity();
	}

}
