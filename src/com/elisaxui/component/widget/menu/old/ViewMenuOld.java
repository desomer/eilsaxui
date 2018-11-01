/**
 * 
 */
package com.elisaxui.component.widget.menu.old;

import static com.elisaxui.component.page.CssPage.widthMenu;
import static com.elisaxui.component.toolkit.transition.ConstTransition.PERFORM_3D;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_MENU_ITEMS;
import static com.elisaxui.component.toolkit.transition.ConstTransition.ZINDEX_MENU;

import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.component.widget.menu.ViewMenuDivider;
import com.elisaxui.component.widget.menu.ViewMenuItems;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
@xComment("ViewMenuOld")
@Deprecated
public class ViewMenuOld extends XHTMLPart {

	public static CSSClass menu;

	@xComment("fixedLeft")
	CSSClass cFixedLeft;

	CSSClass cHeaderMenu;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {

		String style = ""; 
		if (XUIFactoryXHtml.getXMLFile().getMainXMLPart() instanceof XUIScene )
			style = "background:" + ((XUIScene)XUIFactoryXHtml.getXMLFile().getMainXMLPart()).getConfigScene().getBgColorMenu();
					
		
		return cStyle()
				.on(menu, "z-index: "+ZINDEX_MENU+";background-color: #ffffff;height: 100vh;width: " + widthMenu
						+ "px; color:black;"
						+ PERFORM_3D
						//+ "box-shadow: 4px 4px 2px 0 rgba(0,0,0,0.14);"
						)
				.on(cFixedLeft,
						"position:absolute; top:0px; transform:translate3d(-" + (widthMenu+5) + "px,0px,0px)")
				.on(cHeaderMenu, "height:53px;"+style
						+ "box-shadow: 0px 5px 13px 0px #969696;")

				.on(".menu ul", "padding-left: 0;  list-style-type: none; margin:0px;")

				.on(".menu li", "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   "
						+ "cursor: pointer;   min-height: 50px; line-height: 21px; "
						+ "width: 100%;   text-align: left;   text-transform: none;"
						+ PERFORM_3D
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

	
	public static XMLElement getTemplateMenu(Object name, Object icon, Object idAction) {
		
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xListNode(vPart(new ViewMenuItems()
						.vProperty(ViewMenuItems.PROPERTY_NAME, xVar(name))
						.vProperty(ViewMenuItems.PROPERTY_ICON, xVar(icon))
						.vProperty(ViewMenuItems.PROPERTY_ACTION, xVar(idAction))
						));
			}
		};
		
		return template.getTemplate();
		
	}
	
	public static XMLElement getTemplateMenuDivider() {
		return xListNodeStatic(vPart(new ViewMenuDivider()));
	}
}
