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

	public <E extends JSVariable> E setName(Object name) {
		this.name = name;
		return (E)this;
	}

	@Override
	public String toString() {
		return name==null?"":""+getName();
	}
	
}
