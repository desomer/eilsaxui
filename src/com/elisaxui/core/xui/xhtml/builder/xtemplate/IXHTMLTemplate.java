/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IXHTMLTemplate extends IXHTMLBuilder {
	XMLElement getTemplate();
	

	default XHTMLTemplate jsTemplate(XMLElement xmlElement)
	{
		return new XHTMLTemplate(xmlElement).setModeJS(true);
		
	}



}
