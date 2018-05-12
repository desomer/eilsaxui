/**
 * 
 */
package com.elisaxui.component.widget.navbar;

import static com.elisaxui.component.transition.CssTransition.*;
import static com.elisaxui.component.widget.button.ViewRippleEffect.cRippleEffect;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.transition.ConstTransition;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLHandle;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
@xComment("ViewNavBar")
public class ViewNavBar extends XHTMLPart implements ICSSBuilder {

	public static CSSClass cActionBtnContainer;
	static CSSClass animatedBg;
	public static CSSClass isOpenMenu;
	public static CSSClass navbar;
	public static CSSClass fixedTop;
	public static CSSClass rightAction;
	public static CSSClass center;
	public static CSSClass logo;
	public static CSSClass actionBtn;
	@xComment("material-icons")
	public static CSSClass material_icons;
	public static CSSClass descBar;
	public static CSSClass topBar;

	public static final String PROPERTY_NAME = "PROPERTY_NAME";

	public static VProperty pStyleViewNavBar;

	@xTarget(AFTER_BODY.class)
	@xResource
	public XMLElement xStylePart() {
		String bgColorNavBar = null;

		if (XUIFactoryXHtml.getXMLFile().getMainXMLPart() instanceof XUIScene)
			bgColorNavBar = ((XUIScene) XUIFactoryXHtml.getXMLFile().getMainXMLPart()).getConfigScene()
					.getBgColorNavBar();

		return xListNode(
				xStyle(() -> {
					sOn(descBar, () -> {
						css(pStyleViewNavBar);
					});

				}),
				cStyle()
						.path(navbar).set("z-index: " + XUIScene.ZINDEX_NAV_BAR + ";"
								+ "height: 3rem;"
								+ "width: " + XUIScene.widthScene + "; "
								+ "color:white; "
								+ "transition: transform " + ConstTransition.SPEED_ANIM_SCROLL + "ms ease-in-out;")

						.path(fixedTop).set("position:fixed; top:0px;")
						// ---------------- .select(fixedToAbsolute).set("position:absolute;") // permet
						// de deplacement

						.path(rightAction)
						.set("position: absolute; right: 0px;  top: 0px;  height: 100%;  width: auto;")

						.path(actionBtn).set("margin: 0; padding: 8px;  font-size: 2.5rem;  cursor: pointer;")

						.path(center).set(""
								+ ";height:100%; "
								+ "display: flex; align-items: center;justify-content: center")
						.path(logo).set(""
								+ "z-index: " + (XUIScene.ZINDEX_NAV_BAR + 1) + ";" // pour opacity
								+ " margin-top:calc(" + XUIScene.heightNavBar + " /2); "
								+ "color: inherit; "
								+ "font-size: 2.1rem; "
								+ "transition: all " + ConstTransition.SPEED_ANIM_SCROLL + "ms linear;" // ease-in-out
						)

						.path(animatedBg).set("position: absolute; display: block;"
								+ "  width: 100%; height: 100%;"
								+ " top: 0; right: 0; bottom: 0; left: 0;"
						// +
						// "background:"+XUIFactoryXHtml.getXHTMLFile().getScene().getConfigScene().getBgColorNavBar()+";
						// "
						)

						.path(cActionBtnContainer)
						.set("cursor: pointer; position: relative; background-color: Transparent; color:white;"
								+ "padding: 0;  overflow: hidden; outline: 0 !important; " // pas de bordure au focus
								+ "border:none")

						.path(topBar).set(""
								+ "background:"
								+ "linear-gradient(to bottom, #00000061 0%, rgba(0, 0, 0, 0.23) 64%, rgba(0, 0, 0, 0) 100%);"
								+ "position: absolute;"
								+ "top: 0px;"
								+ "height: 4rem;"
								+ "width: 100%;"
								+ "z-index:-1;")

						.path(descBar).set(""
								+ "top: 0px; width: 100%;"
								+ "position: absolute; "
								+ "height:" + XUIScene.heightNavBar + ";"
								+ (bgColorNavBar == null ? ";" : ("background:" + bgColorNavBar + "; "))
								// --------------------+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0
								// rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);"
								+ "box-shadow: 20px 6px 12px 9px rgba(0, 0, 0, 0.22), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);"
								+ "transition: all " + ConstTransition.SPEED_ANIM_SCROLL + "ms linear;" // ease-in-out
				));
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xHeader(xId(this.vProperty(PROPERTY_NAME)), navbar, fixedTop, cFixedElement,
				xDiv(descBar, this.getChildren()),
				xDiv(topBar));
	}

	
	/***********************************************************************/
	
	public static XMLElement getTemplateBtnBurger() {
		return vPart(new ViewBtnBurger());
	}

	public static XMLElement getTemplateActionBar() {
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate() {
				return xDiv(rightAction);
			}
		};

		return template.getTemplate();
	}

	public static XMLElement getTemplateName(Object name) {
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate() {
				return xDiv(center, xDiv(logo, xVar(name)));
			}
		};

		return template.getTemplate();

	}

	public static XMLElement getTemplateAction(Object name, Object action) {

		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate() {
				return xNode("button", cActionBtnContainer, cRippleEffect,
						xAttr("data-x-action",
								txt(xVar(action))),
						xAttr("type", "\"button\""),
						xI(actionBtn, material_icons, xVar(name)));
			}
		};

		return template.getTemplate();
	}

	public static XMLElement getTemplateBgCanvas() {
		return xNode("canvas", animatedBg); // pour granim
	}

	public static XMLElement getTemplateBgDiv() {
		return xNode("div", animatedBg);
	}

}
