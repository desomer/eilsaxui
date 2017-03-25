package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

@xComment("un item")
public class ViewItem extends XHTMLPart {
	
	@xTarget(CONTENT.class)
	public Element xListe() {
		return xDiv("ViewItem", vHandle("test"));
	}
}
