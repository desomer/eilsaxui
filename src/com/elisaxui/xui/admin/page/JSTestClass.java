package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;	

public interface JSTestClass extends JSClass {

	Object a = "this.a";
	JSTest2Class b = JSBuilder.initVar(JSTest2Class.class);

	default Object constructor(Object val)
	{
		return js()
				.set(a, val)
				.set(b,  JSTest2Class._new())
				;
	}
	
	default Object console(Object p1, Object p2) {
		return js()
				.__(a, "=", p1)
			//	.__(b, "=", JSTest2Class._new())
				.__("console.debug(" + p1 + ", " + p2 + ")");
	}

	default Object test() {
		return js().__("console.debug('rrrrrrr')");
	}

	default Object test(Object a) {
		return js().__("console.debug(a)");
	}

	static Object _new(Object... param) {
		return JSClass._new(JSTestClass.class, param);
	}
}
