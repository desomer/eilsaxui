/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.core.JSTouchManager.TTouchInfo;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
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
public interface JSActionManager extends JSClass {

	public static final String DATA_X_ACTION = "data-x-action";
	public static final String ATTR_X_ACTION = "xAction";

	JSNodeElement targetAction = JSClass.declareType();
	JSNodeElement activity = JSClass.declareType();
	JSInt scrollY = JSClass.declareType();
	JSPageAnimation animMgr = JSClass.declareType();
	JSString actionId = JSClass.declareType();
	TActionEvent actionEvent = JSClass.declareType();
	TActionInfo actionInfo = JSClass.declareType();

	/*****************************************************/
	TKPubSub callBackStart();

	TKPubSub callBackMove();

	TKPubSub callBackStop();

	TActionEvent currentActionEvent();

	JSon listAction();

	JSCallBack actionFct = JSClass.declareType();

	@xStatic(autoCall = true)
	default void init() {
		callBackStart().set(newJS(TKPubSub.class));
		callBackMove().set(newJS(TKPubSub.class));
		callBackStop().set(newJS(TKPubSub.class));
		listAction().set(JSObject.newLitteral());   //TODO faire un newLitteral(JSObject.class)

		onStart(fct(actionEvent, () -> {
			_if(actionEvent.actionId().notEqualsJS(null)).then(() -> {
				JSActionManager that = JSClass.declareTypeClass(JSActionManager.class);
				/***************************************************/
				that.doAction(actionEvent.actionId(), actionEvent);
				/***************************************************/
			});
		}));
	}

	@xStatic()
	default void doAction(JSString idAction, TActionEvent aActionEvent) {
		JSActionManager that = JSClass.declareTypeClass(JSActionManager.class);
		let(actionInfo, that.listAction().attrByStr(idAction));
		_if(actionInfo, "!=null").then(() -> {
			actionInfo.callback().call(actionInfo.that(), aActionEvent);
		});
	}
	
	/************************************************************************/
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
	default void addAction(JSString actionId, Object that, Object callback) {
		let(actionInfo, newJS(TActionInfo.class));

		actionInfo.that().set(that);
		actionInfo.actionId().set(actionId);
		actionInfo.callback().set(callback);

		listAction().attrByStr(actionId).set(actionInfo);
	}

	/************************************************************************/
	@xStatic()
	default void searchStart(TTouchInfo info, JSEventTouch event) {
		let(actionEvent, newJS(TActionEvent.class));
		let(targetAction, event.target().closest("[" + DATA_X_ACTION + "]"));
		let(scrollY, window().pageYOffset());
		let(activity, null);
		
		_if(targetAction.notEqualsJS(null)).then(() -> {
			activity.set(targetAction.closest(CssTransition.activity));
			let(actionId, targetAction.dataset().attrByStr(ATTR_X_ACTION));
			actionEvent.actionId().set(actionId);
		});

		actionEvent.scrollY().set(scrollY);
		actionEvent.activity().set(activity);
		actionEvent.actionTarget().set(targetAction);
		actionEvent.infoEvent().set(info);
		actionEvent.event().set(event);

		currentActionEvent().set(actionEvent);
		consoleDebug(txt(DATA_X_ACTION), actionEvent);

		callBackStart().publish(actionEvent);
	}

	@xStatic()
	default void searchMove(TTouchInfo info, JSEventTouch event) {
		callBackMove().publish(currentActionEvent());
	}

	@xStatic()
	default void searchStop(TTouchInfo info, JSEventTouch event) {
		let(actionEvent, currentActionEvent());
		callBackStop().publish(actionEvent);

		currentActionEvent().set(null);
	}

	/*********************************************************/

	interface TActionEvent extends JSType {
		JSNodeElement actionTarget();

		JSNodeElement activity();

		JSInt scrollY();

		JSString actionId();

		TTouchInfo infoEvent();

		JSEventTouch event();
	}

	interface TActionInfo extends JSType {
		JSString actionId();

		JSon that(); // le this du bind

		JSCallBack callback();
	}

}
