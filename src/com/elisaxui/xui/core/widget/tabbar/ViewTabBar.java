/**
 * 
 */
package com.elisaxui.xui.core.widget.tabbar;

import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffect;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;

/**
 * @author Bureau
 *    https://codepen.io/neillin1023/pen/NrKBbO
 *    http://www.cssscript.com/material-style-tabs-component-with-javascript-and-css/
 *    https://codepen.io/Touficbatache/pen/eERNdo
 */
public class ViewTabBar extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	public static XClass tabbar;
	public static XClass fixedBottom;
	
//	  display: block;
//	  float: left;
//	  padding: 16px;
//	  padding-top: 0;
//	  width: 100%;
//	  max-width: 480px;
//	  left: calc(50% - 480px/2);
//	  position: relative;
//	  margin: 96px auto;
//	  background: #fff;
//	  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23) !important;
//	  border-radius: 2px;

	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()    //"+ScnStandard.bgColor+"
				.select(tabbar).set("z-index: "+XUIScene.ZINDEX_NAV_BAR+";  "+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()+" height: "+XUIScene.heightTabBar+"px;width: 100%; color:white; "
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.select(fixedBottom).set("position:fixed; bottom:0px; transform:translate3d(0px,0px,0px); backface-visibility: hidden;")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xFooter( xId(this.getProperty(PROPERTY_NAME)), tabbar, fixedBottom,
				this.getChildren());
	}
	
	public static XMLElement getTemplateAction(Object name, Object action) {
		return xElement("button", xAttr("data-x-action", txt(xVar(action)))/*, actionBtnContainer*/, cRippleEffect , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
	}
}
