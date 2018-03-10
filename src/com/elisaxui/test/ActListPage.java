package com.elisaxui.test;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.annotation.xVersion;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

@xComment("activite liste des pages")
public class ActListPage extends XHTMLPart {

	public static final String PROPERTY_NAME = "name";

	@xTarget(BODY.class)
	@xRessource
	@xVersion("1.2")
	public XMLElement xAddBody() {
		return xSpan("ca marche target body ", this.vPropertyElement(PROPERTY_NAME));
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xListe() {
		return xButton("ok", xPart(new ViewItem()), xUl(this.getChildren()));
	}

	@xTarget(AFTER_CONTENT.class)
//	@xRessource
	public XMLElement xAddJS() {
		return xScriptJS(js().__("console.debug('ActListPage.xAddJS')"));
	}

}
