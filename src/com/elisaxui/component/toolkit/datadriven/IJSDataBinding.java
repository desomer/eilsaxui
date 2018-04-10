/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;

/**
 * @author gauth
 *
 */
public interface IJSDataBinding {
	
	default JSFunction vChangeable(JSAny value) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		String[] attr = value.toString().split("\\.");
		fct.zzSetComment("vChangeable("+value+")");
		fct._return("JSDataBinding.initVChangeableText(domItem,"+value+",'"+attr[1]+"')");
		return fct;
	}
	
	default JSFunction vChangeable(JSElement row, JSAny value, JSFunction fctOnChange) {
		
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct.zzSetComment("vChangeable("+row+","+value+", ()->...)");
		fctOnChange.zzSetComment("");
		String[] attr = value.toString().split("\\.");
		fct._return("JSDataBinding.initVChangeableFct(domItem,"+row+","+value+",'"+attr[1]+"',"+fctOnChange+")");	
		return fct;
	}
	
	default JSFunction vOnDataChange(JSElement row, JSAny value, JSFunction fctOnChange) {		
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		fct.zzSetComment("vOnDataChange("+row+","+value+", ()->...)");
		fctOnChange.zzSetComment("");
		String[] attr = value.toString().split("\\.");
		fct.__("JSDataBinding.initOnDataChange(domItem,"+row+","+value+",'"+attr[1]+"',"+fctOnChange+")");		
		return fct;
	}

	
}
