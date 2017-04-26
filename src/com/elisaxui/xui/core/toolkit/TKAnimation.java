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
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transform', 'translate3d(-"
								+ (ScnStandard.widthMenu + 5)
								+ "px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('.scene .hamburger.active').css('transition','all "+ScnStandard.activitySpeed+"ms ease-out')"
								+ ".css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px) scale(1)' )"),
				   ")"),		
				ScnStandard.activitySpeed, fct()
				 .__("fastdom.mutate(", fct()
						.__("$('.active .black_overlay').css('display','none')")
						.__("$('.active .navbar').css('transform', 'translate3d(0px,0px,0px)' )")

//						.__("$('body').css('position','block')") // plus de scroll
						.__("$('body').css('overflow','auto')") // remet
																// le
																// scroll
						.__("$('.active .navbar').removeClass('fixedToAbsolute')")
						.var("hamburger", "$('.scene .hamburger.active').detach()")
						.__("hamburger.removeClass('active')")
						.__("$('.active .navbar').append(hamburger)")
						.__("$('.active.activity').removeClass('activityBackMenu')")
						.__("hamburger.css('transition','none "+ScnStandard.activitySpeed+"ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )"),
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
						.__("$('.active .navbar').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('body').css('overflow','hidden')") // plus de scroll
//						.__("$('body').css('position','fixed')") // plus de scroll
						//.__("$('.activity.active').css('overflow','hidden')") // plus de scroll
						.__("$('.active .navbar').addClass('fixedToAbsolute')") // permet la nav de bouger
						
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transition', '' )")
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transform', 'translate3d(-"
								+ ScnStandard.widthMenu
								+ "px,'+$('body').scrollTop()+'px,0px)' )")
						
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
						.__("hamburger.css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")  // positionne en haut
						
						.__("$('.active.activity').toggleClass('activityLeftMenu')") // deplace
																						// l'activity
																						// a
																						// louverture
																						// du
																						// menu
						.__("$('." + ViewMenu.style.cMenu.getId()
								+ "').css('transition', 'transform "+ScnStandard.activitySpeed+"ms ease-out' )")
						.__("$('." + ViewMenu.style.cMenu.getId()
								+ "').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
						
						.__("$('.scene .active.hamburger').css('transition','transform "+ScnStandard.activitySpeed+"ms ease-out')"
								+ ".css('transform', 'translate3d(-15px,'+(-3+$('body').scrollTop())+'px,0px) scale(0.6)' )")

				//, ScnStandard.activitySpeed, fct()  // animation des items de menu					
						._for("var i in window.jsonMainMenu") 
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",(i*"+10+"), window.jsonMainMenu[i])")
						.endfor()
						, ")")	
				, ScnStandard.activitySpeed, fct().__("$('.active .logo').toggleClass('animated shake')").consoleDebug("'end anim'")
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
				   	,ScnStandard.activitySpeed, fct()
				     	.__("fastdom.mutate(", fct()
				   		.__("$('#activity1').removeClass('active')")
				   		.__("$('#activity1').css('display', 'none')")
		    		 	.__("$('#activity2').toggleClass('inactive active tofront')")
		    		, ")")	
				   	, 100, fct().consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(0, fct()   // attente bulle
					.__("fastdom.mutate(", fct()
					.__("$('#activity1').css('display', 'block')")
					, ")")	
				,10, fct()
				  .__("fastdom.mutate(", fct()
			   		.__("$('#activity1').toggleClass('active toback backToFront')")
			   		.__("$('#activity2').addClass('inactive')")
			   		, ")")	
			   	,ScnStandard.activitySpeed, fct()
			   	 .__("fastdom.mutate(", fct()
			   		.__("$('#activity1').css('overflow', '')")
			   		.__("$('#activity1 .navbar').css('top', '0px' )")
			   		.__("$('#activity1').css('transform-origin', '')")
					.__("$('body').scrollTop($('#activity1').data('scrolltop'))")
			   		.__("$('#activity1').removeClass('backToFront')")
			   		.__("$('#activity2').removeClass('active')")
			   		, ")"),	
			   	100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
