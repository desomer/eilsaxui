/**
 * 
 */
package com.elisaxui.xui.core.widget.overlay;

import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_ACTIVITY_TRANSITION_EFFECT;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;
/**
 * @author Bureau
 *
 *
 *   pour gestion transition de type bubble overlay
 */
@xComment("ViewOverlayRipple")
public class ViewOverlayRipple extends XHTMLPart {

	public static final String START_POINT ="85vw 80vh";
	public static XClass ripple_overlay;
	public static XClass transition;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()
				.select(ripple_overlay).set(
					"position: absolute;"
					+ "top: 0px;    left: 0px; "
					+ "width: 100vw;  height: 100vh; "
					+ "z-index:"+XUIScene.ZINDEX_OVERLAY+ ";"
					+ "background: "+ XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorTheme() +"; "
					)
				
				//.on(".ripple_overlay.transitionx2","transition:all  "+SPEED_ACTIVITY_TRANSITION_EFFECT*2+"ms cubic-bezier(1, 0, 1, 1);")
				.select(ripple_overlay.and(transition)).set("transition: all "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms cubic-bezier(1, 0, 1, 1);")
				
			//	.on(".ripple_overlay.t0prct","clip-path: circle(0px at "+START_POINT+");  -webkit-clip-path: circle(0px at "+START_POINT+");  ")
			//	.on(".ripple_overlay.t100prct","clip-path: circle(80% at center);-webkit-clip-path: circle(80% at center);")
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
