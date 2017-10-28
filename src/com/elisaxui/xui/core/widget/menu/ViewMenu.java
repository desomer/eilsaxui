/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.admin.test.JSTest2Class;
import com.elisaxui.xui.admin.test.JSTestClass;
import com.elisaxui.xui.admin.test.JSTestDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataCtx;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.transition.CssTransition;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenu")
public class ViewMenu extends XHTMLPart {

	
//	public static ViewMenu style =new ViewMenu();
//	static{
//	  xPart(style); 
//	}
	
//	@xComment("menu")
	public static XClass menu;

	@xComment("fixedLeft")
	XClass cFixedLeft;

	XClass cHeaderMenu;

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss()
				.on(menu, "z-index: "+XUIScene.ZINDEX_MENU+";background-color: #ffffff;height: 120%;width: " + XUIScene.widthMenu
						+ "px; color:black;"
						+ XUIScene.PREF_3D
						//+ "box-shadow: 4px 4px 2px 0 rgba(0,0,0,0.14);"
						+ "will-change:transform;")
				.on(cFixedLeft,
						"position:absolute; top:0px; transform:translate3d(-" + (XUIScene.widthMenu+5) + "px,0px,0px)")
				.on(cHeaderMenu, "height:53px; " + XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorMenu())

				.on(".menu ul", "padding-left: 0;  list-style-type: none; margin:0px;")

				.on(".menu li", "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   "
						+ "cursor: pointer;   min-height: 50px; line-height: 21px; "
						+ "width: 100%;   text-align: left;   text-transform: none;"
						+ XUIScene.PREF_3D
						+ "animation-duration:" + CssTransition.SPEED_SHOW_MENU_ITEMS + "ms; transform:translate3d(0px,0px,0px);will-change:transform;"
						)

				.on(".menu li>a", "font-size: 16px;  line-height: 26px;  padding: 14px 16px;  float: left;")
				.on(".menu li>a>i", "margin: 0 16px 0 0; float: left;")
				.on(".menu li.cDivider", "min-height: 0;height: 1px; background-color: #e0e0e0;") // overflow:
																									// hidden;
		;
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(menu, cFixedLeft,
				xDiv(xDiv(cHeaderMenu),
						xUl(this.getChildren())));
	}

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xImportAllClass() {
		return xListElement(
			//	xImport(JSMenu.class)
				);
	}


	public static XMLElement getTemplateMenu(Object name, Object icon, Object idAction) {
		return xListElement(xPart(new ViewMenuItems()
				.addProperty(ViewMenuItems.PROPERTY_NAME, xVar(name))
				.addProperty(ViewMenuItems.PROPERTY_ICON, xVar(icon))
				.addProperty(ViewMenuItems.PROPERTY_ACTION, xVar(idAction))
				));
	}
	
	public static XMLElement getTemplateMenuDivider() {
		return xListElement(xPart(new ViewMenuDivider()));
	}
}
