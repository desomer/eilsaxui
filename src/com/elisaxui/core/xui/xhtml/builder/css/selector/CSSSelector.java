/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css.selector;

import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

/**
 * @author Bureau
 *
 */
public class CSSSelector {

	StringBuilder selector = new StringBuilder();
	
	public static final CSSSelector onPath(Object... sel)
	{
		return sel.length==0 ? null : new CSSSelector(sel);
	}
	
	private CSSSelector(Object... sel)
	{
		for (Object obj : sel) {
			if (obj instanceof CSSClass)
			{
				selector.append(".");
				selector.append(((CSSClass)obj).getId());
			}
			else if (obj instanceof JSAny)
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
