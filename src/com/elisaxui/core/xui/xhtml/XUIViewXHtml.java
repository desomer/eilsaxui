package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.XUIFactoryXML;
import com.elisaxui.core.xui.xml.XMLBuilder;
import com.elisaxui.core.xui.xml.XMLBuilder.Tag;
import com.elisaxui.core.xui.xml.XMLPart;

public abstract class XUIViewXHtml extends XMLPart {

	public XMLPart vBody(Tag body)
	{
		
		StringBuilder b = new StringBuilder(1000);
		XMLBuilder buffer = new XMLBuilder(null, b);
		body.setNbInitialTab(1);
		body.toHtml(buffer);
		
		XUIFactoryXML.getXUIPageBuilder().addPart(XUIFileXHtml.HtmlPart.BODY, b);
		return this;
	}

}
