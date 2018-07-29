/**
 * 
 */
package com.elisaxui.component.widget.overlay;

import static com.elisaxui.component.toolkit.transition.ConstTransition.PERFORM_CHANGE_OPACITY;
import static com.elisaxui.component.toolkit.transition.ConstTransition.ZINDEX_OVERLAY;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;;

/**
 * @author Bureau
 *
 */	
@xComment("ViewOverlay")
public class ViewOverlay extends XHTMLPart {

	public static CSSClass cBlackOverlay;
	
	@xTarget(AFTER_BODY.class)
	@xResource()
	public XMLElement xStylePart() {
		return cStyle()
				.path(cBlackOverlay).set("display: none;"
						+ "position: absolute;	"
						+ "top: 0px; left: 0px;"
						+ " width:100vw ; height:101vh;" 
						+ "background-color: black;"
						+ PERFORM_CHANGE_OPACITY
						+ "opacity:0; z-index:"+ZINDEX_OVERLAY+";")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xIdAction(txt("Overlay")), cBlackOverlay, this.getChildren());
	}
	
}
