/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.component.page.CssReset;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPage")
@xComment("Scene Page")
public class ScnPage extends XHTMLPart {

	@xTarget(HEADER.class) // la vue App Shell
	@xResource()
	public XMLElement sStyle() {
		return vPart(new CssReset());
	}
	
	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		vProperty(ViewTabBar.pStyleViewTabBar, "background:linear-gradient(to bottom, rgba(0, 150, 136, 0.52) 0%, #009688 64%, #1d655e 100%)");
		vProperty(ViewNavBar.pStyleViewNavBar, "background:linear-gradient(to bottom, rgb(2, 140, 127) 0%, #009688 64%, #1d65657d 100%)");
		
		return vPart(new ViewPageLayout("Page", false));
	}

}
