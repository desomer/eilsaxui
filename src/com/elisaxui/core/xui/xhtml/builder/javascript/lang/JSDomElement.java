/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

/**
 * @author gauth
 *
 */
public class JSDomElement extends JSClassInterface {

	public JSDomElement appendChild(Object element) {
		return  call("appendChild", element);
	}
	
	public JSString textContent() {
		return castAttr(new JSString(), "textContent");
	}
	
	public JSArray<JSDomElement> childNodes() {
		return castAttr(new JSArray<JSDomElement>().setArrayType(JSDomElement.class), "childNodes");
	}
	

}
