package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLBuilder;
import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLBuilder.Part;
import com.elisaxui.core.xui.xml.XMLPart;

public abstract class XUIViewXHtml extends XMLPart {
	
	public final XMLPart vBody(Element body)
	{
		
		StringBuilder content = new StringBuilder(1000);
		StringBuilder after = new StringBuilder(1000);
		XMLBuilder xml = new XMLBuilder(null, content, after);
		
		
		Part part = xPart( new XMLPart() {

			@Override
			public void doContent(XMLPart root) {
				vContent(body);
			}

		} );
		
		body.setNbInitialTab(2);
		part.toXML(xml);
		((XUIPageXHtml)XUIFactoryXHtml.getXMLRoot()).addPart(XUIPageXHtml.HtmlPart.BODY, content);
		((XUIPageXHtml)XUIFactoryXHtml.getXMLRoot()).addPart(XUIPageXHtml.HtmlPart.SCRIPT_AFTER_BODY, after);
		
		
		return this;
	}
	
	public final Element xDiv(Object... inner)
	{
		return xElement("div",inner);
	}

	public final Element xSpan(Object... inner)
	{
		return xElement("span",inner);
	}
	
	public final Element xH1(Object... inner)
	{
		return xElement("h1",inner);
	}
	
	public final Element xUl(Object... inner)
	{
		return xElement("ul",inner);
	}
	
	public final Element xLi(Object... inner)
	{
		return xElement("li",inner);
	}
	
	public final Attr xID(Object id)
	{
		Attr attr = xAttr("id", id);
		return attr;
	}
	
	public final Element xScriptJS(Object js)
	{
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}
	
}
