/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xInLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
public interface IJSMountFactory extends JSClass {

	@xInLine
	default JSFunction vMount(JSElement aRow,  JSString mountId)
	{
		return fct(()->_return("JSDataBinding.mount(",mountId,","+aRow+")")).zzSetComment("vMount "+mountId);
	}
	
}
