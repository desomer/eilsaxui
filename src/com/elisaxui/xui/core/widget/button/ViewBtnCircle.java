/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
public class ViewBtnCircle extends XHTMLPart {

	public static final String PROPERTY_ICON = "PROPERTY_ICON";

	CSSClass cBtnCircle;
	CSSClass cBtnCircleChangeForm;

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on(cBtnCircle,
						"border-radius: 50%;  font-size: 24px;  height: 56px;  margin: auto;   cursor: pointer; "
								+ "min-width: 56px;  width: 56px; padding: 0;  overflow: hidden; outline: 0 !important; " // pas																						// focus
								+ ScnStandard.bgColor
								+ "box-shadow: 0 1px 1.5px 0 rgba(0,0,0,.12), 3px 3px 3px 0 rgba(0,0,0,.24);"
								+ " position: relative; line-height: normal; border:none; background-color:#ee6e73;")
				
				.on(CSSSelector.onPath(cBtnCircle, cBtnCircleChangeForm), "transition: all  600ms ease-out; border-radius: 25%; width: 415px; transform: translate3d(15px,-661px,0px);")
				.on(CSSSelector.onPath(cBtnCircle, " .material-icons"), "color:white; position: absolute; top: 50%;  left: 50%;"
						+ " transform: translate(-12px,-12px);  line-height: 24px;  width: 24px;");
	}

	@xTarget(CONTENT.class)
	public Element xBurgerBtn() {
		return  xListElement( xPart(new ViewRippleEffect()),   ////////////////// IMPORT //////////////////
		       xElement("button", ViewRippleEffect.cRippleEffect(), cBtnCircle, xAttr("type", "'button'"),
				"<i class='material-icons'>", this.getProperty(PROPERTY_ICON), "</i>")
		       );
	}

}
