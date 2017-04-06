package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xml.builder.javascript.JSClass;	

public interface JSTestClass extends JSClass {

	Object a = null;
	JSTest2Class b = null;

	
	default Object constructor(Object val)
	{
		return 	set(a, val)
				.set(b,  _new())
				;
	}
	
	default Object console(Object p1, Object p2) {
		return   __(a, "=", p1)
				.set(b, _new(5))
				.__("console.debug('aaaa'," + p1 + ", " + p2 + ")");
	}

	default Object test(Object a) {
		return __("console.debug(a)");
	}

}
