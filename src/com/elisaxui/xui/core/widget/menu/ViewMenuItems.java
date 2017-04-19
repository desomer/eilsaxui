/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

/**
 * @author Bureau
 *
 */
public class ViewMenuItems extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	public static final String PROPERTY_ACTION = "PROPERTY_ACTION";
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xLi(xIdAction(this.getProperty(PROPERTY_ACTION)), xA("<i class=\"material-icons\">", this.getProperty(PROPERTY_ICON) ,"</i>", this.getProperty(PROPERTY_NAME) ));
	}
}
