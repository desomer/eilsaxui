/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;

/**
 * @author gauth
 *
 */
public interface IJSDataBinding {

	/** todo js a mettre dans JSDataBinding */
	default JSFunction vBind(JSElement row, JSAny value) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct.__(domItem, ".datadriveninfo={row:", row, ", attr:'" + value + "'}")
			._return(value);

		return fct;
	}
	
	public interface XuiBindInfo extends JSType {
		JSString attr();

		JSon row();
	}
	
}
