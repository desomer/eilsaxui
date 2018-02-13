/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;

/**
 * @author gauth
 *
 */
public class JSElement extends JSClassInterface {

	public JSElement appendChild(Object element) {
		return (JSElement) _callMethod(null, "appendChild", element);
	}

}
