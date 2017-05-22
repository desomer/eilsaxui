/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
public class ViewOverlayRipple extends XHTMLPart {

	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()
				.on(".ripple_overlay","position: absolute;	"
					+ "top: 0px;    left: 0px; "
					+ "width: 100vw;  height: 100vh; background: "+ ScnStandard.bgColorTheme +"; "
					+ "clip-path: circle(0px at 90vw 95vh);  -webkit-clip-path: circle(0px at 90vw 95vh);  "
					+ "transition: "+ScnStandard.SPEED_SHOW_ACTIVITY+"ms;"
					)
				
				.on(".ripple_overlay.anim","clip-path: circle(100% at center);-webkit-clip-path: circle(100% at center)")
				;
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "\"ripple_overlay\""), this.getChildren());
	}
	
	public static Element xTemplate() {
			return xListElement( xPart( new ViewOverlayRipple()  ));	
	}

}
