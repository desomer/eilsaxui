package com.elisaxui.core.xui.xhtml.builder.javascript.lang.value;

public class JSInt extends JSValue {

	public static final JSInt value(int v)
	{		
		JSInt ret =   new JSInt()._setValue(v);
		return ret;
	}
	
}
