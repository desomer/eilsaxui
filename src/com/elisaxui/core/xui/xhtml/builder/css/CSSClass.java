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
	
	
	
	@Override
	public String toString() {
		return "CSSClass [id=" + id + "]";
	}

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
	
	public CSSSelector __(Object desc)
	{
		return descendant(desc) ;
	}
	
	public CSSSelector directChildren(Object desc)
	{
		return CSSSelector.onPath(this, ">", desc);
	}
}
