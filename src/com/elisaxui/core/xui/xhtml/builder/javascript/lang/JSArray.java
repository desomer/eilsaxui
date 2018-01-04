/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassInterface;

/**
 * @author Bureau
 *
 */
public class JSArray extends JSClassInterface {

	public JSArray push(Object value)
	{
		return callMth("push", value);
	}
	
	public JSArray pop()
	{
		return callMth("pop");
	}
	
	public JSArray splice(Object debut, Object nbASupprimer)
	{
		return callMth("splice", debut, ",", nbASupprimer);
	}
	
	public JSArray at(Object idx)
	{
		JSArray ret = new JSArray()._setName(this._getName());
		ret.addContent("[");
		ret.addContent(idx);
		ret.addContent("]");
		return ret;
	}
	
	public JSInt length()
	{
		return castAttr(new JSInt(), "length");
	}
	
}
