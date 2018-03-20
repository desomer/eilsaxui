/**
 * 
 */
package com.elisaxui.component.widget.layout;

import com.elisaxui.component.transition.CssTransition;
import com.elisaxui.component.widget.menu.ViewMenu;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */

//***************Centree horizontalement et taillé 
//width: 100%;
//max-width: 480px;
//left: calc(50% - 480px/2);
//position: relative;

public class ViewPageLayout extends XHTMLPart { 
	
	public static CSSClass cArticle;
	public static CSSClass content;
	
	public static final String ID = "ID";
	
	public ViewPageLayout(Object id) {
		super();
		this.vProperty(ID, id);
	}

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return cStyle().path(cArticle)
						.set("overflow:hidden;")
		;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		return xDiv(xId(vProperty(ID)), CssTransition.activity, CssTransition.inactive, CssTransition.cStateNoDisplay 
				, vPart(new ViewNavBar().vProperty(ViewNavBar.PROPERTY_NAME, "NavBar"+vProperty(ID)))
				, xDiv(content 	
						, xDiv(cArticle, vSearchProperty("children"+vProperty(ID)))
						, vPart(new ViewOverlay())
						)
				, vPart(new ViewTabBar().vProperty(ViewNavBar.PROPERTY_NAME, "TabBar"+vProperty(ID)) ,
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'schedule'", "''")),  
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'today'", "''")),  
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'mic'", "''")) , 
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'apps'", "''"))
						)
		     );
	}

}
