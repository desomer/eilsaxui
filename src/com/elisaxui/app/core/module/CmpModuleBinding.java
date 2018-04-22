/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.JSDataBinding;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;
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
	@xResource(id = "xdatadriven.js")
	public XMLElement xImportDataDriven() {
		return xListNode( xImport(
				TKPubSub.class,
				JSDataDriven.class,
				JSDataSet.class)
				);
	}

	/**************************************************************/
	@xTarget(MODULE.class)
	@xResource(id = "xBinding.js")
	public XMLElement xImportBinding() {
		return xListNode(  xImport(
				JSDomBuilder.class,
				JSDataBinding.class)
				);
	}
		
}
