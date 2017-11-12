/**
 * 
 */
package com.elisaxui.xui.core.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;

/**
 * @author gauth
 *
 */
public interface JSChangeCtx extends JSClass {

	public JSString ope();
	public JSon row();
	public JSInt idx();
	
	public JSString property();
	public JSVariable value();
	public JSVariable old();
	
}
