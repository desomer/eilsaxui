/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.container.ViewCard;

/**
 * @author Bureau
 *
 */
public class ViewFloatAction extends XHTMLPart {


///* Rules for sizing the icon. */
//.material-icons.md-18 { font-size: 18px; }
//.material-icons.md-24 { font-size: 24px; }
//.material-icons.md-36 { font-size: 36px; }
//.material-icons.md-48 { font-size: 48px; }
//
///* Rules for using icons as black on a light background. */
//.material-icons.md-dark { color: rgba(0, 0, 0, 0.54); }
//.material-icons.md-dark.md-inactive { color: rgba(0, 0, 0, 0.26); }
//
///* Rules for using icons as white on a dark background. */
//.material-icons.md-light { color: rgba(255, 255, 255, 1); }
//.material-icons.md-light.md-inactive { color: rgba(255, 255, 255, 0.3); }


	static CSSClass cFloatAction;
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on(cFloatAction, "z-index:"+ScnStandard.ZINDEX_FLOAT+"; position: fixed; left: calc(90vw - 40px); top: calc(95vh - 40px); transform: translate3d(0px,0px,0px);")
		;
	}
	
	@xTarget(CONTENT.class)
	public static Element getTemplateBtnFloat() {
		return xDiv(cFloatAction, xIdAction("\"BtnFloatMain\""),	xPart(new ViewBtnCircle().addProperty(ViewBtnCircle.PROPERTY_ICON, "history")));
	}
	
	public static Element getTemplate() {
		return xListElement(xPart(new ViewFloatAction() ));
	}
	
}
