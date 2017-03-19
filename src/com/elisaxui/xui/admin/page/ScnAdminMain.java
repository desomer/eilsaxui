package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.File;

@File(id = "admin")
public class ScnAdminMain extends XUIViewXHtml {

	@Override
	public void doContent(XMLPart root) {

		Element tag = xDiv( xH1( xID("'test'"), "toto",
				xPart(new ActListPage(), 
						xLi("ligne1"), 
						xLi("ligne2"))));

		vBody(tag);
	}
}
