/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.value;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

/**
 * @author gauth
 *
 */
public class JSValue extends JSAny {

	public JSValue add(Object... objs)
	{
		JSValue ret = (JSValue) declareTypeAny();
		doOperator(ret, "+", objs);
		return ret;
	}
	
	public JSValue append(Object... objs)
	{
		JSValue ret = (JSValue) declareTypeAny();
		doOperator(ret, "+=", objs);
		return ret;
	}
	
	public JSValue substact(Object... objs)
	{
		JSValue ret = (JSValue) declareTypeAny();
		doOperator(ret, "-", objs);
		return ret;
	}
	
	public JSValue multiply(Object... objs)
	{
		JSValue ret = (JSValue) declareTypeAny();
		doOperator(ret, "*", objs);
		return ret;
	}
	
	public JSValue divide(Object... objs)
	{
		JSValue ret = (JSValue) declareTypeAny();
		doOperator(ret, "/", objs);
		return ret;
	}
	
	public JSInt modulo(Object obj)
	{
		JSInt ret = new JSInt();
		ArrayMethod<Object> arr = new ArrayMethod<>();
		Object content = _getValueOrName();
		if (content instanceof ArrayMethod )
			arr.addAll((ArrayMethod<?>)content);
		else
			arr.add(content);
		arr.add("%");
		arr.add(obj);
		ret._setValue(arr);
		return ret;
	}
}
