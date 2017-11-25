/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_SHOW_ACTIVITY;
import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffect;

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
/**
 * @author Bureau
 *
 */
@xComment("ViewBtnCircle")
public class ViewBtnCircle extends XHTMLPart {

	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	public static final String SIZE_CIRCLE = "3.5rem" ;
	public static final String SIZE_BTN = "2.5rem" ;
	//public static final String SIZE_BTN_2 = "1.25rem" ;

	public static XClass cBtnCircle;
	XClass cBtnCircleChangeForm;

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss()
				.on(cBtnCircle,
								"border-radius: 50%;  "
								//+ "font-size: 24px; "
								+ "margin: auto;   cursor: pointer; "
						 		+ "height: "+SIZE_CIRCLE+"; width: "+SIZE_CIRCLE+"; "
								//+ "/*min-width: 56px;*/ "
								+ "padding: 0;  overflow: hidden; outline: 0 !important; " // pas																						// focus
								+ XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()
								+ "box-shadow: 3px 3px 3px 0 rgba(0,0,0,.24);"
								+ "transition:transform " + SPEED_SHOW_ACTIVITY +  "ms ease-out;"
								+ " position: relative; line-height: normal; border:none; background-color:#ee6e73;"
								//+ "-webkit-backface-visibility: hidden;"
								//+ "transform: scale3d(1,1,1); will-change: transform;"
								)
					
				.on(CSSSelector.onPath(cBtnCircle, " .material-icons"), "color:white;"
					//	+ " position: absolute;"
					//	+ " top: 50%;  left: 50%;"
						+ " transform: translate(-1px, 1px); "
					//	+ " line-height: 24px;  width: 24px; "
						+ "font-size:"+SIZE_BTN)

	
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
