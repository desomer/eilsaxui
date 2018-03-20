/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;

/**
 * la ligne de style
 * @author gauth
 *
 */
public class CSSStyleRow extends XUIFormatManager implements IXMLBuilder {
	Object path;
	public final Object getPath() {
		return path;
	}

	Object content;

	@Override
	public String toString() {
		
		if (content==null)
			return null;
		
		if (path instanceof CSSClass)
			return  "."+((CSSClass)path).getId() + " { " +content +" }";
		else
			return  path + " {" +content +"}";
	}

	public CSSStyleRow(Object path, Object content) {
		super();
		this.path = path;
		this.content = content;
	}
	
	public XMLBuilder toXML(XMLBuilder buf)
	{
		String style = toString();
		if (style!=null)
		{
			newLine(buf);
			newTabInternal(buf);
			buf.addContentOnTarget(style);
		}
		return buf;
	}
	
	
}