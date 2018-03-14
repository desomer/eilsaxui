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
public interface IJSDomTemplate extends IXHTMLBuilder {
	XMLElement getTemplate();
	

	default JSDomTemplate createDomTemplate(XMLElement xmlElement)
	{
		return new JSDomTemplate(xmlElement).setModeJS(true);
		
	}



}
