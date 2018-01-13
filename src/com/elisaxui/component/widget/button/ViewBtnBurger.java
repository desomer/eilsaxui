/**
 * 
 */
package com.elisaxui.component.widget.button;

import static com.elisaxui.component.transition.ConstTransition.*;
import static com.elisaxui.component.transition.CssTransition.*;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
/**
 * @author Bureau
 *
 */

@xComment("ViewBtnBurger")
public class ViewBtnBurger extends XHTMLPart {

	XClass cLeftBtn;

	@xComment("hamburger")
	public static XClass hamburger;
	@xComment("hamburger-box")
	XClass hamburger_box;
	@xComment("hamburger-inner")
	XClass hamburger_inner;

	@xComment("hamburger--elastic")
	XClass hamburger_elastic;
	
	@xComment("hamburger--arrow")
	XClass hamburger_arrow;

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle()
				.path(hamburger).add("will-change:transform")
				.path(	hamburger.__(hamburger_inner)
						.or(hamburger.__(hamburger_inner).pseudoClass("after"))
						.or(hamburger.__(hamburger_inner).pseudoClass("before"))
						)
						.add("background-color: #fFf; "
								+ "transition-property:all !important; "
								+ "transition-duration:"+SPEED_BURGER_EFFECT+"ms !important;")

						//				.on(".hamburger.changeColorMenu .hamburger-inner, "  // changement de couleur
//						+ ".hamburger.changeColorMenu .hamburger-inner:after, "
//						+ ".hamburger.changeColorMenu .hamburger-inner:before",
//						"background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")

				.path(cLeftBtn).add("position: absolute;top: 0px; left: 0px; "
						+ "z-index: "+XUIScene.ZINDEX_MENU+"; outline:0 !important") 
								// pas de bord bleu au click
		;
	}

	@xTarget(CONTENT.class)
	public XMLElement xBurgerBtn() {
		boolean modeXCross = true;
		return xElement( "button", /*ViewRippleEffect.cRippleEffect(),*/ xIdAction(txt("burger")), xAttr("aria-label", txt("burger")), xAttr("type", "\"button\""), cLeftBtn,  hamburger, modeXCross? hamburger_elastic:hamburger_arrow,
				xSpan(hamburger_box, xSpan(hamburger_inner)));
	}

}