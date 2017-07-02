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
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLElement;
import com.elisaxui.xui.core.page.ScnStandard;
import static  com.elisaxui.xui.core.transition.CssTransition.*;
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
	public XMLElement xStyle() {

		return xCss()
				.select(hamburger).set("will-change:transform")
				.select(	hamburger.__(hamburger_inner)
						.or(hamburger.__(hamburger_inner).pseudoClass("after"))
						.or(hamburger.__(hamburger_inner).pseudoClass("before"))
						)
						.set("background-color: #fFf; "
								+ "transition-property:all !important; "
								+ "transition-duration:"+SPEED_BURGER_EFFECT+"ms !important;")

						//				.on(".hamburger.changeColorMenu .hamburger-inner, "  // changement de couleur
//						+ ".hamburger.changeColorMenu .hamburger-inner:after, "
//						+ ".hamburger.changeColorMenu .hamburger-inner:before",
//						"background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")

				.select(cLeftBtn).set("position: absolute;top: 0px; left: 0px; "
						+ "z-index: "+ScnStandard.ZINDEX_MENU+"; outline:0 !important") 
								// pas de bord bleu au click
		;
	}

	@xTarget(CONTENT.class)
	public XMLElement xBurgerBtn() {
		return xElement( "button", /*ViewRippleEffect.cRippleEffect(),*/ xIdAction(txt("burger")), xAttr("type", "\"button\""), cLeftBtn,  hamburger, true? hamburger_elastic:hamburger_arrow,
				xSpan(hamburger_box, xSpan(hamburger_inner)));
	}

}
