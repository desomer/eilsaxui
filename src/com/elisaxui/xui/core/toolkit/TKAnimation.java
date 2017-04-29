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
		// ferme le menu
		._if("$('.active .fixedToAbsolute').hasClass('fixedToAbsolute')")
		.__(TKQueue.start(
				fct()
				    .__("fastdom.mutate(", fct()
//						.__("$('.active .logo').toggleClass('animated shake')")  // retire le shake
						.__("$('.active.activity').toggleClass('activityMoveForShowMenu activityMoveForHideMenu')")
						.__("$('.scene .hamburger.active').toggleClass('is-active changeColorMenu')")
//						.__("$('.active .black_overlay').css('opacity','0')")
						.__(_overlay.doHide(1))
						.__("$('.menu').css('transform', 'translate3d(-"+ (ScnStandard.widthMenu + 5)
								+ "px,'+$('.scene').scrollTop()+'px,0px)' )")
						.__("$('.scene .hamburger.active').css('transition','all "+ScnStandard.SPEDD_SHOW_BURGERMENU+"ms ease-out')"
								+ ".css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px) scale(1)' )"),
				   ")"),		
				ScnStandard.SPEDD_SHOW_BURGERMENU, fct()
				 .__("fastdom.mutate(", fct()
//						.__("$('.active .black_overlay').css('display','none')")
						.__(_overlay.doHide(2))
						.__("$('.active .navbar').css('transform', 'translate3d(0px,0px,0px)' )")

//						.__("$('body').css('position','block')") // plus de scroll
						.__("$('.scene').css('overflow','auto')") // remet
																// le
																// scroll
						.__("$('.active .navbar').removeClass('fixedToAbsolute')")
						.var("hamburger", "$('.scene .hamburger.active').detach()")
						.__("hamburger.removeClass('active')")
						.__("$('.active .navbar').append(hamburger)")
						.__("$('.active.activity').removeClass('activityMoveForHideMenu')")
						.__("hamburger.css('transition','none "+ScnStandard.SPEDD_SHOW_BURGERMENU+"ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )"),
					")"),	
				1, fct().consoleDebug("'end anim'")
				)
			)
		._else()
		// ouvre le menu
		.__(TKQueue.start(
				fct()
				         .__("fastdom.mutate(", fct()
						// fige la barre nav
						.__("$('.active .navbar').css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")
						.__("$('.scene').css('overflow','hidden')") // plus de scroll
//						.__("$('body').css('position','fixed')") // plus de scroll
						//.__("$('.activity.active').css('overflow','hidden')") // plus de scroll
						.__("$('.active .navbar').addClass('fixedToAbsolute')") // permet la nav de bouger
						
						.__("$('.menu').css('transition', '' )")
						.__("$('.menu').css('transform', 'translate3d(-"+ ScnStandard.widthMenu	+ "px,'+$('.scene').scrollTop()+'px,0px)' )")
						//.__("$('.active .black_overlay').css('display','block')")
						.__(_overlay.doShow(1))
						.__("$('.active .hamburger').toggleClass('is-active')"),
					 ")"),	
				//  100, fct().__("$('.active .hamburger').toggleClass('changeColorMenu')"), // passe en back
				 300, fct()    	// attent passage en croix
				 		.__("fastdom.mutate(", fct()
				 		.__(_overlay.doShow(2))
					//	.__("$('.active .black_overlay').css('transition','opacity "+ScnStandard.overlaySpeed+"ms ease-out')")
					//	.__("$('.active .black_overlay').css('opacity','0.3')")
						.var("hamburger", "$('.active .hamburger').detach()")	

						.__("hamburger.addClass('active')")
						.__("$('.scene').append(hamburger)")
						.__("hamburger.css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")  // positionne en haut
						
						// deplace l'activity a louverture du menu
						.__("$('.active.activity').toggleClass('activityMoveForShowMenu')") 

						.__("$('.menu').css('transition', 'transform "+ScnStandard.SPEDD_SHOW_BURGERMENU+"ms ease-out' )")
						.__("$('.menu').css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")
						
						.__("$('.scene .active.hamburger').css('transition','transform "+ScnStandard.SPEDD_SHOW_BURGERMENU+"ms ease-out')"
								+ ".css('transform', 'translate3d(-15px,'+(-3+$('.scene').scrollTop())+'px,0px) scale(0.6)' )")

						// anim des item de menu	
						._for("var i in window.jsonMainMenu") 
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",(i*"+10+"), window.jsonMainMenu[i])")
						.endfor()
						, ")")	
				, ScnStandard.SPEDD_SHOW_BURGERMENU, fct()
										//.__("$('.active .logo').toggleClass('animated shake')")
										.consoleDebug("'end anim'")
				  )
				)
		.endif();
		return null;
	}
	
	default Object doOpenActivity()
	{
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, 0.6))
	 	.var("sct", "$('.scene').scrollTop()")
	 	
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(
		    		 fct() 
			 	    	.__("fastdom.mutate(", fct()
						   	.__(_overlay.doShow(1))
			 	    			, ")")	
		    		 ,100, fct()   // attente bulle
		 	    	.__("fastdom.mutate(", fct()
				   		.__("$('#activity1').data('scrolltop', sct ) ") 
				   		.__("$('#activity1 .navbar').css('top', sct+'px' )")
				   		.__("$('#activity1').css('transform-origin', '50% 150px')")
				   		.__("$('#activity1').css('overflow', 'auto')")
				   		.__("$('#activity1').scrollTop(sct)")
				   		.__("$('#activity1').addClass('toback')")
				   		.__("$('#activity2').addClass('tofront')")
				   		.__("$('#activity2').removeClass('hidden')")	
				   //		.__("$('.scene').scrollTop($('#activity2').data('scrolltop'))")
		    		 	.__(_overlay.doShow(2))
				   	, ")")	
				   	,ScnStandard.SPEED_SHOW_ACTIVITY, fct()
				     	.__("fastdom.mutate(", fct()		
				   		.__("$('#activity1').removeClass('active')")
				   		.__("$('#activity2').removeClass('inactive')")
				   		.__("$('#activity2').addClass('active')")
				   		.__("$('#activity2').removeClass('tofront')")
				   //		.__("$('#activity1').css('display', 'none')")
		    		 	.__("$('#activity2').removeClass('toHidden')") 
		    		// 	.__("$('#activity2').removeClass('hidden')")
					    .__("$('.scene').scrollTop($('#activity2').data('scrolltop'))")		
		    		, ")")	
				   	, 100, fct().consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(0, fct()   // attente bulle
					.__("fastdom.mutate(", fct()
				//	.__("$('#activity1').css('display', 'block')")
					.__("$('#activity2').addClass('inactive')")
					.__("$('#activity2').data('scrolltop', sct ) ") 
					, ")")
				 ,100, fct()   // attente obligatoire pour bug changement de display trop rapide
				  .__("fastdom.mutate(", fct()
					.__("$('#activity1').removeClass('toback')")	  
					.__("$('#activity1').addClass('active backToFront')")
			   		, ")")	 					
					
				,50, fct()   // 100 ms attente obligatoire pour bug changement de display trop rapide car rejoue les anim en attente
				  .__("fastdom.mutate(", fct()
						  .__("$('#activity2').addClass('hidden')")
					   	.__("$('#activity2').addClass('toHidden')")
						.__(_overlay.doHide(1))
			   		, ")")	

			   	,ScnStandard.SPEED_SHOW_ACTIVITY, fct()
			   	 .__("fastdom.mutate(", fct()
					.__("$('#activity2').removeClass('active')")
					//.__("$('#activity2').addClass('hidden')")
					
			   		.__("$('#activity1 .navbar').css('top', '0px' )")
					.__("$('#activity1').css('overflow', '')")
		   			.__("$('#activity1').css('transform-origin', '')")
		   			.__("$('.scene').scrollTop($('#activity1').data('scrolltop'))")
		   			.__("$('#activity1').removeClass('backToFront')")
					.__(_overlay.doHide(2))
			   		, ")"),		
			   	100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
