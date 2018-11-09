/**
 * 
 */
package com.elisaxui.component.widget.overlay;

import static com.elisaxui.component.toolkit.transition.ConstTransition.PERFORM_CHANGE_OPACITY;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_ACTIVITY_TRANSITION_EFFECT;
import static com.elisaxui.component.toolkit.transition.ConstTransition.ZINDEX_OVERLAY;

import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
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

	public static final String START_POINT ="85vw 80vh";
	public static CSSClass ripple_overlay;
	public static CSSClass transitionOpacity;
	
	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {
		
		return cStyle()
				.path(ripple_overlay).set(""
					+ "position: absolute;"
					+ "top: 0px; left: 0px; "
					+ "width: 100vw;  height: 100vh; "
					+ "z-index:"+ZINDEX_OVERLAY+ ";"
					+ "background: "+ ((XUIScene)XUIFactoryXHtml.getXMLFile().getMainXMLPart()).getConfigScene().getBgColorTheme() +"; "
					+ "transition: all "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms linear;"
					+ PERFORM_CHANGE_OPACITY
					)

				.path(ripple_overlay.and(transitionOpacity))
			//		.set("transition: opacity "+SPEED_ACTIVITY_TRANSITION_EFFECT+"ms linear;")  //cubic-bezier(1, 0, 1, 1)
					.set("opacity:0;") 
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(ripple_overlay, this.getChildren());
	}
	
	@Deprecated
	public static XMLElement xTemplate() {
			return xListNodeStatic( vPart( new ViewOverlayRipple()  ));	
	}

}
