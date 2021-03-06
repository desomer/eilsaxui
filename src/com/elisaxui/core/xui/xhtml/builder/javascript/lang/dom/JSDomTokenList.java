/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;

/**
 * @author gauth
 *
 */
public class JSDomTokenList extends JSAny {

	public JSInt length() {
		return castAttr(new JSInt(), "length");
	}

	public void add(CSSClass cClass) {
		callMth("add", cClass);
	}
	
	public void remove(CSSClass cClass) {
		callMth("remove", cClass);
	}
	
	public void toggle(CSSClass cClass) {
		callMth("toggle", cClass);
	}
	
	public JSBool contains(CSSClass cClass) {
		return callTyped(new JSBool(), "contains", cClass);
	}
}
