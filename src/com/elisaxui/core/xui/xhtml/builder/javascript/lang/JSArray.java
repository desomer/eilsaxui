/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;

/**
 * @author Bureau
 *
 */
public class JSArray<E> extends JSClassInterface {

	public Class _type;
	
	public JSArray push(E value)
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
	
	public E at(Object idx)
	{
		JSArray ret = new JSArray()._setName(this._getName());
		ret.addContent("[");
		ret.addContent(idx);
		ret.addContent("]");
		
		if (_type!=null)
		{
			E t = JSClass.declareType(_type, null);
			if (t instanceof JSVariable)
				((JSVariable)t)._setContent(ret);
			else
				((JSClass)t)._setContent(ret);
			return t;
		}
		return (E) ret;
	}
	
	public JSInt length()
	{
		return castAttr(new JSInt(), "length");
	}
	
}
