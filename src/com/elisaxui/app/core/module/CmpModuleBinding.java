/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.MODULE;

/**
 * @author gauth
 *
 */
public class CmpModuleBinding extends XHTMLPart {

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportLib() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js")
				);
	}

	/**************************************************************/
	@xTarget(MODULE.class)
	@xResource(id = "xComPoly.js")
	@xPriority(50)
	public XMLElement xImportCom() {
		return xListNode( 
				xScriptSrc("https://cdn.polyfill.io/v2/polyfill.js?features=default,fetch"),
				xInclude(TKCom.class)
				);
	}
	
	/**************************************************************/
	@xTarget(MODULE.class)
	@xResource(id = "xdatadriven.js")
	public XMLElement xImportDataDriven() {
		return xListNode( xInclude(
				TKPubSub.class,
				JSDataDriven.class,
				JSDataSet.class)
				);
	}

	/**************************************************************/
	@xTarget(MODULE.class)
	@xResource(id = "xBinding.js")
	public XMLElement xImportBinding() {
		return xListNode(  xInclude(
				JSDomBuilder.class,
				JSDataBinding.class)
				);
	}
		
}
