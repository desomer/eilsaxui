/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenu")
public class ViewMenu extends XHTMLPart {

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.add(".menu", "z-index: 3;background-color: #ffffff;height: 120%;width: 100px; color:black; transition:transform 100ms ease-out;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				.add(".fixedLeft", "position:absolute; top:0px; transform:translate3d(-100px,0px,0px)")
				;
	}

	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'menu fixedLeft'"), this.getChildren());
	}

}
