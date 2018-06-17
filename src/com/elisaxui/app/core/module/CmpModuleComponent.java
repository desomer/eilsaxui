/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.app.core.admin.MntPage;
import com.elisaxui.component.widget.button.CssRippleEffect.JSRippleEffect;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xImport;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.factory.MountFactory;
import com.elisaxui.core.xui.xml.target.FILE_MODULE;

/**
 * @author gauth
 *
 */
public class CmpModuleComponent extends XHTMLPart {

	@xTarget(FILE_MODULE.class)
	@xResource(id="xComponent.js")
	@xImport(export="JSActionManager", module="xStandard.js")
	public XMLElement xLoad() {
		return xElem(JSRippleEffect.class);
	}
	
	
	@xTarget(FILE_MODULE.class)
	@xResource(id = "xMount.js")
	@xImport(export = "JSDataDriven", module = "xDatadriven.js")
	@xImport(export = "JSDataBinding", module = "xBinding.js")
	public XMLElement xFactory() {
		return xElem(JSMount.class, new MntPage());
	}
	
	/********************************************************/
	// register des mount factory  
	/********************************************************/
	@xExport
	public interface JSMount extends JSClass {
		@xStatic(autoCall = true)
		default void doFactory() {
			__(MountFactory.register(new MntPage()));
		}
	}
	
}
