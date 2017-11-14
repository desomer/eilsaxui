/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSBool;

/**
 * @author Bureau
 *
 */
public class JSVariable {
	protected Object name;
	protected Object value;

	public Object _getName() {
		return name;
	}

	public <E extends JSVariable> E _setName(Object name) {
		this.name = name;
		return (E)this;
	}


	public Object _getValue() {
		return value;
	}
	
	public Object _getString() {
		if (value==null)
			return name==null?"":""+_getName();
		return value;
	}

	public <E extends JSVariable> E _setContent(Object value) {
		this.value = value;
		return (E)this;
	}

	@Override
	public String toString() {
		if (value==null)
			return name==null?"":""+_getName();
		return value.toString();
	}
	
	
	/**************************************************************/
	public JSBool isEqual(Object obj)
	{
		JSBool ret = new JSBool();
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("==");
		arr.add(obj);
		ret._setContent(arr);
		return ret;
	}
}
