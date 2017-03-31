package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;

public interface JSTestClass extends JSClass {

	Object a = JSBuilder.initVar("a");
	JSTest2Class b = JSBuilder.initVar(null);

	default Object console(Object p1, Object p2) {
		return js().__(a, "=", p1).__("console.debug(" + p1 + ", " + p2 + ")");
	}

	default Object test() {
		return js().__("console.debug('rrrrrrr')");
	}

	default Object test(Object a) {
		return js().__("console.debug(a)");
	}

	static Object _new() {
		return JSClass._new(JSTestClass.class);
	}
}
