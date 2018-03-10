/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.Arrays;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * 
 * permet d ajouter un xPart exemple xPart(new ViewRippleEffect())
 * 
 * @author gauth
 *
 */
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
		for (XMLElement elem : part.getListElementFromTarget(CONTENT.class)) {
			elem.toXML(buf);
		}
		buf.after = true;
		for (XMLElement elem : part.getListElementFromTarget(AFTER_CONTENT.class)) {
			elem.toXML(buf);
		}
		buf.after = false;
		XUIFactoryXHtml.getXHTMLFile().listParent.removeLast();
		return buf;
	}
}