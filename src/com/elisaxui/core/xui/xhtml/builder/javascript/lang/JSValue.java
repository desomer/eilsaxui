/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;

/**
 * @author gauth
 *
 */
public class JSValue extends JSVariable {

	public JSValue add(Object... objs)
	{
		JSValue ret = (JSValue) declareType();
		_doOperator(ret, "+", objs);
		return ret;
	}
	
	public JSValue append(Object... objs)
	{
		JSValue ret = (JSValue) declareType();
		_doOperator(ret, "+=", objs);
		return ret;
	}
	
	public JSValue substact(Object... objs)
	{
		JSValue ret = (JSValue) declareType();
		_doOperator(ret, "-", objs);
		return ret;
	}
	
	
	public JSInt modulo(Object obj)
	{
		JSInt ret = new JSInt();
		Array arr = new Array();
		Object content = _getValueOrName();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("%");
		arr.add(obj);
		ret._setValue(arr);
		return ret;
	}
}
