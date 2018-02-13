package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSString extends JSValue {
	
	public String _getClassType() {
		return "String";
	}
	
	
	public static final JSString value(String v)
	{
		return  new JSString()._setValue("\""+ v + "\"");
	}
	
	public static final JSString value(JSAny v)
	{
		return  new JSString()._setValue(v._getValue());
	}
	
	/********************************************************************/
	
	public JSString substring(Object start)
	{
		return (JSString) _callMethod(null, "substring", start);
	}
	
	public JSString substring(Object start, Object end)
	{
		return (JSString) _callMethod(null, "substring", start, SEP, end);
	}
	
}
