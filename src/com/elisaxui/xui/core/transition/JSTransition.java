/**
 * 
 */
package com.elisaxui.xui.core.transition;

import static com.elisaxui.xui.core.toolkit.JQuery.$;
import static com.elisaxui.xui.core.transition.ConstTransition.*;
import static com.elisaxui.xui.core.transition.CssTransition.*;
import static com.elisaxui.xui.core.page.XUIScene.*;
import static com.elisaxui.xui.core.widget.navbar.ViewNavBar.fixedToAbsolute;
import static com.elisaxui.xui.core.widget.navbar.ViewNavBar.navbar;
import static com.elisaxui.xui.core.widget.tabbar.ViewTabBar.cTabbar;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
import com.elisaxui.xui.core.widget.layout.ViewPageLayout;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;
import com.elisaxui.xui.core.widget.overlay.ViewOverlayRipple;

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

	public static final String DATA_SCROLLTOP = "scrolltop";


	default JSVoid doNavBarToAbsolute(JSInt sct) {
		
		JQuery $NavBar = let(JQuery.class, "$NavBar", $(activity.and(active).directChildren(navbar)));

		JSInt posTop = let(JSInt.class, "posTop", sct);
		$NavBar.css("top", txt(posTop,"px"));
		$NavBar.css("position","absolute"); // permet la nav de bouger
		$NavBar.addClass(fixedToAbsolute);
		return _void();
	}
	
	default JSVoid doNavBarToFixe() {
		JQuery $NavBar = let(JQuery.class, "$NavBar", $(activity.and(active).directChildren(navbar)));
		$NavBar.removeClass(fixedToAbsolute);
		$NavBar.css("top", "");
		$NavBar.css("position","");
		return _void();
	}
	
	default JSVoid doTabBarToAbsolute(JSInt sct) {
		
		JQuery $tabbar = let(JQuery.class, "$tabbar", $(activity.and(active).directChildren(cTabbar)));

		JSInt posTop = let(JSInt.class, "posTop", sct);
		posTop.set(posTop.add($tabbar.get(0), ".getBoundingClientRect().y"));   //TODO
		$tabbar.css("top", txt(posTop,"px"));
		$tabbar.css("position","absolute");
		return _void();
	}

	default JSVoid doTabBarToFixe() {
		
		JQuery $activity = let(JQuery.class, "$activity", $(activity.and(active)));
		JQuery $tabbar = let(JQuery.class, "$tabbar", $activity.find("footer"));

		$tabbar.css("top", "");
		$tabbar.css("position","");
		return _void();
	}
	


	default JSVoid doToggleBurgerMenu() {
		
		JSOverlay overlay = let( JSOverlay.class, "overlay2", "null" /*, _new(SPEED_SHOW_MENU, OVERLAY_OPACITY_MENU)*/ );
		set(overlay, _new(SPEED_SHOW_MENU, XUIScene.OVERLAY_OPACITY_MENU));  /* TODO a faire marcher*/
		
		JQuery jqMenu = let( JQuery.class, "jqMenu", $(ViewMenu.menu) );
		JQuery jqScene = let( JQuery.class, "jqScene", $(scene) );
	
		JQuery jqActivityActive = let( JQuery.class, "jqActivityActive", $(activity.and(active)) );
		JQuery jqNavBar = let( JQuery.class, "jqNavBar", $(activity.and(active).directChildren(navbar)) );

		JSInt sct = let( JSInt.class, "sct",    $(jsvar("document")).scrollTop() );

		JSTransition self = let(JSTransition.class, "self", "this");

		_if(jqNavBar.hasClass(fixedToAbsolute));
		
			/*************************************************/
			// ferme le menu
			/*************************************************/
			JQuery jqHamburgerDetach = let( JQuery.class, "jqHamburgerDetach", $(scene.descendant(ViewBtnBurger.hamburger.and(detach))) );
			__(TKQueue.startAnimQueued(	 callback(()->{  
							overlay.doHide(1);
							// -------------------------- repositionne l'activity --------------------
							jqActivityActive.removeClass(activityMoveForShowMenu);
							jqActivityActive.addClass(activityMoveForHideMenu);
							// ----------------------------- cache le menu ------------------------
							jqMenu.css("transform", txt("translate3d(-" + (XUIScene.widthMenu + 5)	+ "px," , sct ,  "px, 0px)"));
							// ----------------------------- repasse en croix ----------------------
							jqHamburgerDetach.css("transition", "transform " + SPEED_SHOW_MENU	+ "ms linear");
							jqHamburgerDetach.css("transform", txt("translate3d(0px,", sct, "px,0px) scale(1)" ));
							})
					, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, callback(()->{
							overlay.doHide(2);
							$("body").css("overflow",""); // remet de scroll

							// ----------- fige la barre nav en haut (fixed) --------
							self.doTabBarToFixe(); 
							self.doNavBarToFixe();
							// anime le burger et le passe de la scene vers l'activity
							JQuery hamburger = let(JQuery.class, "hamburger", jqHamburgerDetach.detach() ) ;
							hamburger.removeClass(detach);
							jqNavBar.append(hamburger);
							hamburger.css("transition", "");
							hamburger.css("transform", "");
							
							// -------------------------- fin du repositionnement l'activity
							jqActivityActive.removeClass(activityMoveForHideMenu);
							})
					, NEXT_FRAME,  callback(()->{
							jqHamburgerDetach.removeClass("is-active"); 
					})
					, SPEED_BURGER_EFFECT, fct()
				))
			._else();
				JQuery jqHamburger = let( JQuery.class, "jqHamburger", jqNavBar.find(ViewBtnBurger.hamburger) );
				/*************************************************/
				// ouvre le menu
				/*************************************************/
				__(TKQueue.startAnimQueued(callback("param", ()->{
							$("body").css("overflow","hidden"); //???? plus de scroll du body sur l'ouverture du menu
							overlay.doShow(jqActivityActive, 1);
							// ---------------------------------------
							jqMenu.css("transition", ""); // fige le menu en haut sans animation
							jqMenu.css("transform", txt("translate3d(-" + XUIScene.widthMenu + "px," ,sct, "px,0px)" ));
	
							// ----------- detache la barre nav en haut par rapport au scroll et ajoute a l'activité --------
							self.doNavBarToAbsolute(sct);
							self.doTabBarToAbsolute(sct);
	
							// ---------- anime le burger et le passe sur la scene---------------
							jqHamburger.detach();
							jqHamburger.addClass(detach);
							jqHamburger.css("transform", txt("translate3d(0px,",sct,"px,0px)")); // positionne en haut
							jqHamburger.css("transition", "transform " + SPEED_SHOW_MENU + "ms linear");   // prepare
																												// transition
							jqScene.append(jqHamburger);
						})
						, NEXT_FRAME, callback(()->{
								// ------------ deplace l'activity a l ouverture du menu-------------
								jqActivityActive.addClass(activityMoveForShowMenu);
								// ------------ ouvre le menu avec animation---------
								jqMenu.css("transition", "transform " + SPEED_SHOW_MENU + "ms linear");
								jqMenu.css("transform", txt("translate3d(0px,",sct,"px,0px"));
								// -------------------------------------------------
								overlay.doShow(jqActivityActive, 2);
								// ------------ deplace le hamburger---------
								jqHamburger.css("transform", txt("translate3d(-15px,",calc("(",sct,"-3)"),"px,0px) scale(0.6)"));
								// ------------- anim des item de menu---------
								_for("var i in window.jsonMainMenu");
									JSon jsonMenu = let(JSon.class, "jsonMenu", "window.jsonMainMenu[i]");
									$( jsonMenu.attrByString("_dom_") ).css("visibility","hidden");
									
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
		act.addClass(fixedForAnimated);

		_if(sct.isEqual(-1));
				sct.set(act.data(DATA_SCROLLTOP));
				sct.set(calc(sct,"==null?0:",sct));  // met à 0 si null
		endif();

		act.data(DATA_SCROLLTOP, sct );   // sauvegarde scroll position
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.content));
		// freeze
		actContent.css("overflow", "hidden"); // fait clignoter en ios
		actContent.css("height", "100vh");
		actContent.scrollTop(sct);
		return _void();
	}

	default JSVoid doActivityDeFreeze(JQuery act) {
		JQuery actContent = let(JQuery.class, "actContent", act.find(ViewPageLayout.content));
		actContent.css("overflow", "");
		actContent.css("height", "");
		$(act).removeClass(fixedForAnimated);
		return _void();
	}

	default JSVoid doActivityInactive(JQuery act) // Object sct
	{
		act.removeClass(active);
		act.addClass(inactive);
		return _void();
	}

	default JSVoid doActivityActive(JQuery act) {
		act.removeClass(nodisplay);
		act.addClass(active);
		act.removeClass(inactive);
		return _void();
	}

	default JSVoid doActivityNoDisplay(JQuery act) {
		act.removeClass(active);
		act.addClass(nodisplay);
		return _void();
	}

	default JSVoid doInitScrollTo(JQuery act) {
		JSInt scrposition = let(JSInt.class, "scrposition", act.data(DATA_SCROLLTOP));
		$(jsvar("document")).scrollTop(calc(scrposition,"==null?0:",scrposition));
		return _void();
	}

	default JSVoid doOpenActivityFromBottom() {
		JSOverlay overlay = let(JSOverlay.class, "overlay", "null");
		set(overlay, _new(SPEED_SHOW_ACTIVITY, XUIScene.OVERLAY_OPACITY_BACK));
		
		JSInt sct = let(JSInt.class, "sct", $(jsvar("document")).scrollTop());
		JSString act1 = let(JSString.class, "act1", "'#'+$xui.intent.prevActivity");
		JSString act2 = let(JSString.class, "act2", "'#'+$xui.intent.activity");
		JQuery jqAct1 = let(JQuery.class, "jqAct1", $(act1));
		JQuery jqAct2 = let(JQuery.class, "jqAct2", $(act2));

		JSInt MEM_SCROLL = cast(JSInt.class,"-1");
		JSInt ZERO = cast(JSInt.class,"0");
		JSTransition self = let(JSTransition.class, "self", "this");
		
		_if(jqAct1.hasClass(active))
			// ouverture activity 2
			.__(TKQueue.startAnimQueued( fct() .__(()->{ 
							self.doNavBarToAbsolute(ZERO);
							overlay.doShow(jqAct1, 1); // init
							self.doActivityInactive(jqAct1);
							self.doActivityFreeze(jqAct1, sct); // freeze 1
							self.doActivityActive(jqAct2);
	
							jqAct1.addClass(backActivity);
	
							jqAct2.addClass(frontActivity);
							jqAct2.addClass(toBottom); // prepare l'animation top 0 fixed
							})
					, NEXT_FRAME, fct() .__(()->{ 
							// lance les anim
							overlay.doShow(jqAct1, 2);
							self.doActivityFreeze(jqAct2, MEM_SCROLL); // freeze 2
	
							jqAct1.addClass(transitionSpeed);
							jqAct1.addClass(zoom09);
							jqAct2.addClass(tofront);
						})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{ 
							self.doActivityNoDisplay(jqAct1);
	
							jqAct2.removeClass(tofront);
							jqAct2.removeClass(toBottom);
							jqAct2.removeClass(frontActivity);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doNavBarToFixe();
	
							self.doInitScrollTo(jqAct2);
						})
					, NEXT_FRAME, fct()
			))
		._else()
		// fermeture activity 2
			.__(TKQueue.startAnimQueued(
					fct().__(()->{ 
							self.doNavBarToAbsolute(ZERO);
							self.doActivityInactive(jqAct2);
							self.doActivityActive(jqAct1);
							self.doActivityFreeze(jqAct2, sct); // frezze 2
							jqAct2.addClass(frontActivity);
					})
					, NEXT_FRAME, fct().__(()->{  // lance les anim
							overlay.doHide(1);
	
						//	jqAct1.removeClass(transitionSpeed);
							jqAct1.removeClass(zoom09);
							jqAct1.addClass(transitionSpeed);
							jqAct2.addClass(toBottom);
					})
					, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{
							overlay.doHide(2);
							self.doActivityNoDisplay(jqAct2);
							self.doActivityDeFreeze(jqAct2); // defrezze 2
							self.doNavBarToFixe();
	
							jqAct1.removeClass(transitionSpeed);
							jqAct2.removeClass(toBottom);
							jqAct2.removeClass(frontActivity);
							jqAct1.removeClass(backActivity);
	
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
		JSInt ZERO = cast(JSInt.class,"0");
		
		JSOverlay overlay = let(JSOverlay.class, "overlay", "null");
		set(overlay, _new(SPEED_SHOW_ACTIVITY, 0.6));

		JSTransition self = let(JSTransition.class, "self", "this");

		JSInt sct = let(JSInt.class, "sct", $(jsvar("document")).scrollTop());
		
		JSString act1 = let(JSString.class, "act1", "'#'+$xui.intent.prevActivity");
		JSString act2 = let(JSString.class, "act2", "'#'+$xui.intent.activity");
		JQuery jqAct1 = let(JQuery.class, "jqAct1", $(act1));
		JQuery jqAct2 = let(JQuery.class, "jqAct2", $(act2));

		_if(jqAct1.hasClass("active"))
		// ouverture activity 2
		.__(TKQueue.startAnimQueued(
				fct().__(()->{
						overlay.doShow(jqAct1, 1);
						self.doNavBarToAbsolute(ZERO);
						self.doActivityFreeze(jqAct1, sct); // frezze 1
						jqAct1.addClass(backActivity);
						// ajoute le template du ripple overlay
						JSXHTMLPart template = let(JSXHTMLPart.class, "template", ViewOverlayRipple.xTemplate());
						template.appendInto($(act2));
				})
				, NEXT_FRAME, fct().__(()->{ // lance animation
						overlay.doShow(jqAct1, 2);
						jqAct1.addClass(transitionSpeed);
						jqAct1.addClass(zoom09);

						// , ScnStandard.SPEED_ACTIVITY_TRANSITION_EFFECT-50, fct() // 50 l'anim de la
						// bulle peut etre arreter avant la fin
						self.doActivityInactive(jqAct1);
						self.doActivityActive(jqAct2);

						// prepare anim
						jqAct2.addClass(frontActivity);
						jqAct2.addClass(circleAnim0prt);
						jqAct2.addClass(zoom12);

						self.doActivityFreeze(jqAct2, MEM_SCROLL); // frezze 2
				})
				, NEXT_FRAME, fct().__(()->{ // lance animation
						jqAct2.removeClass(circleAnim0prt);

						jqAct2.addClass(transitionSpeedx2); // cercle effect
						jqAct2.addClass(circleAnim100prt);
						$(ViewOverlayRipple.ripple_overlay).addClass(ViewOverlayRipple.transition);
						$(ViewOverlayRipple.ripple_overlay).css("opacity", 0);
					})
				, SPEED_ACTIVITY_TRANSITION_EFFECT, fct().__(()->{  // lance animation dezoom plus tard
						jqAct2.removeClass(circleAnim100prt);
						jqAct2.addClass(transitionSpeed);
						jqAct2.removeClass(transitionSpeedx2);
						jqAct2.removeClass(zoom12);
						jqAct2.addClass(zoom10);
						})
				, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{

						self.doNavBarToFixe();
						self.doActivityNoDisplay(jqAct1);
						self.doActivityDeFreeze(jqAct2); // defrezze 2

						jqAct1.removeClass(transitionSpeed);
						
						$(ViewOverlayRipple.ripple_overlay).remove();
						jqAct2.removeClass(transitionSpeed);
						jqAct2.removeClass(frontActivity);
						jqAct2.removeClass(zoom10);

						// annule l'animation
						jqAct2.css("transform", "");

						self.doInitScrollTo(jqAct2);
					})
					, NEXT_FRAME, fct()
					)
				
				)
		._else()
		// fermeture activity 2
		.__(TKQueue.startAnimQueued(fct() .__(()->{
					self.doNavBarToAbsolute(ZERO);
					self.doActivityInactive(jqAct2);
					self.doActivityActive(jqAct1);
					self.doActivityFreeze(jqAct2, sct); // frezze 2

					jqAct2.addClass(circleAnim100prt);
					jqAct2.addClass(frontActivity);
				})
				, NEXT_FRAME, fct().__(()->{
					jqAct2.addClass(transitionSpeed);
					jqAct2.addClass(zoom12); // lance le zoome
				})
				, NEXT_FRAME, fct().__(()->{ // puis lance la circle
					jqAct2.addClass(circleAnim0prt);
					jqAct2.removeClass(circleAnim100prt);
				})
				, SPEED_ACTIVITY_TRANSITION_EFFECT / 2, fct().__(()->{ // lance animation activity 1
					jqAct1.removeClass(zoom09);
					jqAct1.addClass(transitionSpeed);
					overlay.doHide(1);
				})
				, Math.max(SPEED_SHOW_ACTIVITY, SPEED_ACTIVITY_TRANSITION_EFFECT) + DELAY_SURETE_END_ANIMATION,
					fct().__(()->{ 
					overlay.doHide(2);
					self.doActivityNoDisplay(jqAct2);
					self.doNavBarToFixe();
					self.doActivityDeFreeze(jqAct1); // defrezze 1

					jqAct1.removeClass(transitionSpeed);

					self.doActivityDeFreeze(jqAct2); // defrezze 2

					jqAct2.removeClass(transitionSpeed);  // TODO gestion remove multiple class
					jqAct2.removeClass(transitionSpeedx2);
					jqAct2.removeClass(circleAnim0prt);
					jqAct2.removeClass(zoom12);
					jqAct2.removeClass(frontActivity);
					
					jqAct1.removeClass(backActivity);

					self.doInitScrollTo(jqAct1);
				})
				, NEXT_FRAME, fct()
				))
		.endif();

		return _void();
	}
}
