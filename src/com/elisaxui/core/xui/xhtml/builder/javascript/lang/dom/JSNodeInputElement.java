/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
public class JSNodeInputElement extends JSNodeElement {
	public JSString value() {
		return castAttr(new JSString(), "value");
	}
	
	public JSString checked() {
		return castAttr(new JSString(), "checked");
	}
	
	public JSString type() {
		return castAttr(new JSString(), "type");
	}
}
