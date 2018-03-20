/**
 * 
 */
package com.elisaxui.component.widget.tabbar;

import static com.elisaxui.component.transition.CssTransition.cFixedElement;
import static com.elisaxui.component.widget.button.ViewRippleEffect.cRippleEffect;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.transition.ConstTransition;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *    https://codepen.io/neillin1023/pen/NrKBbO
 *    http://www.cssscript.com/material-style-tabs-component-with-javascript-and-css/
 *    https://codepen.io/Touficbatache/pen/eERNdo
 */
public class ViewTabBar extends XHTMLPart {

	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	public static CSSClass cTabbar;
	public static CSSClass cFixedBottom;
	private static CSSClass cListReset;
	private static CSSClass cFlex;
	public static CSSClass cFlex_1;
	public static CSSClass cTextAlignCenter;
	

	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {
		
		return cStyle()   
				.path(cTabbar).set("z-index: "+XUIScene.ZINDEX_NAV_BAR+";"
						+ "background:"+((XUIScene)XUIFactoryXHtml.getXHTMLFile().getScene()).getConfigScene().getBgColorNavBar()+";"
						+ "height: "+XUIScene.heightTabBar+";"
						+ "width: "+XUIScene.widthScene+"; "
						+ "color:white; "
						+ XUIScene.PREFORM_3D
						+ "transition: transform "+ConstTransition.SPEED_ANIM_SCROLL+"ms ease-in-out;" 
						+ "box-shadow: 16px -14px 20px 0 rgba(0, 0, 0, 0.21), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);")
				
				.path(cFixedBottom).set("position:fixed; bottom:0px; "+XUIScene.PREFORM_3D)
				.path(cListReset).set("list-style: none;margin: 0; padding: 0;")
				.path(cFlex).set("display:flex;")
				.path(cFlex_1).set("flex:1;")
				.path(cTextAlignCenter).set("text-align: center;")
				;
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xFooter( xId(this.vProperty(PROPERTY_NAME)), cTabbar, cFixedBottom, cFixedElement,
				xUl(cListReset, cFlex, this.getChildren()));
	}
	
	public static XMLElement getTemplateAction(Object name, Object action) {
		
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{		
				return xElement("button", xAttr("data-x-action", txt(xVar(action))), ViewNavBar.actionBtnContainer, cRippleEffect , xAttr("type", "\"button\""),  "<i class=\"actionBtn material-icons\">",xVar(name),"</i>");
			}
		};
		
		return template.getTemplate();
	}
}
