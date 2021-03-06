/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import java.lang.reflect.Proxy;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ILitteral;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author Bureau
 *
 */
public class JSArray<E> extends JSAny implements ILitteral, IJSClassInterface {

	/**
	 *  use  JSArray.newLitteral();
	 */
	@Deprecated 
	public JSArray() {
		super();
		if (ProxyHandler.isModeJava())
			this.asLitteral();
	}

	public static <E> JSArray<E> newLitteral()
	{
		return new JSArray<E>().asLitteral();
	}
	
	private Class<?> _type;

	/**
	 * @return the _type
	 */
	public final Class<?> getArrayType() {
		return _type;
	}

	/**
	 * @param _type
	 *            the _type to set
	 */
	public final JSArray<E> setArrayType(Class<?> type) {
		this._type = type;
		return this;
	}

	public JsonArrayBuilder jsonBuilder = null;

	@Override
	public String zzGetJSClassType() {
		return "Array";
	}

	public JSArray<E> asLitteral() {
		jsonBuilder = Json.createArrayBuilder();
		return this;
	}

	public JSArray<E> asNonLitteral() {
		jsonBuilder = null;
		return this;
	}

	@Override
	public boolean isLitteral() {
		return jsonBuilder != null;
	}

	@Override
	public Object _getValue() {
		if (jsonBuilder != null)
			return jsonBuilder.build().toString();
		else
			return super._getValue();
	}

	/**************************************************************/
	
	public JSArray<E> push(E value) {
		JsonObjectBuilder objLitteral = null;

		if (value instanceof Proxy) {
			// recupere le type avec le premier push
			ProxyHandler inv = (ProxyHandler) Proxy.getInvocationHandler(value);
			if (inv.getJsonBuilder() != null)
				objLitteral = inv.getJsonBuilder();
			_type = inv.getImplementClass();
		} else
			_type = value == null ? null : value.getClass();

		if (isLitteral()) {
			if (value instanceof JSArray)
				jsonBuilder.add(((JSArray<?>) value).jsonBuilder);
			else if (objLitteral != null)
				jsonBuilder.add(objLitteral);
			else if (value instanceof Integer)
				jsonBuilder.add((Integer) value);
			else if (value instanceof Double)
				jsonBuilder.add((Double) value);
			else if (value instanceof JSString)
				jsonBuilder.add(((JSString) value)._getDirectValue().toString());
			else
				jsonBuilder.add(value.toString());
			return this;
		} else
			return (JSArray<E>) callMth("push", value);
	}

	public E pop() {
		return (E) callMth("pop");
	}

	public JSArray<E> concat(JSArray<E> array) {
		return (JSArray<E>) callMth("concat", array);
	}

	public JSArray<E> splice(Object debut, Object nbASupprimer) {
		return (JSArray<E>) callMth("splice", debut, nbASupprimer);
	}
	
	public JSArray<E> reverse() {
		JSArray<E> r =  (JSArray<E>) callMth("reverse");
		r.setArrayType(this.getArrayType());
		return r;
	}
	
	public JSInt indexOf(E elem) {
		return callTyped(new JSInt(), "indexOf", elem);
	}
	
	public E join(Object... p) {
		Object r = callMth( "join", p);
		if (_type != null) {
			return getTypedReturn(r);
		}
		return (E)r;
	}

	public void pushAll(JSArray<E> arrSrc) {

		ArrayMethod<Object> arr = new ArrayMethod<>();

		arr.add("for (var i = 0, l = ");
		Object src = arrSrc._getValueOrName();
		if (src instanceof ArrayMethod)
			arr.addAll((ArrayMethod<?>) src);
		else
			arr.add(src);

		arr.add(".length; i < l; i++) {");

		Object content = _getValueOrName();
		if (content instanceof ArrayMethod)
			arr.addAll((ArrayMethod<?>) content);
		else
			arr.add(content);

		arr.add(".push(");

		if (src instanceof ArrayMethod)
			arr.addAll((ArrayMethod<?>) src);
		else
			arr.add(src);

		arr.add("[i]);}");

		JSAny type = getReturnType();

		type._setValue(arr);

		zzRegisterMethod(type);

	}

	/***********************************************************************/
	public E at(Object idx) {
		JSArray<?> ret = new JSArray<Object>()._setName(_getValueOrName());
		ret.addContent("[");
		ret.addContent(idx);
		ret.addContent("]");

		if (_type != null) {
			return getTypedReturn(ret);
		}
		return (E) ret;
	}

	/**
	 * @param ret
	 */
	private E getTypedReturn(Object ret) {
		E t = (E) JSContent.declareType(_type, null);
		if (t != null) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSonInterface#
	 * getStringJSON()
	 */
	@Override
	public String getStringJSON() {
		return "" + _getValue();
	}

}
