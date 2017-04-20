/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.menu.ViewMenu;

/**
 * @author Bureau
 *
 */
public interface JSNavBar extends JSClass {

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	default Object doBurger() {
		__()
		// ferme le menu
		._if("$('.active .fixedTop2').hasClass('fixedTop2')")
		.__(TKQueue.start(
				fct()
						.__("$('.active .logo').toggleClass('animated shake')")
						.__("$('.active.activity').toggleClass('activityLeftMenu')")
						.__("$('.scene .hamburger.active').toggleClass('is-active hmenu')")
						.__("$('.active .black_overlay').css('opacity','0')")
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transform', 'translate3d(-"
								+ (ScnStandard.widthMenu + 5)
								+ "px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('.scene .hamburger.active').css('transition','all 300ms ease-out')"
								+ ".css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px) scale(1)' )"),
				500, fct()
						.__("$('.active .black_overlay').css('display','none')")
						.__("$('.active .fixedTop').css('transform', 'translate3d(0px,0px,0px)' )")
						.__("$('body').css('overflow','auto')") // remet
																// le
																// scroll
						.__("$('.active .navbar').removeClass('fixedTop2')")
						.var("hamburger", "$('.scene .hamburger.active').detach()")
						.__("hamburger.removeClass('active')")
						.__("$('.active .navbar').append(hamburger)")
						.__("hamburger.css('transition','none 300ms ease-out').css('transform', 'translate3d(0px,0px,0px) scale(1)' )"),
				1, fct().consoleDebug("'end anim'")
				)
			)
		._else()
		// ouvre le menu
		.__(TKQueue.start(
				fct()
						.__("$('.active .fixedTop').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('body').css('overflow','hidden')") // plus
																	// de
																	// scroll
						.__("$('.active .navbar').addClass('fixedTop2')") // permet
																			// la
																			// nav
																			// de
																			// bouger
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transition', '' )")
						.__("$('." + ViewMenu.style.cMenu.getId() + "').css('transform', 'translate3d(-"
								+ ScnStandard.widthMenu
								+ "px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('.active .black_overlay').css('display','block')")

						.__("$('.active .hamburger').toggleClass('is-active')"),
				100, fct().__("$('.active .hamburger').toggleClass('hmenu')") // passe
																				// en
																				// back
				, 300, fct()
						.__("$('.active .black_overlay').css('transition','opacity 300ms ease-out')")
						.__("$('.active .black_overlay').css('opacity','0.3')")
						.var("hamburger", "$('.active .hamburger').detach()")
						.__("hamburger.css('transform', 'translate3d(" + ScnStandard.widthMenu
								+ "px,'+$('body').scrollTop()+'px,0px)' )")
						.__("hamburger.addClass('active')")
						.__("$('.scene').append(hamburger)")
						.__("$('.active.activity').toggleClass('activityLeftMenu')") // deplace
																						// l'activity
																						// a
																						// louverture
																						// du
																						// menu
						.__("$('." + ViewMenu.style.cMenu.getId()
								+ "').css('transition', 'transform 200ms ease-out' )")
						.__("$('." + ViewMenu.style.cMenu.getId()
								+ "').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
						.__("$('.scene .active.hamburger').css('transition','all 200ms ease-out').css('transform', 'translate3d(-15px,'+(-3+$('body').scrollTop())+'px,0px) scale(0.5)' )")
						.__("$('.active .logo').toggleClass('animated shake')")
						._for("var i in window.jsonMainMenu") // animation
																// des
																// items
																// de
																// menu
						.__("setTimeout(", fct("elem")
								.__("elem.anim='fadeInLeft'")
								.__("elem.anim=''"), ",i*20,window.jsonMainMenu[i])")
						.endfor()
						
				  , 400, fct().consoleDebug("'end anim'")
				  )
				)
		.endif();
		return null;
	}

	default Object getData(Object selector) {

		set(aDataSet, _new())
				.__(aDataSet.setData("[]"))

				.set(aDataDriven, _new(aDataSet))
				.__(aDataDriven.onEnter(fct("ctx")
						._if("ctx.row['_dom_']==null")
							._if("ctx.row.type=='burger'")
								.set(template, ViewNavBar.getTemplateBtnBurger())
								.var("jqdom", template.append("$(selector+' .navbar')"))
								.__("ctx.row['_dom_']=jqdom[0]")
							._elseif("ctx.row.type=='name'")
								.set(template, ViewNavBar.getTemplateName("ctx.row.name"))
								.var("jqdom", template.append("$(selector+' .navbar')"))
								.__("ctx.row['_dom_']=jqdom[0]")
							._elseif("ctx.row.type=='action'")
								._if("$(selector+' .rightAction').length==0")
									.set(template, ViewNavBar.getTemplateActionBar())
									.var("jqdom", template.append("$(selector+' .navbar')"))
								.endif()
								.set(template, ViewNavBar.getTemplateAction("ctx.row.icon", "ctx.row.idAction"))
								.var("jqdom", template.append("$(selector+' .rightAction')"))
								.__("ctx.row['_dom_']=jqdom[0]")
							.endif()
						.endif()))
				.__(aDataDriven.onExit(fct("value")
						._if("value!=null && value.row['_dom_']!=null")

						.endif()))

				.__(aDataDriven.onChange(fct("value")
						._if("value.row['_dom_']!=null && value.property=='idx'")

						.endif()
						._if("value.row['_dom_']!=null && value.property=='b'")

						.endif()))

				.var("jsonMenu", aDataSet.getData())
				.__("return jsonMenu")

		;

		return null;
	}

}
