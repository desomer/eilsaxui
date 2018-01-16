/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import java.lang.reflect.Proxy;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;

/**
 * @author Bureau
 *
 */
public class JSArray<E> extends JSClassInterface {

	public Class _type;
	
	public JSArray push(E value)
	{
		if (value instanceof Proxy)
		{
			MethodInvocationHandler inv = (MethodInvocationHandler) Proxy.getInvocationHandler(value);
			_type = inv.getImplementClass();
		}
		else
			_type = value==null?null:value.getClass();
		
		return (JSArray) _callMethod(null, "push", value);
	}
	
	public JSArray pop()
	{
		return  (JSArray) _callMethod(null, "pop", value);
	}
	
	public JSArray splice(Object debut, Object nbASupprimer)
	{
		return (JSArray) _callMethod(null, "splice", debut, SEP, nbASupprimer);
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
