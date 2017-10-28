/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;;

/**
 * @author Bureau
 *
 */	
@xComment("ViewOverlay")
public class ViewOverlay extends XHTMLPart {

	public static XClass cBlackOverlay;
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()
				.select(cBlackOverlay).set("display: none;	position: absolute;	"
						+ "top: 0px;    left: 0px;   min-width:100% ; min-height:100%;"
						+ "background-color: black; will-change:opacity, display;"
						+ "opacity:0; z-index:"+XUIScene.ZINDEX_OVERLAY+";")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xIdAction(txt("Overlay")), cBlackOverlay, this.getChildren());
	}
	
}
