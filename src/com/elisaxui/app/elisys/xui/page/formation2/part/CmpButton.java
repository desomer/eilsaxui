/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2.part;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
public class CmpButton extends XHTMLPart {

	public static final String PROPERTY_ICON = "PROPERTY_ICON";
	public static final String PROPERTY_LABEL = "PROPERTY_LABEL";
	
	@xTarget(CONTENT.class)
	public XMLElement xBtn() {
		return xButton(this.<Object>getProperty(PROPERTY_LABEL) );
	}
	
}
