/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.component.toolkit.transition.ConstTransition.NEXT_FRAME;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_RIPPLE_EFFECT;
import static com.elisaxui.component.widget.button.CssRippleEffect.cRippleEffect;
import static com.elisaxui.component.widget.button.CssRippleEffect.cRippleEffectShow;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.app.core.admin.JSTouchManager.TTouchInfo;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEventTouch;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface JSActionManager extends JSClass {

	public static final String DATA_X_ACTION = "data-x-action";
	public static final String ATTR_X_ACTION = "xAction";

	JSNodeElement target = JSClass.declareType();
	JSNodeElement activity = JSClass.declareType();
	JSInt scrollp = JSClass.declareType();
	JSPageAnimation animMgr = JSClass.declareType();
	JSString actionId = JSClass.declareType();
	JSNodeElement ripple = JSClass.declareType();

	/*****************************************************/

	TKPubSub callBackStart();

	TKPubSub callBackMove();

	TKPubSub callBackStop();

	@xStatic(autoCall = true)
	default void init(Object data) {
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
		let(target, event.target().closest("[" + DATA_X_ACTION + "]"));
		_if(target.notEqualsJS(null)).then(() -> {
			let(actionId, target.dataset().attrByString(ATTR_X_ACTION));
			/****************************************************/
			let(ripple, searchRipple(target));
			_if(ripple.notEqualsJS(null)).then(() -> {
				__(TKQueue.startProcessQueued(
						NEXT_FRAME,
						fct(() -> ripple.classList().add(cRippleEffectShow)),
						SPEED_RIPPLE_EFFECT,
						fct(() -> ripple.classList().remove(cRippleEffectShow))));
			});
			/***************************************************/
			consoleDebug(txt(DATA_X_ACTION), actionId, ripple);
			/***************************************************/
			let(scrollp, window().pageYOffset());
			let(activity, target.closest(CssTransition.activity));

			let(animMgr, newJS(JSPageAnimation.class));
			animMgr.doActivityFreeze(activity, scrollp);
			animMgr.doFixedElemToAbsolute(activity);
			/***************************************************/
		});
	}

	@xStatic()
	default void searchMove(TTouchInfo info, JSEventTouch event) {
		let(target, event.target().closest("[" + DATA_X_ACTION + "]"));

		_if(target.notEqualsJS(null)).then(() -> {
			let(activity, target.closest(CssTransition.activity));
			activity.style().attr("transform").set(txt("translate3d(", info.deltaX(), "px,", info.deltaY(), "px,0px)"));
		});
	}

	@xStatic()
	default void searchStop(TTouchInfo info, JSEventTouch event) {
		let(target, event.target().closest("[" + DATA_X_ACTION + "]"));

		_if(target.notEqualsJS(null)).then(() -> {
			let(animMgr, newJS(JSPageAnimation.class));
			let(activity, target.closest(CssTransition.activity));

			animMgr.doActivityDeFreeze(activity);
			animMgr.doInitScrollTo(activity);
			animMgr.doFixedElemToFixe(activity);
			activity.style().attr("transform").set(txt());
		});
	}

	@xStatic()
	default JSNodeElement searchRipple(JSNodeElement target) {
		// recherche le ripple btn
		let(ripple, target);

		_if("!", ripple.classList().contains(cRippleEffect))
				.then(() -> ripple.set(target.closest(cRippleEffect)));

		_if(ripple, "==null")
				.then(() -> ripple.set(target.querySelector(cRippleEffect)));

		return ripple;
	}

}
