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

	
	Object value;

	public Object getValue() {
		return value;
	}
	
	public Object getString() {
		if (value==null)
			return name==null?"":""+getName();
		return value;
	}

	public <E extends JSVariable> E _setContent(Object value) {
		this.value = value;
		return (E)this;
	}

	@Override
	public String toString() {
		if (value==null)
			return name==null?"":""+getName();
		return value.toString();
	}
	
}
