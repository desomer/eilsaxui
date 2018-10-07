/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import javax.swing.text.Document;

import com.elisaxui.app.core.admin.ScnPageA;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionEvent;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionListener;
import com.elisaxui.component.toolkit.core.JSRequestAnimationFrame.TAnimFrameEvent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.es6.JSPromise;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 *         https://stackoverflow.com/questions/10592823/how-to-reverse-engineer-a-webkit-matrix3d-transform
 *         https://developer.apple.com/documentation/webkitjs/webkitcssmatrix
 *         https://stackoverflow.com/questions/19675997/how-to-exact-set-a-dynamic-style-transform-using-javascript
 *         https://stackoverflow.com/questions/8517173/change-image-opacity-using-javascript
 *         https://stackoverflow.com/questions/18574576/halting-a-translate3d-transition
 *         https://stackoverflow.com/questions/42267189/how-to-get-value-translatex-by-javascript
 *         https://css-tricks.com/controlling-css-animations-transitions-javascript/
 * 
 *         https://css-tricks.com/what-houdini-means-for-animating-transforms/
 *         https://css-tricks.com/emulating-css-timing-functions-javascript/
 *         https://codepen.io/thebabydino/pen/dzVyqZ
 *         https://github.com/gre/bezier-easing
 */
@xCoreVersion("1")
public interface JSActivityAnimation extends JSClass {

	public static final String OPACITY = "OPACITY";
	public static final String SCALE_XY = "SCALE_XY";
	public static final String BOTTOM_TO_FRONT = "BOTTOM_TO_FRONT";

	TAnimation aAnimation = JSClass.declareType();

	TAnimFrameEvent aAnimEvent = JSClass.declareType();

	TPhase aPhase = JSClass.declareType();
	TAnimation aAnim = JSClass.declareType();

	JSAny cssMatrix = JSClass.declareType();
	JSFloat timeAnim = JSClass.declareType();
	JSFloat prctAnim = JSClass.declareType();
	JSFloat valAnim = JSClass.declareType();
	JSInt nbEnd = JSClass.declareType();
	JSInt idx = JSClass.declareType();

	JSPromise aPromise = JSClass.declareType();
	JSCallBack resolve = JSClass.declareType();
	JSCallBack reject = JSClass.declareType();
	JSCallBack subscriber = JSClass.declareType();

	JSRequestAnimationFrame aRequestAnimation = JSClass.declareTypeClass(JSRequestAnimationFrame.class);
	JSActionManager theActionManager = JSClass.declareTypeClass(JSActionManager.class);

	TActionEvent actionEvent = JSClass.declareType();
	TActionListener anActionListener = JSClass.declareType();
	JSFloat prctDeltaTouch = JSClass.declareType();
	JSBool modeTouchActionInProgress = JSClass.declareType();
	JSBool modeTouchActionEnd = JSClass.declareType();
	JSInt nbEndTouchActionStopped = JSClass.declareType();

	TQueueAnim aQueue();

	TPhase lastPhase();

	TActionEvent lastActionEvent();

	JSInt touchActionSign();

	TActionEvent touchActionStopped();

	JSBool withEasing();

	default void constructor(JSString id) {
		aQueue().set(newJS(TQueueAnim.class));
		aQueue().id().set(id);
		aQueue().listPhase().set(JSArray.newLitteral());
		aQueue().idxPhase().set(0);
		touchActionSign().set(1);
		withEasing().set(true);
	}

	default void addPhase() {
		lastPhase().set(newJS(TPhase.class));
		lastPhase().listAnimation().set(JSArray.newLitteral());
		aQueue().listPhase().push(lastPhase());
	}

	default void addAnimation(TAnimation anim) {
		lastPhase().listAnimation().push(anim);
	}

	default TAnimation getNewAnimation() {
		let(aAnim, newJS(TAnimation.class));
		aAnim.speed().set(0);
		aAnim.startIdx().set(0);
		aAnim.stopIdx().set(0);
		addAnimation(aAnim);
		return aAnim;
	}

