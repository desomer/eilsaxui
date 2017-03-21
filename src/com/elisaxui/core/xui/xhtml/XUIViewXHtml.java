package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLPart;

public abstract class XUIViewXHtml extends XMLPart {

	public final XMLPart vBody(Element body) {
		((XUIPageXHtml) XUIFactoryXHtml.getXMLRoot()).addPart(XUIPageXHtml.HtmlPart.BODY, body);
		return this;
	}

	public final XMLPart vAfterBody(Element elem) {
		((XUIPageXHtml) XUIFactoryXHtml.getXMLRoot()).addPart(XUIPageXHtml.HtmlPart.SCRIPT_AFTER_BODY, elem);
		return this;
	}

	public final Element xDiv(Object... inner) {
		return xElement("div", inner);
	}

	public final Element xSpan(Object... inner) {
		return xElement("span", inner);
	}

	public final Element xH1(Object... inner) {
		return xElement("h1", inner);
	}

	public final Element xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public final Element xLi(Object... inner) {
		return xElement("li", inner);
	}

	public final Attr xID(Object id) {
		Attr attr = xAttr("id", id);
		return attr;
	}

	public final Element xScriptJS(Object js) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}

}
