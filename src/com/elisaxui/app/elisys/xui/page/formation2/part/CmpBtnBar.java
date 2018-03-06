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
public class CmpBtnBar extends XHTMLPart {

	@xTarget(CONTENT.class)
	public XMLElement xBar() {
		return xDiv( xAttr("style", txt("height:auto;border: 1px black solid;")), this.getChildren() );
	}
	
}
