/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.app.core.admin.JSTouchManager.TTouchInfo;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
@xExport
public interface JSActionManager extends JSClass {

	public static final String DATA_X_ACTION = "data-x-action";
	public static final String ATTR_X_ACTION = "xAction";

	JSNodeElement target = JSClass.declareType();
	JSNodeElement activity = JSClass.declareType();
	JSInt scrollY = JSClass.declareType();
	JSPageAnimation animMgr = JSClass.declareType();
	JSString actionId = JSClass.declareType();
	TActionEvent actionEvent = JSClass.declareType();

	/*****************************************************/
	TKPubSub callBackStart();

	TKPubSub callBackMove();

	TKPubSub callBackStop();

	TActionEvent currentActionEvent();

	@xStatic(autoCall = true)
	default void init() {
		callBackStart().set(newJS(TKPubSub.class));
		callBackMove().set(newJS(TKPubSub.class));
		callBackStop().set(newJS(TKPubSub.class));
	}

	@xStatic()
	default void onStart(Object callback) {
		callBackStart().subscribe(callback);
	}

	@xStatic()
	default void onMove(Object callback) {
		callBackMove().subscribe(callback);
	}

	@xStatic()
	default void onStop(Object callback) {
		callBackStop().subscribe(callback);
	}

	@xStatic()
	default void searchStart(TTouchInfo info, JSEventTouch event) {
		let(actionEvent, newJS(TActionEvent.class));
		let(target, event.target().closest("[" + DATA_X_ACTION + "]"));
		let(scrollY, window().pageYOffset());
		let(activity, target.closest(CssTransition.activity));

		_if(target.notEqualsJS(null)).then(() -> {
			let(actionId, target.dataset().attrByString(ATTR_X_ACTION));
			actionEvent.actionId().set(actionId);
		});

		actionEvent.scrollY().set(scrollY);
		actionEvent.activity().set(activity);
		actionEvent.target().set(target);
		actionEvent.infoEvent().set(info);
		actionEvent.event().set(event);

		currentActionEvent().set(actionEvent);
		consoleDebug(txt(DATA_X_ACTION), actionEvent);

		callBackStart().publish(actionEvent);

		_if(currentActionEvent().target().notEqualsJS(null)).then(() -> {
			/***************************************************/
			let(animMgr, newJS(JSPageAnimation.class));
			animMgr.doActivityFreeze(activity, scrollY);
			animMgr.doFixedElemToAbsolute(activity);
			/***************************************************/
		});
	}

	@xStatic()
	default void searchMove(TTouchInfo info, JSEventTouch event) {
		_if(currentActionEvent().target().notEqualsJS(null)).then(() -> {
			currentActionEvent().activity().style().attr("transform")
					.set(txt("translate3d(", info.deltaX(), "px,", info.deltaY(), "px,0px)"));
		});
	}

	@xStatic()
	default void searchStop(TTouchInfo info, JSEventTouch event) {
		_if(currentActionEvent().target().notEqualsJS(null)).then(() -> {
			let(animMgr, newJS(JSPageAnimation.class));
			let(activity, currentActionEvent().activity());

			animMgr.doActivityDeFreeze(activity);
			animMgr.doInitScrollTo(activity);
			animMgr.doFixedElemToFixe(activity);
			activity.style().attr("transform").set(txt());
		});
		currentActionEvent().set(null);
	}

	/*********************************************************/

	interface TActionEvent extends JSType {
		JSNodeElement target();

		JSNodeElement activity();

		JSInt scrollY();

		JSString actionId();

		TTouchInfo infoEvent();

		JSEventTouch event();
	}

}
