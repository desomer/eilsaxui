package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

public interface JSTest2Class extends JSClass {

	Object a = null;
	
	default Object constructor(Object val)
	{
		return set(a, val);
	}

}
