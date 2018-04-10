/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;

/**
 * @author gauth
 *
 */
public interface JSChangeCtx extends JSType {

	public JSNodeElement parent();
	public JSNodeElement element();
	
	public JSString ope();
	public JSon row();
	public JSInt idx();
	
	public JSString property();
	public JSAny value();
	public JSAny old();
	
}
