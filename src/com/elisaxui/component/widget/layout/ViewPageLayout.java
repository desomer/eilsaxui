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
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;

/**
 * @author Bureau
 *
 */

//***************Centree horizontalement et taillé 
//width: 100%;
//max-width: 480px;
//left: calc(50% - 480px/2);
//position: relative;

public class ViewPageLayout extends XHTMLPart implements ICSSBuilder { 
		
	public static final String ID = "ID";
	
	private static CSSClass cArticle;
	@xComment("content")
	private static CSSClass cContent;

	public static final CSSClass getcContent() {
		return cContent;
	}
	
	public static final CSSClass getcArticle() {
		return cArticle;
	}
	
	private boolean addDefault = true;
	
	public ViewPageLayout(Object id, boolean defaut) {
		super();
		this.vProperty(ID, id);
		this.addDefault = defaut;
	}

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {
		return xStyle(sMedia("all"), () -> {
			sOn(sSel(cArticle), ()-> css("overflow:hidden;"));
		});
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		
		XMLElement cont = null;
		/***********************************************************************************/		
		if (addDefault)
			cont=xListNode(
				xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'schedule'", "''")),  
				xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'today'", "''")),  
				xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'mic'", "''")) , 
				xLi (ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, ViewTabBar.getTemplateAction("'apps'", "''"))
				);
		else
			cont=xListNode();
		/**********************************************************************************/
		return xDiv(xId(vProperty(ID)), CssTransition.activity, CssTransition.inactive, CssTransition.cStateNoDisplay 
				, vPart(new ViewNavBar().vProperty(ViewNavBar.PROPERTY_NAME, "NavBar"+vProperty(ID)))
				, xDiv(cContent 	
						, xDiv(cArticle, vProperty("children"+vProperty(ID)))
						, vPart(new ViewOverlay())
						)
				, vPart(new ViewTabBar().vProperty(ViewTabBar.PROPERTY_NAME, "TabBar"+vProperty(ID)) , cont	)
		     );
	}

}
