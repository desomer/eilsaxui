/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;
import com.elisaxui.xui.core.widget.overlay.ViewOverlay;

/**
 * @author Bureau
 *
 */
public class ViewPageLayout extends XHTMLPart { 
	
	public static final String ID = "ID";
	
	public ViewPageLayout(Object id) {
		super();
		this.addProperty(ID, id);
	}

	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		return xDiv(xId(getProperty(ID)), xAttr("class", txt("activity inactive nodisplay"))
			//	, xPart(new ViewMenu())
				, xPart(new ViewNavBar().addProperty(ViewNavBar.PROPERTY_NAME, "NavBar"+getProperty(ID)))
				, xDiv(xAttr("class", txt("content")) 	
						, xDiv(xAttr("class", txt("article")), vHandle("children"+getProperty(ID)))
				, xPart(new ViewOverlay())
			   )	
		     );
	}

}
