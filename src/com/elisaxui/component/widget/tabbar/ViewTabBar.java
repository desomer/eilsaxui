/**
 * 
 */
package com.elisaxui.component.widget.tabbar;

import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.toolkit.transition.ConstTransition;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
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
public class ViewTabBar extends XHTMLPart implements ICSSBuilder {

	public static VProperty pStyle;
	public static VProperty pChildren;
	public static VProperty pHeight;
	public static VProperty pId;
	
	public static CSSClass cTabbar;
	public static CSSClass cFixedBottom;
	public static CSSClass cFlex_1;
	public static CSSClass cTextAlignCenter;
	
	private static CSSClass cListReset;
	private static CSSClass cFlex;
	

	@xTarget(HEADER.class)
	@xResource()
	public XMLElement xStylePart() {

		return xStyle(()-> {
			sOn(cTabbar, ()-> {
				css("z-index: "+XUIScene.ZINDEX_NAV_BAR);
				css(pHeight); 
				css("width: "+XUIScene.widthScene);
				css("color:white");
				css(XUIScene.PERFORM_3D);
				css("transition: transform "+ConstTransition.SPEED_ANIM_SCROLL+"ms ease-in-out");
				css("box-shadow: 16px -14px 20px 0 rgba(0, 0, 0, 0.21), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);");
				css(pStyle);
			});
			
			sOn(cFixedBottom, ()-> css("position:fixed; bottom:0px; "+XUIScene.PERFORM_3D));
			sOn(cListReset, ()-> css("list-style: none;margin: 0; padding: 0;"));
			sOn(cFlex, ()-> css("display:flex;"));
			sOn(cFlex_1, ()-> css("flex:1;"));
			sOn(cTextAlignCenter, ()-> css("text-align: center;"));

		});
		
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xFooter( xId(this.vProperty(pId)), cTabbar, cFixedBottom, cFixedElement,
				xUl(cListReset, cFlex, this.getChildren(), pChildren));
	}
	
}
