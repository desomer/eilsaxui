/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.core.JSActionManager;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.widget.button.CssRippleEffect.JSRippleEffect;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.mount.MountFactory;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.FILE;

/**
 * @author gauth
 *
 */
public class CmpModuleComponent extends XHTMLPart {

	public static final String X_COMPONENT_JS = "xComponent.mjs";
	public static final String X_MOUNT_JS = "xMount.mjs";
	
	@xTarget(FILE.class)
	@xResource(id=X_COMPONENT_JS)
	@xImport(idClass=JSActionManager.class)
	public XMLElement xLoad() {
		return xModule(JSRippleEffect.class);
	}
	
	
	@xTarget(FILE.class)
	@xResource(id = X_MOUNT_JS)
	@xImport(idClass=JSDataDriven.class)
	@xImport(idClass=JSDataBinding.class)
	public XMLElement xFactory() {
		return xModule(JSMount.class, new MntPage());
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
