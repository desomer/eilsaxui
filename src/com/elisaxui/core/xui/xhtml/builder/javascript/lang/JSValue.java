/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;

/**
 * @author gauth
 *
 */
public class JSValue extends JSVariable {

	public JSBool add(Object obj)
	{
		JSBool ret = new JSBool();
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("+");
		arr.add(obj);
		ret._setContent(arr);
		return ret;
	}
	
	public JSBool substact(Object obj)
	{
		JSBool ret = new JSBool();
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("-");
		arr.add(obj);
		ret._setContent(arr);
		return ret;
	}
	
	
	public JSInt modulo(Object obj)
	{
		JSInt ret = new JSInt();
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("%");
		arr.add(obj);
		ret._setContent(arr);
		return ret;
	}
	
}