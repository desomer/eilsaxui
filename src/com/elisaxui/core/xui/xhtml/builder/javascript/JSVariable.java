/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

/**
 * @author Bureau
 *
 */
public class JSVariable {
	Object name;

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name==null?"":""+getName();
	}
	
}
