/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.app.core.module.MntInput.MountMenuDivider;
import com.elisaxui.app.core.module.MntInput.MountMenuItem;
import com.elisaxui.app.core.module.MntInput.TInput;
import com.elisaxui.app.core.module.MntPage.MountMenuContainer;
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
public interface ActPageMenu1 extends JSClass, IJSNodeTemplate, IJSMountFactory {

	TActivity aPage = JSClass.declareType();

	@xStatic(autoCall = true)
	default void initPage() {
		TActivity page = newJS(TActivity.class);
		page.titre().set("PageMenu");
				
		JSArray<JSElement> listeArticle = JSArray.newLitteral();

		TInput anItem = newJS(TInput.class);
		anItem.label().set("Paramètres");
		anItem.iconId().set("settings"); 
		anItem.implement().set(MountMenuItem.class);
		listeArticle.push(anItem);
		
		anItem = newJS(TInput.class);
		anItem.label().set("Configuration");
		anItem.iconId().set("build");  
		anItem.implement().set(MountMenuItem.class);
		listeArticle.push(anItem);
		
		anItem = newJS(TInput.class);
		anItem.implement().set(MountMenuDivider.class);
		listeArticle.push(anItem);
		
		anItem = newJS(TInput.class);
		anItem.label().set("Aide");
		anItem.iconId().set("help_outline");  
		anItem.implement().set(MountMenuItem.class);
		listeArticle.push(anItem);
		
		JSArray<JSElement> listeContent = JSArray.newLitteral();
		listeContent.push(listeArticle);
		page.mountContentActivity().set(MountMenuContainer.class);
		page.dataContentActivity().set(listeContent);
		
		let(aPage, page);
		JSWindow.window().attr("ActPageMenu1").set(aPage);
	}

}
