/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.core.JSActionManager;
import com.elisaxui.component.toolkit.core.JSActivityAnimation;
import com.elisaxui.component.toolkit.core.JSActivityManager;
import com.elisaxui.component.toolkit.core.JSActivityStateManager;
import com.elisaxui.component.toolkit.core.JSRequestAnimationFrame;
import com.elisaxui.component.toolkit.core.JSTouchManager;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.overlay.JSOverlay;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.IResourceLoader;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.FILE;

/**
 * @author gauth
 *
 */
public class CmpModuleCore extends XHTMLPart implements IResourceLoader {

	public static final String X_STANDARD_JS = "xStandard.mjs";
	public static final String X_BINDING_JS = "xBinding.mjs";
	public static final String X_DATADRIVEN_JS = "xDatadriven.mjs";
	public static final String X_COM_JS = "xCom.mjs";
	public static final String X_CORE_JS = "xCore.mjs";

	/**********************************************************************/
	@xTarget(FILE.class)
	@xPriority(10)
	@xResource(id = X_CORE_JS)
	public XMLElement xCore() {
		return xModule(
				TKPubSub.class,
				JSDomBuilder.class);
	}

	@xTarget(FILE.class)
	@xPriority(50)
	@xResource(id = X_STANDARD_JS)
	@xImport(idClass = TKPubSub.class)
	public XMLElement xStandard() {
		return xModule(
				JSOverlay.class,
				JSActivityStateManager.class,
				JSActivityAnimation.class,
				JSTouchManager.class,
				JSActionManager.class,
				JSActivityManager.class,
				JSRequestAnimationFrame.class,
				new TKQueue());
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
	@xImport(idClass = JSActionManager.class)
	public XMLElement xImportDataDriven() {
		return xModule(
				xScriptJS(loadResourceFromURL("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js", false)),
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
