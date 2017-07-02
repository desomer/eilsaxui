/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLElement;

/**
 * @author Bureau
 *
 */
public class ViewMenuDivider extends XHTMLPart {

	@xComment("cDivider")
	CSSClass cDivider;
	
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xLi(cDivider);
	}
}
