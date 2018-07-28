/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.widget.button.CssRippleEffect;
import com.elisaxui.component.widget.button.ViewBtnBurger;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.tabbar.ViewTabBar;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
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
public class MntPage extends XHTMLPart implements IJSDataDriven {

	static JSArray<TPage> listPage;
	static TPage aPage;
	static TBtn aBtn;
	static JSArray<JSAny> listArticle;

	@xTarget(HEADER.class)
	@xResource() 
	@xPriority(4)
	public XMLElement xImport() {
		return xElem(
				xLinkCssPreload("https://fonts.googleapis.com/icon?family=Material+Icons"));
	}
	
	@xTarget(AFTER_BODY.class)
	@xResource() 
	public XMLElement xImport2() {
		return xElem(
				xLinkCssPreload("https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css"));
	}
	
	public static class MountPage extends MountFactory {
	}

	@xMount(MountPage.class)
	public XMLElement createPage(JSArray<TPage> arrPage) {
		return xElem(
				vFor(arrPage, aPage, xElem(new ViewPageLayout()
						.vProp(ViewPageLayout.pIsNoVisible, true)
						.vProp(ViewPageLayout.pIdPage, aPage.titre())
						.vProp(ViewPageLayout.pArticle, xDiv(
								vFor(aPage.contentArticle(), listArticle, xDiv(
										vMount(aPage, aPage.mountArticle())))))
						.vProp(ViewNavBar.pChildren, vMount(aPage, aPage.mountNavNar()))
						.vProp(ViewTabBar.pChildren, vMount(aPage.dataTabBar(), aPage.mountTabNar())))));
	}

	public static class MountTabBar extends MountFactory {
	}

	@xMount(MountTabBar.class)
	public XMLElement createTabBar(JSArray<TBtn> arrBtn) {
		return xElem(
				vFor(arrBtn, aBtn,
						xLi(ViewTabBar.cFlex_1, ViewTabBar.cTextAlignCenter, vMount(aBtn, aBtn.mountBtn()))));
	}

	public static class MountNavBar extends MountFactory {
	}

	@xMount(MountNavBar.class)
	public XMLElement createNavBar(TPage aPage) {
		return xElem(new ViewBtnBurger(), xDiv(ViewNavBar.rightAction,
				xElem(vFor(aPage.dataNavBar(), aBtn, xElem(vMount(aBtn, aBtn.mountBtn()))))));
	}

	public static class MountBtn extends MountFactory {
	}

	@xMount(MountBtn.class)
	public XMLElement createBtn(TBtn btn) {
		return xButton(xIdAction(btn.action()),
				ViewNavBar.cActionBtnContainer, CssRippleEffect.cRippleEffect, xAttr("type", "button"),
				xI(ViewNavBar.actionBtn, ViewNavBar.material_icons, btn.titre()));
	}

	public static class MountBtn2 extends MountBtn {
	}

	@xMount(MountBtn2.class)
	public XMLElement createBtn2(TBtn btn) {
		return xSpan(btn.titre());
	}

	public static class MountArticle extends MountFactory {
	}

	@xMount(MountArticle.class)
	public XMLElement createArticle(TPage aPage) {
		return xElem(
				vFor(aPage.contentArticle().at(0), aBtn,
						xDiv(aBtn.titre())));
	}

	public static class MountArticle2 extends MountArticle {
	}

	@xMount(MountArticle2.class)
	public XMLElement createArticle2(TPage aPage) {
		return xElem(
				vFor(aPage.contentArticle().at(0), aBtn,
						xH1(aBtn.titre())));
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
		JSArray<TBtn> dataTabBar();

		JSString mountNavNar();
		JSArray<TBtn> dataNavBar();

		JSString mountArticle();
		JSArray<JSAny> contentArticle();
	}
}
