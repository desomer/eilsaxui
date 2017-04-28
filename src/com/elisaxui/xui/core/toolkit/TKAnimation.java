/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.menu.ViewMenu;

/**
 * @author Bureau
 *
 */
public interface TKAnimation extends JSClass {

	
	default Object doOpenBurgerMenu() {
		__()
		// ferme le menu
		._if("$('.active .fixedToAbsolute').hasClass('fixedToAbsolute')")
		.__(TKQueue.start(
				fct()
				    .__("fastdom.mutate(", fct()
						.__("$('.active .logo').toggleClass('animated shake')")  // retire le shake
						.__("$('.active.activity').toggleClass('activityLeftMenu activityBackMenu')")
						.__("$('.scene .hamburger.active').toggleClass('is-active changeColorMenu')")
						.__("$('.active .black_overlay').css('opacity','0')")
						.__("$('.menu').css('transform', 'translate3d(-"+ (ScnStandard.widthMenu + 5)
								+ "px,'+$('.scene').scrollTop()+'px,0px)' )")
						.__("$('.scene .hamburger.active').css('transition','all "+ScnStandard.bugerMenuAnimSpeed+"ms ease-out')"
								+ ".css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px) scale(1)' )"),
				   ")"),		
				ScnStandard.bugerMenuAnimSpeed, fct()
				 .__("fastdom.mutate(", fct()
						.__("$('.active .black_overlay').css('display','none')")
						.__("$('.active .navbar').css('transform', 'translate3d(0px,0px,0px)' )")

//						.__("$('body').css('position','block')") // plus de scroll
						.__("$('.scene').css('overflow','auto')") // remet
																// le
																// scroll
						.__("$('.active .navbar').removeClass('fixedToAbsolute')")
						.var("hamburger", "$('.scene .hamburger.active').detach()")
						.__("hamburger.removeClass('active')")
						.__("$('.active .navbar').append(hamburger)")
						.__("$('.active.activity').removeClass('activityBackMenu')")
						.__("hamburger.css('transition','none "+ScnStandard.bugerMenuAnimSpeed+"ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )"),
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
						.__("$('.active .black_overlay').css('display','block')")
						.__("$('.active .hamburger').toggleClass('is-active')"),
					 ")"),	
				//  100, fct().__("$('.active .hamburger').toggleClass('changeColorMenu')"), // passe en back
				 50, fct()    	// attent passage en croix
				 		.__("fastdom.mutate(", fct()
						.__("$('.active .black_overlay').css('transition','opacity "+ScnStandard.overlaySpeed+"ms ease-out')")
						.__("$('.active .black_overlay').css('opacity','0.3')")
						.var("hamburger", "$('.active .hamburger').detach()")	

						.__("hamburger.addClass('active')")
						.__("$('.scene').append(hamburger)")
						.__("hamburger.css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")  // positionne en haut
						
						.__("$('.active.activity').toggleClass('activityLeftMenu')") // deplace
																						// l'activity
																						// a
																						// louverture
																						// du
																						// menu
						.__("$('.menu').css('transition', 'transform "+ScnStandard.bugerMenuAnimSpeed+"ms ease-out' )")
						.__("$('.menu').css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")
						
						.__("$('.scene .active.hamburger').css('transition','transform "+ScnStandard.bugerMenuAnimSpeed+"ms ease-out')"
								+ ".css('transform', 'translate3d(-15px,'+(-3+$('.scene').scrollTop())+'px,0px) scale(0.6)' )")

				//, ScnStandard.activitySpeed, fct()  // animation des items de menu					
						._for("var i in window.jsonMainMenu") 
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",(i*"+10+"), window.jsonMainMenu[i])")
						.endfor()
						, ")")	
				, ScnStandard.bugerMenuAnimSpeed, fct().__("$('.active .logo').toggleClass('animated shake')").consoleDebug("'end anim'")
				  )
				)
		.endif();
		return null;
	}
	
	default Object doOpenActivity()
	{
		__()
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(0, fct()   // attente bulle
		 	    	.__("fastdom.mutate(", fct()
		    		 	.var("sct", "$('body').scrollTop()")
				   		.__("$('#activity1').data('scrolltop', sct ) ") 
				   		.__("$('#activity1 .navbar').css('top', sct+'px' )")
				   		.__("$('#activity1').css('transform-origin', '50% '+ (sct+150) +'px')")
				   		.__("$('#activity1').css('overflow', 'auto')")
				   		.__("$('#activity1').scrollTop(sct)")
				   		.__("$('#activity1').addClass('toback')")
				   		.__("$('#activity2').addClass('tofront')")
				   	, ")")	
				   	,ScnStandard.activityAnimSpeed, fct()
				     	.__("fastdom.mutate(", fct()
				   		.__("$('#activity1').removeClass('active')")
				   //		.__("$('#activity1').css('display', 'none')")
		    		 	.__("$('#activity2').toggleClass('inactive active tofront toHidden')")
		    		, ")")	
				   	, 100, fct().consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(0, fct()   // attente bulle
					.__("fastdom.mutate(", fct()
				//	.__("$('#activity1').css('display', 'block')")
					.__("$('#activity2').addClass('inactive')")
					, ")")
				 ,100, fct()   // attente obligatoire pour bug changement de display trop rapide
				  .__("fastdom.mutate(", fct()
					.__("$('#activity1').removeClass('toback')")	  
					.__("$('#activity1').addClass('active backToFront')")
			   		, ")")	 					
					
				,10, fct()   // 100 ms attente obligatoire pour bug changement de display trop rapide car rejoue les anim en attente
				  .__("fastdom.mutate(", fct()
					   	.__("$('#activity2').addClass('toHidden')")
			   		, ")")	

			   	,ScnStandard.activityAnimSpeed, fct()
			   	 .__("fastdom.mutate(", fct()
					.__("$('#activity2').removeClass('active')")
			   		
			   		.__("$('#activity1 .navbar').css('top', '0px' )")
					.__("$('#activity1').css('overflow', '')")
		   			.__("$('#activity1').css('transform-origin', '')")
		   			.__("$('body').scrollTop($('#activity1').data('scrolltop'))")
		   			.__("$('#activity1').removeClass('backToFront')")
			   		, ")"),		
			   	100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
