/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;
import static  com.elisaxui.xui.core.transition.CssTransition.*;
import static  com.elisaxui.xui.core.widget.button.ViewRippleEffect.*;
/**
 * @author Bureau
 *
 */
@xComment("ViewBtnCircle")
public class ViewBtnCircle extends XHTMLPart {

	public static final String PROPERTY_ICON = "PROPERTY_ICON";

	public static XClass cBtnCircle;
	XClass cBtnCircleChangeForm;

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss()
				.on(cBtnCircle,
								"border-radius: 50%;  font-size: 24px; margin: auto;   cursor: pointer; "
						 		+ "height: 56px; width: 56px; "
								+ "min-width: 56px; padding: 0;  overflow: hidden; outline: 0 !important; " // pas																						// focus
								+ XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColor()
								+ "box-shadow: 3px 3px 3px 0 rgba(0,0,0,.24);"
								+ "transition:transform " + SPEED_SHOW_ACTIVITY +  "ms ease-out;"
								+ " position: relative; line-height: normal; border:none; background-color:#ee6e73;"
								//+ "-webkit-backface-visibility: hidden;"
								//+ "transform: scale3d(1,1,1); will-change: transform;"
								)
					
				.on(CSSSelector.onPath(cBtnCircle, " .material-icons"), "color:white; position: absolute; top: 50%;  left: 50%;"
						+ " transform: translate(-12px,-12px);  line-height: 24px;  width: 24px;")

	
//				.on(CSSSelector.onPath(cBtnCircle, cBtnCircleChangeForm), "transform: scale3d(40,40,1)")    //-webkit-backface-visibility: hidden; transform: scale3d(30,30,1);
//				.on(CSSSelector.onPath(cBtnCircle, cBtnCircleChangeForm, " .material-icons"), "display: none;")
	
				;
	}

	@xTarget(CONTENT.class)
	public XMLElement xBurgerBtn() {
		return  xListElement( xPart(new ViewRippleEffect()),   ////////////////// IMPORT //////////////////
		       xElement("button", cRippleEffect, cBtnCircle, xAttr("type", "\"button\""),
				"<i class=\"material-icons\">", this.getProperty(PROPERTY_ICON), "</i>")
		       );
	}

}
