/**
 * 
 */
package com.elisaxui.component.widget.layout;

import com.elisaxui.component.transition.CssTransition;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */

// ***************Centree horizontalement et taillé
// width: 100%;
// max-width: 480px;
// left: calc(50% - 480px/2);
// position: relative;

public class ViewPageLayout extends XHTMLPart implements ICSSBuilder {

	public static VProperty pIdPage;

	private static CSSClass cArticle;
	@xComment("content")
	private static CSSClass cContent;

	public static final CSSClass getcContent() {
		return cContent;
	}

	public static final CSSClass getcArticle() {
		return cArticle;
	}

	public ViewPageLayout(Object id) {
		super();
		this.vProp(pIdPage, id);
	}
	
	public ViewPageLayout() {
		super();
	}

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {
		return xStyle(() -> sOn(cArticle, () -> css("overflow:hidden;")));
	}

	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {

		/**********************************************************************************/
		return xDiv(xId(vProperty(pIdPage)), CssTransition.activity, CssTransition.inactive, CssTransition.cStateNoDisplay,
				vPart(new ViewNavBar().vProp(ViewNavBar.PROPERTY_NAME, vCalc("NavBar", vProperty(pIdPage)))),
				xDiv(cContent, xDiv(cArticle, vProperty(vCalc("children", vProperty(pIdPage)))), vPart(new ViewOverlay())),
				vPart(new ViewTabBar().vProp(ViewTabBar.PROPERTY_NAME, vCalc("TabBar", vProperty(pIdPage)))));
	}

}
