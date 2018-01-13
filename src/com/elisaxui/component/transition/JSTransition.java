/**
 * 
 */
package com.elisaxui.component.transition;

import static com.elisaxui.component.page.XUIScene.*;
import static com.elisaxui.component.toolkit.JQuery.$;
import static com.elisaxui.component.transition.ConstTransition.*;
import static com.elisaxui.component.transition.CssTransition.*;
import static com.elisaxui.component.widget.navbar.ViewNavBar.isOpenMenu;
import static com.elisaxui.component.widget.navbar.ViewNavBar.navbar;
import static com.elisaxui.component.widget.tabbar.ViewTabBar.cTabbar;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.menu.ViewMenu;
import com.elisaxui.component.widget.overlay.JSOverlay;
import com.elisaxui.component.widget.overlay.ViewOverlayRipple;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * TODO
 * 
 * - creer un layout intercalaire entre activity (ex ripple anim) 
 * - creer un layout intercalaire au dessus des activity ( ex zoom image anim, overlay layout) 
 * - gerer status de l'intention (route push or route pop meme page ou autre page SEO) 
 * - ne pas reutiliser une activité deja dans l historique (ou remettre dans l'etat animation du status de l'intention) 
 * - gerer le menu burger comme une activity 
 * - gerer sur tous le animation les classes transitionSpeedx1 transitionSpeedx2 - class StateOpen , StateClose, StateXXX   (voir ViewOverlayRipple)
 * - empecher ou differer le button back durant l'animation sinon pb execution fin d'animation
 *
 */

public interface JSTransition extends JSClass {


	public static final String NULL = "null";
	
	public static final String HIDDEN = "hidden";
	
	public static final String VISIBILITY = "visibility";
	public static final String HEIGHT = "height";
	public static final String ABSOLUTE = "absolute";
	public static final String POSITION = "position";
	public static final String TOP = "top";
	public static final String OVERFLOW = "overflow";
	public static final String TRANSITION = "transition";
	public static final String TRANSFORM = "transform";
	
	public static final String DATA_SCROLLTOP = "scrolltop";

	
	default JSVoid doFixedElemToAbsolute(JQuery act, JSInt sct) {
		
		JQuery $FixedElem = let(JQuery.class, "$FixedElem", act.find(cFixedElement));

		$FixedElem.each(callback(()->{
			JSInt posTop = let(JSInt.class, "posTop", sct);
			posTop.set(posTop.add($(var("this")).get(0), ".getBoundingClientRect().y"));   //TODO add JSElement
			_if(posTop, ">0");
				$(var("this")).css(TOP, txt(posTop,"px"));	
			endif();
		}));
		
		$FixedElem.css(POSITION, ABSOLUTE); // permet la nav de bouger
		return _void();
	}
	
	
	default JSVoid doFixedElemToFixe(JQuery act) {
		JQuery $FixedElem = let(JQuery.class, "$FixedElem", act.find(cFixedElement));
		$FixedElem.css(TOP, "");
		$FixedElem.css(POSITION,"");
		return _void();
	}

