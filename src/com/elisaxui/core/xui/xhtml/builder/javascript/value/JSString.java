package com.elisaxui.core.xui.xhtml.builder.javascript.value;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;

public class JSString extends JSVariable {
	String value;

	public String getValue() {
		return value;
	}

	public JSString setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		if (value==null)
			return super.toString();
		return value;
	}
	
	
}
