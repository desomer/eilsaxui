package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.annotation.AfterContent;
import com.elisaxui.core.xui.xml.annotation.Comment;
import com.elisaxui.core.xui.xml.annotation.Content;
import com.elisaxui.core.xui.xml.annotation.File;

@File(id = "admin.html")
@Comment("activite d'admin")
public class ScnAdminMain extends XUIViewXHtml {

	@Content
	public Element xContenu()
	{
		return xDiv( xH1( xID("'test'"), "toto",
				xPart(new ActListPage(), 
						xLi("ligne1"), 
						xLi("ligne2"))));
	}
	
	@AfterContent
	public Element xAddJS()
	{
		return  xScriptJS("console.debug('admin')");
	}
}
