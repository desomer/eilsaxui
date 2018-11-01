/**
 * 
 */
package com.elisaxui.component.widget.menu;

import static com.elisaxui.component.page.CssPage.widthMenu;
import static com.elisaxui.component.toolkit.transition.ConstTransition.PERFORM_3D;
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
 * @author Bureau
 *    https://codepen.io/neillin1023/pen/NrKBbO
 *    http://www.cssscript.com/material-style-tabs-component-with-javascript-and-css/
 *    https://codepen.io/Touficbatache/pen/eERNdo
 */
public class ViewMenuContainer extends XHTMLPart implements ICSSBuilder {

	public static VProperty pStyle;
	public static VProperty pChildren;
	public static VProperty pId;
		
	public static CSSClass cMenu;
	public static CSSClass cPageMenu;

	@xTarget(HEADER.class)
	@xResource()
	public XMLElement xStylePart() {

		return xStyle(()-> {
			sOn(cPageMenu, ()->{
				css("width: 250px !important;  min-width: 250px !important;");
			});
			sOn(cMenu, ()-> {
				css("z-index: "+ZINDEX_MENU);
				css("background-color: #ffffff;height: 100vh;float: right;"); 
				css("width: "+widthMenu+"px");
				css("color:black");
				css(pStyle);
			});
			
		});
		
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xNav( xId(this.vProperty(pId)), cMenu,
						xUl(this.getChildren(), pChildren));
	}
	
}
