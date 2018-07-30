/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.admin.JSActionManager.TActionEvent;
import com.elisaxui.app.core.admin.MntPage.MountArticle;
import com.elisaxui.app.core.admin.MntPage.MountArticle2;
import com.elisaxui.app.core.admin.MntPage.MountBtn;
import com.elisaxui.app.core.admin.MntPage.MountNavBar;
import com.elisaxui.app.core.admin.MntPage.MountPage;
import com.elisaxui.app.core.admin.MntPage.MountTabBar;
import com.elisaxui.app.core.admin.MntPage.TBtn;
import com.elisaxui.app.core.admin.MntPage.TPage;
import com.elisaxui.app.core.module.CmpModuleComponent.JSMount;
import com.elisaxui.component.page.ScnPage;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
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
@xResource(id = "ScnPage")
@xComment("Scene Page A")
public class ScnPageA extends ScnPage implements ICSSBuilder {

	@xTarget(AFTER_BODY.class)
	@xResource(id = "xControlerPageA.mjs") // insert un <Script type module> dans le body
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

		double HEIGHT_NAV = 3.5;
		double HEIGHT_TAB = 3.5;

		vProp(ViewPageLayout.pStyleContent,
				"min-height: 100vh; min-width: 100vw; padding-top: " + (HEIGHT_NAV + .5) + "rem; padding-bottom: "
						+ HEIGHT_TAB + "rem");

		vProp(ViewNavBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%);"
				+"box-shadow: 20px 6px 12px 9px rgba(0, 0, 0, 0.22), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)");
		vProp(ViewNavBar.pHeight, "height: " + HEIGHT_NAV + "rem");

		vProp(ViewTabBar.pHeight, "height: " + HEIGHT_TAB + "rem");
		vProp(ViewTabBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%);"
						+ "box-shadow: 16px -14px 20px 0 rgba(0, 0, 0, 0.21), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);");

		vProp(ViewPageLayout.pWithTabBar, true);
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

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");

			let(arrPage, JSArray.newLitteral());

			document().querySelector(getcMain()).appendChild(xElem(vMount(arrPage, JSString.value(MountPage.class))));
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

			consoleDebug(txt("jsonpage="), arrPage);

			/**************************************************************************************/
			let(animMgr, newJS(JSPageAnimation.class));

			__("fastdom.mutate(", fct(() -> {
				let(activity, document().querySelector("#Page1"));
				document().querySelector(getcMain()).children().at(0).remove();
				animMgr.doActivityActive(activity);
			}), ")");

			doChangeContent(arrPage);
			doMoveOnAction();

			actionManager.addAction(JSString.value("A"), _this(), fct(() -> {
				consoleDebug(txt("cvcbc"));
			}));

		}

		@xStatic()
		default void doChangeContent(JSArray<TPage> arrPage) {
			arrPage.setArrayType(TPage.class);
			setTimeout(() -> {
				consoleDebug(txt("change article"));
				arrPage.at(0).contentArticle().pop(); // suppresion de l'article
				arrPage.at(0).mountArticle().set(MountArticle2.class); // change le template
				arrPage.at(0).contentArticle().push(JSArray.newLitteral()); // ajout d'un

				let(list, arrPage.at(0).contentArticle().at(0));

				_forIdx(idx, 0, 10)._do(() -> {
					TBtn ligne1 = newJS(TBtn.class);
					ligne1.titre().set(calc(txt("Ligne"), "+", idx));
					list.push(ligne1);
				});

			}, 5000);
		}

		@xStatic()
		default void doMoveOnAction() {
			actionManager.onStart(fct(actionEvent, () -> {
				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
					/***************************************************/
					let(animMgr, newJS(JSPageAnimation.class));
					animMgr.doActivityFreeze(actionEvent.activity(), actionEvent.scrollY());
					animMgr.doFixedElemToAbsolute(actionEvent.activity());
					/***************************************************/
				});
			}));
			actionManager.onMove(fct(actionEvent, () -> {
				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
					/***************************************************/
					consoleDebug(actionEvent.infoEvent().distance());

					actionEvent.activity().style().attr("transform")
							.set(txt("translate3d(", actionEvent.infoEvent().deltaX(), "px,",
									actionEvent.infoEvent().deltaY(), "px, 0px)"));
					/***************************************************/
				});
			}));
			actionManager.onStop(fct(actionEvent, () -> {
				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
					/***************************************************/
					let(animMgr, newJS(JSPageAnimation.class));
					let(activity, actionEvent.activity());

					animMgr.doActivityDeFreeze(activity);
					animMgr.doInitScrollTo(activity);
					animMgr.doFixedElemToFixe(activity);
					activity.style().attr("transform").set(txt());
					/***************************************************/
				});
			}));
		}
	}

}
