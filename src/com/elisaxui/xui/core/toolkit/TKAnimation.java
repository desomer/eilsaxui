/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;

/**
 * @author Bureau
 *
 */
public interface TKAnimation extends JSClass {

	JSOverlay _overlay = null;
	
	default Object doOpenBurgerMenu() {
		var(_overlay, _new(ScnStandard.SPEED_SHOW_OVERLAY, 0.3))
		.var("jqMenu", "$('.menu')")
		.var("jqScene", "$('.scene')")
		.var("jqNavBar", "$('.activity.active .navbar')")
		.var("jqActivityActive", "$('.activity.active')")
		.var("sct", "$('.scene').scrollTop()")
		
		// ferme le menu
		._if("jqNavBar.hasClass('fixedToAbsolute')")
			.var("jqHamburgerDetach", "$('.scene .hamburger.detach')")
			.__(TKQueue.start(
				fct()
//						.__("$('.active .logo').toggleClass('animated shake')")  // retire le shake
						.__("jqHamburgerDetach.removeClass('is-active changeColorMenu')")
				, "$xui.config.delayWaitForShowMenu", fct()   // attente ouverte burger		
				    	.__(_overlay.doHide(1))
				    	//-------------------------- repositionne l'activity --------------------
				    	.__("jqActivityActive.removeClass('activityMoveForShowMenu')")
						.__("jqActivityActive.addClass('activityMoveForHideMenu')")
						//----------------------------- cache le menu ------------------------
						.__("jqMenu.css('transform', 'translate3d(-"+ (ScnStandard.widthMenu + 5)+ "px,'+sct+'px,0px)' )")
						//----------------------------- repasse en croix ----------------------
						.__("jqHamburgerDetach.css('transition','all "+ScnStandard.SPEED_SHOW_BURGERMENU+"ms ease-out')")
						.__("jqHamburgerDetach.css('transform', 'translate3d(0px,'+sct+'px,0px) scale(1)' )")
						
				, ScnStandard.SPEED_SHOW_BURGERMENU, fct()
						.__(_overlay.doHide(2))
						//-----------  fige la barre nav en haut  (fixed) --------
						.__("jqNavBar.css('transform', 'translate3d(0px,0px,0px)' )")
						.__("jqScene.css('overflow','auto')") // remet le scroll
						.__("$('body').css('overflow','auto')") // remet de scroll
						.__("jqNavBar.removeClass('fixedToAbsolute')")
						// anime le burger et le passe de la scene vers l'activity
						.var("hamburger", "jqHamburgerDetach.detach()")
						.__("hamburger.removeClass('detach')")
						.__("jqNavBar.append(hamburger)")
						.__("hamburger.css('transition','none "+ScnStandard.SPEED_SHOW_BURGERMENU+"ms ease-out')")
						.__("hamburger.css('transform', 'translate3d(0px,0px,0px) scale(1)' )")
						
						//-------------------------- fin du repositionnement l'activity --------------------
						.__("jqActivityActive.removeClass('activityMoveForHideMenu')"),

				1, fct().consoleDebug("'end anim'")
				)
			)
		._else()
		.var("jqHamburger", "$('.activity.active .hamburger')")
		// ouvre le menu
		.__(TKQueue.start(
				fct()
						.__(_overlay.doShow(1))		 	        		 
						//-----------  passe en absolute la barre nav en haut par rapport au scroll  --------
						.__("jqNavBar.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")					
						.__("jqScene.css('overflow','hidden')") // plus de scroll
						.__("$('body').css('overflow','hidden')") // plus de scroll
						.__("jqNavBar.addClass('fixedToAbsolute')") // permet la nav de bouger
						//---------------------------------------
						.__("jqMenu.css('transition', '' )")   // fige le menu en haut
						.__("jqMenu.css('transform', 'translate3d(-"+ ScnStandard.widthMenu	+ "px,'+$('.scene').scrollTop()+'px,0px)' )")
						//---------------------------------------
						.__("jqHamburger.addClass('is-active')"),  // passe en croix
				//  100, fct().__("$('.active .hamburger').addClass('changeColorMenu')"), // passe en back
				 "$xui.config.delayWaitForShowMenu", fct()    	// attent passage en croix
				 		.__(_overlay.doShow(2))
				 		
				 		//---------- anime le burger et le passe sur la scene---------------
						.var("hamburger", "jqHamburger.detach()")	
						.__("hamburger.addClass('detach')")
						.__("jqScene.append(hamburger)")
						.__("hamburger.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")  // positionne en haut
						.__("hamburger.css('transition','transform "+ScnStandard.SPEED_SHOW_BURGERMENU+"ms ease-out')")
						
						//------------ deplace l'activity a louverture du menu-------------
						.__("jqActivityActive.addClass('activityMoveForShowMenu')") 

						//------------ ouvre le menu---------
						.__("jqMenu.css('transition', 'transform "+ScnStandard.SPEED_SHOW_BURGERMENU+"ms ease-out' )")
						.__("jqMenu.css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")
						.__("hamburger.css('transform', 'translate3d(-15px,'+(-3+sct)+'px,0px) scale(0.6)' )")
						//------------- anim des item de menu----------	
						._for("var i in window.jsonMainMenu") 
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",(i*"+10+"), window.jsonMainMenu[i])")
						.endfor()
						
				, ScnStandard.SPEED_SHOW_BURGERMENU, fct()
										//.__("$('.active .logo').toggleClass('animated shake')")
										.consoleDebug("'end anim'")
				  )
				)
		.endif();
		return null;
	}
	
	default Object doOpenActivityFromBottom()
	{
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, 0.6))
	 	.var("sct", "$('.scene').scrollTop()")
	 	
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(  // ScnStandard.SPEED_RIPPLE_EFFECT, // attente bulle
		    		 fct() 
						.__(_overlay.doShow(1))
		    		 ,100, fct()   // attente overlay
				   		.__("$('#activity1').data('scrolltop', sct ) ") 
				   		.__("$('#activity1 .navbar').css('top', sct+'px' )")
				   		.__("$('#activity1').css('transform-origin', '50% 150px')")
				   		.__("$('#activity1').css('overflow', 'auto')")
				   		.__("$('#activity1').scrollTop(sct)")
				   		.__("$('#activity1').addClass('toback')")
				   		.__("$('#activity2').addClass('tofront')")
				   		.__("$('#activity2').removeClass('inactivefixed')")	
		    		 	.__(_overlay.doShow(2))
				   	,ScnStandard.SPEED_SHOW_ACTIVITY, fct()		
				   		.__("$('#activity1').removeClass('active')")
				   		.__("$('#activity2').removeClass('inactive')")
				   		.__("$('#activity2').addClass('active')")
				   		.__("$('#activity2').removeClass('tofront')")
		    		 	.__("$('#activity2').removeClass('toHidden')") 
					    .__("$('.scene').scrollTop($('#activity2').data('scrolltop'))")		
				   	, 100, fct().consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(  // ScnStandard.SPEED_RIPPLE_EFFECT, 
				fct()   // attente bulle
					.__("$('#activity2').addClass('inactive')")
					.__("$('#activity2').data('scrolltop', sct ) ") 
				,100, fct()   // attente obligatoire pour bug changement de display trop rapide
					.__("$('#activity1').removeClass('toback')")	  
					.__("$('#activity1').addClass('active backToFront')") 								
				,50, fct()   // 100 ms attente obligatoire pour bug changement de display trop rapide car rejoue les anim en attente
					.__("$('#activity2').addClass('inactivefixed')")
					.__("$('#activity2').addClass('toHidden')")
					.__(_overlay.doHide(1))
			   	,ScnStandard.SPEED_SHOW_ACTIVITY, fct()
					.__("$('#activity2').removeClass('active')")
					
			   		.__("$('#activity1 .navbar').css('top', '0px' )")
					.__("$('#activity1').css('overflow', '')")
		   			.__("$('#activity1').css('transform-origin', '')")
		   			.__("$('.scene').scrollTop($('#activity1').data('scrolltop'))")
		   			.__("$('#activity1').removeClass('backToFront')")
					.__(_overlay.doHide(2))	
			   	,100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
	
	
	default Object doOpenActivityFromOpacity()
	{
		__()
	 	.var("sct", "$('.scene').scrollTop()")
	 	
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(  // ScnStandard.SPEED_RIPPLE_EFFECT, // attente bulle
		    		 fct() 
		    		 .__("$('#activity1').data('scrolltop', sct ) ") 
					 .__("$('#activity2').css('clip-path' ,'circle(0.0% at 100% 100%)')")
		    		 .__("$('#activity1').removeClass('active')")  
		    	//, 100, fct()
		    		 .__("$('#activity2').removeClass('inactivefixed')")
		    		 .__("$('#activity2').addClass('active')")
		    	//	 .__("$('#activity2').addClass('tofrontSlow')")
		    	//	 .__("$('#activity2').css('opacity', '0.1')")
					 .__("$('.scene').scrollTop($('#activity2').data('scrolltop'))")	
	    	    	 .__("$('#activity2').removeClass('inactive')")
	    	    	 //        .__("$('#activity2').removeClass('tofrontSlow')")
	    		    .__("$('#activity2').removeClass('toHidden')") 
					 .__("$('#activity2').css('transition', 'clip-path "+ ScnStandard.SPEED_SHOW_ACTIVITY+"ms ease-out')")
				 , 100, fct()
				 //	 .__("$('#activity2').css('transition', 'opacity 500ms ease-out')")
				 //	 .__("$('#activity2').css('opacity', '1')")
				 .__("$('#activity2').css('clip-path', 'circle(150% at 100% 100%)')")
		    	, ScnStandard.SPEED_SHOW_ACTIVITY, 
		    	    fct()	
		    	    	.__("$('#activity2').css('transition', '')")
		    	    	.__("$('#activity2').css('clip-path', '')")
		    	         .consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(  fct()
				 	// .__("$('#activity2').css('transition', 'opacity 500ms ease')")
				 	// .__("$('#activity2').css('opacity', '0')")
					 .__("$('#activity2').data('scrolltop', sct ) ") 
			//	, 200, fct()   // attente bulle
					.__("$('#activity2').css('transition', '')")
					.__("$('#activity2').addClass('inactive')")
					.__("$('#activity2').addClass('inactivefixed')")
					.__("$('#activity2').addClass('toHidden')")
				    .__("$('#activity2').removeClass('active')")
					.__("$('#activity1').addClass('active')") 		
		   			.__("$('.scene').scrollTop($('#activity1').data('scrolltop'))")
			   	, 100,  fct()
			   		//	.__("$('#activity2').css('opacity', '1')")
			   		//	.__("$('#activity2').css('opacity', '')")
			   	 		.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
