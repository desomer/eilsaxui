/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLElement;
import com.elisaxui.xui.core.page.ScnStandard;;

/**
 * @author Bureau
 *
 */	
@xComment("Overlay")
public class ViewOverlay extends XHTMLPart {

	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()
				.on(".black_overlay","display: none;	position: absolute;	"
						+ "top: 0px;    left: 0px;   min-width:100% ; min-height:100%;"
						+ "background-color: black; will-change:opacity, display;"
						+ "opacity:0; z-index:"+ScnStandard.ZINDEX_OVERLAY+";")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xIdAction(txt("Overlay")), xAttr("class", txt("black_overlay")), this.getChildren());
	}
	
}
