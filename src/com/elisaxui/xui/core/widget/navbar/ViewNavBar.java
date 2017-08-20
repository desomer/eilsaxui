/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenuDivider;
import com.elisaxui.xui.core.widget.menu.ViewMenuItems;
import static  com.elisaxui.xui.core.widget.button.ViewRippleEffect.*;
/**
 * @author Bureau
 *
 */
@xComment("ViewNavBar")
public class ViewNavBar extends XHTMLPart {
	
	static XClass actionBtnContainer;
	static XClass animatedBg;
	public static XClass fixedToAbsolute;
	
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()    //"+ScnStandard.bgColor+"
				.on(".navbar","z-index: "+XUIScene.ZINDEX_NAV_BAR+";  "+XUIScene.bgColor+" height: "+XUIScene.heightNavBar+"px;width: 100%; color:white; "
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.on(".fixedTop", "position:fixed; top:0px; transform:translate3d(0px,0px,0px); backface-visibility: hidden;")
				.on(fixedToAbsolute, "position:absolute;")  // permet de deplacement
				
				.on(".rightAction", "position: absolute; right: 0px;  top: 0px;  height: 100%;  width: auto;")
				.on(".actionBtn", "margin: 0; padding: 8px;  font-size: 36px !important;  cursor: pointer;")
				
				.on(".center", "z-index:"+(XUIScene.ZINDEX_NAV_BAR+1)+";height:100%; display: flex; align-items: center;justify-content: center")
				.on(".logo", "z-index:"+(XUIScene.ZINDEX_NAV_BAR+1)+"; margin-top:" + XUIScene.heightNavBar/2 +"px; color: inherit; font-size: 2.1rem; animation-duration: 700ms;")
				
				.on(animatedBg, "position: absolute; display: block;  width: 100%; height: 100%; top: 0; right: 0; bottom: 0; left: 0;")
				
				.on(actionBtnContainer, "cursor: pointer; position: relative; background-color: Transparent; color:white;"
						+ "padding: 0;  overflow: hidden; outline: 0 !important; " // pas de bordure au focus
						+ "border:none")
				;
	}
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xImportAllClass() {
		return	xImport(JSNavBar.class);
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xHeader( xId(this.getProperty(PROPERTY_NAME)), xAttr("class", "\"navbar fixedTop\""),
				this.getChildren());
	}
	
	public static XMLElement getTemplateBtnBurger() {
		return xListElement(xPart(new ViewBtnBurger()));
	}
	
	public static XMLElement getTemplateActionBar() {
		return xDiv(xAttr("class", "\"rightAction\""));
	}
	
	public static XMLElement getTemplateName(Object name) {
		return xDiv(xAttr("class", "\"center\""), xDiv(xAttr("class", "\"logo\""), xVar(name)));
	}
	
	public static XMLElement getTemplateAction(Object name, Object action) {
		return xElement("button", xAttr("data-x-action", txt(xVar(action))), actionBtnContainer, cRippleEffect , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
	}
	
	public static XMLElement getTemplateBgCanvas() {
		return xElement("canvas", animatedBg); // pour granim
	}
	
	public static XMLElement getTemplateBgDiv() {
		return xElement("div", animatedBg); 
	}
	
//	@xTarget(AFTER_CONTENT.class)
//	public Element xAddJS() {
//		return xScriptJS(js().__("$('body').on('scroll',", fct("e")
//				.consoleDebug("e.currentTarget.scrollTop", "e") 
//				//.__("$('.fixedTop').css('transform', 'translate3d(0px,'+e.currentTarget.scrollTop+'px,0px)' )")
//				,")")
//			);
//	}
	

}
