/**
 * 
 */
package com.elisaxui.xui.core.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenu")
public class ViewMenu extends XHTMLPart {

	CSSClass cHeaderMenu;
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on(".menu", "z-index: 3;background-color: #ffffff;height: 120%;width: "+ScnStandard.widthMenu+"px; color:black;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				.on(".fixedLeft", "position:absolute; top:0px; transform:translate3d(-"+ScnStandard.widthMenu+"px,0px,0px)")
				.on(cHeaderMenu, "height:53px; "+ScnStandard.bgColorMenu)
				
				.on(".menu ul",  "padding-left: 0;  list-style-type: none; margin:0px;")
				
				.on(".menu li",  "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   "
						+ "cursor: pointer;   min-height: 50px; line-height: 21px; "
						+ "width: 100%;   text-align: left;   text-transform: none;"
					)
				
				.on(".menu li>a",  "font-size: 16px;  line-height: 26px;  padding: 14px 16px;  float: left;")
				.on(".menu li>a>i", "margin: 0 16px 0 0; float: left;")
				.on(".menu li.cDivider", "min-height: 0;height: 1px; background-color: #e0e0e0;")  //overflow: hidden; 
				;
	}


	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'menu fixedLeft'"), 
				xDiv( xDiv(xAttr("class", "'"+cHeaderMenu.getId()+"'")), 
						xUl(  this.getChildren())
						)
			);
	}

}
