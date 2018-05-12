/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;
import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow.window;

import com.elisaxui.component.page.CssReset;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.button.ViewRippleEffect;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFactory;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPage")
@xComment("Scene Page")
public class ScnPage extends XHTMLPart implements IJSDataDriven {
	static CSSClass main;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImport() {
		return xElem(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xLinkCss("https://fonts.googleapis.com/icon?family=Material+Icons"),
				JSDomBuilder.class,
				TKPubSub.class,
				JSDataDriven.class,
				JSDataSet.class);
	}

	@xTarget(HEADER.class) // la vue App Shell
	@xResource()
	public XMLElement sStyle() {
		return xElem(new CssReset());
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {

		vProp(ViewTabBar.pStyleViewTabBar,
				"background:linear-gradient(to bottom, rgba(0, 150, 136, 0.52) 0%, #009688 64%, #1d655e 100%)");
		vProp(ViewNavBar.pStyleViewNavBar,
				"background:linear-gradient(to bottom, rgb(2, 140, 127) 0%, #009688 64%, #1d65657d 100%)");

		return xDiv(main);
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xElem(JSFactory.class, JSController.class);
	}

	/********************************************************/
	public interface JSFactory extends JSClass {

		@xStatic(autoCall = true)
		default void doFactory() {
			ScnPage page = new ScnPage();
			JSArray<TBtn> container = declareArray(TBtn.class, "container");
			window().attr("createTabBar").set(fct(container, () -> _return(page.createTabBar(container))));

			JSArray<TPage> containerPage = declareArray(TPage.class, "containerPage");
			window().attr("createPage").set(fct(containerPage, () -> _return(page.createPage(containerPage))));

			TBtn containerBtn = declareType(TBtn.class, "containerBtn");
			window().attr("createBtn").set(fct(containerBtn, () -> _return(page.createBtn(containerBtn))));
			window().attr("createBtn2").set(fct(containerBtn, () -> _return(page.createBtn2(containerBtn))));
		}

		default JSNodeElement callFactory(JSString factoryID, JSElement data) {
			JSCallBack factory = let(JSCallBack.class, "factory", window().attrByString(factoryID));
			return cast(JSNodeElement.class, factory.call(_this(), data));
		}

	}
	
	/*************************************************************/
	static JSArray<TPage> listPage;
	static TPage aPage;
	static TBtn aBtn;

	@xFactory("createPage")
	public XMLElement createPage(JSArray<TPage> arrPage) {
		return xElem(vFor(arrPage, aPage, xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIdPage, aPage.titre())
				.vProp(ViewTabBar.pChildrenTabBar, vMount(aPage, aPage.mountTabNar())))));
	}

	@xFactory("createTabBar")
	public XMLElement createTabBar(JSArray<TBtn> arrBtn) {
		return xElem(vFor(arrBtn, aBtn,
					xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, vMount(aBtn, aBtn.mountBtn()))));
	}

	@xFactory("createBtn")
	public XMLElement createBtn(TBtn btn) {
		return xButton(xAttr("data-x-action", btn.action()),
				ViewNavBar.cActionBtnContainer, ViewRippleEffect.cRippleEffect, xAttr("type", xTxt("button")),
				xI(ViewNavBar.actionBtn, ViewNavBar.material_icons, btn.titre()));
	}
	
	@xFactory("createBtn2")
	public XMLElement createBtn2(TBtn btn) {
		return xSpan(btn.titre());
	}

	/********************************************************/
	// les dto
	/********************************************************/
	public interface TBtn extends JSType {
		JSString titre();
		JSString action();
		JSCallBack mountBtn();
	}

	public interface TPage extends JSType {
		JSString titre();
		JSCallBack mountTabNar();
		JSArray<TBtn> dataTabBar();
	}

	/********************************************************/
	// une class js CONTROLEUR avec template et datadriven
	/********************************************************/

	public interface JSController extends JSClass, IJSNodeTemplate, IJSDataDriven {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			JSArray<TPage> arrPage = let("arrPage", new JSArray<TPage>().asLitteral());

			/********************************************************************/
			JSFactory factory = let("factory", newJS(JSFactory.class));

			JSFunction mountPage = fct(listPage, () -> _return(factory.callFactory(JSString.value("createPage"), listPage)));

			JSCallBack mountTabBar = let(JSCallBack.class, "mountTabBar",
					fct(aPage, () -> _return(factory.callFactory(JSString.value("createTabBar"), aPage.dataTabBar()))));
			
			JSCallBack mountBtn = let(JSCallBack.class, "mountBtn",
					fct(aBtn, () -> _return(factory.callFactory(JSString.value("createBtn"), aBtn))));
			
			JSCallBack mountBtn2 = let(JSCallBack.class, "mountBtn2",
					fct(aBtn, () -> _return(factory.callFactory(JSString.value("createBtn2"), aBtn))));
			/*******************************************************************/
			
			document().querySelector(main).appendChild(xElem(vMount(arrPage, mountPage)));

			TPage page = newJS(TPage.class);
			page.titre().set("Page1");
			page.mountTabNar().set(mountTabBar);
			page.dataTabBar().set(new JSArray<TBtn>().asLitteral());
			arrPage.push(page);

			TPage page2 = newJS(TPage.class);
			page2.titre().set("Page2");
			page2.mountTabNar().set(mountTabBar);
			page2.dataTabBar().set(new JSArray<TBtn>().asLitteral());
			arrPage.push(page2);

			setTimeout(() -> {
				TBtn btn = newJS(TBtn.class);
				btn.titre().set("schedule");
				btn.action().set("A");
				btn.mountBtn().set(mountBtn);
				arrPage.at(0).dataTabBar().push(btn);

				btn = newJS(TBtn.class);
				btn.titre().set("today");
				btn.action().set("B");
				btn.mountBtn().set(mountBtn);
				arrPage.at(0).dataTabBar().push(btn);

				btn = newJS(TBtn.class);
				btn.titre().set("apps");
				btn.action().set("C");
				btn.mountBtn().set(mountBtn);
				arrPage.at(0).dataTabBar().push(btn);
				
				btn = newJS(TBtn.class);
				btn.titre().set("schedule");
				btn.action().set("D");
				btn.mountBtn().set(mountBtn2);
				arrPage.at(1).dataTabBar().push(btn);

				btn = newJS(TBtn.class);
				btn.titre().set("today");
				btn.action().set("E");
				btn.mountBtn().set(mountBtn);
				arrPage.at(1).dataTabBar().push(btn);
				
			}, 100);


			/*
			 * xui().mount("createPage") .container("listePage") .on("append", main)
			 */ // "replace"

			/*
			 * xui().container("listePage").set(id);
			 */
		}
	}

}
