/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_ACTIVITY;
import static com.elisaxui.component.toolkit.transition.CssTransition.active;
import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateBackActivity;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFixedForFreeze;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateFrontActivity;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateHiddenToBottom;
import static com.elisaxui.component.toolkit.transition.CssTransition.cStateNoDisplay;
import static com.elisaxui.component.toolkit.transition.CssTransition.inactive;
import static com.elisaxui.component.widget.overlay.ViewOverlay.cBlackOverlay;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.component.page.CssPage;
import com.elisaxui.component.toolkit.core.JSAnimationManager.TAnimation;
import com.elisaxui.component.toolkit.core.JSAnimationManager.TPhase;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xForceInclude;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDomTokenList;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
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
	JSActivityStateManager that = JSClass.declareType();
	JSAnimationManager anAnimationQueueSrc = JSClass.declareType();
	JSAnimationManager anAnimationQueueDest = JSClass.declareType();
	JSNodeElement anOverlay = JSClass.declareType();
	TAnimation anAnim = JSClass.declareType();

	public interface TAnimConfiguration extends JSType {
		JSString transitionId();

		JSInt prct();

		JSInt srcTranslation();
	}

	/*****************************************************************************************/

	default void doOpenActivityFrom(JSNodeElement act1, JSNodeElement act2, TAnimConfiguration aConfig) {

		let(posTop, window().pageYOffset());
		let(that, _this());
		let(classesAct1, act1.classList());
		let(classesAct2, act2.classList());
		let(anOverlay, act1.querySelector(cBlackOverlay));

//		aConfig.srcTranslation().set(30);

		/*****************************************************************/
		let(anAnimationQueueSrc, newJS(JSAnimationManager.class, txt("reduce scale act1")));
		anAnimationQueueSrc.addPhase();
		let(anAnim, anAnimationQueueSrc.getNewAnimationOn(act1, txt(JSAnimationManager.SCALE_XY)));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(1.0);
		anAnim.stopIdx().set(0.9);
		anAnim.beforeStart().set(fct(() -> {
			that.doFixedElemToAbsolute(act1);
			anOverlay.style().attr("display").set(txt("block"));
			that.doActivityInactive(act1);
			that.doActivityFreeze(act1, posTop);
			classesAct1.add(cStateBackActivity);
			JSDocument.document().querySelector("body").classList().add(CssTransition.cStateNoPullToRefresh);
		}));

		anAnim.set(anAnimationQueueSrc.getNewAnimationOn(anOverlay, txt(JSAnimationManager.OPACITY)));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(0);
		anAnim.stopIdx().set(CssPage.OVERLAY_OPACITY_BACK);

		_if(aConfig.srcTranslation().notEqualsJS(null)).then(() -> {
			anAnim.set(anAnimationQueueSrc.getNewAnimationOn(act1, aConfig.transitionId()));
			anAnim.speed().set(SPEED_SHOW_ACTIVITY);
			anAnim.startIdx().set(100);
			anAnim.stopIdx().set(100, "+", aConfig.srcTranslation());
		});
		/*-------------------------------------------------------------------*/
		anAnimationQueueSrc.addPhase();
		anAnim.set(anAnimationQueueSrc.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// termine l'animation
			that.doActivityNoDisplay(act1);
			act1.style().attr("transform").set(null);
			anOverlay.style().attr("display").set(null);
			anOverlay.style().attr("opacity").set(null);
		}));

		/*********************************************************/
		let(anAnimationQueueDest, newJS(JSAnimationManager.class, txt("bottom to fromt act2")));

		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// prepare l'animation
			classesAct2.remove(cStateBackActivity);
			classesAct2.add(cStateFrontActivity);
			that.doActivityActive(act2);
			act2.style().attr("visibility").set(txt("hidden")); // pour le calcul des position des fixed
		}));
		/*-------------------------------------------------------------------*/
		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimationOn(act2, aConfig.transitionId()));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(0);
		anAnim.stopIdx().set(100);
		anAnim.beforeStart().set(fct(() -> {
			// lance les anim
			that.doFixedElemToAbsolute(act2); // fixe les elem fixed en absolute
			that.doActivityFreeze(act2, MEM_SCROLL);
			act2.style().attr("visibility").set(null);
			classesAct2.add(cStateHiddenToBottom);
		}));
		/*-------------------------------------------------------------------*/
		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// termine l'animation
			act2.style().attr("transform").set(null);
			classesAct2.remove(cStateFrontActivity);
			classesAct2.remove(cStateHiddenToBottom);
			that.doActivityDeFreeze(act2);
			that.doFixedElemToFixe(act2);
			that.doInitScrollTo(act2);
			JSDocument.document().querySelector("body").classList().remove(CssTransition.cStateNoPullToRefresh);
		}));

		/***************************************************************************/
		anAnimationQueueSrc.start();
		anAnimationQueueDest.start();

		// consoleDebug(txt("anim act1"), anAnimationQueueSrc);
		// consoleDebug(txt("anim act2"), anAnimationQueueDest);

	}

	/***************************************************************************************/
	default void doCloseActivityTo(JSNodeElement act1, JSNodeElement act2, TAnimConfiguration aConfig) {

		let(posTop, window().pageYOffset());
		let(that, _this());
		let(classesAct1, act1.classList());
		let(classesAct2, act2.classList());
		let(anOverlay, act1.querySelector(cBlackOverlay));

//		aConfig.srcTranslation().set(30);

		/*****************************************************************/
		let(anAnimationQueueSrc, newJS(JSAnimationManager.class, txt("bottom to front act2")));
		anAnimationQueueSrc.touchActionSign().set(-1);
		anAnimationQueueSrc.addPhase();
		let(anAnim, anAnimationQueueSrc.getNewAnimationOn(act2, aConfig.transitionId()));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(100);
		anAnim.stopIdx().set(0);
		anAnim.beforeStart().set(fct(() -> {
			that.doFixedElemToAbsolute(act2);
			that.doActivityInactive(act2);
			that.doActivityFreeze(act2, posTop);
			classesAct2.add(cStateFrontActivity);
			JSDocument.document().querySelector("body").classList().add(CssTransition.cStateNoPullToRefresh);
		}));
		/*-------------------------------------------------------------------*/
		anAnimationQueueSrc.addPhase();
		anAnim.set(anAnimationQueueSrc.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// termine l'animation
			that.doActivityNoDisplay(act2);
			act2.style().attr("transform").set(null);
			classesAct2.remove(cStateFrontActivity);
		}));
		/**********************************************************************/
		let(anAnimationQueueDest, newJS(JSAnimationManager.class, txt("augmente scale act1")));
		anAnimationQueueDest.touchActionSign().set(-1);
		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// prepare l'animation
			classesAct1.add(cStateBackActivity);
			that.doActivityActive(act1);
			anOverlay.style().attr("display").set(txt("block"));
		}));
		/*-------------------------------------------------------------------*/
		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimationOn(act1, txt(JSAnimationManager.SCALE_XY)));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(0.9);
		anAnim.stopIdx().set(1);

		anAnim.set(anAnimationQueueDest.getNewAnimationOn(anOverlay, txt(JSAnimationManager.OPACITY)));
		anAnim.speed().set(SPEED_SHOW_ACTIVITY);
		anAnim.startIdx().set(CssPage.OVERLAY_OPACITY_BACK);
		anAnim.stopIdx().set(0);

		_if(aConfig.srcTranslation().notEqualsJS(null)).then(() -> {
			anAnim.set(anAnimationQueueDest.getNewAnimationOn(act1, aConfig.transitionId()));
			anAnim.speed().set(SPEED_SHOW_ACTIVITY);
			anAnim.startIdx().set(100, "+", aConfig.srcTranslation());
			anAnim.stopIdx().set(100);
		});

		/*-------------------------------------------------------------------*/
		anAnimationQueueDest.addPhase();
		anAnim.set(anAnimationQueueDest.getNewAnimation());
		anAnim.beforeStart().set(fct(() -> {
			// termine l'animation
			act1.style().attr("transform").set(null);
			anOverlay.style().attr("display").set(null);
			anOverlay.style().attr("opacity").set(null);
			classesAct1.remove(cStateBackActivity);
			that.doActivityDeFreeze(act1);
			that.doFixedElemToFixe(act1);
			that.doInitScrollTo(act1);
			JSDocument.document().querySelector("body").classList().remove(CssTransition.cStateNoPullToRefresh);
		}));

		/**********************************************************************/
		anAnimationQueueSrc.start();
		anAnimationQueueDest.start();

		// consoleDebug(txt("anim act1"), anAnimationQueueSrc);
		consoleDebug(txt("anim act2"), anAnimationQueueDest);
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

		forIdx(idx, listfixedElem)._do(() -> {

			let(aElemNode, listfixedElem.at(idx));
			let(classesAct, aElemNode.classList());

			_if(classesAct.contains(ViewTabBar.cFixedBottom)).then(() -> {
				// cas des node en bas de page
				let(posTop, aElemNode.getBoundingClientRect().attr("height"));
				posTop.set(JSWindow.window().innerHeight(), "-", posTop);
				aElemNode.style().attr(TOP).set(txt(posTop, "px"));
				// aElemNode.style().attr(BOTTOM).set(txt("0px"));
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

		forIdx(idx, listfixedElem)._do(() -> {
			listfixedElem.at(idx).style().attr(TOP).set(txt());
			listfixedElem.at(idx).style().attr(POSITION).set(txt());
			listfixedElem.at(idx).style().attr(BOTTOM).set(txt());
		});
	}

	/********************************************************************/
}
