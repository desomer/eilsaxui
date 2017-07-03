/**
 * 
 */
package com.elisaxui.xui.core.transition;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;
import com.elisaxui.xui.core.widget.overlay.ViewOverlayRipple;
import static  com.elisaxui.xui.core.transition.CssTransition.*;
import static  com.elisaxui.xui.core.toolkit.JQuery.*;
import static  com.elisaxui.xui.core.widget.navbar.ViewNavBar.*;
/**
 * @author Bureau
 *
 */

/** TODO 
 * 
 
 - creer un layout intercalaire entre activity   (ex ripple anim)
 - creer un layout intercalaire au dessus des activity  ( ex  zoom image anim, overlay layout)
 - gerer status de l'intention (route push or route pop    meme page ou autre page SEO)
 - ne pas reutiliser une activité deja dans l historique (ou remettre dans l'etat animation du status de l'intention)
 - gerer le menu burger comme une activity
 - gerer sur tous le animation les classes
 		- transitionSpeedx1 transitionSpeedx2
 		- class StateOpen , StateClose, StateXXX  
 - empecher ou differer le button back durant l'animation sinon pb execution fin d'animation 	
 *
 */

public interface TKTransition extends JSClass {

	JSOverlay _overlay = null;
	TKTransition _this = null;
	TKTransition _self = null;
	JQuery _jqNavBar = null;
	JQuery _jqActivityActive = null;
	
	default Object doNavBarToActivity(Object sct) {
		var(_jqNavBar, $(CSSSelector.onPath("body>.navbar")))
		//.var("jqNavBar", "$('body>.navbar')")
		.var(_jqActivityActive, $(activity.and(active)))
		//.var("jqActivityActive", "$('.activity.active')")
		//-----------  detache la barre nav en haut par rapport au scroll et ajoute a l'activité  --------
		.__("jqNavBar.detach()")
		.__("jqNavBar.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")		
		.__(_jqNavBar.addClass(fixedToAbsolute)) // permet la nav de bouger
		.__("jqActivityActive.prepend(jqNavBar)") 
		;
		return null;
	}

	default Object doNavBarToBody() {
		var("jqNavBar", "$('.activity.active>.navbar')")
		.__("jqNavBar.detach()")
		.__("jqNavBar.css('transform', 'translate3d(0px,0px,0px)' )")
		.__("jqNavBar.removeClass('fixedToAbsolute')")
		.__("$('body').prepend(jqNavBar)")
	;
	
	return null;
	}
	
