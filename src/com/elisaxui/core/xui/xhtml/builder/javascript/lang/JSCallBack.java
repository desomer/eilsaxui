package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSCallBack extends JSAny {

	public final <E extends JSAny> E call(Object... param) {
		return callMth("call", param);
	}
	
}
