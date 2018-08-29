/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import static com.elisaxui.component.page.CssPage.OVERLAY_OPACITY_BACK;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_ACTIVITY;
import static com.elisaxui.component.toolkit.transition.CssTransition.active;
import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateBackActivity;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFixedForFreeze;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFrontActivity;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateHiddenToBottom;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateNoDisplay;
import static com.elisaxui.component.toolkit.transition.CssTransition.inactive;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.component.toolkit.core.JSActivityAnimation.TAnimation;
import com.elisaxui.component.widget.activity.JSOverlayOld;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.overlay.JSOverlay;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xForceInclude;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDomTokenList;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */

@xCoreVersion("1")
@xExport
public interface JSActivityStateManager extends JSClass {

	public static final String DATA_SCROLLTOP = "scrolltop";

	public static final String HIDDEN = "hidden";
	public static final String VISIBILITY = "visibility";
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String ABSOLUTE = "absolute";
	public static final String POSITION = "position";
	public static final String TOP = "top";
	public static final String BOTTOM = "bottom";
	public static final String OVERFLOW = "overflow";
	public static final String TRANSITION = "transition";
	public static final String TRANSFORM = "transform";

	public static final JSInt MEM_SCROLL = JSInt.value(-1);
	public static final JSInt ZERO = JSInt.value(0);

	JSDomTokenList classesAct = JSClass.declareType();
	JSDomTokenList classesAct1 = JSClass.declareType();
	JSDomTokenList classesAct2 = JSClass.declareType();

	JSInt idx = JSClass.declareType();
	JSNodeElement actContent = JSClass.declareType();
	JSArray<JSNodeElement> listfixedElem = JSClass.declareTypeArray(JSNodeElement.class);
	JSInt scrposition = JSClass.declareType();
	JSInt posTop = JSClass.declareType();
	JSNodeElement aElemNode = JSClass.declareType();
	JSOverlayOld aOverlay = JSClass.declareType();
	JSActivityStateManager that = JSClass.declareType();
	JSActivityAnimation anAnimationQueue = JSClass.declareType();


	/*****************************************************************************************/

	default void doOpenActivityFromBottom(JSNodeElement act1, JSNodeElement act2) {

		let(posTop, window().pageYOffset());
		let(aOverlay, newJS(JSOverlay.class, SPEED_SHOW_ACTIVITY, OVERLAY_OPACITY_BACK));
		let(that, _this());
		let(classesAct1, act1.classList());
		let(classesAct2, act2.classList());

		/*****************************************************************/
		let(anAnimationQueue, newJS(JSActivityAnimation.class, "act1"));
		anAnimationQueue.addPhase();

		TAnimation anAnim = newJS(TAnimation.class);
		anAnim.src().set(act1);
		anAnim.type().set(JSActivityAnimation.SCALE_XY);
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(1.0);
		anAnim.stopIdx().set(0.9);
		anAnim.beforeStart().set(fct(() -> {
			that.doFixedElemToAbsolute(act1);
			// overlay.doShowOverlay(jqAct1, 1);
			that.doActivityInactive(act1);
			that.doActivityFreeze(act1, posTop);
			classesAct1.add(cStateBackActivity);
		}));

		anAnimationQueue.addAnimation(anAnim);
		anAnimationQueue.start();
		consoleDebug(anAnimationQueue);

		

		/**********************************************************/
		// __(TKQueue.startAnimQueued(fct(() -> {
		// // prepare l'animation
		// that.doFixedElemToAbsolute(act1);
		// // overlay.doShowOverlay(jqAct1, 1);
		// that.doActivityInactive(act1);
		// that.doActivityFreeze(act1, posTop);
		// classesAct1.add(cStateBackActivity);
		// }), NEXT_FRAME, fct(() -> {
		// // lance les anim
		// // overlay.doShowOverlay(jqAct1, 2);
		// classesAct1.add(cTransitionSpeed);
		// classesAct1.add(cStateZoom09);
		// })));

		/*********************************************************/
		anAnimationQueue.set(newJS(JSActivityAnimation.class, "act2"));
		anAnimationQueue.addPhase();

		anAnim = newJS(TAnimation.class);
		anAnim.speed().set(0);
		anAnim.startIdx().set(0);
		anAnim.stopIdx().set(0);
		anAnim.beforeStart().set(fct(() -> {
			// prepare l'animation
			classesAct2.add(cStateFrontActivity);
			classesAct2.add(cStateHiddenToBottom);
			that.doActivityActive(act2);
		}));

		anAnimationQueue.addAnimation(anAnim);
		
		anAnimationQueue.addPhase();

		anAnim = newJS(TAnimation.class);
		anAnim.src().set(act2);
		anAnim.type().set(JSActivityAnimation.BOTTOM_TO_FRONT);
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(0);
		anAnim.stopIdx().set(100);
		anAnim.beforeStart().set(fct(() -> {
			// lance les anim
			that.doActivityFreeze(act2, MEM_SCROLL);
			//classesAct2.add(cStateMoveToFront);
		}));

		anAnimationQueue.addAnimation(anAnim);
		
		anAnimationQueue.start();
		consoleDebug(anAnimationQueue);
		
		
//		__(TKQueue.startAnimQueued(fct(() -> {
//			// prepare l'animation
//			classesAct2.add(cStateFrontActivity);
//			classesAct2.add(cStateMoveToBottom);
//			that.doActivityActive(act2);
//		}), NEXT_FRAME, fct(() -> {
//			// lance les anim
//			that.doActivityFreeze(act2, MEM_SCROLL);
//			classesAct2.add(cStateMoveToFront);
//		})));
	}

