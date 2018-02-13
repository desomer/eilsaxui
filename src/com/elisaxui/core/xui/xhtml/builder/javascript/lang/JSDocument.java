/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;

/**
 * @author gauth
 *
 */
public class JSDocument extends JSClassInterface {

	
	public static final JSDocument document()
	{
		JSDocument ret = new JSDocument();
		ret.addContent("document");
		return ret;
	}
	
	public JSElement querySelector(CSSSelector selector)
	{
		return (JSElement) _callMethod(new JSElement(), "querySelector", ""+selector);
	}
	
	public JSElement querySelector(JSAny variable)
	{
		return (JSElement) _callMethod(new JSElement(), "querySelector", variable);
	}
	
	public JSElement querySelector(Object... selector)
	{
		if (selector.length==1 && selector[0] instanceof JSAny)
			return querySelector((JSAny)selector[0]);	
		else
			return querySelector(CSSSelector.onPath(selector));
	}
	
}
