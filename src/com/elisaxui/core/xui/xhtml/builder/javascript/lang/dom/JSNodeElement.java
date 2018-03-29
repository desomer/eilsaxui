/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public class JSNodeElement extends JSAny  implements IJSClassInterface {

	public JSNodeElement appendChild(Object element) {
		return  callMth("appendChild", element);
	}
	
	public JSNodeElement remove() {
		return  callMth("remove");
	}
	
	public JSString textContent() {
		return castAttr(new JSString(), "textContent");
	}
	
	public JSString nodeName() {
		return castAttr(new JSString(), "nodeName");
	}
	
	public void addEventListener(Object event, JSFunction fct) {
		callMth("addEventListener", event, fct);
	}
	
	public JSArray<JSNodeElement> childNodes() {
		return castAttr(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "childNodes");
	}
	
	public JSNodeElement querySelector(CSSSelector selector)
	{
		return callTyped(new JSNodeElement(), "querySelector", ""+selector);
	}
	
	public JSNodeElement querySelector(JSAny variable)
	{
		return callTyped(new JSNodeElement(), "querySelector", variable);
	}
	
	public JSNodeElement querySelector(Object... selector)
	{
		if (selector.length==1 && selector[0] instanceof JSAny)
			return querySelector((JSAny)selector[0]);	
		else
			return querySelector(CSSSelector.onPath(selector));
	}
	
	public JSArray<JSNodeElement> querySelectorAll(CSSSelector selector)
	{
		return callTyped(new JSArray<JSNodeElement>(), "querySelectorAll", ""+selector);
	}
	
	public JSArray<JSNodeElement> querySelectorAll(JSAny variable)
	{
		return callTyped(new JSArray<JSNodeElement>(), "querySelectorAll", variable);
	}
	
	public JSArray<JSNodeElement> querySelectorAll(Object... selector)
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
