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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
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
 * - gerer le menu burger comme une activity - gerer sur tous le animation les classes 
 * - transitionSpeedx1 transitionSpeedx2 - class StateOpen , StateClose, StateXXX   (voir ViewOverlayRipple)
 * - empecher ou differer le button back durant l'animation sinon pb execution fin d'animation
 *
 */

public interface TKTransition extends JSClass {

	JSOverlay _overlay = null;

	TKTransition _this = null;
	TKTransition _self = null;

	default JSVoid doNavBarToAbsolute(JSInt sct) {
		
		JQuery $NavBar = let(JQuery.class, "$NavBar", $(activity.and(active).directChildren(navbar)));

		JSInt posTop = let(JSInt.class, "posTop", sct);
	//	posTop.set(posTop.add($NavBar.get(0), ".getBoundingClientRect().y"));
		// retirer la transform translate3d
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
		posTop.set(posTop.add($tabbar.get(0), ".getBoundingClientRect().y"));
		// retirer la transform translate3d
		$tabbar.css("top", txt(posTop,"px"));
		$tabbar.css("position","absolute"); // permet la nav de bouger

		return _void();
	}

	default JSVoid doTabBarToFixe() {
		
		JQuery $activity = let(JQuery.class, "$activity", $(activity.and(active)));
		JQuery $tabbar = let(JQuery.class, "$tabbar", $activity.find("footer"));

		$tabbar.css("top", "");
		$tabbar.css("position","");

		return _void();
	}
	


	default Object doToggleBurgerMenu() {
		//JSOverlay overlay = let( JSOverlay.class, "overlay", _new(CssTransition.SPEED_SHOW_MENU, XUIScene.OVERLAY_OPACITY_MENU) );
		//JSOverlay 
		var(_overlay, _new(SPEED_SHOW_MENU, XUIScene.OVERLAY_OPACITY_MENU));
		
		JQuery jqMenu = let( JQuery.class, "jqMenu", $(ViewMenu.menu) );
		JQuery jqScene = let( JQuery.class, "jqScene", $(scene) );
	
		JQuery jqActivityActive = let( JQuery.class, "jqActivityActive", $(activity.and(active)) );
		JQuery jqNavBar = let( JQuery.class, "jqNavBar", $(activity.and(active).directChildren(navbar)) );

		JSInt sct = let( JSInt.class, "sct",    $(jsvar("document")).scrollTop() );

		var(_self, _this);

		// ferme le menu
		_if(jqNavBar.hasClass(fixedToAbsolute));
			JQuery jqHamburgerDetach = let( JQuery.class, "jqHamburgerDetach", $(scene.__(ViewBtnBurger.hamburger.and(detach))) );
			//.var("jqHamburgerDetach", "$('.scene .hamburger.detach')")
			__(TKQueue.startAnimQueued(	fct().__(()->{
								__(_overlay.doHide(1));
								// -------------------------- repositionne l'activity --------------------
								__(jqActivityActive.removeClass("activityMoveForShowMenu"));
								__(jqActivityActive.addClass("activityMoveForHideMenu"));
								// ----------------------------- cache le menu ------------------------
								__(jqMenu.css("transform", txt("translate3d(-" + (XUIScene.widthMenu + 5)	+ "px," , sct ,  "px, 0px)")));
								// ----------------------------- repasse en croix ----------------------
								__(jqHamburgerDetach.css("transition", "transform " + SPEED_SHOW_MENU	+ "ms linear"));
								__(jqHamburgerDetach.css("transform", txt("translate3d(0px,",sct, "px,0px) scale(1)" )));
								})
						, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, fct().__(()->{
								__(_overlay.doHide(2));
								__("$('body').css('overflow','')"); // remet de scroll

								// ----------- fige la barre nav en haut (fixed) --------
								__(_self.doTabBarToFixe());
								__(_self.doNavBarToFixe());

								// anime le burger et le passe de la scene vers l'activity
								var("hamburger", "jqHamburgerDetach.detach()");
								__("hamburger.removeClass('detach')");
								__("jqNavBar.append(hamburger)");
								__("hamburger.css('transition','')");
								__("hamburger.css('transform', '' )");
								// -------------------------- fin du repositionnement l'activity
								// --------------------
								__("jqActivityActive.removeClass('activityMoveForHideMenu')");
								})
						, NEXT_FRAME, fct()
								.__("jqHamburgerDetach.removeClass('is-active')") // changeColorMenu.consoleDebug("'end
																					// anim'")
						, SPEED_BURGER_EFFECT, fct()))
			._else();
				JQuery jqHamburger = let( JQuery.class, "jqHamburger", jqNavBar.find(ViewBtnBurger.hamburger) );
				// ouvre le menu
				__(TKQueue.startAnimQueued(fct().__(()->{
							__($("body").css("overflow","hidden")); //???? plus de scroll du body sur l'ouverture du menu
							__(_overlay.doShow("'.active'", 1));
							// ---------------------------------------
							__("jqMenu.css('transition', '' )"); // fige le menu en haut sans animation
							__("jqMenu.css('transform', 'translate3d(-" + XUIScene.widthMenu + "px,'+sct+'px,0px)' )");
	
							// ----------- detache la barre nav en haut par rapport au scroll et ajoute a
							// l'activité --------
							__(_self.doNavBarToAbsolute(sct));
							// TODO a faire marche
							//__(_self.doTabBarToActivity(cast(JSInt.class, "sct")));   
							__(_self.doTabBarToAbsolute(sct));
	
							// ---------- anime le burger et le passe sur la scene---------------
							__("jqHamburger.detach()");
							__(jqHamburger.addClass(detach));
							__("jqHamburger.css('transform', 'translate3d(0px,'+sct+'px,0px)' )"); // positionne en haut
							__(jqHamburger.css("transition", "transform " + SPEED_SHOW_MENU + "ms linear") );   // prepare
																												// transition
							__(jqScene.append("jqHamburger"));
						})
						, NEXT_FRAME, fct().__(()->{
								// ------------ deplace l'activity a l ouverture du menu-------------
								__("jqActivityActive.addClass('activityMoveForShowMenu')");
								// ------------ ouvre le menu avec animation---------
								__("jqMenu.css('transition', 'transform " + SPEED_SHOW_MENU + "ms linear' )");
								__("jqMenu.css('transform', 'translate3d(0px,'+sct+'px,0px)' )");
								// -------------------------------------------------
								__(_overlay.doShow("'.active'", 2));
								// ------------ deplace le hamburger---------
								__("jqHamburger.css('transform', 'translate3d(-15px,'+(-3+sct)+'px,0px) scale(0.6)' )");

								// ------------- anim des item de menu----------
								_for("var i in window.jsonMainMenu");
									__("$(window.jsonMainMenu[i]['_dom_']).css('visibility','hidden')");
									__("setTimeout(", fct("elem")
										.__("elem.anim='fadeInLeft'")
										.__("elem.anim=''"),
										",(i*" + SPEED_SHOW_MENU_ITEMS_ANIM
												+ "), window.jsonMainMenu[i])");

								endfor();
						})
						, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, fct()
								.__("jqHamburger.addClass('is-active')") // passe en croix
						// .__("$('.active .logo').toggleClass('animated shake')")
						, SPEED_BURGER_EFFECT, fct()))
				.endif();
		return null;
	}

