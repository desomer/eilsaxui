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
		var("actContent", "$(act+' .content')")
   		.__("$(act).data('scrolltop', sct ) ") 
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
	
	default Object doActivity2IsFront()
	{
		__()
	 	.__("$('#activity2').removeClass('inactivefixed')")	
	 	.__("$('#activity2').removeClass('inactive')")
		.__("$('#activity2').removeClass('tofront')")
	 	.__("$('#activity2').removeClass('toHidden')") 
	 	;
		return null;
	}
	
	default Object doActivity1IsFront()
	{
		__()
		.__("$('#activity1').css('overflow', '')") //.css('transform-origin', '')
		.__("$('#activity1').removeClass('backToFront')")
		;
		return null;
	}
	
	
	default Object doOpenActivityFromBottom()
	{
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, ScnStandard.OVERLAY_OPACITY_BACK))
	 	.var("sct", "$('body').scrollTop()")
	 	.var(_self, _this)
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start(
		    		 fct() 
					.__(_self.doNavBarToActivity(0))
					.__(_overlay.doShow("'#activity1'",1))    // init
					.__(_self.doActivityInactive("'#activity1'"))	
					.__(_self.doActivityActive("'#activity2'"))		
			   		.__(_self.doActivityFreeze("'#activity1'","sct"))
//	 				 .__("$('#activity2').css('transform', 'scale3d(1.2,1.2,1)')") 
		
	    		 ,ScnStandard.SPEED_NEXT_FASTDOM, fct()   // lance les anim
	    		 	.__(_overlay.doShow("'#activity1'", 2))
	    	   		.__("$('#activity1').addClass('toback')")
			   		.__("$('#activity2').addClass('tofront')")
//		    	 ,ScnStandard.SPEED_NEXT_FASTDOM, fct()   // lance les anim			   		
//	    	    	.__("$('#activity2').css('transform', 'scale3d(1,1,1)')")
	    		 	
			   	,ScnStandard.SPEED_SHOW_ACTIVITY+ScnStandard.DELAY_SURETE_END_ANIMATION, fct()		// 50 attente vraiment la fin de l'animation
			   		.__(_self.doActivityNoDisplay("'#activity1'"))	
			   		.__(_self.doActivity2IsFront())	
			   		.__(_self.doActivityDeFreeze("'#activity2'"))			 	
			   		.__(_self.doNavBarToBody())
			   		
					.var("scrposition", "$('#activity2').data('scrolltop')")
				    .__("$('body').scrollTop(scrposition==null?0:scrposition)")	
				    
			   	, ScnStandard.SPEED_NEXT_FASTDOM, fct()
			   		.consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start( 
				fct()
					 .__(_self.doNavBarToActivity(0))				 
					 .__(_self.doActivityInactive("'#activity2'"))
					 .__(_self.doActivityActive("'#activity1'"))
					 .__(_self.doActivityFreeze("'#activity2'","sct"))
					 .__("$('#activity2').addClass('inactivefixed')") //prepare l'animation
					 
				,ScnStandard.SPEED_NEXT_FASTDOM, fct()    // lance les anim
					.__(_overlay.doHide(1))
					.__("$('#activity1').removeClass('toback')")	
					.__("$('#activity1').addClass('backToFront')") 
					.__("$('#activity2').addClass('toHidden')")
					
			   	,ScnStandard.SPEED_SHOW_ACTIVITY+ScnStandard.DELAY_SURETE_END_ANIMATION, fct()
			   		.__(_overlay.doHide(2))	
			   		.__(_self.doActivityNoDisplay("'#activity2'"))	
			   		.__(_self.doNavBarToBody())	
					.__(_self.doActivity1IsFront())	
					.__(_self.doActivityDeFreeze("'#activity1'"))
			   		
					.__("$('body').scrollTop($('#activity1').data('scrolltop'))")	
					
			   	,ScnStandard.SPEED_NEXT_FASTDOM, fct()
			   		.consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
	
	
	JSXHTMLPart _template = null;
	default Object doOpenActivityFromOpacity()
	{
		var(_overlay, _new(ScnStandard.SPEED_SHOW_ACTIVITY, 0.6))
	 	.var(_self, _this)
	 	.var("sct", "$('body').scrollTop()")	 	
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2
		     .__(TKQueue.start( 
		    	fct() 
					  .__(_overlay.doShow("'#activity1'", 1))
					  .__(_self.doNavBarToActivity(0))
				   	  .__(_self.doActivityFreeze("'#activity1'","sct"))
		    		 .var(_template,  ViewOverlayRipple.xTemplate() )
		    		 .var("rippleOverlay", _template.append("$('.scene')"))   //rippleOverlay par defaut invisible
		    		 
				, ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation    		 
		    	   	 .__("$('#activity1').addClass('toback')")	    		 
		    		 .__(_overlay.doShow("'#activity1'", 2))
		    		 
				,  ScnStandard.SPEED_NEXT_FASTDOM, fct()	 
				 	.__("$('.scene .ripple_overlay').addClass('anim')") 
					 
				, ScnStandard.SPEED_SHOW_ACTIVITY-50, fct()
				 	.__(_self.doActivityInactive("'#activity1'"))	
					.__(_self.doActivityActive("'#activity2'"))
					.__(_self.doActivity2IsFront())
					 
					// prepare anim
					 .__("$('#activity2').css('position', 'absolute')") 
	 				 .__("$('#activity2').css('transform', 'scale3d(1.2,1.2,1)')") 
					 .__("$('#activity2').css('clip-path' ,'circle(0.0% at 100vw 100vh)')")     // invisible
					 .__("$('#activity2').css('-webkit-clip-path' ,'circle(0.0% at 100vw 100vh)')")
					 .__("$('#activity2').css('z-index' ,'1')")
					 .var("scrposition", "$('#activity2').data('scrolltop')")
					 .__("$('body').scrollTop(scrposition==null?0:scrposition)")
				 	 .__("$('#activity2').css('transition', 'all "+ ScnStandard.SPEED_SHOW_ACTIVITY/2 +"ms linear')")
				 	 
					.__(_self.doActivityNoDisplay("'#activity1'"))
					
				 , ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation 	 		 		
				 		.__("$('#activity2').css('clip-path', 'circle(100% at center)')")
				 		.__("$('#activity2').css('-webkit-clip-path', 'circle(100% at center)')")	// rend visible
				 
				 , ScnStandard.SPEED_NEXT_FASTDOM, fct()	 //lance animation 	 	
		    	    	.__("$('#activity2').css('transform', 'scale3d(1,1,1)')")
		    	    	
		    	, ScnStandard.SPEED_SHOW_ACTIVITY+ScnStandard.DELAY_SURETE_END_ANIMATION,     fct()
			   		.__(_self.doActivityDeFreeze("'#activity2'"))			 	
			   		.__(_self.doNavBarToBody())
		    	    .__("$('.scene .ripple_overlay').css('display', 'none')")	
		    	    
		    	    .__("$('#activity1').addClass('nodisplay')")
    	    		.__("$('#activity2').css('z-index' ,'')")
	    	    	.__("$('#activity2').css('transition', '')")
	    	    	.__("$('#activity2').css('clip-path', '')")
	    	    	.__("$('#activity2').css('-webkit-clip-path', '')")
	    	    	.__("$('#activity2').css('transform', '')")
		    	        .consoleDebug("'end activity anim'")
				   )) 	
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(  fct()
					.__("$('#activity1').removeClass('nodisplay')")
					.__("$('#activity2').data('scrolltop', sct ) ") 
					.__("$('#activity2').css('z-index' ,'1')")
					.__("$('.scene .ripple_overlay').css('display', '')")
					.__("$('#activity2').css('clip-path', 'circle(100% at center)')")
					.__("$('#activity2').css('-webkit-clip-path', 'circle(100% at center)')")
					.__("$('#activity2').css('transition', ' "+ ScnStandard.SPEED_SHOW_ACTIVITY +"ms ease-out')")
					.__("$('#activity2').css('transition-property', '-webkit-clip-path, clip-path')")
				, 100 , fct()	
					.__("$('#activity2').css('clip-path' ,'circle(0.0% at 100vw 100vh)')")	
					.__("$('#activity2').css('-webkit-clip-path' ,'circle(0.0% at 100vw 100vh)')")	
				,ScnStandard.SPEED_SHOW_ACTIVITY, fct()	
					.__("$('#activity2').addClass('inactive')")
					.__("$('#activity2').addClass('inactivefixed')")
					.__("$('#activity2').css('transition', 'none')")  // pas d'animation pour le toHidden
					.__("$('#activity2').addClass('toHidden')")
				    .__("$('#activity2').removeClass('active')")
				    
		   			.__("$('.scene').scrollTop($('#activity1').data('scrolltop'))")
		   			.__("$('.scene .ripple_overlay').removeClass('anim')")
		   		 ,50, fct()	
					.__("$('#activity1').removeClass('toback')")
					.__("$('#activity1').addClass('active backToFront')") 
		   			.__(_overlay.doHide(1))
				,ScnStandard.SPEED_SHOW_ACTIVITY, fct()	
					.__(_self.doActivity1IsFront())
					.__("$('body').scrollTop($('#activity1').data('scrolltop'))")	
		   		 	.__("$('.scene .ripple_overlay').remove()")
		   		 	.__("$('#activity2').css('transition', '')")
		   		 	.__("$('#activity2').css('z-index' ,'')")
		   		 	.__("$('#activity2').css('clip-path', '')")
		   		 	.__("$('#activity2').css('-webkit-clip-path', '')")
		   		 	.__("$('#activity2').addClass('nodisplay')")
				    .__(_overlay.doHide(2))
				  ,100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
