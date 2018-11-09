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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
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
	JSActivityStateManager animMgr = JSClass.declareType();
	JSString actionId = JSClass.declareType();
	TActionEvent actionEvent = JSClass.declareType();
	TActionInfo actionInfo = JSClass.declareType();
	TActionListener anActionListener = JSClass.declareType();

	/*****************************************************/
	TKPubSub callBackStart();

	TKPubSub callBackMove();

	TKPubSub callBackStop();

	TActionEvent currentActionEvent();
	TActionInfo currentActionInfo();

	TActionEvent lastActionEvent();

	JSon listAction(); /* liste des actions */

	JSCallBack actionFct = JSClass.declareType();
	JSActionManager that = JSClass.declareType();

	@xStatic(autoCall = true)
	default void init() {

		currentActionEvent().set(null);
		lastActionEvent().set(null);
		currentActionInfo().set(null);

		callBackStart().set(newJS(TKPubSub.class));
		callBackMove().set(newJS(TKPubSub.class));
		callBackStop().set(newJS(TKPubSub.class));
		listAction().set(JSObject.newLitteral()); // TODO faire un newLitteral(JSObject.class)

		// ajoute PAR DEFAUT un listener sur le click d'un element avec DATA_X_ACTION
		onStart(fct(actionEvent, () -> {
			_if(actionEvent.zoneActionId().notEqualsJS(null)).then(() -> {
				JSActionManager that = JSClass.declareTypeClass(JSActionManager.class);
				/***************************************************/
				that.doAction(actionEvent.zoneActionId(), actionEvent);
				/***************************************************/
			});
		}));

	}

	JSActivityHistoryManager activityManager = JSClass.declareTypeClass(JSActivityHistoryManager.class);
	JSBool isSwipeStarted = JSClass.declareType();

	/**
	 * excution d'une action de type DATA_X_ACTION
	 * 
	 * @param idAction
	 * @param aActionEvent
	 */
	@xStatic()
	default void doAction(JSString idAction, TActionEvent aActionEvent) {
		JSActionManager that = JSClass.declareTypeClass(JSActionManager.class);
		let(actionInfo, that.listAction().attrByStr(idAction));
		
		_if(currentActionEvent(), "!=null").then(() -> {
			currentActionEvent().actionInfo().set(actionInfo);
		});
				
		_if(actionInfo, "!=null").then(() -> {
			
			_if(actionInfo.modeTouch().equalsJS("SWIPE")).then(() -> {
				doSwipe(aActionEvent);
			})._else(() -> {
				actionInfo.callback().call(actionInfo.that(), aActionEvent);
			});
		});
	}

	@xStatic()
	default void doSwipe(TActionEvent aActionEvent) {
		// gestion du touch swipe down
		let(anActionListener, newJS(JSObject.class));
		let(isSwipeStarted, false);
		
		JSDocument.document().querySelector("body").classList().add(CssTransition.cStateNoPullToRefresh);

		anActionListener.onStart().set(fct(actionEvent, () -> {
				// ne fait rien sur le start
		}).bind(_this()));
		
		anActionListener.onMove().set(fct(actionEvent, () -> {
			// attends un delta de 10 px avant lancement du swipe
			_if(aActionEvent.infoEvent().distance(), ">10", " && ", isSwipeStarted.equalsJS(false)).then(() -> {
				isSwipeStarted.set(true);
				consoleDebug("'swipe'", aActionEvent.infoEvent().distance());

				aActionEvent.actionInfo().callback().call(aActionEvent.actionInfo().that(), aActionEvent);
			});

		}).bind(_this()));
		
		anActionListener.onStop().set(fct(actionEvent, () -> {
			consoleDebug("'swipe removed'");

			let(that,_this());
			setTimeout(fct(() -> {
				that.removeListener(anActionListener);
			}), 1);

		}).bind(_this()));

		addListener(anActionListener);
	}

	/******************************
	 * METHODE REGISTER PUBLIC
	 *********************************/
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
	default void addListener(TActionListener listener) {
		callBackStart().subscribe(listener.onStart());
		callBackMove().subscribe(listener.onMove());
		callBackStop().subscribe(listener.onStop());
	}

	@xStatic()
	default void removeListener(TActionListener listener) {
		callBackStart().unsubscribe(listener.onStart());
		callBackMove().unsubscribe(listener.onMove());
		callBackStop().unsubscribe(listener.onStop());
	}

	@xStatic()
	default void addAction(JSString actionId, Object that, Object callback) {
		let(actionInfo, newJS(TActionInfo.class));

		actionInfo.that().set(that);
		actionInfo.actionId().set(actionId);
		actionInfo.callback().set(callback);

		listAction().attrByStr(actionId).set(actionInfo);
		currentActionInfo().set(actionInfo);
	}

	/****************************
	 * APPELER PRIVATE EN STATIC PAR JSTouchManager
	 *******************************/
	/* APPELER EN STATIC PAR JSTouchManager */
	@xStatic()
	default void searchStart(TTouchInfo info, JSEventTouch event) {
		let(actionEvent, newJS(TActionEvent.class));
		let(scrollY, window().pageYOffset());
		let(activity, null);
		let(targetAction, event.target().closest("[" + DATA_X_ACTION + "]"));

		_if(targetAction.notEqualsJS(null)).then(() -> {
			activity.set(targetAction.closest(CssTransition.activity));
			let(actionId, targetAction.dataset().attrByStr(ATTR_X_ACTION));
			actionEvent.zoneActionId().set(actionId);
		});

		actionEvent.scrollY().set(scrollY);
		actionEvent.activity().set(activity);
		actionEvent.actionTarget().set(targetAction);
		actionEvent.infoEvent().set(info);
		actionEvent.event().set(event);

		lastActionEvent().set(null);
		currentActionEvent().set(actionEvent);

		consoleDebug(txt(DATA_X_ACTION), actionEvent);

		callBackStart().publish(actionEvent);
	}

	/* APPELER EN STATIC PAR JSTouchManager */
	@xStatic()
	default void searchMove(TTouchInfo info, JSEventTouch event) {
		callBackMove().publish(currentActionEvent());
	}

	/* APPELER EN STATIC PAR JSTouchManager */
	@xStatic()
	default void searchStop(TTouchInfo info, JSEventTouch event) {
		let(actionEvent, currentActionEvent());
		callBackStop().publish(actionEvent);
		lastActionEvent().set(actionEvent);
		currentActionEvent().set(null);
	}

	/*********************************************************/
	interface TActionListener extends JSType {
		JSCallBack onStart();

		JSCallBack onMove();

		JSCallBack onStop();
	}

	interface TActionEvent extends JSType {
		JSNodeElement actionTarget();

		JSNodeElement activity();

		JSInt scrollY();

		JSString zoneActionId();
		TActionInfo actionInfo();

		TTouchInfo infoEvent();

		JSEventTouch event();
	}

	interface TActionInfo extends JSType {
		
		public static final String MODE_TOUCH_SWIPE = "MODE_TOUCH_SWIPE";
		
		JSString actionId();

		JSString modeTouch();

		JSon that(); // le this du bind

		JSCallBack callback();
	}

}
