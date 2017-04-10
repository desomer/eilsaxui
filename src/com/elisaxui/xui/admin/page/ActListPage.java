package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLRoot.BODY;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.annotation.xVersion;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

@xComment("activite liste des pages")
public class ActListPage extends XHTMLPart {

	public static final String PROPERTY_NAME = "name";

	@xTarget(BODY.class)
	@xRessource
	@xVersion("1.2")
	public Element xAddBody() {
		return xSpan("ca marche target body ", this.getPropertyElement(PROPERTY_NAME));
	}
	
	@xTarget(CONTENT.class)
	public Element xListe() {
		return xButton("ok", xPart(new ViewItem()), xUl(this.getChildren()));
	}

	@xTarget(AFTER_CONTENT.class)
//	@xRessource
	public Element xAddJS() {
		return xScriptJS(js().__("console.debug('ActListPage.xAddJS')"));
	}

}
