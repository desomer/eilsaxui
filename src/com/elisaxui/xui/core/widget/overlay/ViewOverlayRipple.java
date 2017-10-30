/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_ACTIVITY_TRANSITION_EFFECT;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
/**
 * @author Bureau
 *
 *
 *   pour gestion transition de type bubble overlay
 */
@xComment("ViewOverlayRipple")
public class ViewOverlayRipple extends XHTMLPart {

	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()
				.on(".ripple_overlay","position: absolute;	"
					+ "top: 0px;    left: 0px; "
					+ "width: 100vw;  height: 100vh; background: "+ XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorThemeOpacity() +"; "
					)
				
				.on(".ripple_overlay.transitionx2","transition:all  "+SPEED_ACTIVITY_TRANSITION_EFFECT*2+"ms cubic-bezier(1, 0, 1, 1);")
				.on(".ripple_overlay.transition","transition: all "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms cubic-bezier(1, 0, 1, 1);")
				
				.on(".ripple_overlay.t0prct","clip-path: circle(0px at 90vw 95vh);  -webkit-clip-path: circle(0px at 90vw 95vh);  ")
				.on(".ripple_overlay.t100prct","clip-path: circle(100% at center);-webkit-clip-path: circle(100% at center);")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xAttr("class", "\"ripple_overlay\""), this.getChildren());
	}
	
	public static XMLElement xTemplate() {
			return xListElement( xPart( new ViewOverlayRipple()  ));	
	}

}
