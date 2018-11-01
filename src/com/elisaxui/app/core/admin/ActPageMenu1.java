/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.app.core.module.MntPage.TActivity;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
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
		
		let(aPage, page);
		JSWindow.window().attr("ActPageMenu1").set(aPage);
	}

}
