/**
 * 
 */
package com.elisaxui.component.widget.menu;

import static com.elisaxui.component.page.CssPage.widthMenu;
import static com.elisaxui.component.toolkit.transition.ConstTransition.PERFORM_3D;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_MENU_ITEMS;
import static com.elisaxui.component.toolkit.transition.ConstTransition.ZINDEX_MENU;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau https://codepen.io/neillin1023/pen/NrKBbO
 *         http://www.cssscript.com/material-style-tabs-component-with-javascript-and-css/
 *         https://codepen.io/Touficbatache/pen/eERNdo
 */
public class ViewMenuContainer extends XHTMLPart implements ICSSBuilder {

	public static VProperty pStyle;
	public static VProperty pChildren;
	public static VProperty pId;

	public static CSSClass cMenu;
	public static CSSClass cDivider;
	public static CSSClass cPageMenu;

	@xTarget(HEADER.class)
	@xResource()
	public XMLElement xStylePart() {

		return xStyle(() -> {
			sOn(cPageMenu, () -> {
				css("width: 250px !important;  min-width: 250px !important;");
			});
			sOn(cMenu, () -> {
				css("z-index: " + ZINDEX_MENU);
				css("background-color: #ffffff;height: 100vh;float: right;");
				css("width: " + widthMenu + "px");
				css("color:black");
				css(pStyle);
				sOn(sSel("ul"), () -> {
					css("padding-left: 0;  list-style-type: none; margin:0px;");
					sOn(sSel("li"), () -> {
						css("list-style-type: none;clear: both;  color: rgba(0,0,0,0.87)");
						css("cursor: pointer;   min-height: 50px; line-height: 21px");
						css("width: 100%;   text-align: left;   text-transform: none;" + PERFORM_3D);
						css("animation-duration:" + SPEED_SHOW_MENU_ITEMS + "ms");
						sOn(sSel(">a"), () -> {
							css("font-size: 1rem;  line-height: 26px;  padding: 14px 16px;  float: left;");
							sOn(sSel(">i"), () -> {
								css("margin: 0 16px 0 0; float: left;");
							});
						});
					});
					sOn(cDivider, ()->css("min-height: 0;height: 1px; background-color: #e0e0e0"));
				});
			});

			/*
			 * .on(".menu ul", "padding-left: 0;  list-style-type: none; margin:0px;")
			 * 
			 * .on(".menu li",
			 * "list-style-type: none;clear: both;  color: rgba(0,0,0,0.87);   " +
			 * "cursor: pointer;   min-height: 50px; line-height: 21px; " +
			 * "width: 100%;   text-align: left;   text-transform: none;" + PERFORM_3D +
			 * "animation-duration:" + SPEED_SHOW_MENU_ITEMS + "ms;" )
			 * 
			 * .on(".menu li>a",
			 * "font-size: 1rem;  line-height: 26px;  padding: 14px 16px;  float: left;")
			 * .on(".menu li>a>i", "margin: 0 16px 0 0; float: left;")
			 * .on(".menu li.cDivider",
			 * "min-height: 0;height: 1px; background-color: #e0e0e0;") // overflow: //
			 * hidden;
			 */

		});

	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xNav(xId(this.vProperty(pId)), cMenu, this.getChildren(), pChildren);
	}

}
