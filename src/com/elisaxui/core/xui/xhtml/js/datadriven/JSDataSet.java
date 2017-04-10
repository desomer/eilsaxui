package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

public interface JSDataSet extends JSClass {

	Object data=null;
	
	default Object constructor(Object data)
	{
		return set(this.data, data);
	}
	
	default Object set(Object data) 
	{
		return set(this.data, data);
	}
	
	default Object get() 
	{
		return __("return ", this.data);
	}
	
}
