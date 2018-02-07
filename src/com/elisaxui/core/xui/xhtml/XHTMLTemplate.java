/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface XHTMLTemplate extends XHTMLElement {
	XMLElement getTemplate();
	
	// TODO a terminer
	default XMLElement getTemplateByName(String name) {
		return null;
	}
}
