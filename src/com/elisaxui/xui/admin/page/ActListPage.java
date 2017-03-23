package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPage.BODY;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.annotation.xVersion;

@xComment("activite liste des pages")
public class ActListPage extends XHTMLPart {

	@xTarget(BODY.class)
	@xRessource
	@xVersion("1.2")
	public Element xAddBody() {
		return xSpan("ca marche");
	}
	
	@xTarget(CONTENT.class)
	public Element xListe() {
		return xSpan("ok", xUl(this.getChildren()));
	}

	@xTarget(AFTER_CONTENT.class)
	@xRessource
	public Element xAddJS() {
		return xScriptJS("console.debug('ok super')");
	}

}
