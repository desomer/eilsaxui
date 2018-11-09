/**
 * 
 */
package com.elisaxui.app.core.admin;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.app.core.module.CmpModuleComponent.JSMount;
import com.elisaxui.app.core.module.MntPage.MountArticle2;
import com.elisaxui.app.core.module.MntPage.MountInfoBox;
import com.elisaxui.app.core.module.MntPage.MountMain;
import com.elisaxui.app.core.module.MntPage.MountMenu;
import com.elisaxui.app.core.module.MntPage.MountPage;
import com.elisaxui.app.core.module.MntPage.TActivity;
import com.elisaxui.app.core.module.MntPage.TBtn;
import com.elisaxui.app.core.module.MntPage.TMain;
import com.elisaxui.component.page.ScnPage;
import com.elisaxui.component.toolkit.core.JSActionManager;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionEvent;
import com.elisaxui.component.toolkit.core.JSActionManager.TActionInfo;
import com.elisaxui.component.toolkit.core.JSActivityHistoryManager;
import com.elisaxui.component.toolkit.core.JSActivityHistoryManager.TIntent;
import com.elisaxui.component.toolkit.core.JSActivityStateManager.TAnimConfiguration;
import com.elisaxui.component.toolkit.core.JSActivityStateManager;
import com.elisaxui.component.toolkit.core.JSAnimationManager;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.widget.button.CssRippleEffect.JSRippleEffect;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
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
import com.elisaxui.core.xui.xml.target.FILE;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnPage")
@xComment("Scene Page A")
public class ScnPageA extends ScnPage implements ICSSBuilder {

	
	public static final String[] listPhotos= new String[] {
			"https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/6337/light-coffee-pen-working.jpg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/117729/pexels-photo-117729.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/211122/pexels-photo-211122.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/210745/pexels-photo-210745.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/164482/pexels-photo-164482.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/256417/pexels-photo-256417.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/207665/pexels-photo-207665.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/272228/pexels-photo-272228.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/287336/pexels-photo-287336.jpeg?h=350&auto=compress&cs=tinysrgb"
	}; 
	
	public static CSSClass cIdlog;
	
	
	@xTarget(AFTER_BODY.class)
	@xResource(id = "xControlerPageA.mjs", async = false) // insert un <Script type module> dans le body
	@xImport(idClass = JSMount.class)
	@xImport(idClass = JSActivityStateManager.class)
	@xImport(idClass = JSRippleEffect.class)
	@xImport(idClass = JSActionManager.class)
	@xImport(idClass = JSActivityHistoryManager.class)
	@xImport(idClass = JSDataBinding.class)
	@xImport(idClass = ActPageA1.class)  
	@xImport(idClass = ActPageA2.class)
	@xImport(idClass = ActPageA3.class)
	@xImport(idClass = ActPageA4.class)
	@xImport(idClass = ActPageMenu1.class)
	public XMLElement xControlerPage() {
		return xElem(JSController.class);
	}

	@xTarget(FILE.class)
	@xResource(id = "xListPage.mjs") // insert un <Script type module> dans le body
	public XMLElement xListPage() {
		return xElem(ActPageA1.class, ActPageA2.class, ActPageA3.class, ActPageA4.class, ActPageMenu1.class);
	}
	
	@xTarget(HEADER.class)
	@xResource
	@xPriority(1)
	public XMLElement preLoadPageB() {
		return xElem(xLinkPrerender("/rest/page/fr/fra/id/ScnPageB"));
	}
	/************************************************************************/

