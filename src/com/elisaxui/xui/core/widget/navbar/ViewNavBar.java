/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffect;
import static com.elisaxui.xui.core.transition.CssTransition.*;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.transition.ConstTransition;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
/**
 * @author Bureau
 *
 */
@xComment("ViewNavBar")
public class ViewNavBar extends XHTMLPart {
	
	public static XClass actionBtnContainer;
	static XClass animatedBg;
	public static XClass isOpenMenu;
	public static XClass navbar;
	public static XClass fixedTop;
	public static XClass rightAction;
	public static XClass center;
	public static XClass logo;
	public static XClass actionBtn;
	@xComment("material-icons")
	public static XClass material_icons;
	public static XClass descBar;
	public static XClass topBar;
	
	public static final String PROPERTY_NAME = "PROPERTY_NAME";
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {
		
		return xStyle()  
				.path(navbar).add("z-index: "+XUIScene.ZINDEX_NAV_BAR+";"
						+ "height: 3rem;"
						+ "width: "+XUIScene.widthScene+"; "
						+ "color:white; "
						+ "transition: transform "+ConstTransition.SPEED_ANIM_SCROLL+"ms ease-in-out;" 
						+ XUIScene.PREFORM_3D_2
					)
				
				.path(fixedTop).add("position:fixed; top:0px;"
						+ XUIScene.PREFORM_3D_2
						 )
			//----------------	.select(fixedToAbsolute).set("position:absolute;")  // permet de deplacement
				
				.path(rightAction).add("position: absolute; right: 0px;  top: 0px;  height: 100%;  width: auto;")
				
				.path(actionBtn).add("margin: 0; padding: 8px;  font-size: 2.5rem;  cursor: pointer;")
				
				.path(center).add(""
						+ ";height:100%; "
						+ "display: flex; align-items: center;justify-content: center"
						)
				.path(logo).add( ""
						+ "z-index: "+(XUIScene.ZINDEX_NAV_BAR+1)+";"  // pour opacity
						+ " margin-top:calc(" + XUIScene.heightNavBar +" /2); "
						+ "color: inherit; "
						+ "font-size: 2.1rem; "
						+ "transition: all "+ConstTransition.SPEED_ANIM_SCROLL+"ms linear;"    //ease-in-out
						 )
				
				.path(animatedBg).add("position: absolute; display: block;"
						+ "  width: 100%; height: 100%;"
						+ " top: 0; right: 0; bottom: 0; left: 0;"
					//	+ "background:"+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()+"; "
						)
				
				.path(actionBtnContainer).add("cursor: pointer; position: relative; background-color: Transparent; color:white;"
						+ "padding: 0;  overflow: hidden; outline: 0 !important; " // pas de bordure au focus
						+ "border:none")
				
				.path(topBar).add(""
						+ "background:"+"linear-gradient(to bottom, #00000061 0%, rgba(0, 0, 0, 0.23) 64%, rgba(0, 0, 0, 0) 100%);"
						+ "position: absolute;" 
						+ "top: 0px;" 
						+ "height: 4rem;" 
						+ "width: 100%;"
						+ "z-index:-1;"
						+ XUIScene.PREFORM_3D_2
						)
				
				.path(descBar).add( ""
					    + "top: 0px; width: 100%;"
					    + "position: absolute; "
					    + "height:"+ XUIScene.heightNavBar+";"
					    + XUIScene.PREFORM_3D_2
						+ "background:"+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()+"; "
						//--------------------+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);"
						+ "box-shadow: 20px 6px 12px 9px rgba(0, 0, 0, 0.22), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);"
						+ "transition: all "+ConstTransition.SPEED_ANIM_SCROLL+"ms linear;"    //ease-in-out
						)
				;
	}
	
	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xImportAllClass() {
		return	xImport(JSNavBar.class);
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {  
		return xHeader( xId(this.getProperty(PROPERTY_NAME)), navbar, fixedTop, cFixedElement,
			    xDiv(descBar, this.getChildren()),
				xDiv(topBar)
				);
	}
	
	public static XMLElement getTemplateBtnBurger() {
		return xPart(new ViewBtnBurger());
	}
	
	public static XMLElement getTemplateActionBar() {
		return xDiv(rightAction);
	}
	
	public static XMLElement getTemplateName(Object name) {
		return xDiv(center, xDiv(logo, xVar(name)));
	}
	
	public static XMLElement getTemplateAction(Object name, Object action) {
		return xElement("button", actionBtnContainer, cRippleEffect , xAttr("data-x-action", txt(xVar(action))), xAttr("type", "\"button\""), 
				xI(actionBtn, material_icons, xVar(name)));
	}
	
	public static XMLElement getTemplateBgCanvas() {
		return xElement("canvas", animatedBg); // pour granim
	}
	
	public static XMLElement getTemplateBgDiv() {
		return xElement("div", animatedBg); 
	}
		

}
