/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public class JSNodeHTMLInputElement extends JSNodeElement {
	public JSString value() {
		return castAttr(new JSString(), "value");
	}
}