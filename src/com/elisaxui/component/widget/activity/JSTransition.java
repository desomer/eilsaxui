/**
 * 
 */
package com.elisaxui.component.widget.activity;

import static com.elisaxui.component.toolkit.old.JQuery.$;
import static com.elisaxui.component.toolkit.transition.ConstTransition.*;
import static com.elisaxui.component.toolkit.transition.CssTransition.*;
import static com.elisaxui.component.page.CssPage.*;
import static com.elisaxui.component.page.old.XUIScene.*;
import static com.elisaxui.component.widget.navbar.ViewNavBar.isOpenMenu;
import static com.elisaxui.component.widget.navbar.ViewNavBar.navbar;

import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.old.JQuery;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.menu.old.ViewMenuOld;
import com.elisaxui.component.widget.overlay.ViewOverlayRipple;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * TODO
 * 
 * - creer un layout intercalaire entre activity (ex ripple anim) 
 * - creer un layout intercalaire au dessus des activity ( ex zoom image anim, overlay layout) 
 * - gerer status de l'intention (route push or route pop meme page ou autre page SEO) 
 * - ne pas reutiliser une activité deja dans l historique (ou remettre dans l'etat animation du status de l'intention) 
 * - gerer le menu burger comme une activity 
 * - gerer sur tous le animation les classesAct1 transitionSpeedx1 transitionSpeedx2 - class StateOpen , StateClose, StateXXX   (voir ViewOverlayRipple)
 * - empecher ou differer le button back durant l'animation sinon pb execution fin d'animation
 *
 */