	@xTarget(HEADER.class)
	@xResource()
	public XMLElement xStylePart() {
		return xElem(xStyle(() -> {
			sOn(cIdlog, () -> {
				css("height: 50px; width: 100%;  border: 1px solid red;  background-color: #ef19233d;  position: fixed;  bottom: 75px;  z-index: 100;"); 
			});

		}));
	}
		
	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {

		double heightnav = 3.5;
		double heighttab = 3.5;

		vProp(ViewPageLayout.pStyleContent,
				"min-height: 100vh; min-width: inherit; "
				+ "background-color: rgb(245, 243, 237); "
				+ "padding-top: " + (heightnav + .5) + "rem; "
				+ "padding-bottom: "+ heighttab + "rem");

		vProp(ViewNavBar.pStyle,
				"background:linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%);"
						+ "box-shadow: 20px 6px 12px 9px rgba(0, 0, 0, 0.22), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)");
		vProp(ViewNavBar.pHeight, "height: " + heightnav + "rem");

		vProp(ViewTabBar.pHeight, "height: " + heighttab + "rem");
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
	static JSArray<TActivity> arrPage;
	static JSArray<TActivity> arrPage2;
	static JSActivityStateManager animMgr;
	static JSNodeElement activity;
	static JSNodeElement activityDest;
	static TActivity aPage;

	static JSArray<TMain> arrMount;
	static TMain aMount;
	static TIntent aIntent;

	@xExport
	@xCoreVersion("1")
	public interface JSController extends JSClass, IJSNodeTemplate, IJSMountFactory {
		JSActionManager actionManager = JSClass.declareTypeClass(JSActionManager.class);
		JSActivityHistoryManager activityManager = JSClass.declareTypeClass(JSActivityHistoryManager.class);
		
		TActionEvent actionEvent = JSClass.declareType();

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			__("window.datadrivensync=true");

			let(arrMount, JSArray.newLitteral());
			
			JSNodeElement querySelector = document().querySelector(getcMain());
			querySelector.appendChild(xElem(vMount(arrMount, JSString.value(MountMain.class))));
			/*************************************************/
			
			let(aMount, newJS(TMain.class));
			aMount.mount().set(MountPage.class);
			let(arrPage, JSArray.newLitteral());
			aMount.data().set(arrPage);
			
			let(aPage, JSWindow.window().attr("ActPageA"));
			arrPage.push(aPage);
			aPage.set(JSWindow.window().attr("ActPageB"));
			arrPage.push(aPage);
			aPage.set(JSWindow.window().attr("ActPageC"));
			arrPage.push(aPage);
			aPage.set(JSWindow.window().attr("ActPageD"));
			arrPage.push(aPage);
			
			arrMount.push(aMount);
		
			/************************************************/

//			aMount.set( newJS(TMain.class));
//			aMount.mount().set(MountInfoBox.class);
//			aMount.data().set(JSString.value("info"));
//			arrMount.push(aMount);
			
			/************************************************/
			setTimeout(()->{
				// lance en differer pour etre en dernier . le faire plutot sur l action mount de la Page1
				aMount.set( newJS(TMain.class));
				aMount.mount().set(MountMenu.class);
				aMount.data().set(JSWindow.window().attr("ActPageMenu1"));
				arrMount.push(aMount);
			}, 100);

			

			/************************************************/
			consoleDebug(txt("arrMount ="), arrMount);
						
			/**************************************************************************************/
			let(animMgr, newJS(JSActivityStateManager.class));

//			doMoveOnAction();
			
			
			actionManager.addAction(JSString.value("ACTMOUNT1"), _this(), fct(() -> {
				consoleDebug("'act 1 mount'");
				let(activity, document().querySelector("#Page1"));
				document().querySelector(getcMain()).children().at(0).remove();
				animMgr.doActivityActive(activity);
				__("window.datadrivensync=false");
				
				
//				__("var stateObj = { foo: \"bar\" };\r\n" + 
//						"history.pushState(stateObj, \"page 2\", \"/bar.html\")");
			}));

			actionManager.addAction(JSString.value("CHANGE"), _this(), fct(() -> {
				doChangeContent(arrPage);
			}));
			
			actionManager.addAction(JSString.value("burger"), _this(), fct(() -> {
				consoleDebug("'burger ok'");
				let(aIntent, newJS(TIntent.class));
				aIntent.activityDest().set("PageMenu");
				
				TAnimConfiguration configAnim = newJS(TAnimConfiguration.class);
				configAnim.transitionId().set(JSAnimationManager.LEFT_TO_FRONT);
				configAnim.srcVisible().set(true);
				configAnim.srcTranslation().set(20);
				aIntent.animConfig().set(configAnim);
				activityManager.doRouteToActivity(aIntent);
			}));
			
			actionManager.addAction(JSString.value("AJOUT"), _this(), fct(() -> {
				consoleDebug("'ok'", "window.ActPageA.contentArticles[0][0]");
				__("window.ActPageA.contentArticles[0][0].titre=window.innerHeight");
				__("window.ActPageA.contentArticles[0][1].titre=window.outerHeight");
				__("window.ActPageA.contentArticles[0][2].titre=document.documentElement.clientHeight");
			}));
			
			actionManager.addAction(JSString.value("TO_PAGE_B"), _this(), fct(() -> {
				__("location.assign('/rest/page/fr/fra/id/ScnPageB')");
			}));
			
			actionManager.addAction(JSString.value("TO_NEXT"), _this(), fct(() -> {
				let(aIntent, newJS(TIntent.class));
				aIntent.activityDest().set("Page2");
				TAnimConfiguration configAnim = newJS(TAnimConfiguration.class);
				configAnim.transitionId().set(JSAnimationManager.BOTTOM_TO_FRONT);
				aIntent.animConfig().set(configAnim);
				activityManager.doRouteToActivity(aIntent);
			}));
			
			actionManager.addAction(JSString.value("TO_PAGE3"), _this(), fct(() -> {
				let(aIntent, newJS(TIntent.class));
				aIntent.activityDest().set("Page3");
				TAnimConfiguration configAnim = newJS(TAnimConfiguration.class);
				configAnim.transitionId().set(JSAnimationManager.RIGHT_TO_FRONT);
				configAnim.srcTranslation().set(30);
				aIntent.animConfig().set(configAnim);
				activityManager.doRouteToActivity(aIntent);
			}));
			
			
			actionManager.addAction(JSString.value("MORE_INFO"), _this(), fct(() -> {
				let(aIntent, newJS(TIntent.class));
				aIntent.activityDest().set("Page4");
				TAnimConfiguration configAnim = newJS(TAnimConfiguration.class);
				configAnim.transitionId().set(JSAnimationManager.RIGHT_TO_FRONT);
				configAnim.srcTranslation().set(0);
				aIntent.animConfig().set(configAnim);
				activityManager.doRouteToActivity(aIntent);
			}));
			
			actionManager.addAction(JSString.value("TO_PAGE1"), _this(), fct(() -> {
				activityManager.doRouteToPrevActivity();
			}));
			
			actionManager.addAction(JSString.value("SWIPE_HEADER"), _this(), fct(() -> {
				activityManager.doRouteToPrevActivity();
			}));
			actionManager.currentActionInfo().modeTouch().set(TActionInfo.MODE_TOUCH_SWIPE);
			
			actionManager.addAction(JSString.value("Overlay"), _this(), fct(() -> {
				activityManager.doRouteToPrevActivity();
			}));
			actionManager.currentActionInfo().modeTouch().set(TActionInfo.MODE_TOUCH_SWIPE);
					
		}

		/*****************************************************************************************/
		@xStatic()
		default void doChangeContent(JSArray<TActivity> arrPage) {
			arrPage.setArrayType(TActivity.class);
			consoleDebug(txt("change article"), arrPage.at(0).mountContentActivity());
			
			arrPage.at(0).dataContentActivity().pop(); // suppresion de l'article

		//	setTimeout(()->{
				arrPage.at(0).mountContentActivity().set(MountArticle2.class); // change le template	
				arrPage.at(0).dataContentActivity().push(JSArray.newLitteral()); // nouvelle article

				let(list, arrPage.at(0).dataContentActivity().at(0));

				forIdx(idx, 0, 10)._do(() -> {
					TBtn ligne1 = newJS(TBtn.class);
					ligne1.titre().set(calc(txt("Ligne"), "+", "Math.floor(Math.random() * 11)"));
					list.push(ligne1);
				});
		//	}, 1000);
			
			
		}

		
//		/****************************************************************************************/
//		@xStatic()
//		default void doMoveOnAction() {
//			actionManager.onStart(fct(actionEvent, () -> {
//				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
//					/***************************************************/
//					let(animMgr, newJS(JSActivityStateManager.class));
//					animMgr.doActivityFreeze(actionEvent.activity(), actionEvent.scrollY());
//					animMgr.doFixedElemToAbsolute(actionEvent.activity());
//					/***************************************************/
//				});
//			}));
//			actionManager.onMove(fct(actionEvent, () -> {
//				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
//					/***************************************************/
//					consoleDebug(actionEvent.infoEvent().distance());
//
//					actionEvent.activity().style().attr("transform")
//							.set(txt("translate3d(", actionEvent.infoEvent().deltaX(), "px,",
//									actionEvent.infoEvent().deltaY(), "px, 0px)"));
//					/***************************************************/
//				});
//			}));
//			actionManager.onStop(fct(actionEvent, () -> {
//				_if(actionEvent.actionTarget().notEqualsJS(null)).then(() -> {
//					/***************************************************/
//					let(animMgr, newJS(JSActivityStateManager.class));
//					let(activity, actionEvent.activity());
//
//					animMgr.doActivityDeFreeze(activity);
//					animMgr.doInitScrollTo(activity);
//					animMgr.doFixedElemToFixe(activity);
//					activity.style().attr("transform").set(txt());
//					/***************************************************/
//				});
//			}));
//		}
	}

}
