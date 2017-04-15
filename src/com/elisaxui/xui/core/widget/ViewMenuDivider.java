/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

/**
 * @author Bureau
 *
 */
public class ViewMenuDivider extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xLi(xAttr("class","'cDivider'"));
	}
}
