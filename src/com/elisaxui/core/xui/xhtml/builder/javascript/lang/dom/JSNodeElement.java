/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class JSNodeElement extends JSAny  implements IJSClassInterface, IXHTMLBuilder {
	
	public JSNodeElement appendChildTemplate(Object... element) {
		XMLElement e = null;
		
		if (element.length==0 && element[0] instanceof XMLElement)
			e = (XMLElement)element[0];
		else
			e = xListNode(element);
		
		return  callMth("appendChild", new JSNodeTemplate(e).setModeJS(true));
	}
	
	/**************************************************/
	public JSNodeElement appendChild(Object element) {
		return  callMth("appendChild", element);
	}
	
	public JSNodeElement remove() {
		return  callMth("remove");
	}
	
	public JSString textContent() {
		return castAttr(new JSString(), "textContent");
	}
	
	public JSDomTokenList classList() {
		return castAttr(new JSDomTokenList(), "classList");
	}
	
	public JSString nodeName() {
		return castAttr(new JSString(), "nodeName");
	}
	
	public JSon dataset() {
		return castAttr(new JSon(), "dataset");
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
		return callTyped(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "querySelectorAll", ""+selector);
	}
	
	public JSArray<JSNodeElement> querySelectorAll(JSAny variable)
	{
		return callTyped(new JSArray<JSNodeElement>().setArrayType(JSNodeElement.class), "querySelectorAll", variable);
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
