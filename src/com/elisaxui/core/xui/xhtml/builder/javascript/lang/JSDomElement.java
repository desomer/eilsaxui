/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;

/**
 * @author gauth
 *
 */
public class JSDomElement extends JSAny  implements IJSClassInterface {

	public JSDomElement appendChild(Object element) {
		return  callMth("appendChild", element);
	}
	
	public JSDomElement remove() {
		return  callMth("remove");
	}
	
	public JSString textContent() {
		return castAttr(new JSString(), "textContent");
	}
	
	public JSArray<JSDomElement> childNodes() {
		return castAttr(new JSArray<JSDomElement>().setArrayType(JSDomElement.class), "childNodes");
	}
	
	public JSDomElement querySelector(CSSSelector selector)
	{
		return callTyped(new JSDomElement(), "querySelector", ""+selector);
	}
	
	public JSDomElement querySelector(JSAny variable)
	{
		return callTyped(new JSDomElement(), "querySelector", variable);
	}
	
	public JSDomElement querySelector(Object... selector)
	{
		if (selector.length==1 && selector[0] instanceof JSAny)
			return querySelector((JSAny)selector[0]);	
		else
			return querySelector(CSSSelector.onPath(selector));
	}
	
	public JSArray<JSDomElement> querySelectorAll(CSSSelector selector)
	{
		return callTyped(new JSArray<JSDomElement>(), "querySelectorAll", ""+selector);
	}
	
	public JSArray<JSDomElement> querySelectorAll(JSAny variable)
	{
		return callTyped(new JSArray<JSDomElement>(), "querySelectorAll", variable);
	}
	
	public JSArray<JSDomElement> querySelectorAll(Object... selector)
	{
		if (selector.length==1 && selector[0] instanceof JSAny)
			return querySelectorAll((JSAny)selector[0]);	
		else
			return querySelectorAll(CSSSelector.onPath(selector));
	}
	
	public <E extends JSAny> E firstNodeValue()
	{
			return this.childNodes().at(0).attr("nodeValue");
	}
	
}
