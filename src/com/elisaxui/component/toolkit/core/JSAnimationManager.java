/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import com.elisaxui.app.core.admin.ScnPageA;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionEvent;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionListener;
import com.elisaxui.component.toolkit.core.JSRequestAnimationFrame.TAnimFrameEvent;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
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
public interface JSAnimationManager extends JSClass {

	public static final String OPACITY = "OPACITY";
	public static final String SCALE_XY = "SCALE_XY";
	public static final String BOTTOM_TO_FRONT = "BOTTOM_TO_FRONT";
	public static final String LEFT_TO_FRONT = "LEFT_TO_FRONT";
	public static final String RIGHT_TO_FRONT = "RIGHT_TO_FRONT";

	TAnimation aAnimation = JSClass.declareType();

	TAnimFrameEvent aAnimEvent = JSClass.declareType();

	TPhase aPhase = JSClass.declareType();
	TAnimation aAnim = JSClass.declareType();

	JSAny cssMatrix = JSClass.declareType();
	JSFloat timeAnim = JSClass.declareType();
	JSFloat prctAnim = JSClass.declareType();
	JSFloat valAnim = JSClass.declareType();
	JSInt nbEndAnimationInPhase = JSClass.declareType();
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
	JSObject listSrcAnim = JSClass.declareType();
	TAnimTransform aTAnimTransform = JSClass.declareType();
	JSon key = JSClass.declareType();
	JSInt viewPortSize = JSClass.declareType();

	TQueueAnim aQueue();

	TPhase lastPhase();

	// TActionEvent lastActionEvent();

	JSInt touchActionSign();

	JSInt touchStartTime();

	TActionEvent touchActionStopped();

	JSBool withEasing();

	default void constructor(JSString id) {
		aQueue().set(newJS(TQueueAnim.class));
		aQueue().id().set(id);
		aQueue().listPhase().set(JSArray.newLitteral());
		aQueue().idxPhase().set(0);
		touchActionSign().set(1);
		withEasing().set(true);
		touchStartTime().set(1); // 1 = rien au premier stop click
		_if(theActionManager.currentActionEvent(), "!=null").then(() -> {
			_if(theActionManager.currentActionEvent().actionInfo().modeTouch().equalsJS("SWIPE")).then(() -> {
				touchStartTime().set(0); // conserve le touchDown en tant que demarrage de l'animation
			});
		});
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
		aAnim.isStarted().set(false);
		aAnim.speed().set(0);
		aAnim.startIdx().set(0);
		aAnim.stopIdx().set(0);
		aAnim.currentIdx().set(0);
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
		aAnim.currentIdx().set(0);
		addAnimation(aAnim);
		return aAnim;
	}

