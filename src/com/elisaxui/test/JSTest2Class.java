package com.elisaxui.test;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

public interface JSTest2Class extends JSClass {

	JSAny a = null;
	
	default Object constructor(Object val)
	{
		return _set(a, val);
	}

}
