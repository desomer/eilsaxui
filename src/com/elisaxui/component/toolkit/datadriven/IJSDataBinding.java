/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.IInlineJS;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
public interface IJSDataBinding extends JSClass, IInlineJS {

	default JSFunction vChangeable(JSAny value) {
		String[] attr = value.toString().split("\\.");
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");

		return fct(domItem, () -> _return(JSContent.callstatic(JSDataBinding.class).initVChangeableText(domItem, value,
				JSString.value(attr[1]))))
			   .zzSetComment("vChangeable(" + value + ")");
	}

	default JSFunction vChangeable(JSElement row, JSAny value, JSFunction fctOnChange) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct.zzSetComment("vChangeable(" + row + "," + value + ", ()->...)");
		fctOnChange.zzSetComment("");  // pas de commentaire
		String[] attr = value.toString().split("\\.");
		fct._return("JSDataBinding.initVChangeableFct(domItem," + row + "," + value + ",'" + attr[1] + "',"
				+ fctOnChange + ")");
		return fct;
	}

	default JSFunction vOnDataChange(JSElement row, JSAny value, JSFunction fctOnChange) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct.zzSetComment("vOnDataChange(" + row + "," + value + ", ()->...)");
		fctOnChange.zzSetComment(""); // pas de commentaire
		String[] attr = value.toString().split("\\.");
		fct.__("JSDataBinding.initOnDataChange(domItem," + row + "," + value + ",'" + attr[1] + "'," + fctOnChange
				+ ")");
		return fct;
	}

}
