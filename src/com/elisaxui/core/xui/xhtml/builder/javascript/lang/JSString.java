package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSString extends JSValue {
	
	boolean isLitteral = false;
	
	public String zzGetJSClassType() {
		return "String";
	}
	
	
	public static final JSString value(String v)
	{		
		JSString ret =   new JSString()._setValue(v);
		ret.isLitteral = true;
		return ret;
	}
	
	public static final JSString value(JSAny v)
	{
		return  new JSString()._setValue(v._getValue());
	}
	
	
	@Override
	public Object _getValue() {
		return isLitteral?("\""+ value + "\""):value;
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
	
	/********************************************************************/
	public JSArray<JSString> split(Object separateur)
	{
		JSArray<JSString> ret= callTyped(new JSArray<JSString>(), "split", separateur);
		ret.setArrayType(JSString.class);
		return ret;
	}

	public JSInt length() {
		return castAttr(new JSInt(), "length");
	}
}
