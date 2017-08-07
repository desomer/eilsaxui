package com.elisaxui.xui.admin.test;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;

public interface JSTestClass extends JSClass {

	JSVariable a = null;
	JSTest2Class b = null;

	default Object constructor(Object val) {
		__()
				.set(a, val)
				.set(b, _new());

		return null;
	}

	default Object console(Object p1, Object p2) {
		__(a, "=", p1)
				.set(b, _new(5))
				.__("console.debug('aaaa'," + p1 + ", " + p2 + ")");

		return null;
	}

	default Object test(Object a) {
		__("console.debug(a)");
		return null;
	}

}
