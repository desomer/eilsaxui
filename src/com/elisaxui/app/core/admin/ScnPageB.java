/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleComponent.JSMount;
import com.elisaxui.app.core.module.MntPage.MountArticle;
import com.elisaxui.app.core.module.MntPage.MountBtn;
import com.elisaxui.app.core.module.MntPage.MountNavBar;
import com.elisaxui.app.core.module.MntPage.MountPage;
import com.elisaxui.app.core.module.MntPage.MountTabBar;
import com.elisaxui.app.core.module.MntPage.TBtn;
import com.elisaxui.app.core.module.MntPage.TPage;
import com.elisaxui.component.page.ScnPage;
import com.elisaxui.component.toolkit.core.JSActionManager;
import com.elisaxui.component.toolkit.core.JSPageAnimation;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionEvent;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.widget.button.CssRippleEffect.JSRippleEffect;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPageB")
@xComment("Scene Page B")
public class ScnPageB extends ScnPage implements ICSSBuilder {

	@xTarget(AFTER_BODY.class)
	@xResource(id = "xControlerPageB.mjs") // insert un <Script type module> dans le body
	@xImport(idClass = JSMount.class)
	@xImport(idClass = JSPageAnimation.class)
	@xImport(idClass = JSRippleEffect.class)
	@xImport(idClass = JSActionManager.class)
	@xImport(idClass = JSDataBinding.class)
	public XMLElement xControlerPage() {
		return xElem(JSController.class);
	}

	/************************************************************************/

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {

		double heightNav = 3.5;
		double heightTab = 3.5;

		vProp(ViewPageLayout.pStyleContent,
				"min-height: 100vh; min-width: 100vw; padding-top: " + (heightNav + .5) + "rem; padding-bottom: "
						+ heightTab + "rem");

		vProp(ViewNavBar.pStyle,
				"background:linear-gradient(to bottom, #00BCD4 0%, rgb(0, 208, 255) 64%, rgb(133, 226, 241) 100%);" +
						"box-shadow: 5px 5px 17px 0px #00000033");
		
		vProp(ViewNavBar.pHeight, "height: " + heightNav + "rem");

		vProp(ViewTabBar.pHeight, "height: "+heightTab+"rem");
		vProp(ViewTabBar.pStyle, "background:linear-gradient(to bottom, #00BCD4 0%, rgb(0, 208, 255) 64%, rgb(133, 226, 241) 100%)");

		return xDiv(ScnPage.getcMain(), getAppShell());
	}

	/********************************************************/
	// une class js CONTROLEUR avec template et datadriven
	/********************************************************/
	static JSInt idx;
	static JSArray<TBtn> list;
	static JSArray<TPage> arrPage;
	static JSPageAnimation animMgr;
	static JSNodeElement activity;

	@xExport
	@xCoreVersion("1")
	public interface JSController extends JSClass, IJSNodeTemplate, IJSMountFactory {
		JSActionManager actionManager = JSClass.declareTypeClass(JSActionManager.class);
		TActionEvent actionEvent = JSClass.declareType();

		TPage aPage = JSClass.declareType();

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");
			let(arrPage, JSArray.newLitteral());
			document().querySelector(getcMain()).appendChild(xElem(vMount(arrPage, JSString.value(MountPage.class))));

			/******************************************************************/
			TPage page = newJS(TPage.class);
			page.titre().set("Page1");

			page.mountNavNar().set(MountNavBar.class);
			page.dataNavBar().set(JSArray.newLitteral());

			page.mountArticles().set(MountArticle.class);
			page.contentArticles().set(JSArray.newLitteral());

			let(aPage, page);

			JSArray<TBtn> listeArticle = JSArray.newLitteral();
			aPage.contentArticles().push(listeArticle);

			TBtn btn = newJS(TBtn.class);
			btn = newJS(TBtn.class);
			btn.titre().set("add_circle_outline");
			btn.action().set("D");
			btn.mountBtn().set(MountBtn.class);
			aPage.dataNavBar().push(btn);

			arrPage.push(aPage);
			
			TPage page2 = newJS(TPage.class);
			page2.titre().set("Page2");

			page2.mountNavNar().set(MountNavBar.class);
			page2.dataNavBar().set(JSArray.newLitteral());

			page2.mountArticles().set(MountArticle.class);
			page2.contentArticles().set(JSArray.newLitteral());
			
			page2.mountTabNar().set(MountTabBar.class);
			page2.dataTabBar().set(JSArray.newLitteral());
			
			aPage.set(page2);
			
			btn = newJS(TBtn.class);
			btn.titre().set("more_vert");
			btn.action().set("E");
			btn.mountBtn().set(MountBtn.class);
			aPage.dataTabBar().push(btn);
			
			arrPage.push(aPage);
			
			consoleDebug(txt("jsonpage="), arrPage);
			/*****************************************************************/

			let(animMgr, newJS(JSPageAnimation.class));

			__("fastdom.mutate(", fct(() -> {
				let(activity, document().querySelector("#Page1"));
				document().querySelector(getcMain()).children().at(0).remove();
				animMgr.doActivityActive(activity);
			}), ")");

		}

	}

}
