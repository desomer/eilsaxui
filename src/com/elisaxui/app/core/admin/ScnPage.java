/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.admin.ViewPageFactory.MountArticle;
import com.elisaxui.app.core.admin.ViewPageFactory.MountArticle2;
import com.elisaxui.app.core.admin.ViewPageFactory.MountBtn;
import com.elisaxui.app.core.admin.ViewPageFactory.MountNavBar;
import com.elisaxui.app.core.admin.ViewPageFactory.MountPage;
import com.elisaxui.app.core.admin.ViewPageFactory.MountTabBar;
import com.elisaxui.app.core.admin.ViewPageFactory.TBtn;
import com.elisaxui.app.core.admin.ViewPageFactory.TPage;
import com.elisaxui.component.page.CssReset;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
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
public class ScnPage extends XHTMLPart implements ICSSBuilder {
	
	static CSSClass main;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImport() {
		return xElem(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xLinkCss("https://fonts.googleapis.com/icon?family=Material+Icons"),
				xLinkCss("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css"),
				new CssTransition());
	}

	@xTarget(HEADER.class)
	@xResource(id = "standard.js")
	public XMLElement xStandard() {
		return xElem(
				JSDomBuilder.class,
				TKPubSub.class,
				JSDataDriven.class,
				JSDataSet.class,
				JSPageAnimation.class,
				JSTouchManager.class,
				JSActionManager.class);
	}

	@xTarget(AFTER_CONTENT.class)
	@xResource(id = "factory.js")
	public XMLElement xFactory() {
		return xElem(JSFactory.class);
	}

	@xTarget(AFTER_CONTENT.class)
	@xResource
	public XMLElement xLoad() {
		return xElem(JSController.class);
	}

	/************************************************************************/

	@xTarget(HEADER.class) // la vue App Shell
	@xResource(id = "main.css")
	public XMLElement sStyle() {
		return xElem(new CssReset(),
				xStyle(() -> {
					sOn(sSel("html"), () -> css("font-size: 16px;"));
					sOn(sSel("body"), () -> css("font-family: 'Roboto', sans-serif; font-weight: normal;"));
				}));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		vProp(ViewPageLayout.pStyleContent,
				"min-height: 100vh; min-width: 100vw; padding-top: 4rem; padding-bottom: 3.5rem");

		vProp(ViewNavBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%)");
		vProp(ViewNavBar.pHeight, "height: 4rem");

		vProp(ViewTabBar.pHeight, "height: 3.5rem");
		vProp(ViewTabBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%)");

		return xDiv(main);
	}

	/********************************************************/
	public interface JSFactory extends JSClass {
		@xStatic(autoCall = true)
		default void doFactory() {
			__(MountFactory.register(new ViewPageFactory()));
		}
	}


	/********************************************************/
	// une class js CONTROLEUR avec template et datadriven
	/********************************************************/
	static JSInt idx;
	static JSArray<TBtn> list;
	static JSArray<TPage> arrPage;
	static JSPageAnimation animMgr;
	static JSNodeElement activity;
	
	public interface JSController extends JSClass, IJSNodeTemplate, IJSDataDriven {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");

			let(arrPage, JSArray.newLitteral());

			document().querySelector(main).appendChild(xElem(vMount(arrPage, JSString.value(MountPage.class))));

			/******************************************************************/
			TPage page = newJS(TPage.class);
			page.titre().set("Page1");
			page.mountNavNar().set(MountNavBar.class);
			page.mountTabNar().set(MountTabBar.class);
			page.dataTabBar().set(JSArray.newLitteral());
			page.dataNavBar().set(JSArray.newLitteral());
			page.mountArticle().set(MountArticle.class);
			page.contentArticle().set(JSArray.newLitteral());

			JSArray<TBtn> listeArticle = JSArray.newLitteral();

			for (int i = 0; i < 100; i++) {
				TBtn ligne1 = newJS(TBtn.class);
				ligne1.titre().set("Ligne" + i);
				listeArticle.push(ligne1);
			}

			arrPage.push(page);
			arrPage.at(0).contentArticle().push(listeArticle);

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
			arrPage.at(0).dataNavBar().push(btn);

			btn = newJS(TBtn.class);
			btn.titre().set("more_vert");
			btn.action().set("E");
			btn.mountBtn().set(MountBtn.class);
			arrPage.at(0).dataNavBar().push(btn);
			let(animMgr, newJS(JSPageAnimation.class));

			/**************************************************************************************/

			__("fastdom.mutate(", fct(() -> {
				let(activity, document().querySelector("#Page1"));
				animMgr.doActivityActive(activity);
			}), ")");

			setTimeout(() -> {
				arrPage.at(0).contentArticle().pop();  // suppresion de l'article
				arrPage.at(0).mountArticle().set(MountArticle2.class);  // change le template
				arrPage.at(0).contentArticle().push(JSArray.newLitteral());  // ajout d'un article

				let(list, arrPage.at(0).contentArticle().at(0));
				
				_forIdxBetween(idx, 0, 10)._do(() -> {
					TBtn ligne1 = newJS(TBtn.class);
					ligne1.titre().set("Ligne");
					list.push(ligne1);
				});

			}, 5000);

		}
	}

}