	default Object doToggleBurgerMenu() {
		var(_overlay, _new(CssTransition.SPEED_SHOW_MENU, ScnStandard.OVERLAY_OPACITY_MENU))
		.var("jqMenu", "$('.menu')")
		.var("jqScene", "$('.scene')")
		.var("jqNavBar", "$('body>.navbar')")
		._if("jqNavBar.length==0")
			.set("jqNavBar", "$('.activity.active>.navbar')")
		.endif()
		.var("jqActivityActive", "$('.activity.active')")
		.var("sct", "$('body').scrollTop()")
		
	 	.var(_self, _this)
		
		// ferme le menu
		._if("jqNavBar.hasClass('fixedToAbsolute')")
			.var("jqHamburgerDetach", "$('.scene .hamburger.detach')")
			.__(TKQueue.start(
				fct()
//						.__("$('.active .logo').toggleClass('animated shake')")  // retire le shake
				    	.__(_overlay.doHide(1))    	
				    	//-------------------------- repositionne l'activity --------------------
				    	.__("jqActivityActive.removeClass('activityMoveForShowMenu')")
						.__("jqActivityActive.addClass('activityMoveForHideMenu')")
						//----------------------------- cache le menu ------------------------
						.__("jqMenu.css('transform', 'translate3d(-"+ (ScnStandard.widthMenu + 5)+ "px,'+sct+'px,0px)' )")
						//----------------------------- repasse en croix ----------------------
						.__("jqHamburgerDetach.css('transition','transform "+CssTransition.SPEED_SHOW_MENU+"ms linear')")
						.__("jqHamburgerDetach.css('transform', 'translate3d(0px,'+sct+'px,0px) scale(1)' )")
						
				, SPEED_SHOW_MENU + DELAY_SURETE_END_ANIMATION, fct()
						.__(_overlay.doHide(2))		
						.__("$('body').css('overflow','')") // remet de scroll
						
						//-----------  fige la barre nav en haut  (fixed) --------
						.__(_self.doNavBarToBody())
						
						// anime le burger et le passe de la scene vers l'activity
						.var("hamburger", "jqHamburgerDetach.detach()")
						.__("hamburger.removeClass('detach')")
						.__("jqNavBar.append(hamburger)")
						.__("hamburger.css('transition','')")
						.__("hamburger.css('transform', '' )")
						//-------------------------- fin du repositionnement l'activity --------------------
						.__("jqActivityActive.removeClass('activityMoveForHideMenu')")

				, NEXT_FRAME, fct()	
						.__("jqHamburgerDetach.removeClass('is-active')")   //changeColorMenu.consoleDebug("'end anim'")
				, SPEED_BURGER_EFFECT, fct()
				)
			)
		._else()
		.var("jqHamburger", "jqNavBar.find('.hamburger')")
		// ouvre le menu
		.__(TKQueue.start(
				fct()
						.__(_overlay.doShow("'.active'", 1))		 	        		 			
						.__("$('body').css('overflow','hidden')") // plus de scroll		
						//---------------------------------------
						.__("jqMenu.css('transition', '' )")   // fige le menu en haut sans animation
						.__("jqMenu.css('transform', 'translate3d(-"+ ScnStandard.widthMenu	+ "px,'+sct+'px,0px)' )")
						
						//-----------  detache la barre nav en haut par rapport au scroll et ajoute a l'activité  --------
						.__(_self.doNavBarToActivity("sct"))
					
				 		//---------- anime le burger et le passe sur la scene---------------
						.__("jqHamburger.detach()")	
						.__("jqHamburger.addClass('detach')")
						.__("jqHamburger.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")  // positionne en haut
						.__("jqHamburger.css('transition','transform "+SPEED_SHOW_MENU+"ms linear')")  // prepare transition
						.__("jqScene.append(jqHamburger)")
						
						
				, NEXT_FRAME, fct()	
						//------------ deplace l'activity a l ouverture du menu-------------
						.__("jqActivityActive.addClass('activityMoveForShowMenu')") 
						//------------ ouvre le menu avec animation---------
						.__("jqMenu.css('transition', 'transform "+SPEED_SHOW_MENU+"ms linear' )")
						.__("jqMenu.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")
						//-------------------------------------------------
		 				.__(_overlay.doShow("'.active'", 2))
						//------------ deplace le hamburger---------
						.__("jqHamburger.css('transform', 'translate3d(-15px,'+(-3+sct)+'px,0px) scale(0.6)' )")
				
						//------------- anim des item de menu----------	
						._for("var i in window.jsonMainMenu") 
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",(i*"+30+"), window.jsonMainMenu[i])")
						.endfor()		
						
				, SPEED_SHOW_MENU+DELAY_SURETE_END_ANIMATION, fct()
						.__("jqHamburger.addClass('is-active')")  // passe en croix
						//.__("$('.active .logo').toggleClass('animated shake')")
				, SPEED_BURGER_EFFECT, fct()		
				  )
				)
		.endif();
		return null;
	}
	
	
	default Object doActivityFreeze(Object act, Object sct)
	{
   		__("$(act).addClass('fixedForAnimated')")
   		
   		._if("sct==-1")
   			.set("sct", "$(act).data('scrolltop')") 
   			.set("sct", "sct==null?0:sct")	
   		.endif()
   		
   		.__("$(act).data('scrolltop', sct ) ") 
		.var("actContent", "$(act+' .content')")
		// freeze
		.__("actContent.css('overflow', 'hidden')")   // fait clignoter en ios
		.__("actContent.css('height', '100vh')")
		.__("actContent.scrollTop(sct)")
		;
		return null;
	}
	
	default Object doActivityDeFreeze(Object act)
	{
		var("actContent", "$(act+' .content')")
		.__("actContent.css('overflow', '')")
		.__("actContent.css('height', '')")
		
		.__("$(act).removeClass('fixedForAnimated')")
		;
		return null;
	}
	
	default Object doActivityInactive(Object act) //Object sct
	{
		__()
		 .__("$(act).removeClass('active')")
		 .__("$(act).addClass('inactive')")
		 ;
		return null;
	}
	
	default Object doActivityActive(Object act)
	{
		__()
   		.__("$(act).removeClass('nodisplay')")
		.__("$(act).addClass('active')")
		.__("$(act).removeClass('inactive')")
		;
		return null;
	}
	
	default Object doActivityNoDisplay(Object act)
	{
		__()
   		.__("$(act).removeClass('active')")
	 	.__("$(act).addClass('nodisplay')")
		;
		return null;
	}
	
	
	default Object doInitScrollTo(Object act)
	{
		__()
		.var("scrposition", "$(act).data('scrolltop')")
	    .__("$('body').scrollTop(scrposition==null?0:scrposition)")	
		;
		return null;
	}
	
	
	default Object doOpenActivityFromBottom()
	{
		
		var(_overlay, _new(SPEED_SHOW_ACTIVITY, ScnStandard.OVERLAY_OPACITY_BACK))
	 	.var("sct", "$('body').scrollTop()")
	 	.var(_self, _this)
	 	.var("act1", "'#'+$xui.intent.prevActivity")
	 	.var("act2", "'#'+$xui.intent.activity")
	 	.var("jqAct1", "$(act1)")
	 	.var("jqAct2", "$(act2)")
	 	
	    ._if("jqAct1.hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(
		    		 fct() 
					.__(_self.doNavBarToActivity(0))
					.__(_overlay.doShow("act1",1))    // init
					.__(_self.doActivityInactive("act1"))	
			   		.__(_self.doActivityFreeze("act1","sct"))               //freeze 1
					.__(_self.doActivityActive("act2"))	
					
					.__("jqAct1.addClass('backActivity')")
		    		 
					.__("jqAct2.addClass('frontActivity')")
			   		.__("jqAct2.addClass('toHidden')") //prepare l'animation  top 0 fixed
			   	   
	    		 ,NEXT_FRAME, fct()   // lance les anim
	    		 	.__(_overlay.doShow("act1", 2)) 			
	    	   		.__(_self.doActivityFreeze("act2", -1))    //freeze 2
	    	   		
	    	   		.__("jqAct1.addClass('toback')")
			   		.__("jqAct2.addClass('tofront')")
	    		 	
			   	,SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION, fct()
			   		.__(_self.doActivityNoDisplay("act1"))	
			   		
					.__("jqAct2.removeClass('tofront')")
				 	.__("jqAct2.removeClass('toHidden')") 
				 	.__("jqAct2.removeClass('frontActivity')")
			   		.__(_self.doActivityDeFreeze("act2"))			  // defrezze 2 
			   		.__(_self.doNavBarToBody())
			   		
			   		.__(_self.doInitScrollTo("act2"))
				    
			   	, NEXT_FRAME, fct()
			  // 		.consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start( 
				fct()
					 .__(_self.doNavBarToActivity(0))				 
					 .__(_self.doActivityInactive("act2"))
					 .__(_self.doActivityActive("act1"))
					 .__(_self.doActivityFreeze("act2","sct"))   // frezze 2
					 .__("jqAct2.addClass('frontActivity')")
					 
				, NEXT_FRAME, fct()    // lance les anim
					.__(_overlay.doHide(1))
					
					.__("jqAct1.removeClass('toback')")	
					.__("jqAct1.addClass('backToFront')") 
					.__("jqAct2.addClass('toHidden')")
					
			   	,SPEED_SHOW_ACTIVITY+DELAY_SURETE_END_ANIMATION, fct()
			   		.__(_overlay.doHide(2))	
			   		.__(_self.doActivityNoDisplay("act2"))	
			   		.__(_self.doActivityDeFreeze("act2"))	   // defrezze 2
			   		.__(_self.doNavBarToBody())	
			   		
					.__("jqAct1.removeClass('backToFront')")
				 	.__("jqAct2.removeClass('toHidden')") 
				 	.__("jqAct2.removeClass('frontActivity')")
				 	.__("jqAct1.removeClass('backActivity')")
				 	
					.__(_self.doActivityDeFreeze("act1"))      // defrezze 1
					.__(_self.doInitScrollTo("act1"))
					
			   	,NEXT_FRAME, fct()
			//   		.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
	
	
	JSXHTMLPart _template = null;
	default Object doOpenActivityFromRipple()
	{
		
		var(_overlay, _new(SPEED_SHOW_ACTIVITY, 0.6))
	 	.var(_self, _this)
	 	.var("sct", "$('body').scrollTop()")	
	 	.var("act1", "'#'+$xui.intent.prevActivity")
	 	.var("act2", "'#'+$xui.intent.activity")
	 	.var("jqAct1", "$(act1)")
	 	.var("jqAct2", "$(act2)")
	 	
	    ._if("jqAct1.hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start( 
		    	fct() 
		    		 .__(_overlay.doShow("act1", 1))
				  	 .__(_self.doNavBarToActivity(0))
				   	 .__(_self.doActivityFreeze("act1","sct"))    // frezze 1
				     .__("jqAct1.addClass('backActivity')")
				   	 
		    		 .var(_template,  ViewOverlayRipple.xTemplate() )
		    		 .var("rippleOverlay", _template.append("$(act2)"))
		    	//	 .__("$('.scene .ripple_overlay').addClass('t0prct')")
		    	//	 .__("$('.scene .ripple_overlay').addClass('transitionx2')")
				//	 .__("$('.ripple_overlay').addClass('t100prct')") 
		    		 
				, NEXT_FRAME, fct()	 //lance animation   
	    		 	.__(_overlay.doShow("act1", 2))
				
		    	   	.__("jqAct1.addClass('toback')")	    
		    //	   	.__("$('.scene .ripple_overlay').removeClass('t0prct')")
			//	 	.__("$('.scene .ripple_overlay').addClass('t100prct')") 
					 
			//	, ScnStandard.SPEED_ACTIVITY_TRANSITION_EFFECT-50, fct()   // 50 l'anim de la bulle peut etre arreter avant la fin
				 	.__(_self.doActivityInactive("act1"))
					.__(_self.doActivityActive("act2"))
								 		
					// prepare anim
					.__("jqAct2.addClass('frontActivity')")
	 				.__("jqAct2.addClass('circleAnim0prt')")
	 				.__("jqAct2.addClass('zoom12')")
					
	    	   		.__(_self.doActivityFreeze("act2",-1)) // frezze 2
					
				 , NEXT_FRAME, fct()	 //lance animation 	
				 	.__("jqAct2.removeClass('circleAnim0prt')")
				 	
				    .__("jqAct2.addClass('transitionSpeedx2')")   // cercle effect
				 	.__("jqAct2.addClass('circleAnim100prt')")
			    	.__("$('.ripple_overlay').addClass('transition')")
					.__("$('.ripple_overlay').css('opacity', '0')")
				 
				 , SPEED_ACTIVITY_TRANSITION_EFFECT, fct()	 //lance animation  dezoom	plus tard 
			   		.__("jqAct2.removeClass('circleAnim100prt')")
				 	.__("jqAct2.addClass('transitionSpeed')")
				 	.__("jqAct2.removeClass('transitionSpeedx2')")
				 	.__("jqAct2.removeClass('zoom12')")
				 	.__("jqAct2.addClass('zoom10')")
				 	
		    	    	
		    	, SPEED_SHOW_ACTIVITY + DELAY_SURETE_END_ANIMATION,     fct()

					
		   			.__(_self.doNavBarToBody())
					.__(_self.doActivityNoDisplay("act1"))
			   		.__(_self.doActivityDeFreeze("act2"))	    // defrezze 2
			   		
		   		 	.__("$('.ripple_overlay').remove()")  
			   		.__("jqAct2.removeClass('transitionSpeed')")
			   		.__("jqAct2.removeClass('transitionSpeedx2')")
			   		.__("jqAct2.removeClass('frontActivity')")
				 	.__("jqAct2.removeClass('zoom10')")
			   		
		    	    // annule l'animation
	    	    	.__("jqAct2.css('transform', '')")
	    	    	
	    	    	.__(_self.doInitScrollTo("act2"))  	
		    	        .consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(  fct()
					.__(_self.doNavBarToActivity(0))				 
					.__(_self.doActivityInactive("act2"))
					.__(_self.doActivityActive("act1"))
					.__(_self.doActivityFreeze("act2","sct"))    // frezze 2
					
//					// recherche ripple
//					._if(true)
//						.__("$('.scene .ripple_overlay').removeClass('t0prct')")
//						.__("$('.scene .ripple_overlay').addClass('t100prct')") 
//				 	.endif()
//				 	
//					.__("$('.scene .ripple_overlay').css('display', '')")   // display en grand
					
		   			.__("jqAct2.addClass('circleAnim100prt')")
		   			.__("jqAct2.addClass('frontActivity')")

				, NEXT_FRAME , fct()	
		   			.__("jqAct2.addClass('transitionSpeed')")
		   			.__("jqAct2.addClass('zoom12')")    // lance le zoome
				 	
				, NEXT_FRAME, fct()   //puis lance la circle
			 		.__("jqAct2.addClass('transitionSpeed')")
					.__("jqAct2.addClass('circleAnim0prt')")
					.__("jqAct2.removeClass('circleAnim100prt')")					
					
//				,(ScnStandard.SPEED_ACTIVITY_TRANSITION_EFFECT+100), fct()	
//					.__("$('.scene .ripple_overlay').removeClass('transitionx2')")
//					.__("$('.scene .ripple_overlay').addClass('transition')")
//		    	   	.__("$('.scene .ripple_overlay').addClass('t0prct')")
//				 	.__("$('.scene .ripple_overlay').removeClass('t100prct')") 
					
				// , 1000000, fct()   //puis lance la circle	
		   			
		   		, SPEED_ACTIVITY_TRANSITION_EFFECT/2 , fct()    // lance animation activity 1
					.__("jqAct1.removeClass('toback')")
					.__("jqAct1.addClass('backToFront')") 
		   			.__(_overlay.doHide(1))
		   			
				, Math.max(SPEED_SHOW_ACTIVITY, SPEED_ACTIVITY_TRANSITION_EFFECT) + DELAY_SURETE_END_ANIMATION, fct()	
					.__(_overlay.doHide(2))
			   		.__(_self.doActivityNoDisplay("act2"))	
			   		.__(_self.doNavBarToBody())	
					.__(_self.doActivityDeFreeze("act1"))       // defrezze 1
					
					.__("jqAct1.removeClass('backToFront')")
//		   		 	.__("$('.ripple_overlay').remove()")   //TODO a changer

			   		.__(_self.doActivityDeFreeze("act2"))	    // defrezze 2
					
			   		.__("jqAct2.removeClass('transitionSpeed')")
			   		.__("jqAct2.removeClass('transitionSpeedx2')")
	 				.__("jqAct2.removeClass('circleAnim0prt')")
	 				.__("jqAct2.removeClass('zoom12')")
	 				.__("jqAct2.removeClass('frontActivity')")
	 				.__("jqAct1.removeClass('backActivity')")
		   		 	
					.__(_self.doInitScrollTo("act1"))
					
				 ,NEXT_FRAME , fct()
				 	.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
