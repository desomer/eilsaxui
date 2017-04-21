/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenuDivider;
import com.elisaxui.xui.core.widget.menu.ViewMenuItems;

/**
 * @author Bureau
 *
 */
@xComment("ViewNavBar")
public class ViewNavBar extends XHTMLPart {
	
	static CSSClass actionBtnContainer;
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()
				.on(".navbar","z-index: 2;"+ScnStandard.bgColor+"height: "+ScnStandard.heightNavBar+"px;width: 100%; color:white; "
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.on(".fixedTop", "position:fixed; top:0px; transform:translate3d(0px,0px,0px)")
				.on(".fixedTop2", "position:absolute;")  // pas de scroll
				
				.on(".rightAction", "position: absolute; right: 0px;  top: 0px;  height: 100%;  width: auto;")
				.on(".actionBtn", "margin: 0; padding: 8px;  font-size: 36px !important;  cursor: pointer;")
				
				.on(".center", "height:100%; display: flex; align-items: center;justify-content: center")
				.on(".logo", "color: inherit; font-size: 2.1rem; animation-duration: 700ms;")
				
				.on(actionBtnContainer, "cursor: pointer; position: relative; background-color: Transparent; color:white;"
						+ "padding: 0;  overflow: hidden; outline: 0 !important; " // pas de bordure au focus
						+ "border:none")
				;
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
		return xListElement(
				xImport(JSNavBar.class));
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "\"navbar fixedTop\""), this.getChildren());
	}
	
	public static Element getTemplateBtnBurger() {
		return xListElement(xPart(new ViewBtnBurger()));
	}
	
	public static Element getTemplateActionBar() {
		return xDiv(xAttr("class", "\"rightAction\""));
	}
	
	public static Element getTemplateName(Object name) {
		return xDiv(xAttr("class", "\"center\""), xDiv(xAttr("class", "\"logo\""), xVar(name)));
	}
	
	public static Element getTemplateAction(Object name, Object action) {
		return xElement("button", xAttr("data-x-action", txt(xVar(action))), actionBtnContainer, ViewRippleEffect.cRippleEffect() , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
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