@Deprecated
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

	
	default void doFixedElemToAbsolute(JQuery act, JSInt sct) {
		
		JQuery $fixedElem=let("$fixedElem", act.find(cFixedElement));

		$fixedElem.each(fct(()->{
			JSInt posTop = let(JSInt.class, "posTop", sct);
			posTop.set(posTop.add( $(var("this")).get(0), ".getBoundingClientRect().y") );   
			//TODO add JSElement
			_if(posTop, ">0").then(()->	$(var("this")).css(TOP, txt(posTop,"px")));
		}));
		
		$fixedElem.css(POSITION, ABSOLUTE); // permet la nav de bouger
	}
	
	
	default JSVoid doFixedElemToFixe(JQuery act) {
		JQuery $FixedElem = let(JQuery.class, "$FixedElem", act.find(cFixedElement));
		$FixedElem.css(TOP, "");
		$FixedElem.css(POSITION,"");
		return _void();  // TODO a retirer
	}

	default JSVoid doToggleBurgerMenu() {
		
		JSOverlayOld overlay = let("overlay", newJS(JSOverlayOld.class, SPEED_SHOW_MENU, OVERLAY_OPACITY_MENU) ); 
		
		JQuery jqMenu = let( JQuery.class, "jqMenu", $(ViewMenuOld.menu) );
		JQuery jqScene = let( JQuery.class, "jqScene", $(scene) );
	
		JQuery jqActivityActive = let( JQuery.class, "jqActivityActive", $(activity.and(active)) );
		JQuery jqNavBar = let( JQuery.class, "jqNavBar", $(activity.and(active).directChildren(navbar)) );
		JSAny document = declareType(JSAny.class, "document");
		
		JSInt sct = let( JSInt.class, "sct",    $(document).scrollTop() );

		JSTransition self = let(JSTransition.class, "self", "this");

		_if(jqNavBar.hasClass(isOpenMenu));
		
			/*************************************************/
			// ferme le menu
			/*************************************************/
			jqNavBar.removeClass(isOpenMenu);
		
			JQuery jqHamburgerDetach = let( JQuery.class, "jqHamburgerDetach", $(scene.descendant(ViewBtnBurger.cHamburger.and(detach))) );
			__(TKQueue.startAnimQueued(	 fct(()->{  
							overlay.doHideOverlay(1);
							// -------------------------- repositionne l'activity --------------------
							jqActivityActive.removeClass(activityMoveForShowMenu);
							jqActivityActive.addClass(activityMoveForHideMenu);
							// ----------------------------- cache le menu ------------------------
							jqMenu.css(TRANSFORM, txt("translate3d(-" + (widthMenu + 5)	+ "px," , sct ,  "px, 0px)"));
							// ----------------------------- repasse en croix ----------------------
							jqHamburgerDetach.css(TRANSITION, "transform " + SPEED_SHOW_MENU	+ "ms linear");
							jqHamburgerDetach.css(TRANSFORM, txt("translate3d(0px,", sct, "px,0px) scale(1)" ));
							})
					, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, fct(()->{
							overlay.doHideOverlay(2);

							// ----------- fige la barre nav en haut (fixed) --------
							self.doActivityDeFreeze(jqActivityActive);
							self.doInitScrollTo(jqActivityActive);
							self.doFixedElemToFixe(jqActivityActive);
							
							// anime le burger et le passe de la scene vers l'activity
							JQuery hamburger = let(JQuery.class, "cHamburger", jqHamburgerDetach.detach() ) ;
							hamburger.removeClass(detach);
							jqNavBar.append(hamburger);
							hamburger.css(TRANSITION, "");
							hamburger.css(TRANSFORM, "");
							
							// -------------------------- fin du repositionnement l'activity
							jqActivityActive.removeClass(activityMoveForHideMenu);
							})
					//, NEXT_FRAME,  fct(()->{    //marche plus  chrome v67
					, BUG_NEXT_FRAME_CHROME_V67, fct(()->{	
							jqHamburgerDetach.removeClass("is-active"); 
							__("window.disableScrollEvent=null");
					})
					, SPEED_BURGER_EFFECT, funct()
				))

			._else();
				jqNavBar.addClass(isOpenMenu);
				
				JQuery jqHamburger = let( JQuery.class, "jqHamburger", jqNavBar.find(ViewBtnBurger.cHamburger) );
				/*************************************************/
				// ouvre le menu
				/*************************************************/
				__(TKQueue.startAnimQueued(fct(()->{
							__("window.disableScrollEvent=true");
							overlay.doShowOverlay(jqActivityActive, 1);
							
							// ----------- detache les barres et float action --------
							self.doActivityFreeze(jqActivityActive, sct);
							self.doFixedElemToAbsolute(jqActivityActive, cast(JSInt.class, 0));
	
							// ---------------------------------------	
							jqMenu.css(TRANSITION, ""); // fige le menu en haut sans animation
							jqMenu.css(TRANSFORM, txt("translate3d(-" + widthMenu + "px," ,/*sct*/0, "px,0px)" ));
							// ---------- anime le burger et le passe sur la scene---------------
							jqHamburger.detach();
							jqHamburger.addClass(detach);
							jqHamburger.css(TRANSFORM, txt("translate3d(0px,",/*sct*/0,"px,0px)")); // TODO mettre une class
							jqHamburger.css(TRANSITION, "transform " + SPEED_SHOW_MENU + "ms linear");   // prepare transition
							jqScene.append(jqHamburger);
						})
						//, NEXT_FRAME, fct(()->{     marche plus  chrome v67
						, BUG_NEXT_FRAME_CHROME_V67, fct(()->{	
							
								// ------------ deplace l'activity a l ouverture du menu-------------
								jqActivityActive.addClass(activityMoveForShowMenu);
								// ------------ ouvre le menu avec animation---------
								jqMenu.css(TRANSITION, "transform " + SPEED_SHOW_MENU + "ms linear");  // TODO mettre une class
								jqMenu.css(TRANSFORM, txt("translate3d(0px,",/*sct*/0,"px,0px"));
								// -------------------------------------------------
								overlay.doShowOverlay(jqActivityActive, 2);
								// ------------ deplace le cHamburger---------
								jqHamburger.css(TRANSFORM, txt("translate3d(-15px,",calc(/*sct*/0,"-3"),"px,0px) scale(0.6)"));
								// ------------- anim des item de menu---------
								_for("var i in window.jsonMainMenu");
									JSon jsonMenu = let(JSon.class, "jsonMenu", "window.jsonMainMenu[i]");
									$( jsonMenu.attrByStr("_dom_") ).css(VISIBILITY, HIDDEN);
									
									setTimeout( funct("itemMenu").__(()->{
													__("itemMenu.anim='fadeInLeft'");
													__("itemMenu.anim=''");
												})
										, calc("i*" + SPEED_SHOW_MENU_ITEMS_ANIM)
										, jsonMenu);

								endfor();
						})
						, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, fct(()->{
								jqHamburger.addClass("is-active"); // passe en croix
								})
						, SPEED_BURGER_EFFECT, funct()))
				.endif();
		return _void();
	}

	default JSVoid doActivityFreeze(JQuery act, JSInt sct) {
		act.addClass(cStateFixedForFreeze);

		_if(sct.equalsJS(-1));
				sct.set(act.data(DATA_SCROLLTOP));
				sct.set(calc(sct,"==null?0:",sct));  // met à 0 si null
		endif();

		act.data(DATA_SCROLLTOP, sct );   // sauvegarde scroll position
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.getcContent()));
		// freeze
		actContent.css(OVERFLOW, HIDDEN); // fait clignoter en ios
		actContent.css(HEIGHT, "100vh");
		actContent.scrollTop(sct);
		return _void();
	}

	default JSVoid doActivityDeFreeze(JQuery act) {
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.getcContent()));
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
		JSAny document = declareType(JSAny.class, "document");
		$(document).scrollTop(calc(scrposition,"==null?0:",scrposition));
		return _void();
	}

	default JSVoid doOpenActivityFromBottom() {
		JSOverlayOld overlay = let(JSOverlayOld.class, "overlay", NULL);
		_set(overlay, _new(SPEED_SHOW_ACTIVITY, OVERLAY_OPACITY_BACK));
		JSAny document = declareType(JSAny.class, "document");
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
			.__(TKQueue.startAnimQueued( fct(()->{
							self.doFixedElemToAbsolute(jqAct1, ZERO);
							overlay.doShowOverlay(jqAct1, 1); // init
							self.doActivityInactive(jqAct1);
							self.doActivityFreeze(jqAct1, sct); // freeze 1
							self.doActivityActive(jqAct2);
	
							jqAct1.addClass(cStateBackActivity);
	
							jqAct2.addClass(cStateFrontActivity);
							jqAct2.addClass(cStateMoveToBottom); // prepare l'animation top 0 fixed
							})
					, NEXT_FRAME, fct(()->{
							// lance les anim
							overlay.doShowOverlay(jqAct1, 2);
							self.doActivityFreeze(jqAct2, MEM_SCROLL); // freeze 2
	
							jqAct1.addClass(cTransitionSpeed);
							jqAct1.addClass(cStateZoom09);
							jqAct2.addClass(cStateMoveToFront);
						})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct(()->{ 
							self.doActivityNoDisplay(jqAct1);
	
							jqAct2.removeClass(cStateMoveToFront);
							jqAct2.removeClass(cStateMoveToBottom);
							jqAct2.removeClass(cStateFrontActivity);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doFixedElemToFixe(jqAct2);
	
							self.doInitScrollTo(jqAct2);
						})
					, NEXT_FRAME, funct()
			))
		._else()
		// fermeture activity 2
			.__(TKQueue.startAnimQueued(
					fct(()->{ 
							self.doFixedElemToAbsolute(jqAct2, ZERO);
							self.doActivityInactive(jqAct2);
							self.doActivityActive(jqAct1);
							self.doActivityFreeze(jqAct2, sct); // frezze 2
							jqAct2.addClass(cStateFrontActivity);
					})
					, NEXT_FRAME, fct(()->{  // lance les anim
							jqAct2.addClass(cStateMoveToBottom);	
					})
					, DELAY_SURETE_END_ANIMATION, fct(()->{  // lance les anim
							overlay.doHideOverlay(1);
							jqAct1.removeClass(cStateZoom09);  // revient en zoom 1

					})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct(()->{
							overlay.doHideOverlay(2);
							self.doActivityNoDisplay(jqAct2);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doFixedElemToFixe(jqAct1);
	
							jqAct1.removeClass(cTransitionSpeed);
							jqAct2.removeClass(cStateMoveToBottom);
							jqAct2.removeClass(cStateFrontActivity);
							jqAct1.removeClass(cStateBackActivity);
	
							self.doActivityDeFreeze(jqAct1); // defrezze 1
							self.doInitScrollTo(jqAct1);
					})
					, NEXT_FRAME, funct()
			))
		.endif();

		return _void();
	}


	default JSVoid doOpenActivityFromRipple() {

		JSInt MEM_SCROLL = cast(JSInt.class,"-1");
		JSInt SCROLL_TOP = cast(JSInt.class,"0");
		
		JSOverlayOld overlay = let(JSOverlayOld.class, "overlay", NULL);
		_set(overlay, _new(SPEED_SHOW_ACTIVITY, 0.6));

		JSTransition self = let(JSTransition.class, "self", "this");
		JSAny document = declareType(JSAny.class, "document");
		
		JSInt sct = let(JSInt.class, "sct", $(document).scrollTop());
		
		JSString act1 = let(JSString.class, "act1", "'#'+$xui.intent.prevActivity");
		JSString act2 = let(JSString.class, "act2", "'#'+$xui.intent.activity");
		JQuery jqAct1 = let(JQuery.class, "jqAct1", $(act1));
		JQuery jqAct2 = let(JQuery.class, "jqAct2", $(act2));

		_if(jqAct1.hasClass(active))
		// ouverture activity 2
		.__(TKQueue.startAnimQueued(
				fct(()->{
						overlay.doShowOverlay(jqAct1, 1);   // met en gris
						self.doFixedElemToAbsolute(jqAct1, SCROLL_TOP);
						self.doActivityFreeze(jqAct1, sct); // frezze 1
						jqAct1.addClass(cStateBackActivity);
						
						self.doFixedElemToAbsolute(jqAct2, SCROLL_TOP);   // fixe pour ripple
				})
				, NEXT_FRAME , fct(()->{ // prepare animation de ripple	  
						self.doActivityActive(jqAct2);
						// prepare anim
						jqAct2.addClass(cStateFrontActivity);
						jqAct2.addClass(circleAnim0prt);
						jqAct2.addClass(cStateZoom12);

						self.doActivityFreeze(jqAct2, MEM_SCROLL); // frezze 2
				})
				, NEXT_FRAME, fct(()->{ // lance animation dezoom  
						overlay.doShowOverlay(jqAct1, 2);
						jqAct1.addClass(cTransitionSpeed);
						jqAct1.addClass(cStateZoom09);     // reduit la act 1 
						self.doActivityInactive(jqAct1);
				})
				, DELAY_SURETE_END_ANIMATION , fct(()->{ // lance animation ripple   
						jqAct2.removeClass(circleAnim0prt);
						jqAct2.addClass(cTransitionSpeedEffect); // cercle effect
						jqAct2.addClass(circleAnim100prt);
					})
				,  SPEED_ACTIVITY_TRANSITION_EFFECT - DELAY_SURETE_END_ANIMATION  , fct(()->{  // lance animation dezoom plus tard
						jqAct2.removeClass(circleAnim100prt);
					
						jqAct2.addClass(cTransitionSpeed);
						jqAct2.removeClass(cTransitionSpeedEffect);
						jqAct2.removeClass(cStateZoom12);
						jqAct2.addClass(cStateZoom1);
						})
				, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct(()->{

						self.doFixedElemToFixe(jqAct2);
						self.doActivityNoDisplay(jqAct1);
						self.doActivityDeFreeze(jqAct2); // defrezze 2

						jqAct1.removeClass(cTransitionSpeed);
						
						jqAct2.removeClass(cTransitionSpeed);
						jqAct2.removeClass(cStateFrontActivity);
						jqAct2.removeClass(cStateZoom1);

						// annule l'animation
					//	jqAct2.removeClass(circleAnim100prt);
						jqAct2.css(TRANSFORM, "");

						self.doInitScrollTo(jqAct2);
					})
					, NEXT_FRAME, funct()
					)
				
				)
		._else()
		// fermeture activity 2
		.__(TKQueue.startAnimQueued(fct(()->{
					self.doFixedElemToAbsolute(jqAct2, SCROLL_TOP);
					self.doActivityInactive(jqAct2);
					self.doActivityActive(jqAct1);
					self.doActivityFreeze(jqAct2, sct);

					jqAct2.addClass(circleAnim100prt);
					jqAct2.addClass(cStateFrontActivity);
				})
				, NEXT_FRAME, fct(()->{
					jqAct2.addClass(cTransitionSpeed);
					jqAct2.addClass(cStateZoom12); // lance le zoome
				})
				//, NEXT_FRAME, fct(()->{ // puis lance la circle          marche plus  chrome v67
				, BUG_NEXT_FRAME_CHROME_V67, fct(()->{ // puis lance la circle     				marche chrome v67
					jqAct2.addClass(cTransitionSpeedEffect);
					jqAct2.removeClass(circleAnim100prt);
					jqAct2.addClass(circleAnim0prt);
				})
				, SPEED_ACTIVITY_TRANSITION_EFFECT / 2, fct(()->{ // lance animation activity 1
				//, 1000, fct(()->{ // lance animation activity 1
					jqAct1.removeClass(cStateZoom09);
					jqAct1.addClass(cTransitionSpeed);
					overlay.doHideOverlay(1);
				})
				, Math.max(SPEED_SHOW_ACTIVITY, SPEED_ACTIVITY_TRANSITION_EFFECT) + DELAY_SURETE_END_ANIMATION,
					fct(()->{ 
					overlay.doHideOverlay(2);
					self.doActivityNoDisplay(jqAct2);
					self.doFixedElemToFixe(jqAct1);
					self.doActivityDeFreeze(jqAct1); // defrezze 1

					jqAct1.removeClass(cTransitionSpeed);

					self.doActivityDeFreeze(jqAct2); // defrezze 2

					jqAct2.removeClass(cTransitionSpeed);  // TODO gestion remove multiple class
					jqAct2.removeClass(cTransitionSpeedEffect);
					jqAct2.removeClass(circleAnim0prt);
					jqAct2.removeClass(cStateZoom12);
					jqAct2.removeClass(cStateFrontActivity);
					
					jqAct1.removeClass(cStateBackActivity);

					self.doInitScrollTo(jqAct1);
				})
				, NEXT_FRAME, funct()
				))
		.endif();

		return _void();
	}
}
