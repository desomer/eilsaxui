/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

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
		for (Object object : sel) {
			if (object instanceof CSSClass)
			{
				selector.append(".");
				selector.append(((CSSClass)object).getId());
			}
			else
				selector.append(object.toString()); 		
		}
	}
	
	@Override
	public String toString() {
		return selector.toString();
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
