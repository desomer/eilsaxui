/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.template;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xInLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IJSNodeTemplate extends JSClass, IXHTMLBuilder {

	@xInLine
	default JSNodeTemplate createNodeTemplate(XMLElement xmlElement) {
		return new JSNodeTemplate(xmlElement).setModeJS(true);
	}

}
