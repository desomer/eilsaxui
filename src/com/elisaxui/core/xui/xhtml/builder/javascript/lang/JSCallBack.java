package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSCallBack extends JSAny {

	/**
	 * fct.call(_this(), param)
	 * @param param
	 * @return
	 */
	public final <E extends JSAny> E call(Object... param) {
		return callMth("call", param);
	}
	
	/**
	 * @param classes
	 * @return
	 */
	public final <E extends JSAny> E apply(Object... classes) {
		return callMth("apply", classes);
	}
	

	
}
