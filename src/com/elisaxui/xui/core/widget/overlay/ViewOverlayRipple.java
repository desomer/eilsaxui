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
	public static XClass transitionOpacity;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {
		
		return xStyle()
				.path(ripple_overlay).add(""
					+ "position: absolute;"
					+ "top: 0px; left: 0px; "
					+ "width: 100vw;  height: 100vh; "
					+ "z-index:"+XUIScene.ZINDEX_OVERLAY+ ";"
					+ "background: "+ XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorTheme() +"; "
					+ "transition: all "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms linear;"
					+ XUIScene.PREFORM_CHANGE_OPACITY
					)

				.path(ripple_overlay.and(transitionOpacity))
			//		.set("transition: opacity "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms linear;")  //cubic-bezier(1, 0, 1, 1)
					.add("opacity:0;") 
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(ripple_overlay, this.getChildren());
	}
	
	public static XMLElement xTemplate() {
			return xListElement( xPart( new ViewOverlayRipple()  ));	
	}

}
