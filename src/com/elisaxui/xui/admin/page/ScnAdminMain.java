package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPage.HEADER;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xTarget;

@xFile(id = "admin.html")
@xComment("activite d'admin")
public class ScnAdminMain extends XHTMLPart {

	@xTarget(HEADER.class)
	public Element xTitle()
	{
		return xElement("title", "un titre");
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu()
	{
		return xDiv( xH1( xID("'test'"), "toto",
				xPart(new ActListPage(), 
						xLi("ligne1"), 
						xLi("ligne2"))));
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu2()
	{
		return xDiv( xH1( xID("'test'"), "sdsdsd",
				xPart(new ActListPage(), 
						xLi("ligne5"), 
						xLi("ligne5"))));
	}
	
	@xTarget()
	public void xContent()
	{
		vBody(xElement(null, "<!--vBody-->"));
		vAfterBody(xElement(null,"<!--vAfterBody-->"));
		vContent(xElement(null,"<!--vContent-->"));
		vAfterContent(xElement(null,"<!--vAfterContent-->"));
	}
	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS()
	{
		return  xScriptJS("console.debug('admin')");
	}
}
