package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

@xComment("un item")
public class ViewItem extends XHTMLPart {
	
	public static final String TEST_HANDLE = "testHandle";

	@xTarget(CONTENT.class)
	public XMLElement xListe() {
		return xDiv("ViewItem handle = ", vHandle(TEST_HANDLE));
	}
}
