/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.value;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;

/**
 * @author Bureau
 *
 */
public class JSArray extends JSClassMethod {

	public JSArray push(Object value)
	{
		return callMth("push", value);
	}
	
	public JSArray get(Object idx)
	{
		JSArray ret = new JSArray().setName(this.getName());
		ret.addContent("[");
		ret.addContent(idx);
		ret.addContent("]");
		return ret;
	}
	
	public JSInt length()
	{
		return attrOfType(new JSInt(), "length");
	}
	
}
