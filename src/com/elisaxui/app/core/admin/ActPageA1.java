/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.app.core.module.MntPage.MountArticle;
import com.elisaxui.app.core.module.MntPage.MountBtn;
import com.elisaxui.app.core.module.MntPage.MountNavBar;
import com.elisaxui.app.core.module.MntPage.MountTabBar;
import com.elisaxui.app.core.module.MntPage.TBtn;
import com.elisaxui.app.core.module.MntPage.TActivity;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */

@xExport
@xCoreVersion("1")
public interface ActPageA1 extends JSClass , IJSNodeTemplate, IJSMountFactory {
	
	TActivity aPage = JSClass.declareType();
	
	@xStatic(autoCall=true)
	default void initPage() {
		TActivity page = newJS(TActivity.class);
		page.titre().set("Page1");
		page.mountNavNar().set(MountNavBar.class);
		page.mountTabNar().set(MountTabBar.class);
		page.mountContentActivity().set(MountArticle.class);
		page.mountAction().set("ACTMOUNT1");
		
		TBtn btn = null;
		
		JSArray<TBtn> dataNabBar = JSArray.newLitteral() ;
		btn = newJS(TBtn.class);
		btn.titre().set("add_circle_outline");
		btn.action().set("AJOUT");
		btn.mountBtn().set(MountBtn.class);
		dataNabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("more_vert");
		btn.action().set("MORE_INFO");
		btn.mountBtn().set(MountBtn.class);
		dataNabBar.push(btn);
		
		page.dataNavBar().set(dataNabBar);
		
		JSArray<TBtn> dataTabBar = JSArray.newLitteral() ;

		btn.titre().set("schedule");
		btn.action().set("CHANGE");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("today");
		btn.action().set("TO_PAGE_B");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);

		btn = newJS(TBtn.class);
		btn.titre().set("apps");
		btn.action().set("TO_NEXT");
		btn.mountBtn().set(MountBtn.class);
		dataTabBar.push(btn);
		
		page.dataTabBar().set(dataTabBar);
		
		JSArray<TBtn> listeArticle = JSArray.newLitteral();

		for (int i = 0; i < 100; i++) {
			TBtn ligne1 = newJS(TBtn.class);
			ligne1.titre().set("Ligne AA" + i);
			listeArticle.push(ligne1);
		}
		JSArray<JSElement> listeContent = JSArray.newLitteral();
		listeContent.push(listeArticle);
		page.dataContentActivity().set(listeContent);
		
		
		let(aPage, page);
		JSWindow.window().attr("ActPageA").set(aPage);
	}

}
