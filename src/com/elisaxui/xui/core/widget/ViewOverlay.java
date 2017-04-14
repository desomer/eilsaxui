/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;;

/**
 * @author Bureau
 *
 */
@xComment("Overlay")
public class ViewOverlay extends XHTMLPart {

	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()
				.add(".black_overlay","display: none;	position: absolute;	"
						+ "top: 0px;    left: 0px;    right: 0px;    bottom: 0px; z-index:1;"
						+ "background-color: black;"
						+ "opacity:0;")
				;
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'black_overlay'"), this.getChildren());
	}
	
	/*
	 .black_overlay{
		}
	 * */
	
}
