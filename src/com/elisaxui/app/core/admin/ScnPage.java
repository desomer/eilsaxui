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
import com.elisaxui.app.core.module.CmpModuleCore;
import com.elisaxui.app.core.module.CmpModuleComponent.JSMount;
import com.elisaxui.app.core.module.CmpModuleComponent;
import com.elisaxui.component.page.CssReset;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.transition.CssTransition;
import com.elisaxui.component.widget.button.CssRippleEffect;
import com.elisaxui.component.widget.button.CssRippleEffect.JSRippleEffect;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
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
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPage")
@xComment("Scene Page")
public class ScnPage extends XHTMLPart implements ICSSBuilder {

	static CSSClass cMain;

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImport() {
		return xElem(new CmpModuleCore(), new CmpModuleComponent());
	}

	@xTarget(HEADER.class)
	@xResource
	public XMLElement preLoad() {
		return xElem(xLinkModulePreload("/asset/mjs/xMount.mjs"),
				xLinkModulePreload("/asset/mjs/xStandard.mjs"),
				xLinkModulePreload("/asset/mjs/xComponent.mjs"),
				xLinkModulePreload("/asset/mjs/xDatadriven.mjs"),
				xLinkModulePreload("/asset/mjs/xBinding.mjs"),
				xLinkModulePreload("/asset/mjs/xCore.mjs"));
	}

	@xTarget(AFTER_BODY.class)
	@xResource(id = "xControlerPage.mjs") // insert un <Script type module> dans le body
	@xImport(idClass = JSMount.class)
	@xImport(idClass = JSPageAnimation.class)
	@xImport(idClass = JSRippleEffect.class)
	@xImport(idClass = JSActionManager.class)
	public XMLElement xControlerPage() {
		return xElem(JSController.class);
	}

	/************************************************************************/

	@xTarget(HEADER.class) // la vue App Shell
	@xResource()
	@xPriority(10)
	public XMLElement sStyle() {
		return xElem(
				new CssReset(),
				new CssRippleEffect(),
				new CssTransition(),
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
		vProp(ViewNavBar.pHeight, "height: 3.5rem");

		vProp(ViewTabBar.pHeight, "height: 3.5rem");
		vProp(ViewTabBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%)");

		return xDiv(cMain, getAppShell());
	}

	/**
	 * @return
	 */
	private XMLElement getAppShell() {
		return xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIdPage, "Appshell"));
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
	public interface JSController extends JSClass, IJSNodeTemplate, IJSDataDriven {
		JSActionManager actionManager = JSClass.declareTypeClass(JSActionManager.class);
		TActionEvent actionEvent = JSClass.declareType();

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");

			let(arrPage, JSArray.newLitteral());
			
			document().querySelector(cMain).appendChild(xElem(vMount(arrPage, JSString.value(MountPage.class))));
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
				document().querySelector(cMain).children().at(0).remove();
				animMgr.doActivityActive(activity);
			}), ")");

			// doChangeContent(arrPage);
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
