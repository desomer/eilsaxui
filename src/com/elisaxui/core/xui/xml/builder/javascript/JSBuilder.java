package com.elisaxui.core.xui.xml.builder.javascript;

import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class JSBuilder extends Element {
	
	public JSBuilder(Object name, Object[] inner) {
		super(name, inner);
	}

	public JSClassImpl createJSClass()
	{
		return new JSClassImpl(this);
	}
	
	public JSFunction createJSFunction()
	{
		return new JSFunction(this);
	}

	public JSContent createJSContent()
	{
		return new JSContent(this);
	}
	
	public static final class JSNewLine {};

	
	
	public static final class JSElem
	{
		public Object value; 
	}
}
