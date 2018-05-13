/**
 * 
 */
package com.elisaxui.component.widget.menu;

import static com.elisaxui.component.widget.button.CssRippleEffect.*;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewMenuItems extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	public static final String PROPERTY_ACTION = "PROPERTY_ACTION";
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xLi(cRippleEffect, cRippleEffectColorBack, xIdAction(this.vProperty(PROPERTY_ACTION)), xA("<i class=\"material-icons\">", this.vProperty(PROPERTY_ICON) ,"</i>", this.vProperty(PROPERTY_NAME) ));
	}
}