	default JSVoid doToggleBurgerMenu() {
		
		JSOverlay overlay = let( JSOverlay.class, "overlay2", NULL );         /*, _new(SPEED_SHOW_MENU, OVERLAY_OPACITY_MENU)*/
		_set(overlay, _new(SPEED_SHOW_MENU, XUIScene.OVERLAY_OPACITY_MENU));  /* TODO a faire marcher*/
		
		JQuery jqMenu = let( JQuery.class, "jqMenu", $(ViewMenu.menu) );
		JQuery jqScene = let( JQuery.class, "jqScene", $(scene) );
	
		JQuery jqActivityActive = let( JQuery.class, "jqActivityActive", $(activity.and(active)) );
		JQuery jqNavBar = let( JQuery.class, "jqNavBar", $(activity.and(active).directChildren(navbar)) );
		JSVariable document = JSClass.declareType(JSVariable.class, "document");
		
		JSInt sct = let( JSInt.class, "sct",    $(document).scrollTop() );

		JSTransition self = let(JSTransition.class, "self", "this");

		_if(jqNavBar.hasClass(isOpenMenu));
		
			/*************************************************/
			// ferme le menu
			/*************************************************/
			jqNavBar.removeClass(isOpenMenu);
		
			JQuery jqHamburgerDetach = let( JQuery.class, "jqHamburgerDetach", $(scene.descendant(ViewBtnBurger.hamburger.and(detach))) );
			__(TKQueue.startAnimQueued(	 callback(()->{  
							overlay.doHideOverlay(1);
							// -------------------------- repositionne l'activity --------------------
							jqActivityActive.removeClass(activityMoveForShowMenu);
							jqActivityActive.addClass(activityMoveForHideMenu);
							// ----------------------------- cache le menu ------------------------
							jqMenu.css(TRANSFORM, txt("translate3d(-" + (XUIScene.widthMenu + 5)	+ "px," , sct ,  "px, 0px)"));
							// ----------------------------- repasse en croix ----------------------
							jqHamburgerDetach.css(TRANSITION, "transform " + SPEED_SHOW_MENU	+ "ms linear");
							jqHamburgerDetach.css(TRANSFORM, txt("translate3d(0px,", sct, "px,0px) scale(1)" ));
							})
					, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, callback(()->{
							overlay.doHideOverlay(2);

							// ----------- fige la barre nav en haut (fixed) --------
							self.doActivityDeFreeze(jqActivityActive);
							self.doInitScrollTo(jqActivityActive);
							self.doFixedElemToFixe(jqActivityActive);
							
							// anime le burger et le passe de la scene vers l'activity
							JQuery hamburger = let(JQuery.class, "hamburger", jqHamburgerDetach.detach() ) ;
							hamburger.removeClass(detach);
							jqNavBar.append(hamburger);
							hamburger.css(TRANSITION, "");
							hamburger.css(TRANSFORM, "");
							
							// -------------------------- fin du repositionnement l'activity
							jqActivityActive.removeClass(activityMoveForHideMenu);
							})
					, NEXT_FRAME,  callback(()->{
							jqHamburgerDetach.removeClass("is-active"); 
							__("window.disableScrollEvent=null");
					})
					, SPEED_BURGER_EFFECT, fct()
				))

			._else();
				jqNavBar.addClass(isOpenMenu);
				
				JQuery jqHamburger = let( JQuery.class, "jqHamburger", jqNavBar.find(ViewBtnBurger.hamburger) );
				/*************************************************/
				// ouvre le menu
				/*************************************************/
				__(TKQueue.startAnimQueued(callback("param", ()->{
							__("window.disableScrollEvent=true");
							overlay.doShowOverlay(jqActivityActive, 1);
							
							// ----------- detache les barres et float action --------
							self.doActivityFreeze(jqActivityActive, sct);
							self.doFixedElemToAbsolute(jqActivityActive, cast(JSInt.class, 0));
	
							// ---------------------------------------	
							jqMenu.css(TRANSITION, ""); // fige le menu en haut sans animation
							jqMenu.css(TRANSFORM, txt("translate3d(-" + XUIScene.widthMenu + "px," ,/*sct*/0, "px,0px)" ));
							// ---------- anime le burger et le passe sur la scene---------------
							jqHamburger.detach();
							jqHamburger.addClass(detach);
							jqHamburger.css(TRANSFORM, txt("translate3d(0px,",/*sct*/0,"px,0px)")); // TODO mettre une class
							jqHamburger.css(TRANSITION, "transform " + SPEED_SHOW_MENU + "ms linear");   // prepare transition
							jqScene.append(jqHamburger);
						})
						, NEXT_FRAME, callback(()->{
							
								// ------------ deplace l'activity a l ouverture du menu-------------
								jqActivityActive.addClass(activityMoveForShowMenu);
								// ------------ ouvre le menu avec animation---------
								jqMenu.css(TRANSITION, "transform " + SPEED_SHOW_MENU + "ms linear");  // TODO mettre une class
								jqMenu.css(TRANSFORM, txt("translate3d(0px,",/*sct*/0,"px,0px"));
								// -------------------------------------------------
								overlay.doShowOverlay(jqActivityActive, 2);
								// ------------ deplace le hamburger---------
								jqHamburger.css(TRANSFORM, txt("translate3d(-15px,",calc("(",/*sct*/0,"-3)"),"px,0px) scale(0.6)"));
								// ------------- anim des item de menu---------
								_for("var i in window.jsonMainMenu");
									JSon jsonMenu = let(JSon.class, "jsonMenu", "window.jsonMainMenu[i]");
									$( jsonMenu.attrByString("_dom_") ).css(VISIBILITY, HIDDEN);
									
									setTimeout( fct("itemMenu").__(()->{
													__("itemMenu.anim='fadeInLeft'");
													__("itemMenu.anim=''");
												})
										, calc("i*" + SPEED_SHOW_MENU_ITEMS_ANIM)
										, jsonMenu);

								endfor();
						})
						, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, callback(()->{
								jqHamburger.addClass("is-active"); // passe en croix
								})
						, SPEED_BURGER_EFFECT, fct()))
				.endif();
		return _void();
	}