	default Object doActivityFreeze(Object act, Object sct) {
		__("$(act).addClass('fixedForAnimated')")

				._if("sct==-1")
				.set("sct", "$(act).data('scrolltop')")
				.set("sct", "sct==null?0:sct")
				.endif()

				.__("$(act).data('scrolltop', sct ) ")
				.var("actContent", "$(act+' .content')")
				// freeze
				.__("actContent.css('overflow', 'hidden')") // fait clignoter en ios
				.__("actContent.css('height', '100vh')")
				.__("actContent.scrollTop(sct)");
		return null;
	}

	default Object doActivityDeFreeze(Object act) {
		var("actContent", "$(act+' .content')")
				.__("actContent.css('overflow', '')")
				.__("actContent.css('height', '')")

				.__("$(act).removeClass('fixedForAnimated')");
		return null;
	}

	default Object doActivityInactive(Object act) // Object sct
	{
		__()
				.__("$(act).removeClass('active')")
				.__("$(act).addClass('inactive')");
		return null;
	}

	default Object doActivityActive(Object act) {
		__()
				.__("$(act).removeClass('nodisplay')")
				.__("$(act).addClass('active')")
				.__("$(act).removeClass('inactive')");
		return null;
	}

	default Object doActivityNoDisplay(Object act) {
		__()
				.__("$(act).removeClass('active')")
				.__("$(act).addClass('nodisplay')");
		return null;
	}

	default Object doInitScrollTo(Object act) {
		__()
				.var("scrposition", "$(act).data('scrolltop')")
				.__("$(document).scrollTop(scrposition==null?0:scrposition)");
		return null;
	}

