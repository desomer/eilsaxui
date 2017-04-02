package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xml.builder.javascript.JSClass;

public interface JSTest2Class extends JSClass {

	Object a = null;
	Object b = null;
	
	static Object _new() {
		
		return JSClass._new(JSTest2Class.class);
	}

}
