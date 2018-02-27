package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSString extends JSValue {
	
	public String zzGetJSClassType() {
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
		return callMth("substring", start);
	}
	
	public JSString substring(Object start, Object end)
	{
		return callMth("substring", start, end);
	}
	
}
