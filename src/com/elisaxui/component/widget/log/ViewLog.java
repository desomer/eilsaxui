/**
 * 
 */
package com.elisaxui.component.widget.log;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewLog extends XHTMLPart {

	public static CSSClass cLog;
	
	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {

		return cStyle().path(cLog)
						.set("min-height:20vh; line-height: 45px;")
						.andChild(cStyle().path("textarea").set("width:100%"))
						;
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(  cLog , xTextArea(xAttr("aria-label", txt("log")),  xAttr("rows", 10)) );  
	}
	
}
