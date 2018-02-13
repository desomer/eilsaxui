/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import java.lang.reflect.Proxy;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSonInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;

/**
 * @author Bureau
 *
 */
public class JSArray<E> extends JSClassInterface implements JSonInterface {

	public JSArray() {
		super();
		if (ProxyHandler.isModeJava())
			this.asLitteral();
	}

	public Class<?> _type;
	public JsonArrayBuilder jsonBuilder = null;
	
	@Override
	public String _getClassType() {
		return "Array";
	}

	public JSArray<E> asLitteral() {
		jsonBuilder = Json.createArrayBuilder();
		return this;
	}
	
	public boolean isLitteral() {
		return jsonBuilder!=null;
	}
	
	@Override
	public Object _getValue() {
		if (jsonBuilder!=null)
			return jsonBuilder.build().toString();
		else
			return super._getValue();
	}
	
	public JSArray<E> push(E value) {
		JsonObjectBuilder objLitteral = null;
		
		if (value instanceof Proxy) {
			ProxyHandler inv = (ProxyHandler) Proxy.getInvocationHandler(value);
			if (inv.getJsonBuilder()!=null)
				objLitteral = inv.getJsonBuilder();
			_type = inv.getImplementClass();
		} else
			_type = value == null ? null : value.getClass();

		if (isLitteral())
		{
			if (value instanceof JSArray)
				jsonBuilder.add(((JSArray<?>)value).jsonBuilder);
			else if (objLitteral!=null)
				jsonBuilder.add(objLitteral);
			else if (value instanceof Integer)
				jsonBuilder.add((Integer)value);
			else if (value instanceof Double)
				jsonBuilder.add((Double)value);
			else
				jsonBuilder.add(value.toString());
			return this;
		}
		else
			return (JSArray<E>) _callMethod(null, "push", value);
	}

	public E pop() {
		return (E) _callMethod(null, "pop", null);
	}

	public JSArray<E> splice(Object debut, Object nbASupprimer) {
		return (JSArray<E>) _callMethod(null, "splice", debut, SEP, nbASupprimer);
	}

	public E at(Object idx) {
		JSArray ret = new JSArray()._setName(this._getName());
		ret.addContent("[");
		ret.addContent(idx);
		ret.addContent("]");

		if (_type != null) {
			E t = (E) JSClass.declareType(_type, null);
			if (t instanceof JSAny)
				((JSAny) t)._setValue(ret);
			else
				((JSClass) t)._setContent(ret);
			return t;
		}
		return (E) ret;
	}

	public JSInt length() {
		return castAttr(new JSInt(), "length");
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSonInterface#getStringJSON()
	 */
	@Override
	public String getStringJSON() {
		return ""+_getValue();
	}

}
