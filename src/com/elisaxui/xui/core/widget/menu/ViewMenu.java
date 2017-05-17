/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataCtx;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.admin.page.JSTest2Class;
import com.elisaxui.xui.admin.page.JSTestClass;
import com.elisaxui.xui.admin.page.JSTestDataDriven;
import com.elisaxui.xui.core.page.ScnStandard;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenu")
public class ViewMenu extends XHTMLPart {

	
	public static ViewMenu style =new ViewMenu();
	static{
	  xPart(style); 
	}
	
	@xComment("menu")
	public CSSClass cMenu;

	@xComment("fixedLeft")
	CSSClass cFixedLeft;

	CSSClass cHeaderMenu;

	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on(cMenu, "z-index: "+ScnStandard.ZINDEX_MENU+";background-color: #ffffff;height: 120%;width: " + ScnStandard.widthMenu
						+ "px; color:black;"
						+ ScnStandard.PREF_3D
						//+ "box-shadow: 4px 4px 2px 0 rgba(0,0,0,0.14);"
						+ "will-change:transform;")
				.on(cFixedLeft,
						"position:absolute; top:0px; transform:translate3d(-" + (ScnStandard.widthMenu+5) + "px,0px,0px)")
				.on(cHeaderMenu, "height:53px; " + ScnStandard.bgColorMenu)

				.on(".menu ul", "padding-left: 0;  list-style-type: none; margin:0px;")

				.on(".menu li", "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   "
						+ "cursor: pointer;   min-height: 50px; line-height: 21px; "
						+ "width: 100%;   text-align: left;   text-transform: none;"
						+ ScnStandard.PREF_3D
						+ "animation-duration:400ms; transform:translate3d(0px,0px,0px);will-change:transform;"
						)

				.on(".menu li>a", "font-size: 16px;  line-height: 26px;  padding: 14px 16px;  float: left;")
				.on(".menu li>a>i", "margin: 0 16px 0 0; float: left;")
				.on(".menu li.cDivider", "min-height: 0;height: 1px; background-color: #e0e0e0;") // overflow:
																									// hidden;
		;
	}

	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(cMenu, cFixedLeft,
				xDiv(xDiv(cHeaderMenu),
						xUl(this.getChildren())));
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
		return xListElement(
			//	xImport(JSMenu.class)
				);
	}


	public static Element getTemplateMenu(Object name, Object icon, Object idAction) {
		return xListElement(xPart(new ViewMenuItems()
				.addProperty(ViewMenuItems.PROPERTY_NAME, xVar(name))
				.addProperty(ViewMenuItems.PROPERTY_ICON, xVar(icon))
				.addProperty(ViewMenuItems.PROPERTY_ACTION, xVar(idAction))
				));
	}
	
	public static Element getTemplateMenuDivider() {
		return xListElement(xPart(new ViewMenuDivider()));
	}
}
