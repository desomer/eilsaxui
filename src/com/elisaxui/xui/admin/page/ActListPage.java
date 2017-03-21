package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.annotation.AfterContent;
import com.elisaxui.core.xui.xml.annotation.Comment;
import com.elisaxui.core.xui.xml.annotation.Content;

@Comment("activite liste des pages")
public class ActListPage extends XUIViewXHtml {

	@Content
	public Element xListe() {
		return xSpan("ok", xUl(this.getChildren()));
	}

	@AfterContent
	public Element xAddJS() {
		return xScriptJS("console.debug('ok super')");
	}

}
