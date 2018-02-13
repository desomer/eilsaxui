package com.elisaxui.test;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

public interface JSTestClass extends JSClass {

	JSAny a = null;
	JSTest2Class b = null;

	default Object constructor(Object val) {
		__()
				._set(a, val)
				._set(b, _new());

		return null;
	}

	default Object console(Object p1, Object p2) {
		__(a, "=", p1)
				._set(b, _new(5))
				.__("console.debug('aaaa'," + p1 + ", " + p2 + ")");

		return null;
	}

	default Object test(Object a) {
		__("console.debug(a)");
		return null;
	}

}
