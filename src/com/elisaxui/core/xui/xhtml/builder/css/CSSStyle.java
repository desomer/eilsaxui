/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;

public final class CSSStyle extends XUIFormatManager implements IXMLBuilder {
	Object path;
	Object content;

	@Override
	public String toString() {
		if (path instanceof XClass)
			return  "."+((XClass)path).getId() + " { " +content +" }";
		else
			return  path + " {" +content +"}";
	}

	public CSSStyle(Object path, Object content) {
		super();
		this.path = path;
		this.content = content;
	}
	
	public XMLBuilder toXML(XMLBuilder buf)
	{
		newLine(buf);
		newTabulation(buf);
		buf.addContent(toString());
		return buf;
	}
	
	
}