/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;

/**
 * @author gauth
 *
 */
public interface JSChangeCtx extends JSClass {

	public JSDomElement parent();
	public JSString ope();
	public JSon row();
	public JSInt idx();
	
	public JSString property();
	public JSAny value();
	public JSAny old();
	
}