	default Object doOpenActivityFromBottom() {

		var(_overlay, _new(SPEED_SHOW_ACTIVITY, XUIScene.OVERLAY_OPACITY_BACK))
				.var("sct", "$(document).scrollTop()")
				.var(_self, _this)
				.var("act1", "'#'+$xui.intent.prevActivity")
				.var("act2", "'#'+$xui.intent.activity")
				.var("jqAct1", "$(act1)")
				.var("jqAct2", "$(act2)")

				._if("jqAct1.hasClass('active')")
				// ouverture activity 2
				.__(TKQueue.startAnimQueued( fct() .__(()->{ 
								__(_self.doNavBarToAbsolute(new JSInt()._setContent(0)));
								__(_overlay.doShow("act1", 1)); // init
								__(_self.doActivityInactive("act1"));
								__(_self.doActivityFreeze("act1", "sct")); // freeze 1
								__(_self.doActivityActive("act2"));

								__("jqAct1.addClass('backActivity')");

								__("jqAct2.addClass('frontActivity')");
								__("jqAct2.addClass('toHidden')"); // prepare l'animation top 0 fixed
								})
						, NEXT_FRAME, fct() .__(()->{ 
								// lance les anim
								__(_overlay.doShow("act1", 2));
								__(_self.doActivityFreeze("act2", -1)); // freeze 2

								__("jqAct1.addClass('toback')");
								__("jqAct2.addClass('tofront')");
							})
						, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{ 
								__(_self.doActivityNoDisplay("act1"));

								__("jqAct2.removeClass('tofront')");
								__("jqAct2.removeClass('toHidden')");
								__("jqAct2.removeClass('frontActivity')");
								__(_self.doActivityDeFreeze("act2")); // defrezze 2
								__(_self.doNavBarToFixe());

								__(_self.doInitScrollTo("act2"));
							})
						, NEXT_FRAME, fct()
				// .consoleDebug("'end activity anim'")
				))
				._else()
				// fermeture activity 2
				.__(TKQueue.startAnimQueued(
						fct().__(()->{ 
								__(_self.doNavBarToAbsolute(new JSInt()._setContent(0)));
								__(_self.doActivityInactive("act2"));
								__(_self.doActivityActive("act1"));
								__(_self.doActivityFreeze("act2", "sct")); // frezze 2
								__("jqAct2.addClass('frontActivity')");
						})
						, NEXT_FRAME, fct().__(()->{  // lance les anim
								__(_overlay.doHide(1));

								__("jqAct1.removeClass('toback')");
								__("jqAct1.addClass('backToFront')");
								__("jqAct2.addClass('toHidden')");
						})
						, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{
								__(_overlay.doHide(2));
								__(_self.doActivityNoDisplay("act2"));
								__(_self.doActivityDeFreeze("act2")); // defrezze 2
								__(_self.doNavBarToFixe());

								__("jqAct1.removeClass('backToFront')");
								__("jqAct2.removeClass('toHidden')");
								__("jqAct2.removeClass('frontActivity')");
								__("jqAct1.removeClass('backActivity')");

								__(_self.doActivityDeFreeze("act1")); // defrezze 1
								__(_self.doInitScrollTo("act1"));
						})
						, NEXT_FRAME, fct()
				// .consoleDebug("'end activity anim'")
				))
				.endif();

		return null;
	}

	JSXHTMLPart _template = null;

	default Object doOpenActivityFromRipple() {

		var(_overlay, _new(SPEED_SHOW_ACTIVITY, 0.6))
				.var(_self, _this)
				.var("sct", "$(document).scrollTop()")
				.var("act1", "'#'+$xui.intent.prevActivity")
				.var("act2", "'#'+$xui.intent.activity")
				.var("jqAct1", "$(act1)")
				.var("jqAct2", "$(act2)")

				._if("jqAct1.hasClass('active')")
				// ouverture activity 2
				.__(TKQueue.startAnimQueued(
						fct().__(()->{
								__(_overlay.doShow("act1", 1));
								__(_self.doNavBarToAbsolute(new JSInt()._setContent(0)));
								__(_self.doActivityFreeze("act1", "sct")); // frezze 1
								__("jqAct1.addClass('backActivity')");

								var(_template, ViewOverlayRipple.xTemplate());
								var("rippleOverlay", _template.appendInto("$(act2)"));
						// .__("$('.scene .ripple_overlay').addClass('t0prct')")
						// .__("$('.scene .ripple_overlay').addClass('transitionx2')")
						// .__("$('.ripple_overlay').addClass('t100prct')")
						})
						, NEXT_FRAME, fct().__(()->{ // lance animation
								__(_overlay.doShow("act1", 2));

								__("jqAct1.addClass('toback')");
								// .__("$('.scene .ripple_overlay').removeClass('t0prct')")
								// .__("$('.scene .ripple_overlay').addClass('t100prct')")

								// , ScnStandard.SPEED_ACTIVITY_TRANSITION_EFFECT-50, fct() // 50 l'anim de la
								// bulle peut etre arreter avant la fin
								__(_self.doActivityInactive("act1"));
								__(_self.doActivityActive("act2"));

								// prepare anim
								__("jqAct2.addClass('frontActivity')");
								__("jqAct2.addClass('circleAnim0prt')");
								__("jqAct2.addClass('zoom12')");

								__(_self.doActivityFreeze("act2", -1)); // frezze 2
						})
						, NEXT_FRAME, fct() // lance animation
								.__("jqAct2.removeClass('circleAnim0prt')")

								.__("jqAct2.addClass('transitionSpeedx2')") // cercle effect
								.__("jqAct2.addClass('circleAnim100prt')")
								.__("$('.ripple_overlay').addClass('transition')")
								.__("$('.ripple_overlay').css('opacity', '0')")

						, SPEED_ACTIVITY_TRANSITION_EFFECT, fct() // lance animation dezoom plus tard
								.__("jqAct2.removeClass('circleAnim100prt')")
								.__("jqAct2.addClass('transitionSpeed')")
								.__("jqAct2.removeClass('transitionSpeedx2')")
								.__("jqAct2.removeClass('zoom12')")
								.__("jqAct2.addClass('zoom10')")

						, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct().__(()->{

								__(_self.doNavBarToFixe());
								__(_self.doActivityNoDisplay("act1"));
								__(_self.doActivityDeFreeze("act2")); // defrezze 2

								__("$('.ripple_overlay').remove()");
								__("jqAct2.removeClass('transitionSpeed')");
								__("jqAct2.removeClass('transitionSpeedx2')");
								__("jqAct2.removeClass('frontActivity')");
								__("jqAct2.removeClass('zoom10')");

								// annule l'animation
								__("jqAct2.css('transform', '')");

								__(_self.doInitScrollTo("act2"));
								consoleDebug("'end activity anim'");
							})
							)
						
						)
				._else()
				// fermeture activity 2
				.__(TKQueue.startAnimQueued(fct() .__(()->{
						__(_self.doNavBarToAbsolute(new JSInt()._setContent(0)));
						__(_self.doActivityInactive("act2"));
						__(_self.doActivityActive("act1"));
						__(_self.doActivityFreeze("act2", "sct")); // frezze 2

						// // recherche ripple
						// ._if(true)
						// .__("$('.scene .ripple_overlay').removeClass('t0prct')")
						// .__("$('.scene .ripple_overlay').addClass('t100prct')")
						// .endif()
						//
						// .__("$('.scene .ripple_overlay').css('display', '')") // display en grand

						__("jqAct2.addClass('circleAnim100prt')");
						__("jqAct2.addClass('frontActivity')");
						})
						, NEXT_FRAME, fct()
								.__("jqAct2.addClass('transitionSpeed')")
								.__("jqAct2.addClass('zoom12')") // lance le zoome

						, NEXT_FRAME, fct() // puis lance la circle
								.__("jqAct2.addClass('transitionSpeed')")
								.__("jqAct2.addClass('circleAnim0prt')")
								.__("jqAct2.removeClass('circleAnim100prt')")

						// ,(ScnStandard.SPEED_ACTIVITY_TRANSITION_EFFECT+100), fct()
						// .__("$('.scene .ripple_overlay').removeClass('transitionx2')")
						// .__("$('.scene .ripple_overlay').addClass('transition')")
						// .__("$('.scene .ripple_overlay').addClass('t0prct')")
						// .__("$('.scene .ripple_overlay').removeClass('t100prct')")

						// , 1000000, fct() //puis lance la circle

						, SPEED_ACTIVITY_TRANSITION_EFFECT / 2, fct().__(()->{ // lance animation activity 1
								__("jqAct1.removeClass('toback')");
								__("jqAct1.addClass('backToFront')");
								__(_overlay.doHide(1));
						})
						, Math.max(SPEED_SHOW_ACTIVITY, SPEED_ACTIVITY_TRANSITION_EFFECT) + DELAY_SURETE_END_ANIMATION,
						fct().__(()->{ 
								__(_overlay.doHide(2));
								__(_self.doActivityNoDisplay("act2"));
								__(_self.doNavBarToFixe());
								__(_self.doActivityDeFreeze("act1")); // defrezze 1

								__("jqAct1.removeClass('backToFront')");
								// .__("$('.ripple_overlay').remove()") //TODO a changer

								__(_self.doActivityDeFreeze("act2")); // defrezze 2

								__("jqAct2.removeClass('transitionSpeed')");
								__("jqAct2.removeClass('transitionSpeedx2')");
								__("jqAct2.removeClass('circleAnim0prt')");
								__("jqAct2.removeClass('zoom12')");
								__("jqAct2.removeClass('frontActivity')");
								__("jqAct1.removeClass('backActivity')");

								__(_self.doInitScrollTo("act1"));
						})
						, NEXT_FRAME, fct()
								.consoleDebug("'end activity anim'")))
				.endif();

		return null;
	}
}
