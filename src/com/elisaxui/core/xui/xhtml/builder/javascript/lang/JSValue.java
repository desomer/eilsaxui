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

	public JSValue add(Object... objs)
	{
		JSValue ret = declareType();
		_doOperator(ret, "+", objs);
		return ret;
	}
	
	public JSValue substact(Object... objs)
	{
		JSValue ret = declareType();
		_doOperator(ret, "-", objs);
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
	
	/*******************************************************/
	/**
	 * @param ret
	 * @return
	 */
	private JSValue declareType() {
		JSValue ret =  null;
		try {
			ret = this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
