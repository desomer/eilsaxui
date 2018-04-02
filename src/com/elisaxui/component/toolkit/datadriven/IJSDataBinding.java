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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;

/**
 * @author gauth
 *
 */
public interface IJSDataBinding {
	
	/** todo js a mettre dans JSDataBinding */
	default JSFunction vChangeable(JSAny value) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		String[] attr = value.toString().split("\\.");
		fct.__(domItem, ".dataset['xui"+attr[1]+"']=true")
			._return(JSNodeTemplate.MTH_ADD_TEXT+"("+value+")");
		
		return fct;
	}
	
	/** todo js a mettre dans JSDataBinding */
	default JSFunction vChangeable(JSElement row, JSAny value, JSFunction fctOnChange) {
		JSChangeCtx changeCtx = JSClassBuilder.declareType(JSChangeCtx.class, "changeCtx");
		
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		String[] attr = value.toString().split("\\.");
		fct.__(domItem, ".dataset['xui"+attr[1]+"']=true")
			.__(domItem, ".XuiBindInfo={row:", row, ", attr:'" + attr[1] + "', fct:"+fctOnChange+"}")
			._var(changeCtx, "{}")
			.__(changeCtx, ".value=", value)
			.__(changeCtx, ".row=", row)
			.__("let ret = domItem.XuiBindInfo.fct.call(",domItem,",",changeCtx,")")
			._return(JSNodeTemplate.MTH_ADD_TEXT+"(ret)");
		
		return fct;
	}
	
	default JSFunction vOnDataChange(JSElement row, JSAny value, JSFunction fctOnChange) {
		JSChangeCtx changeCtx = JSClassBuilder.declareType(JSChangeCtx.class, "changeCtx");
		
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSFunction fct = new JSFunction().setParam(new Object[] { domItem });
		String[] attr = value.toString().split("\\.");
		fct.__(domItem, ".dataset['xui"+attr[1]+"']=true")
			.__(domItem, ".XuiBindInfo={row:", row, ", attr:'" + attr[1] + "', fct:"+fctOnChange+"}")
			._var(changeCtx, "{}")
			.__(changeCtx, ".value=", value)
			.__(changeCtx, ".row=", row)
			.__(changeCtx, ".element=", domItem)
			.__("domItem.XuiBindInfo.fct.call(",domItem,",",changeCtx,")")
		//	._return("")
			;
		
		return fct;
	}

	
}
