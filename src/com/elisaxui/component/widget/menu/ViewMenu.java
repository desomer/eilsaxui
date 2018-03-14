/**
 * 
 */
package com.elisaxui.component.widget.menu;

import static com.elisaxui.component.transition.ConstTransition.*;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.toolkit.datadriven.JSDataCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.transition.CssTransition;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.test.JSTest2Class;
import com.elisaxui.test.JSTestClass;
import com.elisaxui.test.JSTestDataDriven;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenu")
public class ViewMenu extends XHTMLPart {

	public static CSSClass menu;

	@xComment("fixedLeft")
	CSSClass cFixedLeft;

	CSSClass cHeaderMenu;

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle()
				.on(menu, "z-index: "+XUIScene.ZINDEX_MENU+";background-color: #ffffff;height: 100vh;width: " + XUIScene.widthMenu
						+ "px; color:black;"
						+ XUIScene.PREFORM_3D
						//+ "box-shadow: 4px 4px 2px 0 rgba(0,0,0,0.14);"
						//+ "will-change:transform;"
						)
				.on(cFixedLeft,
						"position:absolute; top:0px; transform:translate3d(-" + (XUIScene.widthMenu+5) + "px,0px,0px)")
				.on(cHeaderMenu, "height:53px; background:" + ((XUIScene)XUIFactoryXHtml.getXHTMLFile().getScene()).getConfigScene().getBgColorMenu()
						+ "box-shadow: 0px 5px 13px 0px #969696;")

				.on(".menu ul", "padding-left: 0;  list-style-type: none; margin:0px;")

				.on(".menu li", "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   "
						+ "cursor: pointer;   min-height: 50px; line-height: 21px; "
						+ "width: 100%;   text-align: left;   text-transform: none;"
						+ XUIScene.PREFORM_3D
						+ "animation-duration:" + SPEED_SHOW_MENU_ITEMS + "ms;"
						)

				.on(".menu li>a", "font-size: 1rem;  line-height: 26px;  padding: 14px 16px;  float: left;")
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
		return xListNode(
			//	xImport(JSMenu.class)
				);
	}


	public static XMLElement getTemplateMenu(Object name, Object icon, Object idAction) {
		
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xListElement(vPart(new ViewMenuItems()
						.vProperty(ViewMenuItems.PROPERTY_NAME, xVar(name))
						.vProperty(ViewMenuItems.PROPERTY_ICON, xVar(icon))
						.vProperty(ViewMenuItems.PROPERTY_ACTION, xVar(idAction))
						));
			}
		};
		
		return template.getTemplate();
		
	}
	
	public static XMLElement getTemplateMenuDivider() {
		return xListElement(vPart(new ViewMenuDivider()));
	}
}
