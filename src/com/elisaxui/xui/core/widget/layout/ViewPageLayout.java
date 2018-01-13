/**
 * 
 */
package com.elisaxui.xui.core.widget.layout;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;
import com.elisaxui.xui.core.widget.overlay.ViewOverlay;
import com.elisaxui.xui.core.widget.tabbar.ViewTabBar;

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
	
	public static XClass cArticle;
	public static XClass content;
	
	public static final String ID = "ID";
	
	public ViewPageLayout(Object id) {
		super();
		this.addProperty(ID, id);
	}

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle().path(cArticle)
						.add("overflow:hidden;")
		;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		return xDiv(xId(getProperty(ID)), CssTransition.activity, CssTransition.inactive, CssTransition.cStateNoDisplay 
				, xPart(new ViewNavBar().addProperty(ViewNavBar.PROPERTY_NAME, "NavBar"+getProperty(ID)))
				, xDiv(content 	
						, xDiv(cArticle, vHandle("children"+getProperty(ID)))
						, xPart(new ViewOverlay())
						)
				, xPart(new ViewTabBar().addProperty(ViewNavBar.PROPERTY_NAME, "TabBar"+getProperty(ID)) ,
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'schedule'", "''")),  
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'today'", "''")),  
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'mic'", "''")) , 
						xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'apps'", "''"))
						)
		     );
	}

}
