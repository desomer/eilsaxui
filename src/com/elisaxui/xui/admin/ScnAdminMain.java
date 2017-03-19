package com.elisaxui.xui.admin;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLBuilder.Tag;
import com.elisaxui.core.xui.xml.annotation.File;

@File(id="admin")
public class ScnAdminMain extends XUIViewXHtml {

	@Override
	public void doContent(XMLFile file) {
		
		Tag tag = xTag("div", xTag("h1", xAttr("id", "'test'"), "toto"));

		vBody(tag);
	}
}
