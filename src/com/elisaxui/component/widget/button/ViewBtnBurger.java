/**
 * 
 */
package com.elisaxui.component.widget.button;

import static com.elisaxui.component.transition.ConstTransition.SPEED_BURGER_EFFECT;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
/**
 * @author Bureau
 *
 */

@xComment("ViewBtnBurger")
public class ViewBtnBurger extends XHTMLPart {

	CSSClass cLeftBtn;

	@xComment("hamburger")
	public static CSSClass cHamburger;
	@xComment("hamburger-box")
	static CSSClass cHamburgerBox;
	@xComment("hamburger-inner")
	CSSClass cHamburgerInner;
	/*******************************************************/
	@xComment("hamburger--elastic")
	CSSClass cHamburgerElastic;
	
	@xComment("hamburger--arrow")
	CSSClass cHamburgerArrow;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {

		return cStyle()
				.path(cHamburger).set("will-change:transform")
				.path(CSSSelector.onPath(cHamburger).pseudoClass("hover")).set("opacity:1 !important")
				
				.path(	cHamburger.descendant(cHamburgerInner)
						.or(cHamburger.descendant(cHamburgerInner).pseudoClass("after"))
						.or(cHamburger.descendant(cHamburgerInner).pseudoClass("before"))
						)
						.set("background-color: #fff; "
								+ "transition-property:all !important; "
								+ "transition-duration:"+SPEED_BURGER_EFFECT+"ms !important;")

						//				.on(".hamburger.changeColorMenu .hamburger-inner, "  // changement de couleur
//						+ ".hamburger.changeColorMenu .hamburger-inner:after, "
//						+ ".hamburger.changeColorMenu .hamburger-inner:before",
//						"background-color: #fff; transition-property:all !important; transition-duration:500ms !important;")

				.path(cLeftBtn).set("position: absolute;top: 0px; left: 0px; "
						+ "z-index: "+XUIScene.ZINDEX_MENU+"; outline:0 !important") 
								// pas de bord bleu au click
		;
	}

	@xTarget(CONTENT.class)
	public XMLElement xBurgerBtn() {
		return xNode( "button", xIdAction("burger"), 
				xAttr("aria-label", "burger"), 
				xAttr("type", "button"), cLeftBtn,  cHamburger, 
				cHamburgerElastic,
				xSpan(cHamburgerBox, xSpan(cHamburgerInner)));
	}

}
