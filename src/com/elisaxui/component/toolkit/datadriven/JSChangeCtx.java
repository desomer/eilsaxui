/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;

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
