/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.TKQueue;

/**
 * @author Bureau
 *
 */

@xComment("ViewBtnBurger")
public class ViewBtnBurger extends XHTMLPart {

	CSSClass cLeftBtn;

	@xComment("hamburger")
	CSSClass hamburger;
	@xComment("hamburger--box")
	CSSClass hamburger_box;
	@xComment("hamburger--inner")
	CSSClass hamburger_inner;

	@xComment("hamburger--elastic")
	CSSClass hamburger_elastic;

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on("*", "-webkit-tap-highlight-color: rgba(0,0,0,0);")

				.on(".hamburger .hamburger-inner, "
						+ ".hamburger .hamburger-inner:after, "
						+ ".hamburger .hamburger-inner:before",
						"background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")
				.on(".hamburger.hmenu .hamburger-inner, "  // changement de couleur
						+ ".hamburger.hmenu .hamburger-inner:after, "
						+ ".hamburger.hmenu .hamburger-inner:before",
						"background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")

				.on(cLeftBtn, "position: absolute;top: 0px; left: 0px; z-index: 3; outline:0 !important") // pas
																											// de
																											// bord
																											// bleu
																											// au
																											// click
		;
	}

	@xTarget(CONTENT.class)
	public Element xBurgerBtn() {
		return xElement("button", xAttr("type", "'button'"),
				xAttr("class", "'cLeftBtn hamburger hamburger--elastic'"),
				xSpan(xAttr("class", "'hamburger-box'"), xSpan(xAttr("class", "'hamburger-inner'"))));
	}

	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xScriptJS(js()
				.__("$('.hamburger').on('click',", fct()
						._if("$('.fixedTop2').hasClass('fixedTop2')")
						
							.__(TKQueue.start(
								fct()
								// ferme le menu
								.__("$('.logo').toggleClass('animated shake')")
								.__("$('.activity').toggleClass('activityLeftMenu')")
								.__("$('.hamburger').toggleClass('is-active hmenu')")
								.__("$('.black_overlay').css('opacity','0')")
								.__("$('.menu').css('transform', 'translate3d(-" + ScnStandard.widthMenu
										+ "px,'+$('body').scrollTop()+'px,0px)' )")
								.__("$('.hamburger').css('transition','all 300ms ease-out')"
										+ ".css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px) scale(1)' )")
							, 500
							, fct()
								.__("$('.fixedTop').css('transform', 'translate3d(0px,0px,0px)' )")
								.__("$('body').css('overflow','auto')") // remet
																		// le
																		// scroll
								.__("$('.navbar').removeClass('fixedTop2')")
								.var("hamburger", "$('.hamburger').detach()")
								.__("$('.navbar').append(hamburger)")
								.__("hamburger.css('transition','none 300ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )")
								))
						._else()
							// ouvre le menu						
							.__(TKQueue.start(
								fct()	
									.__("$('.fixedTop').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
									.__("$('body').css('overflow','hidden')") // plus de
																				// scroll
									.__("$('.navbar').addClass('fixedTop2')")
									.__("$('.menu').css('transition', '' )")
									.__("$('.menu').css('transform', 'translate3d(-" + ScnStandard.widthMenu
											+ "px,'+$('body').scrollTop()+'px,0px)' )")
									.__("$('.black_overlay').css('display','block')")
	
									.__("$('.hamburger').toggleClass('is-active')")
								, 100
									, fct()	.__("$('.hamburger').toggleClass('hmenu')") // passe en back
								, 300
									, fct()
									.__("$('.black_overlay').css('transition','opacity 300ms ease-out')")
									.__("$('.black_overlay').css('opacity','0.3')")
									.var("hamburger", "$('.hamburger').detach()")
									.__("hamburger.css('transform', 'translate3d(" + ScnStandard.widthMenu
											+ "px,'+$('body').scrollTop()+'px,0px)' )")
									.__("$('.scene').append(hamburger)")
									.__("$('.activity').toggleClass('activityLeftMenu')")
									.__("$('.menu').css('transition', 'transform 200ms ease-out' )")
									.__("$('.menu').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
									.__("$('.hamburger').css('transition','all 200ms ease-out').css('transform', 'translate3d(-15px,'+(-3+$('body').scrollTop())+'px,0px) scale(0.5)' )")
									.__("$('.logo').toggleClass('animated shake')")
									)
						)
						.endif(), ")"

		));
	}
}
