/**
 * 
 */
package com.elisaxui.xui.core.widget.button;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;

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


	static XClass cFloatAction;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss().select(cFloatAction)
						.set("z-index:"+XUIScene.ZINDEX_FLOAT+"; position: fixed; left: calc(90vw - 40px); top: calc(95vh - 40px); "
								+ "transform: translate3d(0px,0px,0px);")
		;
	}
	
	@xTarget(CONTENT.class)
	public static XMLElement getTemplateBtnFloat() {
		return xDiv(cFloatAction, xIdAction("\"BtnFloatMain\""),	xPart(new ViewBtnCircle().addProperty(ViewBtnCircle.PROPERTY_ICON, "history")));
	}
	
	public static XMLElement getTemplate() {
		return xPart(new ViewFloatAction());
	}
	
}