	default TAnimation getNewAnimationOn(JSNodeElement src, Object type) {
		let(aAnim, newJS(TAnimation.class));
		aAnim.type().set(var(type));
		aAnim.src().set(src);
		aAnim.speed().set(0);
		aAnim.startIdx().set(0);
		aAnim.stopIdx().set(0);
		addAnimation(aAnim);
		return aAnim;
	}

	default void start() {
		__("let easeinout = function(k) { return .5*(Math.sin((k - .5)*Math.PI) + 1)}");

		let(anActionListener, newJS(JSObject.class));
		anActionListener.onStart().set(fct(actionEvent, () -> {
			lastActionEvent().set(actionEvent);
		}).bind(_this()));
		anActionListener.onMove().set(fct(actionEvent, () -> {
			lastActionEvent().set(actionEvent);
		}).bind(_this()));
		anActionListener.onStop().set(fct(actionEvent, () -> {
			lastActionEvent().set(null);
			touchActionStopped().set(actionEvent);
		}).bind(_this()));

		theActionManager.addListener(anActionListener);

		let(aPromise, newJS(JSPromise.class, fct(resolve, reject, () -> {

			let(subscriber, aRequestAnimation.addListener(fct(aAnimEvent, () -> {

				_if(aQueue().idxPhase(), "==", aQueue().listPhase().length()).then(() -> {
					resolve.invoke(subscriber);
					_return();
				});

				let(aPhase, aQueue().listPhase().at(aQueue().idxPhase()));
				let(nbEnd, 0);
				let(nbEndTouchActionStopped, 0);

				/** boucle sur les animations de la phases */
				forIdx(idx, aPhase.listAnimation())._do(() -> {
					let(aAnim, aPhase.listAnimation().at(idx));

					_if(aAnim.timeStart().equalsJS(null)).then(() -> {
						// demarrage de l'animation
						aAnim.timeStart().set(aAnimEvent.time());
						aAnim.lastTickTime().set(aAnimEvent.time());
						aAnim.startIdxInitial().set(aAnim.startIdx());
						aAnim.speedInitial().set(aAnim.speed());
						aAnim.currentIdx().set(aAnim.startIdx());

						_if(aAnim.beforeStart().notEqualsJS(null)).then(() -> {
							aAnim.beforeStart().call(aAnim);
						});
						
					})._elseif(aAnim.timeEnd().equalsJS(null)).then(() -> {
						// tick de l'animation

						let(modeTouchActionInProgress, lastActionEvent(), "!=null");
						let(modeTouchActionEnd, "!", modeTouchActionInProgress, "&&", touchActionStopped(), "!=null",
								"&&", touchActionStopped().infoEvent().deltaY(), "!=0");

						_if(modeTouchActionInProgress).then(() -> {
							// arret de l'anim si touchaction
							aAnim.timeStart().set(
									calc(aAnim.timeStart(), "+ (", aAnimEvent.time(), "-", aAnim.lastTickTime(), ")"));
						});

						_if(modeTouchActionEnd).then(() -> {
							// calcul de timeStart si touchaction terminer
							aAnim.timeStart().set(aAnimEvent.time());
							aAnim.startIdx().set(aAnim.currentIdx());
							let(prctDeltaTouch, "(", aAnim.currentIdx(), "-", aAnim.startIdxInitial(), ")/(",
									aAnim.stopIdx(), "-", aAnim.startIdxInitial(), ")");
							aAnim.speed().set(aAnim.speedInitial(), "*(1-", prctDeltaTouch, ")");
														
							nbEndTouchActionStopped.set(nbEndTouchActionStopped.add(1));							
						});

						/** calcul prct animation par rapport au temps ecoule et la vitesse */
						let(timeAnim, calc(aAnimEvent.time(), "-", aAnim.timeStart()));
						let(prctAnim, calc(timeAnim, "/", aAnim.speed()));

						_if(modeTouchActionInProgress).then(() -> {
							// calcul du delta de touch a ajouter
							let(prctDeltaTouch, 0);
							prctDeltaTouch.set(lastActionEvent().infoEvent().deltaY(), "/",
									JSWindow.window().innerHeight());
							prctAnim.set(var("easeinout(prctAnim)")); // ease in out
							prctAnim.set(prctAnim, "-", prctDeltaTouch, "*", touchActionSign());
						});

						/*************************************************/
						_if(prctAnim, ">=", 1).then(() -> {
							prctAnim.set(1);
							_if("!", modeTouchActionInProgress).then(() -> {
								// fin de l'anim si prct anim à 1 (100%)
								aAnim.timeEnd().set(aAnimEvent.time());
							});
						});

						/**************************************************/
						_if("!", modeTouchActionInProgress, "&&", withEasing()).then(() -> {
							prctAnim.set(var("easeinout(prctAnim)")); // ease in out si pas en touch action
						});

						/**************************************************/
						let(valAnim, calc("(", aAnim.stopIdx(), "- ", aAnim.startIdx(), ") *", prctAnim, "+",
								aAnim.startIdx()));

						aAnim.currentIdx().set(valAnim);
												
						/**************** mode de l'animation ******************/
						_if(aAnim.type().equalsJS(SCALE_XY)).then(() -> {
							valAnim.set(valAnim, ".toFixed(5)");
							aAnim.src().style().attr("transform").set(txt("scale3d(", valAnim, ",", valAnim, ", 1)"));
						})._elseif(aAnim.type().equalsJS(BOTTOM_TO_FRONT)).then(() -> {
							
//							_if(valAnim, "<", aAnim.startIdxInitial(), "*", touchActionSign()).then(() -> {
//								consoleDebug("'eeee'",valAnim, aAnim.startIdxInitial());
//								valAnim.set(aAnim.startIdxInitial());
//							});
							
//							JSDocument.document().querySelector(ScnPageA.cIdlog).firstNodeValue().set(valAnim);

														
							valAnim.set(JSWindow.window().innerHeight(), "- (", JSWindow.window().innerHeight(), "*",
									valAnim, ")/100");
							
							valAnim.set(valAnim, ".toFixed(5)");
							aAnim.src().style().attr("transform").set(txt("translate3d(0px, ", valAnim, "px, 0px)"));

						})._elseif(aAnim.type().equalsJS(OPACITY)).then(() -> {
							valAnim.set(valAnim, ".toFixed(5)");
							aAnim.src().style().attr("opacity").set(valAnim);
						});

					})._else(() -> {
						// fin de l'animation
						nbEnd.set(nbEnd.add(1));
					});

					aAnim.lastTickTime().set(aAnimEvent.time());
				});

				_if(nbEndTouchActionStopped, "==", aPhase.listAnimation().length()).then(() -> {
					// fin du stopped sur toute les animation
					touchActionStopped().set(null);
				});

				_if(nbEnd, "==", aPhase.listAnimation().length()).then(() -> {
					aQueue().idxPhase().set(aQueue().idxPhase(), "+1");
				});

			}).bind(_this())));

		}).bind(_this())));

		aPromise.then(fct(subscriber, () -> {
			aRequestAnimation.removeListener(subscriber);
			theActionManager.removeListener(anActionListener);
		}));

	}

	/********************************************************************/
	public interface TQueueAnim extends JSType {
		JSArray<TPhase> listPhase();

		JSInt idxPhase();

		JSString id();
	}

	public interface TPhase extends JSType {
		// JSCallBack onStart();
		// JSCallBack isTerminated();
		JSArray<TAnimation> listAnimation();
	}

	public interface TAnimation extends JSType {
		JSCallBack beforeStart();

		JSNodeElement src();

		JSString type();

		JSFloat startIdx();

		JSFloat startIdxInitial();

		JSFloat stopIdx();

		JSFloat speed();

		JSFloat speedInitial();

		JSFloat lastTickTime();

		JSFloat timeStart();

		JSFloat timeEnd();

		JSFloat currentIdx();
	}
}
