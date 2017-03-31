package com.elisaxui.core.xui.xml.builder.javascript;

public interface JSClass  {
	
	JSContent js();
	
	static Object _new(Class<? extends JSClass> cl) {
		return "new "+cl.getSimpleName() +"()" ;
	}
	
	
}
