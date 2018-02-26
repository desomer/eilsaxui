/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.css.CSSSelector;

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
	
}
