/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.app.core.admin.ScnPageA;
import com.elisaxui.app.core.module.MntInput.TInput;
import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.widget.button.CssRippleEffect;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.container.ViewCard;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.menu.ViewMenuContainer;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.XHTMLPartJS;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.mount.MountFactory;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class MntPage extends XHTMLPartJS implements IJSDataDriven, IJSMountFactory, IJSDataBinding {

	static TMain aMainMount;
	static JSArray<TActivity> listPage;
	static TActivity aPage;
	static TContainer aContainer;
	static JSArray<JSAny> listArticle;
	static TInput aInput;
	static JSArray<JSElement> anListElem;
	static TBtn aBtn;
	static JSNodeElement aNode;

	@xTarget(HEADER.class)
	@xResource()
	@xPriority(4)
	public XMLElement xImport() {
		return xElem(
				xLinkCssPreload("https://fonts.googleapis.com/icon?family=Material+Icons"));
	}

	@xTarget(AFTER_BODY.class)
	@xResource()
	public XMLElement xImportAfter() {
		return xElem(
				xLinkCssPreload("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css"));
	}

	public static class MountMain extends MountFactory {
	}

	@xMount(MountMain.class)
	public XMLElement createMain(JSArray<TMain> arrMount) {
		return xElem(
				vFor(arrMount, aMainMount, xElem(vMount(aMainMount.data(), aMainMount.mount()))));
	}

	/***************************************************************************/
	public static class MountPage extends MountFactory {
	}

	@xMount(MountPage.class)
	public XMLElement createPage(JSArray<TActivity> arrPage) {
		return xElem(vFor(arrPage, aPage, xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIsNoVisible, true)
				.vProp(ViewPageLayout.pWithTabBar, true)
				.vProp(ViewPageLayout.pIdPage, aPage.titre())
				.vProp(ViewPageLayout.pArticle,
						vMountChangeable(aPage.mountContentActivity(), vMount(aPage, aPage.mountContentActivity())))
				.vProp(ViewNavBar.pChildren, vMount(aPage, aPage.mountNavNar()))
				.vProp(ViewTabBar.pChildren, vMount(aPage.dataTabBar(), aPage.mountTabNar())))));
	}

	public static class MountTabBar extends MountFactory {
	}

	@xMount(MountTabBar.class)
	public XMLElement createTabBar(JSArray<TBtn> arrBtn) {
		return xElem(vFor(arrBtn, aBtn,
				xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, vMount(aBtn, aBtn.mountBtn()))));
	}

	public static class MountNavBar extends MountFactory {
	}

	@xMount(MountNavBar.class)
	public XMLElement createNavBar(TActivity aPage) {
		return xElem(new ViewBtnBurger(), xDiv(ViewNavBar.rightAction,
				xElem(vFor(aPage.dataNavBar(), aBtn, xElem(vMount(aBtn, aBtn.mountBtn()))))));
	}

	/***************************************************************************/

	public static class MountMenu extends MountFactory {
	}

	@xMount(MountMenu.class)
	public XMLElement createPageMenu(TActivity aPage) {

		return xElem(new ViewPageLayout()
				.vProp(ViewPageLayout.pIsNoVisible, true)
				.vProp(ViewPageLayout.pClassType, ViewMenuContainer.cPageMenu)
				.vProp(ViewPageLayout.pWithTabBar, false)
				// .vProp(ViewNavBar.pStyle, "b:r")
				.vProp(ViewPageLayout.pIdPage, aPage.titre())

				.vProp(ViewPageLayout.pArticle, xElem(new ViewMenuContainer()
						.vProp(ViewMenuContainer.pId, "MenuPage")
						.vProp(ViewMenuContainer.pChildren, vMount(aPage, aPage.mountContentActivity()))))

				.vProp(ViewNavBar.pChildren, vMount(aPage, aPage.mountNavNar())));
	}

	/******************************************************************/
	public static class MountInfoBox extends MountFactory {
	}

	@xMount(MountInfoBox.class)
	public XMLElement createInfoBox(JSString aInfo) {
		return xDiv(ScnPageA.cIdlog, aInfo);
	}

	/************************************************************************/
	public static class MountBtn extends MountFactory {
	}

	@xMount(MountBtn.class)
	public XMLElement createBtn(TBtn btn) {
		return xButton(xIdAction(btn.action()),
				ViewNavBar.cActionBtnContainer, CssRippleEffect.cRippleEffect, xAttr("type", "button"),
				xI(ViewNavBar.actionBtn, ViewNavBar.material_icons, vChangeable(btn.titre())));
	}

	public static class MountBtn2 extends MountBtn {
	}

	@xMount(MountBtn2.class)
	public XMLElement createBtn2(TBtn btn) {
		return xSpan(btn.titre());
	}

	/************************************************************************/
	public static class MountMenuContainer extends MountFactory {
	}

	@xMount(MountMenuContainer.class)
	public XMLElement createMenuContainer(TActivity aPage) {
		return xElem(vFor(aPage.dataContentActivity(), anListElem, xUl(
				vFor(anListElem, aInput, xElem(vMount(aInput, aInput.implement()))))));
	}
	/*******************************************************************/
	
	public static class MountInputContainer extends MountFactory {
	}

	@xMount(MountInputContainer.class)
	public XMLElement createInputContainer(TActivity aPage) {
		return xElem(vFor(aPage.dataContentActivity(), anListElem, xDiv(
				vFor(anListElem, aInput, xDiv(vMount(aInput, aInput.implement()))))));
	}
	
	/******************************************************************/
	public static class MountContainer extends MountFactory {
	}

	@xMount(MountContainer.class)
	public XMLElement createContainer(TActivity aPage) {
		return xElem(vFor(aPage.dataContentActivity(), anListElem,
				xDiv(vFor(anListElem, aContainer, xElem(vMount(aContainer, aContainer.implement()))))));
	}

	public static class MountCardContainer extends MountFactory {
	}

	@xMount(MountCardContainer.class)
	public XMLElement createCardContainer(TContainer aCont) {
		return xElem( vBeforeMount(fct(aNode, ()->{ 
			consoleDebug("'ok'", aCont, aNode);  }), xElem(new ViewCard())));
	}

	public static class MountArrayContainer extends MountFactory {
	}

	@xMount(MountArrayContainer.class)
	public XMLElement createArrayContainer(TContainer aCont) {
		return xDiv();
	}

	public static class MountBorderContainer extends MountFactory {
	}

	@xMount(MountBorderContainer.class)
	public XMLElement createBorderContainer(TContainer aCont) {
		return xDiv();
	}


	/************************************************************************/
	public static class MountArticle extends MountFactory {
	}

	@xMount(MountArticle.class)
	public XMLElement createArticle(TActivity aPage) {
		return xElem(vFor(aPage.dataContentActivity(), anListElem,
				xElem(xId("cntArticle"),
						vFor(anListElem, aBtn, xDiv(xId("cntSpan"), xSpan(vChangeable(aBtn.titre())))))));
	}

	public static class MountArticle2 extends MountArticle {
	}

	@xMount(MountArticle2.class)
	public XMLElement createArticle2(TActivity aPage) {
		return xElem(vFor(aPage.dataContentActivity(), anListElem,
				xDiv(vFor(anListElem, aBtn, xH1(aBtn.titre())))));
	}

	/********************************************************/
	// les dto
	/********************************************************/
	public interface TMain extends JSType {
		JSString mount();

		JSAny data();
	}

	public interface TActivity extends JSType {

		JSString titre();

		JSString mountAction();

		JSString mountTabNar();

		JSArray<TBtn> dataTabBar();

		JSString mountNavNar();

		JSArray<TBtn> dataNavBar();

		JSString mountContentActivity();

		JSArray<JSElement> dataContentActivity();
	}

	public interface TContainer extends JSType {
		JSString implement();

		JSString layoutContraint();

		JSAny data();
	}

	public interface TContainerData extends JSType {
		TBackgroundInfo background();

		JSString text();

		JSString actionId();

		JSElement data();
	}
	
	public interface TBackgroundInfo extends JSType {
		JSString mode();
		JSString opacity();
		JSString css();
	}

	public interface TBtn extends JSType {
		JSString titre();

		JSString action();

		JSString mountBtn();
	}

}
