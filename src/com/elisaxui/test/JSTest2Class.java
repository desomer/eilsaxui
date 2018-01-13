package com.elisaxui.test;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;

public interface JSTest2Class extends JSClass {

	JSVariable a = null;
	
	default Object constructor(Object val)
	{
		return _set(a, val);
	}

}
