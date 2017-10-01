/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;

/**
 * @author Bureau
 *
 */
public class CSSSelector {

	StringBuilder selector = new StringBuilder();
	
	public static final CSSSelector onPath(Object... sel)
	{
		return new CSSSelector(sel);
	}
	
	private CSSSelector(Object... sel)
	{
		for (Object obj : sel) {
			if (obj instanceof XClass)
			{
				selector.append(".");
				selector.append(((XClass)obj).getId());
			}
			else if (obj instanceof JSVariable)
			{
				selector.append("'+");
				selector.append(obj);
				selector.append("+'");
			}
			else
				selector.append(obj.toString()); 		
		}
	}
	
	@Override
	public String toString() {
		return selector.toString();
	}
	
	public CSSSelector directChildren(Object desc)
	{
		return CSSSelector.onPath(this, ">", desc);
	}
	
	public CSSSelector pseudoClass(Object desc)
	{
		return CSSSelector.onPath(this, ":", desc);
	}
	
	public CSSSelector pseudoElem(Object desc)
	{
		return CSSSelector.onPath(this, "::", desc);
	}
	
	public CSSSelector or(Object desc)
	{
		return CSSSelector.onPath(this, ",", desc);
	}
}
