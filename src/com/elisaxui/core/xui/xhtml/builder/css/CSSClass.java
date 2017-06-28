/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

/**
 * @author Bureau
 *
 */
public class CSSClass {
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public CSSSelector and(Object desc)
	{
		return CSSSelector.onPath(this, desc);
	}
	
	
	public CSSSelector descendant(Object desc)
	{
		return CSSSelector.onPath(this, " ", desc);
	}
	
	public CSSSelector children(Object desc)
	{
		return CSSSelector.onPath(this, ">", desc);
	}
}
