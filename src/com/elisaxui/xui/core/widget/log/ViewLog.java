/**
 * 
 */
package com.elisaxui.xui.core.widget.log;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewLog extends XHTMLPart {

	public static XClass cLog;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle().path(cLog)
						.add("min-height:20vh; line-height: 45px;")
						.childPath(xStyle().path("textarea").add("width:100%"))
						;
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(  cLog , xTextArea(xAttr("aria-label", txt("log")),  xAttr("rows", 10)) );  
	}
	
}
