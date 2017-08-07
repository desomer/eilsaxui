/**
 * 
 */
package com.elisaxui.xui.core.widget.loader;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author Bureau
 *
 */
public class ViewLoader extends XHTMLPart {

	
	static XClass cLoaderContainer;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss().select(cLoaderContainer)
						.set("color:white")
		;
	}
	
	@xTarget(CONTENT.class)
	public static XMLElement getTemplateBtnFloat() {
		return xDiv(cLoaderContainer, "Loading");
	}
	
	
	
}