	default JSVoid doActivityFreeze(JQuery act, JSInt sct) {
		act.addClass(cStateFixedForFreeze);

		_if(sct.isEqual(-1));
				sct.set(act.data(DATA_SCROLLTOP));
				sct.set(calc(sct,"==null?0:",sct));  // met à 0 si null
		endif();

		act.data(DATA_SCROLLTOP, sct );   // sauvegarde scroll position
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.content));
		// freeze
		actContent.css(OVERFLOW, HIDDEN); // fait clignoter en ios
		actContent.css(HEIGHT, "100vh");
		actContent.scrollTop(sct);
		return _void();
	}

	default JSVoid doActivityDeFreeze(JQuery act) {
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.content));
		actContent.css(OVERFLOW, "");
		actContent.css(HEIGHT, "");
		$(act).removeClass(cStateFixedForFreeze);
		return _void();
	}

	default JSVoid doActivityInactive(JQuery act)
	{
		act.removeClass(active);
		act.addClass(inactive);
		return _void();
	}

	default JSVoid doActivityActive(JQuery act) {
		act.removeClass(cStateNoDisplay);
		act.addClass(active);
		act.removeClass(inactive);
		return _void();
	}

	/**
	 * peux etre inactif mais encore visible durant la transistion
	 * @param act
	 * @return
	 */
	default JSVoid doActivityNoDisplay(JQuery act) {
		act.removeClass(active);
		act.addClass(cStateNoDisplay);
		return _void();
	}

	default JSVoid doInitScrollTo(JQuery act) {
		JSInt scrposition = let(JSInt.class, "scrposition", act.data(DATA_SCROLLTOP));
		JSVariable document = JSClass.declareType(JSVariable.class, "document");
		$(document).scrollTop(calc(scrposition,"==null?0:",scrposition));
		return _void();
	}

	default JSVoid doOpenActivityFromBottom() {
		JSOverlay overlay = let(JSOverlay.class, "overlay", NULL);
		_set(overlay, _new(SPEED_SHOW_ACTIVITY, XUIScene.OVERLAY_OPACITY_BACK));
		JSVariable document = JSClass.declareType(JSVariable.class, "document");
		JSInt sct = let(JSInt.class, "sct", $(document).scrollTop());
		JSString act1 = let(JSString.class, "act1", "'#'+$xui.intent.prevActivity");
		JSString act2 = let(JSString.class, "act2", "'#'+$xui.intent.activity");
		JQuery jqAct1 = let(JQuery.class, "jqAct1", $(act1));
		JQuery jqAct2 = let(JQuery.class, "jqAct2", $(act2));

		JSInt MEM_SCROLL = cast(JSInt.class,"-1");
		JSInt ZERO = cast(JSInt.class,"0");
		JSTransition self = let(JSTransition.class, "self", "this");
		
		_if(jqAct1.hasClass(active))
			// ouverture activity 2
			.__(TKQueue.startAnimQueued( callback(()->{
							self.doFixedElemToAbsolute(jqAct1, ZERO);
							overlay.doShowOverlay(jqAct1, 1); // init
							self.doActivityInactive(jqAct1);
							self.doActivityFreeze(jqAct1, sct); // freeze 1
							self.doActivityActive(jqAct2);
	
							jqAct1.addClass(cStateBackActivity);
	
							jqAct2.addClass(cStateFrontActivity);
							jqAct2.addClass(cStateMoveToBottom); // prepare l'animation top 0 fixed
							})
					, NEXT_FRAME, callback(()->{
							// lance les anim
							overlay.doShowOverlay(jqAct1, 2);
							self.doActivityFreeze(jqAct2, MEM_SCROLL); // freeze 2
	
							jqAct1.addClass(transitionSpeed);
							jqAct1.addClass(cStateZoom09);
							jqAct2.addClass(cStateMoveToFront);
						})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, callback(()->{ 
							self.doActivityNoDisplay(jqAct1);
	
							jqAct2.removeClass(cStateMoveToFront);
							jqAct2.removeClass(cStateMoveToBottom);
							jqAct2.removeClass(cStateFrontActivity);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doFixedElemToFixe(jqAct2);
	
							self.doInitScrollTo(jqAct2);
						})
					, NEXT_FRAME, fct()
			))
		._else()
		// fermeture activity 2
			.__(TKQueue.startAnimQueued(
					callback(()->{ 
							self.doFixedElemToAbsolute(jqAct2, ZERO);
							self.doActivityInactive(jqAct2);
							self.doActivityActive(jqAct1);
							self.doActivityFreeze(jqAct2, sct); // frezze 2
							jqAct2.addClass(cStateFrontActivity);
					})
					, NEXT_FRAME, callback(()->{  // lance les anim
							jqAct2.addClass(cStateMoveToBottom);	
					})
					, DELAY_SURETE_END_ANIMATION, callback(()->{  // lance les anim
							overlay.doHideOverlay(1);
							jqAct1.removeClass(cStateZoom09);  // revient en zoom 1

					})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, callback(()->{
							overlay.doHideOverlay(2);
							self.doActivityNoDisplay(jqAct2);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doFixedElemToFixe(jqAct1);
	
							jqAct1.removeClass(transitionSpeed);
							jqAct2.removeClass(cStateMoveToBottom);
							jqAct2.removeClass(cStateFrontActivity);
							jqAct1.removeClass(cStateBackActivity);
	
							self.doActivityDeFreeze(jqAct1); // defrezze 1
							self.doInitScrollTo(jqAct1);
					})
					, NEXT_FRAME, fct()
			))
		.endif();

		return _void();
	}


	default JSVoid doOpenActivityFromRipple() {

		JSInt MEM_SCROLL = cast(JSInt.class,"-1");
		JSInt SCROLL_TOP = cast(JSInt.class,"0");
		
		JSOverlay overlay = let(JSOverlay.class, "overlay", NULL);
		_set(overlay, _new(SPEED_SHOW_ACTIVITY, 0.6));

		JSTransition self = let(JSTransition.class, "self", "this");
		JSVariable document = JSClass.declareType(JSVariable.class, "document");
		
		JSInt sct = let(JSInt.class, "sct", $(document).scrollTop());
		
		JSString act1 = let(JSString.class, "act1", "'#'+$xui.intent.prevActivity");
		JSString act2 = let(JSString.class, "act2", "'#'+$xui.intent.activity");
		JQuery jqAct1 = let(JQuery.class, "jqAct1", $(act1));
		JQuery jqAct2 = let(JQuery.class, "jqAct2", $(act2));

		_if(jqAct1.hasClass(active))
		// ouverture activity 2
		.__(TKQueue.startAnimQueued(
				callback(()->{
						overlay.doShowOverlay(jqAct1, 1);   // met en gris
						self.doFixedElemToAbsolute(jqAct1, SCROLL_TOP);
						self.doActivityFreeze(jqAct1, sct); // frezze 1
						jqAct1.addClass(cStateBackActivity);
						
						self.doFixedElemToAbsolute(jqAct2, SCROLL_TOP);   // fixe pour ripple
						
						// ajoute le template du ripple overlay
//						JSXHTMLPart template = let(JSXHTMLPart.class, "template", ViewOverlayRipple.xTemplate());
//						JQuery $template = let(JQuery.class, "$template", template.appendInto($(act2)));
//						$template.addClass(circleAnim0prt);
				})
				, NEXT_FRAME, callback(()->{ // prepare animation de ripple	
						self.doActivityActive(jqAct2);
						// prepare anim
						jqAct2.addClass(cStateFrontActivity);
						jqAct2.addClass(circleAnim0prt);
						jqAct2.addClass(cStateZoom12);

						self.doActivityFreeze(jqAct2, MEM_SCROLL); // frezze 2
				})
				, NEXT_FRAME, callback(()->{ // lance animation dezoom
						overlay.doShowOverlay(jqAct1, 2);
						jqAct1.addClass(transitionSpeed);
						jqAct1.addClass(cStateZoom09);     // reduit
						self.doActivityInactive(jqAct1);
				})
				, DELAY_SURETE_END_ANIMATION, callback(()->{ // lance animation ripple
						jqAct2.removeClass(circleAnim0prt);
//						$(ViewOverlayRipple.ripple_overlay).removeClass(circleAnim0prt);
						jqAct2.addClass(transitionSpeedx2); // cercle effect
						jqAct2.addClass(circleAnim100prt);
						
//						$(ViewOverlayRipple.ripple_overlay).addClass(ViewOverlayRipple.transitionOpacity); //TODO pas 2 appel jquery
//						$(ViewOverlayRipple.ripple_overlay).addClass(circleAnim100prt);
					})
				,  NEXT_FRAME /*SPEED_ACTIVITY_TRANSITION_EFFECT+ DELAY_SURETE_END_ANIMATION*/, callback(()->{  // lance animation dezoom plus tard
					//	jqAct2.removeClass(circleAnim100prt);
						jqAct2.addClass(transitionSpeed);
						jqAct2.removeClass(transitionSpeedx2);
						jqAct2.removeClass(cStateZoom12);
						jqAct2.addClass(cStateZoom1);
						})
				, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, callback(()->{

						self.doFixedElemToFixe(jqAct2);
						self.doActivityNoDisplay(jqAct1);
						self.doActivityDeFreeze(jqAct2); // defrezze 2

						jqAct1.removeClass(transitionSpeed);
						
//						$(ViewOverlayRipple.ripple_overlay).remove();
						
						jqAct2.removeClass(transitionSpeed);
						jqAct2.removeClass(cStateFrontActivity);
						jqAct2.removeClass(cStateZoom1);

						// annule l'animation
						jqAct2.removeClass(circleAnim100prt);
						jqAct2.css(TRANSFORM, "");

						self.doInitScrollTo(jqAct2);
					})
					, NEXT_FRAME, fct()
					)
				
				)
		._else()
		// fermeture activity 2
		.__(TKQueue.startAnimQueued(callback(()->{
					self.doFixedElemToAbsolute(jqAct2, SCROLL_TOP);
					self.doActivityInactive(jqAct2);
					self.doActivityActive(jqAct1);
					self.doActivityFreeze(jqAct2, sct);

					jqAct2.addClass(circleAnim100prt);
					jqAct2.addClass(cStateFrontActivity);
				})
				, NEXT_FRAME, callback(()->{
					jqAct2.addClass(transitionSpeed);
					jqAct2.addClass(cStateZoom12); // lance le zoome
				})
				, NEXT_FRAME, callback(()->{ // puis lance la circle
					jqAct2.addClass(circleAnim0prt);
					jqAct2.removeClass(circleAnim100prt);
				})
				, SPEED_ACTIVITY_TRANSITION_EFFECT / 2, callback(()->{ // lance animation activity 1
					jqAct1.removeClass(cStateZoom09);
					jqAct1.addClass(transitionSpeed);
					overlay.doHideOverlay(1);
				})
				, Math.max(SPEED_SHOW_ACTIVITY, SPEED_ACTIVITY_TRANSITION_EFFECT) + DELAY_SURETE_END_ANIMATION,
					callback(()->{ 
					overlay.doHideOverlay(2);
					self.doActivityNoDisplay(jqAct2);
					self.doFixedElemToFixe(jqAct1);
					self.doActivityDeFreeze(jqAct1); // defrezze 1

					jqAct1.removeClass(transitionSpeed);

					self.doActivityDeFreeze(jqAct2); // defrezze 2

					jqAct2.removeClass(transitionSpeed);  // TODO gestion remove multiple class
					jqAct2.removeClass(transitionSpeedx2);
					jqAct2.removeClass(circleAnim0prt);
					jqAct2.removeClass(cStateZoom12);
					jqAct2.removeClass(cStateFrontActivity);
					
					jqAct1.removeClass(cStateBackActivity);

					self.doInitScrollTo(jqAct1);
				})
				, NEXT_FRAME, fct()
				))
		.endif();

		return _void();
	}
}
