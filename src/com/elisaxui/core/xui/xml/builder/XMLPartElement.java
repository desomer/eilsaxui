/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.Arrays;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;

public class XMLPartElement implements IXMLBuilder {
	XMLPart part;

	public XMLPartElement(XMLPart part, Object... inner) {
		this.part = part;
		this.part.getChildren().addAll(Arrays.asList(inner));
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		XUIFactoryXHtml.getXHTMLFile().listParent.add(this);
		buf.after = false;
		for (XMLElement elem : part.getListElement(CONTENT.class)) {
			elem.toXML(buf);
		}
		buf.after = true;
		for (XMLElement elem : part.getListElement(AFTER_CONTENT.class)) {
			elem.toXML(buf);
		}
		buf.after = false;
		XUIFactoryXHtml.getXHTMLFile().listParent.removeLast();
		return buf;
	}
}