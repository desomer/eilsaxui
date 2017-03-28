package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xml.builder.javascript.JSClass;

public interface JSTestClass extends JSClass {
	
	default Object console(Object p1, Object p2) {
		return js().__("console.debug(" + p1 + ", " + p2 + ")");
	}
}