	default void start() {
		__("let easeinout = function(k) { return .5*(Math.sin((k - .5)*Math.PI) + 1)}");

		/*********************************************************************************/
		let(anActionListener, newJS(JSObject.class));
		anActionListener.onStart().set(fct(actionEvent, () -> {
		}).bind(_this()));
		anActionListener.onMove().set(fct(actionEvent, () -> {
		}).bind(_this()));
		anActionListener.onStop().set(fct(actionEvent, () -> {
			_if(touchStartTime(), "<", actionEvent.infoEvent().startTime()).then(() -> {
				consoleDebug("'init 0 stop touch at '", touchStartTime(), aQueue().id());
				touchStartTime().set(0); // attente 1° stop
			});

			touchActionStopped().set(actionEvent);
		}).bind(_this()));

		theActionManager.addListener(anActionListener);

		/*********************************************************************************/
		let(aPromise, newJS(JSPromise.class, fct(resolve, reject, () -> {
			// action en tick animation
			let(subscriber, aRequestAnimation.addListener(fct(aAnimEvent, () -> {

				_if(aQueue().idxPhase(), "==", aQueue().listPhase().length()).then(() -> {
					// fin du tick animation
					resolve.invoke(subscriber);
					_return();
				});

				/*****************************************************************/
				let(aPhase, aQueue().listPhase().at(aQueue().idxPhase()));
				let(nbEndAnimationInPhase, 0);
				let(nbEndTouchActionStopped, 0);

				let(listSrcAnim, JSObject.newLitteral());

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
						aAnim.IdxMax().set(calc("Math.max(",aAnim.startIdx(),",",aAnim.stopIdx(),")" ));
						aAnim.IdxMin().set(calc("Math.min(",aAnim.startIdx(),",",aAnim.stopIdx(),")" ));

						_if(aAnim.beforeStart().notEqualsJS(null)).then(() -> {
							aAnim.beforeStart().call(aAnim);
						});

					})._elseif(aAnim.timeEnd().equalsJS(null), "&&", aAnim.speed(), ">0").then(() -> {
						// tick de l'animation

						_if("!", aAnim.isStarted(), "&&", aAnim.afterStart().notEqualsJS(null)).then(() -> {
							aAnim.isStarted().set(true);
							aAnim.afterStart().call(aAnim);
						});

						let(modeTouchActionInProgress, theActionManager.currentActionEvent(), "!=null && ",
								touchStartTime(), "!=1");

						_if(touchStartTime().equalsJS(0), " && ", modeTouchActionInProgress).then(() -> {
							touchStartTime().set(theActionManager.currentActionEvent().infoEvent().startTime());
							consoleDebug("'start touch at '", touchStartTime(), aQueue().id());
						});

						let(modeTouchActionEnd, "!", modeTouchActionInProgress, " && ", touchActionStopped(), "!=null",
								" && ", touchActionStopped().infoEvent().deltaY(), "!=0");

						_if(modeTouchActionEnd, " && ",
								touchStartTime().notEqualsJS(touchActionStopped().infoEvent().startTime())).then(() -> {
									modeTouchActionEnd.set(false);
									touchActionStopped().set(null);
									consoleDebug("'no stoptouch with same time start'", touchStartTime(),
											aQueue().id());
								});

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
							consoleDebug("'stop touch at '", touchActionStopped().infoEvent().startTime(),
									aQueue().id());
						});

						/** calcul prct animation par rapport au temps ecoule et la vitesse **/
						let(timeAnim, calc(aAnimEvent.time(), "-", aAnim.timeStart()));
						let(prctAnim, calc(timeAnim, "/", aAnim.speed()));

						_if(modeTouchActionInProgress).then(() -> {
							// calcul du delta de touch a ajouter
							let(prctDeltaTouch, 0);

							_if(aPhase.touchType().equalsJS(BOTTOM_TO_FRONT)).then(() -> {
								// from Top
								let(viewPortSize, aAnim.src().getBoundingClientRect().attr("height"));
								prctDeltaTouch.set(theActionManager.currentActionEvent().infoEvent().deltaY(), "/",
										viewPortSize);
							});

							_if(aPhase.touchType().equalsJS(RIGHT_TO_FRONT)).then(() -> {
								// from right
								let(viewPortSize, aAnim.src().getBoundingClientRect().attr("width"));
								prctDeltaTouch.set(theActionManager.currentActionEvent().infoEvent().deltaX(), "/",
										viewPortSize);
							});

							_if(aPhase.touchType().equalsJS(LEFT_TO_FRONT)).then(() -> {
								// from left
								let(viewPortSize, aAnim.src().getBoundingClientRect().attr("width"));
								prctDeltaTouch.set("( -", theActionManager.currentActionEvent().infoEvent().deltaX(),
										")/", viewPortSize);
							});

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

						_if(valAnim, ">", aAnim.IdxMax()).then(() -> {
							valAnim.set(aAnim.IdxMax());
						});
						
						_if(valAnim, "<", aAnim.IdxMin()).then(() -> {
							valAnim.set(aAnim.IdxMin());
						});
						
						aAnim.currentIdx().set(valAnim);

						/**************** mode de l'animation ******************/
						let(aTAnimTransform, listSrcAnim.attrByStr(aAnim.src().attr("id")));
						_if(aTAnimTransform, "==null").then(() -> {
							aTAnimTransform.set(newJS(TAnimTransform.class));
							aTAnimTransform.target().set(aAnim.src());
							aTAnimTransform.scale3d().set("");
							aTAnimTransform.translate3d().set("");
							listSrcAnim.attrByStr(aAnim.src().attr("id")).set(aTAnimTransform);
						});

						_if(aAnim.type().equalsJS(SCALE_XY)).then(() -> {
							valAnim.set(valAnim, ".toFixed(5)");
							aTAnimTransform.scale3d().set(txt("scale3d(", valAnim, ",", valAnim, ", 1)"));

						})._elseif(aAnim.type().equalsJS(BOTTOM_TO_FRONT)).then(() -> {
							let(viewPortSize, aAnim.src().getBoundingClientRect().attr("height"));
							valAnim.set(viewPortSize, "- (", viewPortSize, "*", valAnim, ")/100");

							valAnim.set(valAnim, ".toFixed(5)");
							aTAnimTransform.translate3d().set(txt("translate3d(0px, ", valAnim, "px, 0px)"));

						})._elseif(aAnim.type().equalsJS(RIGHT_TO_FRONT)).then(() -> {
							let(viewPortSize, aAnim.src().getBoundingClientRect().attr("width"));
							valAnim.set(viewPortSize, "- (", viewPortSize, "*", valAnim, ")/100");

							valAnim.set(valAnim, ".toFixed(5)");
							aTAnimTransform.translate3d().set(txt("translate3d(", valAnim, "px, 0px, 0px)"));

						})._elseif(aAnim.type().equalsJS(LEFT_TO_FRONT)).then(() -> {

							let(viewPortSize, aAnim.src().getBoundingClientRect().attr("width"));
							valAnim.set(viewPortSize, "- (", viewPortSize, "*", valAnim, ")/100");

							valAnim.set(valAnim, ".toFixed(5)");
							valAnim.set("-", valAnim);
							doLogInfo(valAnim, aAnimEvent);
							aTAnimTransform.translate3d().set(txt("translate3d(", valAnim, "px, 0px, 0px)"));

						})._elseif(aAnim.type().equalsJS(OPACITY)).then(() -> {

							valAnim.set(valAnim, ".toFixed(5)");
							// doLogInfo(valAnim, aAnimEvent);

							aTAnimTransform.opacity().set(valAnim);
						});

					})._else(() -> {
						// fin de l'animation
						nbEndAnimationInPhase.set(nbEndAnimationInPhase.add(1));

					});

					aAnim.lastTickTime().set(aAnimEvent.time());
				});
				/** fin boucle sur les animations de la phases */

				/** affecte les styles si necessaire */
				_for("var ", key, " in ", listSrcAnim)._do(() -> {
					let(aTAnimTransform, listSrcAnim.attrByStr(key));
					_if(aTAnimTransform.translate3d().notEqualsJS(""), "|| ", aTAnimTransform.scale3d().notEqualsJS(""))
							.then(() -> {
								aTAnimTransform.target().style().attr("transform")
										.set(txt(aTAnimTransform.translate3d(), " ", aTAnimTransform.scale3d()));
							});
					_if(aTAnimTransform.opacity().notEqualsJS(null)).then(() -> {
						aTAnimTransform.target().style().attr("opacity").set(aTAnimTransform.opacity());
					});
				});

				/****************************************/
				_if(nbEndTouchActionStopped, "==", aPhase.listAnimation().length()).then(() -> {
					// fin du stopped sur toute les animation ==> alors remise a zero
					touchActionStopped().set(null);
					touchStartTime().set(0);
				});

				// passe à la phase suivante si plus d'animation sur la phase
				_if(nbEndAnimationInPhase, "==", aPhase.listAnimation().length()).then(() -> {
					aQueue().idxPhase().set(aQueue().idxPhase(), "+1");
				});
				/****************************************/
			}).bind(_this())));

		}).bind(_this())));

		/****************************************************************/
		aPromise.then(fct(subscriber, () -> {
			aRequestAnimation.removeListener(subscriber);
			theActionManager.removeListener(anActionListener);
			touchStartTime().set(0);
		}).bind(_this()));

	}

	JSNodeElement log = JSClass.declareType();

	/**
	 * 
	 */
	default void doLogInfo(JSFloat valAnim, TAnimFrameEvent aAnimEvent) {
		let(log, JSDocument.document().querySelector(ScnPageA.cIdlog));
		_if(log.notEqualsJS(null)).then(() -> {
			log.firstNodeValue().set(valAnim, "+' - '+", aAnimEvent.time());
		});
	}

	/********************************************************************/
	public interface TAnimTransform extends JSType {
		JSNodeElement target();

		JSString translate3d();

		JSString scale3d();

		JSString opacity();
	}

	public interface TQueueAnim extends JSType {
		JSArray<TPhase> listPhase();

		JSInt idxPhase();

		JSString id();
	}

	public interface TPhase extends JSType {
		// JSCallBack onStart();
		// JSCallBack isTerminated();
		JSArray<TAnimation> listAnimation();

		JSString touchType();
	}

	public interface TAnimation extends JSType {
		JSCallBack beforeStart();

		JSCallBack afterStart();

		JSBool isStarted();

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

		/*********************************************/
		JSInt IdxMin();

		JSInt IdxMax();
	}
}
