/**
 * 
 */
package com.elisaxui.xui.core.widget.log;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author Bureau
 *
 */
public class ViewLog extends XHTMLPart {

	public static XClass cLog;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss().select(cLog)
						.set("min-height:20vh; line-height: 45px;")
						.path(xCss().select("textarea").set("width:100%"))
						;
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(  cLog , xTextArea(xAttr("rows", 10)) );  
	}
	
}
