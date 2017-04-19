/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
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

	CSSClass cLeftBtn;

	@xComment("hamburger")
	CSSClass hamburger;
	@xComment("hamburger-box")
	CSSClass hamburger_box;
	@xComment("hamburger-inner")
	CSSClass hamburger_inner;

	@xComment("hamburger--elastic")
	CSSClass hamburger_elastic;
	
	@xComment("hamburger--arrow")
	CSSClass hamburger_arrow;

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()

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
		return xElement( "button", xIdAction(txt("burger")), xAttr("type", "\"button\""), cLeftBtn,  hamburger, true? hamburger_elastic:hamburger_arrow,
				xSpan(hamburger_box, xSpan(hamburger_inner)));
	}

}
