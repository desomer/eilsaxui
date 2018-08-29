/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.app.core.module.MntInput.MountInputCheckBox;
import com.elisaxui.app.core.module.MntInput.MountInputText;
import com.elisaxui.app.core.module.MntInput.TInput;
import com.elisaxui.app.core.module.MntPage.MountBtn;
import com.elisaxui.app.core.module.MntPage.MountCard;
import com.elisaxui.app.core.module.MntPage.MountNavBar;
import com.elisaxui.app.core.module.MntPage.MountTabBar;
import com.elisaxui.app.core.module.MntPage.TBtn;
import com.elisaxui.app.core.module.MntPage.TPage;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */

@xExport
@xCoreVersion("1")
public interface ActPageA3 extends JSClass , IJSNodeTemplate, IJSMountFactory {
	
	TPage aPage = JSClass.declareType();
	
	@xStatic(autoCall=true)
	default void initPage() {
		TPage page = newJS(TPage.class);
		page.titre().set("Page3");
		page.mountNavNar().set(MountNavBar.class);
		page.mountTabNar().set(MountTabBar.class);
		page.mountArticles().set(MountCard.class);
		page.mountAction().set("ACTMOUNT3");
		
		TBtn btn = null;
		
		JSArray<TBtn> dataNabBar = JSArray.newLitteral() ;
		btn = newJS(TBtn.class);
		btn.titre().set("arrow_back");
		btn.action().set("TO_PAGE1");
		btn.mountBtn().set(MountBtn.class);
		dataNabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("more_vert");
		btn.action().set("E");
		btn.mountBtn().set(MountBtn.class);
		dataNabBar.push(btn);
		
		page.dataNavBar().set(dataNabBar);
		
		JSArray<TBtn> dataTabBar = JSArray.newLitteral() ;

		btn.titre().set("schedule");
		btn.action().set("A");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("today");
		btn.action().set("B");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("apps");
		btn.action().set("C");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);
		
		page.dataTabBar().set(dataTabBar);
		
		JSArray<JSElement> listeArticle = JSArray.newLitteral();

		for (int i = 0; i < 3; i++) {
			TInput testInput = newJS(TInput.class);
			testInput.label().set("test input"+i);
			testInput.value().set(JSString.value("AAA"+i));
			testInput.implement().set(MountInputText.class);
			listeArticle.push(testInput);
		}

		JSArray<JSElement> listeArticle2 = JSArray.newLitteral();

		for (int i = 0; i < 1; i++) {
			TInput testInput = newJS(TInput.class);
			testInput.label().set("test input B"+i);
			testInput.value().set(true);
			testInput.implement().set(MountInputCheckBox.class);
			listeArticle2.push(testInput);
		}
		
		JSArray<JSElement> listeContent = JSArray.newLitteral();
		listeContent.push(listeArticle);
		listeContent.push(listeArticle2);
		page.contentArticles().set(listeContent);
		
		
		let(aPage, page);
		JSWindow.window().attr("ActPageC").set(aPage);
	}

}
