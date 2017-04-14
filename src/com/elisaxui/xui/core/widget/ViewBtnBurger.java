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
						+ ".hamburger .hamburger-inner:before", "background-color: #fff;")

				
				.add(".absolute","position: absolute; outline:0 !important")  // pas de bord bleu au click
				;
	}

	@xTarget(CONTENT.class)
	public Element xBurgerBtn() {
		return xElement("button", xAttr("type", "'button'"),
				xAttr("class", "'absolute hamburger hamburger--elastic'"),
				xSpan(xAttr("class", "'hamburger-box'"), xSpan(xAttr("class", "'hamburger-inner'"))));
	}

	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xScriptJS(js()
				.__("$('.hamburger').on('click',", fct()
						.__("$(this).toggleClass('is-active')")						
						._if("$('.fixedTop2').hasClass('fixedTop2')")
						    // ferme le menu
							.__("$('.black_overlay').css('opacity','0')")
						    .__("setTimeout(", fct()
								.__("$('.fixedTop').css('transform', 'translate3d(0px,0px,0px)' )")
								.__("$('body').css('overflow','auto')")  // remet le scroll
								.__("$('.navbar').removeClass('fixedTop2')")
								//.__("$('.black_overlay').css('display','none')")
								, ",300)")
						._else()
						     // ouvre le menu
							.__("$('.fixedTop').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
							.__("$('body').css('overflow','hidden')")   // plus de scroll
							.__("$('.navbar').addClass('fixedTop2')")
							.__("$('.black_overlay').css('display','block')")
							.__("setTimeout(", fct()
								.__("$('.black_overlay').css('transition','opacity 300ms ease-out')")
								.__("$('.black_overlay').css('opacity','0.3')")
							  , ",100)")
						.endif()
						,")"

			));
	}	
}
