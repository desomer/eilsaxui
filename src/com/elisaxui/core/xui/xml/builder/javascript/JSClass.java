package com.elisaxui.core.xui.xml.builder.javascript;

public interface JSClass  {

	public StringBuilder name = new StringBuilder();
	
	JSContent js();
	default <E> E setName(Object n)
	{
		name.append(n);
		return (E) this;
		
	}
	
}
