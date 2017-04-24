/**
 * 
 */
package com.elisaxui.xui.core.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
public interface TKAnimation extends JSClass {

	
	default Object doOpenActivity()
	{
		__()
	    ._if("$('#activity1').hasClass('active')")
	         // ouverture activity 2 
		     .__(TKQueue.start(100, fct()
		    		 	.var("sct", "$('body').scrollTop()")
				   		.__("$('#activity1').data('scrolltop', sct ) ") 
				   		.__("$('#activity1 .navbar').css('top', sct+'px' )")
				   		.__("$('#activity1').css('transform-origin', '50% '+ (sct+150) +'px')")
				   		.__("$('#activity1').css('overflow', 'overlay')")
				   		.__("$('#activity1').scrollTop(sct)")
				   		.__("$('#activity1').addClass('toback')")
				   		.__("$('#activity2').addClass('tofront')")
				   	,ScnStandard.activitySpeed, fct()
				   		.__("$('#activity1').removeClass('active')")
				   		.__("$('#activity1').css('display', 'none')")
		    		 	.__("$('#activity2').toggleClass('inactive active tofront')")
				   	, 100, fct().consoleDebug("'end activity anim'")
				   ))
		._else()
			// fermeture activity 2 
			.__(TKQueue.start(100, fct()
					.__("$('#activity1').css('display', 'block')")
				,100, fct()
			   		.__("$('#activity1').toggleClass('active toback backToFront')")
			   		.__("$('#activity2').addClass('inactive')")
			   	,ScnStandard.activitySpeed, fct()
			   		.__("$('#activity1').css('overflow', '')")
			   		.__("$('#activity1 .navbar').css('top', '0px' )")
			   		.__("$('#activity1').css('transform-origin', '')")
					.__("$('body').scrollTop($('#activity1').data('scrolltop'))")
			   		.__("$('#activity1').removeClass('backToFront')")
			   		.__("$('#activity2').removeClass('active')"),
			   	100, fct().consoleDebug("'end activity anim'")
			   ))	
	   .endif();
		
		return null;
	}
}
