/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;
import com.elisaxui.xui.core.widget.overlay.ViewOverlayRipple;

/**
 * @author Bureau
 *
 */
public interface TKAnimation extends JSClass {

	JSOverlay _overlay = null;
	TKAnimation _this = null;
	TKAnimation _self = null;
	
	default Object doNavBarToActivity(Object sct) {
		var("jqNavBar", "$('body>.navbar')")
		.var("jqActivityActive", "$('.activity.active')")
		//-----------  detache la barre nav en haut par rapport au scroll et ajoute a l'activité  --------
		.__("jqNavBar.detach()")
		.__("jqNavBar.css('transform', 'translate3d(0px,'+sct+'px,0px)' )")		
		.__("jqNavBar.addClass('fixedToAbsolute')") // permet la nav de bouger
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
	
	default Object doOpenBurgerMenu() {
		var(_overlay, _new(ScnStandard.SPEED_SHOW_MENU, ScnStandard.OVERLAY_OPACITY_MENU))
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
						.__("jqHamburgerDetach.css('transition','transform "+ScnStandard.SPEED_SHOW_MENU+"ms linear')")
						.__("jqHamburgerDetach.css('transform', 'translate3d(0px,'+sct+'px,0px) scale(1)' )")
						
				, ScnStandard.SPEED_SHOW_MENU + ScnStandard.DELAY_SURETE_END_ANIMATION, fct()
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

				, ScnStandard.SPEED_NEXT_FASTDOM, fct()	
						.__("jqHamburgerDetach.removeClass('is-active')")   //changeColorMenu.consoleDebug("'end anim'")
				, ScnStandard.SPEED_BURGER_EFFECT, fct()
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
						.__("jqHamburger.css('transition','transform "+ScnStandard.SPEED_SHOW_MENU+"ms linear')")  // prepare transition
						.__("jqScene.append(jqHamburger)")
						
						
				, ScnStandard.SPEED_NEXT_FASTDOM, fct()	
						//------------ deplace l'activity a l ouverture du menu-------------
						.__("jqActivityActive.addClass('activityMoveForShowMenu')") 
						//------------ ouvre le menu avec animation---------
						.__("jqMenu.css('transition', 'transform "+ScnStandard.SPEED_SHOW_MENU+"ms linear' )")
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
						
				, ScnStandard.SPEED_SHOW_MENU+ScnStandard.DELAY_SURETE_END_ANIMATION, fct()
						.__("jqHamburger.addClass('is-active')")  // passe en croix
						//.__("$('.active .logo').toggleClass('animated shake')")
				, ScnStandard.SPEED_BURGER_EFFECT, fct()		
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
		String act1 = "'#Activity1'";
		String act2 = "'#Activity2'";
		
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, ScnStandard.OVERLAY_OPACITY_BACK))
	 	.var("sct", "$('body').scrollTop()")
	 	.var(_self, _this)
	 	.var("act1", act1)
	 	.var("act2", act2)
	 	.var("jqAct1", "$(act1)")
	 	.var("jqAct2", "$(act2)")
	 	
	    ._if("jqAct1.hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(
		    		 fct() 
					.__(_self.doNavBarToActivity(0))
					.__(_overlay.doShow(act1,1))    // init
					.__(_self.doActivityInactive(act1))	
			   		.__(_self.doActivityFreeze(act1,"sct"))               //freeze 1
					.__(_self.doActivityActive(act2))	
					
			   		.__("jqAct2.addClass('toHidden')") //prepare l'animation  top 0 fixed
			   	   
	    		 ,ScnStandard.SPEED_NEXT_FASTDOM, fct()   // lance les anim
	    		 	.__(_overlay.doShow(act1, 2)) 			
	    	   		.__(_self.doActivityFreeze(act2, -1))    //freeze 2
	    	   		
	    	   		.__("jqAct1.addClass('toback')")
			   		.__("jqAct2.addClass('tofront')")
	    		 	
			   	,ScnStandard.SPEED_SHOW_ACTIVITY + ScnStandard.DELAY_SURETE_END_ANIMATION, fct()
			   		.__(_self.doActivityNoDisplay(act1))	
			   		
					.__("jqAct2.removeClass('tofront')")
				 	.__("jqAct2.removeClass('toHidden')") 
			   		
			   		.__(_self.doActivityDeFreeze(act2))			  // defrezze 2 
			   		.__(_self.doNavBarToBody())
			   		
			   		.__(_self.doInitScrollTo(act2))
				    
			   	, ScnStandard.SPEED_NEXT_FASTDOM, fct()
			  // 		.consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start( 
				fct()
					 .__(_self.doNavBarToActivity(0))				 
					 .__(_self.doActivityInactive(act2))
					 .__(_self.doActivityActive(act1))
					 .__(_self.doActivityFreeze(act2,"sct"))   // frezze 2
					
					 
				,ScnStandard.SPEED_NEXT_FASTDOM, fct()    // lance les anim
					.__(_overlay.doHide(1))
					
					.__("jqAct1.removeClass('toback')")	
					.__("jqAct1.addClass('backToFront')") 
					.__("jqAct2.addClass('toHidden')")
					
			   	,ScnStandard.SPEED_SHOW_ACTIVITY+ScnStandard.DELAY_SURETE_END_ANIMATION, fct()
			   		.__(_overlay.doHide(2))	
			   		.__(_self.doActivityNoDisplay(act2))	
			   		.__(_self.doActivityDeFreeze(act2))	   // defrezze 2
			   		.__(_self.doNavBarToBody())	
			   		
					.__("jqAct1.removeClass('backToFront')")
				 	.__("jqAct2.removeClass('toHidden')") 
				 	
					.__(_self.doActivityDeFreeze(act1))      // defrezze 1
					.__(_self.doInitScrollTo(act1))
					
			   	,ScnStandard.SPEED_NEXT_FASTDOM, fct()
			//   		.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
	
	
	JSXHTMLPart _template = null;
	default Object doOpenActivityFromOpacity()
	{
		String act1 = "'#Activity1'";
		String act2 = "'#Activity2'";
		
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, 0.6))
	 	.var(_self, _this)
	 	.var("sct", "$('body').scrollTop()")	
	 	.var("act1", act1)
	 	.var("act2", act2)
	 	.var("jqAct1", "$(act1)")
	 	.var("jqAct2", "$(act2)")
	 	
