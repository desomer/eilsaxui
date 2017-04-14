/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */

@xComment("ViewBtnBurger")
public class ViewBtnBurger extends XHTMLPart {

	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()
				.add("*", "-webkit-tap-highlight-color: rgba(0,0,0,0);")		
		
				.add(".hamburger .hamburger-inner, "
						+ ".hamburger .hamburger-inner:after, "
						+ ".hamburger .hamburger-inner:before", "background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")
				.add(".hamburger.hmenu .hamburger-inner, "
						+ ".hamburger.hmenu .hamburger-inner:after, "
						+ ".hamburger.hmenu .hamburger-inner:before", "background-color: #000; transition-property:all !important; transition-duration:500ms !important;")

				.add(".leftBtn","position: absolute;top: 0px; left: 0px; z-index: 3; outline:0 !important")  // pas de bord bleu au click
				;
	}

	@xTarget(CONTENT.class)
	public Element xBurgerBtn() {
		return xElement("button", xAttr("type", "'button'"),
				xAttr("class", "'leftBtn hamburger hamburger--elastic'"),
				xSpan(xAttr("class", "'hamburger-box'"), xSpan(xAttr("class", "'hamburger-inner'"))));
	}

	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xScriptJS(js()
				.__("$('.hamburger').on('click',", fct()
					
						._if("$('.fixedTop2').hasClass('fixedTop2')")
							.__("$('.logo').toggleClass('animated shake')")	
							.__("$('.activity').toggleClass('activityLeftMenu')")
							.__("$(this).toggleClass('is-active hmenu')")	
						    // ferme le menu
							.__("$('.black_overlay').css('opacity','0')")
							.__("$('.menu').css('transform', 'translate3d(-"+ScnStandard.widthMenu+"px,'+$('body').scrollTop()+'px,0px)' )")
							.__("$('.hamburger').css('transition','all 300ms ease-out').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px) scale(1)' )")
							//.__("$('.hamburger').css('transform', 'translate3d(100px,'+$('body').scrollTop()+'px,0px)' )")
						    .__("setTimeout(", fct()
								.__("$('.fixedTop').css('transform', 'translate3d(0px,0px,0px)' )")
								.__("$('body').css('overflow','auto')")  // remet le scroll
								.__("$('.navbar').removeClass('fixedTop2')")
								.var("hamburger", "$('.hamburger').detach()")
								.__("$('.navbar').append(hamburger)")
								.__("hamburger.css('transition','none 300ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )")
								//.__("$('.black_overlay').css('display','none')")
								, ",500)")
//						    .__("setTimeout(", fct()
//								.__("$('.hamburger').css('transition','all 300ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )")
//								//.__("$('.black_overlay').css('display','none')")
//								, ",600)")
						._else()
						     // ouvre le menu
						    
							.__("$('.fixedTop').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
							.__("$('body').css('overflow','hidden')")   // plus de scroll
							.__("$('.navbar').addClass('fixedTop2')")
							.__("$('.menu').css('transition', '' )")
							.__("$('.menu').css('transform', 'translate3d(-"+ScnStandard.widthMenu+"px,'+$('body').scrollTop()+'px,0px)' )")
							.__("$('.black_overlay').css('display','block')")
							
							.__("$(this).toggleClass('is-active')")	
							
							.__("setTimeout(", fct()	
								.__("$('.black_overlay').css('transition','opacity 300ms ease-out')")
								.__("$('.black_overlay').css('opacity','0.3')")
								.__("$('.hamburger').toggleClass('hmenu')")	   // passe en back	
							  , ",100)")
							.__("setTimeout(", fct()
								    .var("hamburger", "$('.hamburger').detach()")
									.__("hamburger.css('transform', 'translate3d("+ScnStandard.widthMenu+"px,'+$('body').scrollTop()+'px,0px)' )")
									.__("$('.scene').append(hamburger)")
									.__("$('.activity').toggleClass('activityLeftMenu')")
									.__("$('.menu').css('transition', 'transform 200ms ease-out' )")
									.__("$('.menu').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
								    .__("$('.hamburger').css('transition','all 200ms ease-out').css('transform', 'translate3d(-15px,'+(-5+$('body').scrollTop())+'px,0px) scale(0.5)' )")
									.__("$('.logo').toggleClass('animated shake')")
							  , ",400)")
//							.__("setTimeout(", fct()
//									.__("$('.hamburger').toggleClass('hmenu')")	   // passe en back
//							  , ",600)")
						.endif()
						,")"

			));
	}	
}
