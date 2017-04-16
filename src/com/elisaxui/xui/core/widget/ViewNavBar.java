/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
@xComment("ViewNavBar")
public class ViewNavBar extends XHTMLPart {
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()
				.on(".navbar","z-index: 2;"+ScnStandard.bgColor+"height: "+ScnStandard.heightNavBar+"px;width: 100%; color:white; "
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.on(".fixedTop", "position:fixed; top:0px; transform:translate3d(0px,0px,0px)")
				.on(".fixedTop2", "position:absolute;")  // pas de scroll
				
				.on(".rightAction", "position: absolute; right: 0px;  top: 0px;  height: 100%;  width: auto;")
				.on(".actionBtn", "margin: 0; padding: 8px;  font-size: 36px;  cursor: pointer;")
				.on(".rightAction a", "display: inline-block")
				;
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'navbar fixedTop'"), this.getChildren());
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
