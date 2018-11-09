/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.app.core.module.MntInput.MountInputCheckBox;
import com.elisaxui.app.core.module.MntInput.MountInputText;
import com.elisaxui.app.core.module.MntInput.TInput;
import com.elisaxui.app.core.module.MntPage.MountBtn;
import com.elisaxui.app.core.module.MntPage.MountCardContainer;
import com.elisaxui.app.core.module.MntPage.MountContainer;
import com.elisaxui.app.core.module.MntPage.MountInputContainer;
import com.elisaxui.app.core.module.MntPage.MountNavBar;
import com.elisaxui.app.core.module.MntPage.MountTabBar;
import com.elisaxui.app.core.module.MntPage.TBtn;
import com.elisaxui.app.core.module.MntPage.TContainer;
import com.elisaxui.app.core.module.MntPage.TContainerData;
import com.elisaxui.app.core.module.MntPage.TActivity;
import com.elisaxui.app.core.module.MntPage.TBackgroundInfo;
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
public interface ActPageA4 extends JSClass , IJSNodeTemplate, IJSMountFactory {
	
	TActivity aPage = JSClass.declareType();
	
	@xStatic(autoCall=true)
	default void initPage() {
		TActivity page = newJS(TActivity.class);
		page.titre().set("Page4");
		page.mountNavNar().set(MountNavBar.class);
		page.mountTabNar().set(MountTabBar.class);		
		
		JSArray<TBtn> dataNabBar = JSArray.newLitteral() ;
		TBtn btn = newJS(TBtn.class);
		btn.titre().set("arrow_back");
		btn.action().set("TO_PAGE1");
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
		
		/**********************************************************************/
		page.mountContentActivity().set(MountContainer.class);
		JSArray<JSElement> listeContent = JSArray.newLitteral();
		
		TContainer aContainer = newJS(TContainer.class); 		
		aContainer.implement().set(MountCardContainer.class);
		aContainer.layoutContraint().set("Flow");
		
		
		TBackgroundInfo aBgInfo = newJS(TBackgroundInfo.class);
		aBgInfo.opacity().set(0.9);
		aBgInfo.mode().set("Css");
		aBgInfo.css().set("url(" +ScnPageA.listPhotos[1] +") center / cover no-repeat");
		
		TContainerData aContData = newJS(TContainerData.class);
		aContData.background().set(aBgInfo);
		aContainer.data().set(aContData);
		JSArray<JSElement> listeCard = JSArray.newLitteral();
		listeCard.push(aContainer);
		
		listeContent.push(listeCard);
		
		page.dataContentActivity().set(listeContent);
		
		
		let(aPage, page);
		consoleDebug(aPage);
		JSWindow.window().attr("ActPageD").set(aPage);
	}

}