	/***************************************************************************************/

	default void doActivityActive(JSNodeElement activity) {
		let(classesAct, activity.classList());
		classesAct.remove(cStateNoDisplay);
		classesAct.add(active);
		classesAct.remove(inactive);
	}

	@xForceInclude // evite le cherry picking
	default void doActivityInactive(JSNodeElement activity) {
		let(classesAct, activity.classList());
		classesAct.add(inactive);
		classesAct.remove(active);
	}

	@xForceInclude // evite le cherry picking
	/* peux etre inactif mais encore visible durant la transition */
	default void doActivityNoDisplay(JSNodeElement activity) {
		let(classesAct, activity.classList());
		classesAct.add(cStateNoDisplay);
	}

	/***************************************************************************/
	@xForceInclude // evite le cherry picking
	default void doInitScrollTo(JSNodeElement activity) {
		let(scrposition, activity.dataset().attr(DATA_SCROLLTOP));
		window().scrollTo("0px", calc(scrposition, "==null?0:", scrposition));
	}

	/***************************************************************************/
	@xForceInclude // evite le cherry picking
	default void doActivityFreeze(JSNodeElement activity, JSInt sct) {
		let(classesAct, activity.classList());
		classesAct.add(cStateFixedForFreeze);

		_if(sct.equalsJS(MEM_SCROLL)).then(() -> {
			sct.set(activity.dataset().attr(DATA_SCROLLTOP));
			sct.set(calc(sct, "==null?0:", sct)); // met à 0 si null
		});

		activity.dataset().attr(DATA_SCROLLTOP).set(sct); // sauvegarde scroll position

		let(actContent, activity.querySelector(ViewPageLayout.getcContent()));
		// freeze
		actContent.style().attr(OVERFLOW).set(txt(HIDDEN)); // fait clignoter en ios
		actContent.style().attr(HEIGHT).set(txt("100vh"));
		actContent.scrollTop().set(sct);
	}

	@xForceInclude // evite le cherry picking
	default void doActivityDeFreeze(JSNodeElement activity) {
		let(actContent, activity.querySelector(ViewPageLayout.getcContent()));

		actContent.style().attr(OVERFLOW).set(txt());
		actContent.style().attr(HEIGHT).set(txt());

		let(classesAct, activity.classList());
		classesAct.remove(cStateFixedForFreeze);
	}

	/******************************************************************/
	@xForceInclude // evite le cherry picking
	default void doFixedElemToAbsolute(JSNodeElement act) {
		let(listfixedElem, act.querySelectorAll(cFixedElement));

		_forIdx(idx, listfixedElem)._do(() -> {

			let(aElemNode, listfixedElem.at(idx));
			let(classesAct, aElemNode.classList());

			_if(classesAct.contains(ViewTabBar.cFixedBottom)).then(() -> {
				// cas des node en bas de page
				aElemNode.style().attr(BOTTOM).set(txt("0px"));
			})._else(() -> {
				// cas des autres node fixed (ex floatingAction)
				let(posTop, aElemNode.getBoundingClientRect().attr("y"));

				_if(posTop, ">0")
						.then(() -> aElemNode.style().attr(TOP).set(txt(posTop, "px")));
			});

			aElemNode.style().attr(POSITION).set(txt(ABSOLUTE));
		});
	}

	@xForceInclude // evite le cherry picking
	default void doFixedElemToFixe(JSNodeElement act) {
		let(listfixedElem, act.querySelectorAll(cFixedElement));

		_forIdx(idx, listfixedElem)._do(() -> {
			listfixedElem.at(idx).style().attr(TOP).set(txt());
			listfixedElem.at(idx).style().attr(POSITION).set(txt());
			listfixedElem.at(idx).style().attr(BOTTOM).set(txt());
		});
	}

	/********************************************************************/
}
