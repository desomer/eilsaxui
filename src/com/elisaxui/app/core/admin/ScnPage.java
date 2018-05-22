/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.page.CssReset;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.widget.button.CssRippleEffect;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.layout.JSPageAnimation;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFactory;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.factory.MountFactory;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPage")
@xComment("Scene Page")
public class ScnPage extends XHTMLPart implements ICSSBuilder, IJSDataDriven {
	static CSSClass main;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImport() {
		return xElem(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xLinkCss("https://fonts.googleapis.com/icon?family=Material+Icons"),
				xLinkCss("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css"),
				new CssTransition()
				);
	}

	@xTarget(HEADER.class)
	@xResource(id = "standard.js")
	public XMLElement xStandard() {
		return xElem(
				JSDomBuilder.class,
				TKPubSub.class,
				JSDataDriven.class,
				JSDataSet.class,
				JSPageAnimation.class
				);
	}

	@xTarget(HEADER.class) // la vue App Shell
	@xResource()
	public XMLElement sStyle() {
		return xElem(new CssReset(),
				xStyle(() -> {
					sOn(sSel("html"), () -> css("font-size: 16px;"));
					sOn(sSel("body"), () -> css("font-family: 'Roboto', sans-serif; font-weight: normal;"));
				}));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		vProp(ViewPageLayout.pStyleContent, "min-height: 100vh; min-width: 100vw; padding-top: 4rem; padding-bottom: 3.5rem");
		
		vProp(ViewNavBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgba(255, 152, 0, 0.89) 64%, rgba(255, 152, 0, 0.45098039215686275) 100%)");
		vProp(ViewNavBar.pHeight, "height: 4rem");

		vProp(ViewTabBar.pHeight, "height: 3.5rem");
		vProp(ViewTabBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgba(255, 152, 0, 0.89) 64%, rgba(255, 152, 0, 0.45098039215686275) 100%)");

		return xDiv(main);
	}

	@xTarget(AFTER_CONTENT.class)
	@xResource(id = "factory.js")
	public XMLElement xFactory() {
		return xElem(JSFactory.class);
	}

	@xTarget(AFTER_CONTENT.class)
	public XMLElement xLoad() {
		return xElem(JSController.class);
	}

	/********************************************************/
	public interface JSFactory extends JSClass {
		@xStatic(autoCall = true)
		default void doFactory() {
			__(MountFactory.register(new ScnPage()));
		}
	}

	/*************************************************************/
	static JSArray<TPage> listPage;
	static TPage aPage;
	static TBtn aBtn;

	public static class MountPage extends MountFactory {
	}

	@xFactory(MountPage.class)
	public XMLElement createPage(JSArray<TPage> arrPage) {
		return xElem(vFor(arrPage, aPage, xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIdPage, aPage.titre())
				.vProp(ViewPageLayout.pArticle, vMount(aPage, aPage.mountArticle()))
				.vProp(ViewNavBar.pChildren, vMount(aPage, aPage.mountNavNar()))
				.vProp(ViewTabBar.pChildren, vMount(aPage.dataTabBar(), aPage.mountTabNar())))));
	}

	public static class MountTabBar extends MountFactory {
	}

	@xFactory(MountTabBar.class)
	public XMLElement createTabBar(JSArray<TBtn> arrBtn) {
		return xElem(vFor(arrBtn, aBtn,
				xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, vMount(aBtn, aBtn.mountBtn()))));
	}

	public static class MountNavBar extends MountFactory {
	}

	@xFactory(MountNavBar.class)
	public XMLElement createNavBar(TPage aPage) {
		return xElem(new ViewBtnBurger(), xDiv(ViewNavBar.rightAction,
				xElem(vFor(aPage.dataActionNavNar(), aBtn, xElem(vMount(aBtn, aBtn.mountBtn()))))));
	}

	public static class MountBtn extends MountFactory {
	}

	@xFactory(MountBtn.class)
	public XMLElement createBtn(TBtn btn) {
		return xButton(xAttr("data-x-action", btn.action()),
				ViewNavBar.cActionBtnContainer, CssRippleEffect.cRippleEffect, xAttr("type", "button"),
				xI(ViewNavBar.actionBtn, ViewNavBar.material_icons, btn.titre()));
	}

	public static class MountBtn2 extends MountBtn {
	}

	@xFactory(MountBtn2.class)
	public XMLElement createBtn2(TBtn btn) {
		return xSpan(btn.titre());
	}

	public static class MountArticle extends MountFactory {
	}
	@xFactory(MountArticle.class)
	public XMLElement createArticle(TPage aPage) {
		return xSpan("ok");
	}
	
	/********************************************************/
	// les dto
	/********************************************************/
	public interface TBtn extends JSType {
		JSString titre();

		JSString action();

		JSString mountBtn();
	}

	public interface TPage extends JSType {
		JSString titre();

		JSString mountTabNar();
		JSArray<TBtn> dataNavBar();

		JSString mountNavNar();
		JSArray<TBtn> dataTabBar();

		JSArray<TBtn> dataActionNavNar();
		
		JSString mountArticle();
		JSAny contentArticle();
	}

	/********************************************************/
	// une class js CONTROLEUR avec template et datadriven
	/********************************************************/
	public interface JSController extends JSClass, IJSNodeTemplate, IJSDataDriven {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");

			JSArray<TPage> arrPage = let("arrPage", new JSArray<TPage>().asLitteral());

			document().querySelector(main).appendChild(xElem(vMount(arrPage, JSString.value(MountPage.class))));

			/******************************************************************/
			TPage page = newJS(TPage.class);
			page.titre().set("Page1");
			page.mountNavNar().set(MountNavBar.class);
			page.mountTabNar().set(MountTabBar.class);
			page.dataTabBar().set(new JSArray<TBtn>().asLitteral());
			page.dataActionNavNar().set(new JSArray<TBtn>().asLitteral());
			page.mountArticle().set(MountArticle.class);
			arrPage.push(page);

			TBtn btn = newJS(TBtn.class);
			btn.titre().set("schedule");
			btn.action().set("A");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataTabBar().push(btn);

			btn = newJS(TBtn.class);
			btn.titre().set("today");
			btn.action().set("B");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataTabBar().push(btn);

			btn = newJS(TBtn.class);
			btn.titre().set("apps");
			btn.action().set("C");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataTabBar().push(btn);

			btn = newJS(TBtn.class);
			btn.titre().set("add_circle_outline");
			btn.action().set("D");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataActionNavNar().push(btn);

			btn = newJS(TBtn.class);
			btn.titre().set("more_vert");
			btn.action().set("E");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataActionNavNar().push(btn);

			
			__("fastdom.mutate(", fct(()->{  
				JSNodeElement activity = let("page",document().querySelector("#Page1"));
				JSPageAnimation animMgr = let("animMgr", newJS(JSPageAnimation.class));	
				
				JSInt sct = let( JSInt.class, "sct",    document().body().scrollTop() );
				
				animMgr.doActivityActive(activity);
				animMgr.doActivityFreeze(activity, sct);
				animMgr.doFixedElemToAbsolute(activity, sct);
			})  ,")");
		}
	}

}
