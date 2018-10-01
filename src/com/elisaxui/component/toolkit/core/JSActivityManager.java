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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
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
	TIntent lastIntent = JSClass.declareType();

	JSArray<TIntent> historyIntent();    //   gestion des historique d'intention
	
	
	@xStatic(autoCall=true)
	default void init()
	{
		historyIntent().set(JSArray.newLitteral());
	}
	
	
	@xStatic()
	default JSNodeElement doRouteToActivity(TIntent intent) {
		let(animMgr, newJS(JSActivityStateManager.class));
		let(activity, getCurrentActivity());
		let(activityDest, document().querySelector(txt(CSSSelector.onPath("#", intent.activityDest()))));
		intent.activitySrc().set(activity.attr("id"));
		historyIntent().push(intent);
		
		animMgr.doOpenActivityFromBottom(activity, activityDest);
		
		return activityDest;
	}
	
	@xStatic()
	default JSNodeElement doRouteToBackActivity() {
		let(animMgr, newJS(JSActivityStateManager.class));
		let(activity, getCurrentActivity());
		let(lastIntent, historyIntent().pop());
		let(activityDest, document().querySelector(txt(CSSSelector.onPath("#", lastIntent.activitySrc()))));

		animMgr.doCloseActivityToBottom(activityDest, activity);
		
		return activityDest;
	}

	@xStatic()
	default JSNodeElement getCurrentActivity() {
		currentActivity().set(document().querySelector(ScnPage.getcMain()).querySelector(CssTransition.activity, CssTransition.active));
		return currentActivity();
	}
	
	public interface TIntent extends JSType {
		JSString activityDest();
		JSString url();	
		JSString action();
		
		JSString nextAnim();
		JSString activitySrc();
	}

}
