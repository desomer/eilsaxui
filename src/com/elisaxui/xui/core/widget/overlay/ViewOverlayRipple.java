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
import static  com.elisaxui.xui.core.transition.CssTransition.*;
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
					+ "width: 100vw;  height: 100vh; background: "+ ScnStandard.bgColorThemeOpacity +"; "
					)
				
				.on(".ripple_overlay.transitionx2","transition: "+SPEED_ACTIVITY_TRANSITION_EFFECT*2+"ms;")
				.on(".ripple_overlay.transition","transition: "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms;")
				
				.on(".ripple_overlay.t0prct","clip-path: circle(0px at 90vw 95vh);  -webkit-clip-path: circle(0px at 90vw 95vh);  ")
				.on(".ripple_overlay.t100prct","clip-path: circle(100% at center);-webkit-clip-path: circle(100% at center);")
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
