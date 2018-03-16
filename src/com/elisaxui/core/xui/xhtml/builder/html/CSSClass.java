/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.html;

import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;

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

	public CSSClass setId(String id) {
		this.id = id;
		return this;
	}
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public CSSSelector and(Object desc)
	{
		return CSSSelector.onPath(this, desc);
	}
	
	public CSSSelector or(Object desc)
	{
		return CSSSelector.onPath(this, ",", desc);
	}
	
	public CSSSelector descendant(Object desc)
	{
		return CSSSelector.onPath(this, " ", desc);
	}

	/**
	 * descendant  .cA .cB
	 * @param desc
	 * @return
	 */
	@Deprecated
	public CSSSelector __(Object desc)
	{
		return descendant(desc) ;
	}
	
	public CSSSelector directChildren(Object desc)
	{
		return CSSSelector.onPath(this, ">", desc);
	}
}