	    ._if("jqAct1.hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start( 
		    	fct() 
		    		 .__(_overlay.doShow(act1, 1))
				  	 .__(_self.doNavBarToActivity(0))
				   	 .__(_self.doActivityFreeze(act1,"sct"))    // frezze 1
				   	 
		    		 .var(_template,  ViewOverlayRipple.xTemplate() )
		    		 .var("rippleOverlay", _template.append("$('.scene')"))   //rippleOverlay par defaut invisible
		    		 
				, ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation   
	    		 	.__(_overlay.doShow(act1, 2))
				
		    	   	.__("jqAct1.addClass('toback')")	    		  	 
				 	.__("$('.scene .ripple_overlay').addClass('anim')") 
					 
				, ScnStandard.SPEED_SHOW_ACTIVITY - (ScnStandard.SPEED_SHOW_ACTIVITY/2), fct()   // 50 l'anim de la bulle peut etre arreter avant la fin
				 	.__(_self.doActivityInactive(act1))
					.__(_self.doActivityNoDisplay(act1))
					.__(_self.doActivityActive(act2))
								 		
					// prepare anim
	 				.__("jqAct2.addClass('circleAnim0prt')")
	 				.__("jqAct2.addClass('zoom12')")
					
	    	   		.__(_self.doActivityFreeze(act2,-1)) // frezze 2
					
				 , ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation 	
				 	.__("jqAct2.removeClass('circleAnim0prt')")
				    .__("jqAct2.addClass('transitionSpeedx2')")
				 	.__("jqAct2.addClass('circleAnim100prt')")
				 
				 , ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation  dezoom	 
				 	.__("jqAct2.removeClass('transitionSpeedx2')")
				 	.__("jqAct2.addClass('transitionSpeed')")
				 	.__("jqAct2.removeClass('zoom12')")
				 	.__("jqAct2.addClass('zoom10')")
		    	    	
		    	, ScnStandard.SPEED_SHOW_ACTIVITY + ScnStandard.DELAY_SURETE_END_ANIMATION,     fct()
					.__("$('.scene .ripple_overlay').css('display', 'none')")
					
		   			.__(_self.doNavBarToBody())
					.__(_self.doActivityNoDisplay(act1))
			   		.__(_self.doActivityDeFreeze(act2))	    // defrezze 2
			   		
			   		.__("jqAct2.removeClass('transitionSpeed')")
			   		.__("jqAct2.removeClass('circleAnim100prt')")
				 	.__("jqAct2.removeClass('zoom10')")
			   		
		    	    // annule l'animation
	    	    	.__("jqAct2.css('transform', '')")
	    	    	
	    	    	.__(_self.doInitScrollTo(act2))  	
		    	    //    .consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(  fct()
					.__(_self.doNavBarToActivity(0))				 
					.__(_self.doActivityInactive(act2))
					.__(_self.doActivityActive(act1))
					.__(_self.doActivityFreeze(act2,"sct"))    // frezze 2
					
					.__("$('.scene .ripple_overlay').css('display', '')")   // display en grand
					
			   		.__("jqAct2.addClass('circleAnim100prt')")
				 	.__("jqAct2.addClass('transitionSpeed')")
					
				, ScnStandard.SPEED_NEXT_FASTDOM , fct()
					.__("jqAct2.removeClass('circleAnim100prt')")
					.__("jqAct2.addClass('circleAnim0prt')")
					
				,ScnStandard.SPEED_SHOW_ACTIVITY- (ScnStandard.SPEED_SHOW_ACTIVITY/3), fct()	  
		   			.__("$('.scene .ripple_overlay').removeClass('anim')")   // lance la bulle
		   			
		   		, ScnStandard.SPEED_NEXT_FASTDOM , fct()    // lance animation activity 1
					.__("jqAct1.removeClass('toback')")
					.__("jqAct1.addClass('backToFront')") 
		   			.__(_overlay.doHide(1))
		   			
				,ScnStandard.SPEED_SHOW_ACTIVITY + ScnStandard.DELAY_SURETE_END_ANIMATION, fct()	
					.__(_overlay.doHide(2))
			   		.__(_self.doActivityNoDisplay(act2))	
			   		.__(_self.doNavBarToBody())	
					.__(_self.doActivityDeFreeze(act1))       // defrezze 1
					
					.__("jqAct1.removeClass('backToFront')")
		   		 	.__("$('.scene .ripple_overlay').remove()")

			   		.__(_self.doActivityDeFreeze(act2))	    // defrezze 2
					
			   		.__("jqAct2.removeClass('transitionSpeed')")
	 				.__("jqAct2.removeClass('circleAnim0prt')")
		   		 	
					.__(_self.doInitScrollTo(act1))
					
				 ,ScnStandard.SPEED_NEXT_FASTDOM , fct()
				// 	.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
