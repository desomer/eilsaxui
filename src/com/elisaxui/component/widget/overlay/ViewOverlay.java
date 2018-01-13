/**
 * 
 */
package com.elisaxui.component.widget.overlay;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;;

/**
 * @author Bureau
 *
 */	
@xComment("ViewOverlay")
public class ViewOverlay extends XHTMLPart {

	public static XClass cBlackOverlay;
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {
		
		
		
		return xStyle()
				.path(cBlackOverlay).add("display: none;"
						+ "position: absolute;	"
						+ "top: 0px; left: 0px;"
						+ " width:100vw ; height:101vh;" 
						+ "background-color: black;"
						+ XUIScene.PREFORM_CHANGE_OPACITY
						+ "opacity:0; z-index:"+XUIScene.ZINDEX_OVERLAY+";")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xIdAction(txt("Overlay")), cBlackOverlay, this.getChildren());
	}
	
}
