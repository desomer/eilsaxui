/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.app.core.admin.JSActionManager;
import com.elisaxui.app.core.admin.JSPageAnimation;
import com.elisaxui.app.core.admin.JSTouchManager;
import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.FILE;

/**
 * @author gauth
 *
 */
public class CmpModuleCore extends XHTMLPart {

	public static final String X_STANDARD_JS = "xStandard.js";
	public static final String X_BINDING_JS = "xBinding.js";
	public static final String X_DATADRIVEN_JS = "xDatadriven.js";
	public static final String X_COM_JS = "xCom.js";
	public static final String X_CORE_JS = "xCore.js";

	/**********************************************************************/
	
	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportLib() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"));
	}
	
	/**********************************************************************/
	
	@xTarget(FILE.class)
	@xResource(id = X_STANDARD_JS)
	@xImport(idClass = TKPubSub.class)
	public XMLElement xStandard() {
		return xModule(
				JSPageAnimation.class,
				JSTouchManager.class,
				JSActionManager.class,
				new TKQueue());
	}

	@xTarget(FILE.class)
	@xResource(id = X_CORE_JS)
	public XMLElement xCore() {
		return xModule(
				TKPubSub.class,
				JSDomBuilder.class);
	}

	/**************************************************************/
	@xTarget(FILE.class)
	@xResource(id = X_COM_JS)
	@xPriority(50)
	public XMLElement xImportCom() {
		return xModule(
				xScriptSrc("https://cdn.polyfill.io/v2/polyfill.js?features=default,fetch"),
				TKCom.class);
	}

	/**************************************************************/
	
	@xTarget(FILE.class)
	@xResource(id = X_DATADRIVEN_JS)
	@xImport(idClass = TKPubSub.class)
	public XMLElement xImportDataDriven() {
		return xModule(
				JSDataDriven.class,
				JSDataSet.class);
	}

	/**************************************************************/
	@xTarget(FILE.class)
	@xResource(id = X_BINDING_JS)
	public XMLElement xImportBinding() {
		return xModule(JSDataBinding.class);
	}

}
