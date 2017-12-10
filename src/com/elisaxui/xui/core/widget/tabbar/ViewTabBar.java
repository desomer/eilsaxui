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
import com.elisaxui.xui.core.transition.ConstTransition;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;

/**
 * @author Bureau
 *    https://codepen.io/neillin1023/pen/NrKBbO
 *    http://www.cssscript.com/material-style-tabs-component-with-javascript-and-css/
 *    https://codepen.io/Touficbatache/pen/eERNdo
 */
public class ViewTabBar extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	public static XClass cTabbar;
	public static XClass cFixedBottom;
	private static XClass cListReset;
	private static XClass cFlex;
	public static XClass cFlex_1;
	public static XClass cTextAlignCenter;
	
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
				.select(cTabbar).set("z-index: "+XUIScene.ZINDEX_NAV_BAR+";"
						+ "background:"+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()+";"
						+ "height: "+XUIScene.heightTabBar+";"
						+ "width: "+XUIScene.widthScene+"; "
						+ "color:white; "
						+ "transition: transform "+ConstTransition.SPEED_ANIM_SCROLL+"ms ease-in-out;" 
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.select(cFixedBottom).set("position:fixed; bottom:0px; "+XUIScene.PREFORM_3D)
				.select(cListReset).set("list-style: none;margin: 0; padding: 0;")
				.select(cFlex).set("display:flex;")
				.select(cFlex_1).set("flex:1;")
				.select(cTextAlignCenter).set("text-align: center;")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xFooter( xId(this.getProperty(PROPERTY_NAME)), cTabbar, cFixedBottom,
				xUl(cListReset, cFlex, this.getChildren()));
	}
	
	public static XMLElement getTemplateAction(Object name, Object action) {
		return xElement("button", xAttr("data-x-action", txt(xVar(action))), ViewNavBar.actionBtnContainer, cRippleEffect , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
	}
}
