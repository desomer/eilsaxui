/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonValue;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyMethodDesc;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author Bureau
 *
 */
public class JSAny implements JSElement {
	
	protected static final String SEP = ",";
	protected Object name;
	protected Object value;
	protected Object parentLitteral;
	
	
	public String _getClassType() {
		return this.getClass().getSimpleName();
	}

//	/**
//	 * @return the parent
//	 */
//	public final Object _getParent() {
//		return parentLitteral;
//	}

	/**
	 * @param parent the parent to set
	 */
	public final void _setParentLitteral(Object parent) {
		this.parentLitteral = parent;
	}

	public final Object _getName() {
		return name;
	}

	public final <E extends JSAny> E _setName(Object name) {
		this.name = name;
		return (E)this;
	}


	public Object _getValue() {
		return value;
	}
	
	public final <E extends JSAny> E _setValue(Object value) {
		this.value = value;
		return (E)this;
	}
	
	public  Object _getValueOrName() {
		Object v =_getValue();
		if (v==null)
			return name==null?"":""+_getName();
		else
			return v;
	}

	@Override
	public String toString() {
		if (_getValue()==null)
			return name==null?"":""+_getName();
		return _getValue().toString();
	}
	/*************************************************************/
	protected static final void _registerMethod(Object obj ) {
			 ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();
			 try {
				ProxyHandler.doLastSourceLineInsered( false);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			 
			 StackTraceElement[]  stack = Thread.currentThread().getStackTrace();
			
			 int numLigne = -1;
			 for (StackTraceElement stackTraceElement : stack) {
				 try {
						if (JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))  && stackTraceElement.getLineNumber()!=-1 )
						{
							numLigne = stackTraceElement.getLineNumber();
							currentMethodDesc.lastLineNoInsered = numLigne;
							currentMethodDesc.lastMthNoInserted = obj;
							break;
						}
				} catch (ClassNotFoundException e) {
				}
			 }
	}
	
	public boolean isLitteral() {
		return false;
	}
	
	/**************************************************************/
	public final JSBool isEqual(Object obj)
	{
		JSBool ret = new JSBool();
		_doOperator(ret, "==", obj);
		return ret;
	}
	
	public final JSBool isNotEqual(Object obj)
	{
		JSBool ret = new JSBool();
		_doOperator(ret, "!=", obj);
		return ret;
	}
	
	public final JSAny set(Object... objs)
	{
		
		JSAny ret = declareTypeAny();
		boolean isLitteral = false;
		
		if (this.parentLitteral instanceof ProxyHandler)
		{
			ProxyHandler mh = (ProxyHandler)this.parentLitteral;
			isLitteral = mh.getJsonBuilder()!=null;
			
			if (isLitteral || ProxyHandler.isModeJava()  ) {
				if (mh.getJsonBuilder()==null)
					mh.setJsonBuilder(Json.createObjectBuilder());
				
				String attr = ""+this.name;
				attr = attr.substring(attr.lastIndexOf('.')+1);
				
				if (objs[0] instanceof String && this instanceof JSValue)
					objs[0] = "\"" + objs[0] + "\"";
				
				mh.getJsonBuilder().add(attr, new JsonNumberImpl(objs[0].toString()));
			}
		}
		else if (this.parentLitteral instanceof JSObject)
		{
			JSObject mh = (JSObject)this.parentLitteral;
			isLitteral = mh.jsonBuilder!=null;
			String attr = ""+this.name;
			
			if (objs[0] instanceof String && this instanceof JSValue)
				objs[0] = "\"" + objs[0] + "\"";
			
			mh.jsonBuilder.add(attr, new JsonNumberImpl(objs[0].toString()));
		}
		
		if (!isLitteral)
			_doOperator(ret, "=", objs);
		
		return ret;
	}

	/*******************************************************************************/
	/**
	 * @param ret
	 * @param objs
	 */
	protected final void _doOperator(JSAny ret, String operator,  Object... objs) {
		
		if (ProxyHandler.isModeJava()) {
			
			
		} else {
			ArrayMethod arr = new ArrayMethod();
			Object content = _getValueOrName();
			if (content instanceof ArrayMethod)
				arr.addAll((ArrayMethod<?>) content);
			else
				arr.add(content);

			arr.add(operator);

			if (objs != null) {
				int i = objs.length;

				for (Object obj : objs) {

					if (i == 1 && obj instanceof String && this instanceof JSValue)
						obj = "\"" + obj + "\"";

					if (obj instanceof ArrayMethod)
						arr.addAll((ArrayMethod<?>) obj);
					else
						arr.add(obj);
				}
			} else {
				arr.add(null);
			}

			ret._setValue(arr);
			_registerMethod(ret);
		}
	}
	
	public final <E extends JSAny> E call(String mth, Object... objs)
	{
		return (E)_callMethod(null, mth, objs );
	}
	
	public final <E extends JSAny> E callTyped(E type, String mth, Object... objs)
	{
		return (E)_callMethod(type, mth, objs );
	}
	
	protected final Object _callMethod(JSAny ret, String mth, Object... objs) {
		if (ret == null) {
			ret = this;
			if (value == null && this.name != null) {
				// gestion premier appel de variable pour chainage
				ret = ret.declareTypeAny();
				ret._setName(this._getName());
			}
		}
		
		if (ProxyHandler.isModeJava()) {
			return ret;
		} else {

			ArrayMethod arr = new ArrayMethod();
			Object content = _getValueOrName();
			if (content instanceof ArrayMethod)
				arr.addAll((ArrayMethod<?>) content);
			else
				arr.add(content);

			arr.add("." + mth + "(");
			_addContent(arr, objs);
			arr.add(")");

			ret._setValue(arr);
			_registerMethod(ret);
			return ret;
		}
	}

	
	protected static final void _addContent(ArrayMethod inner, Object content) {
		
		if ( content instanceof Object[])
		{
			Object[] arr = (Object[])content;
			for (Object object : arr) {
				
				if (object instanceof XClass)
					inner.add(((XClass)object).getId());
				
				else if (object instanceof JSFunction)
				{
					StringBuilder strBuf = new StringBuilder();
					XMLBuilder buf = new XMLBuilder("????", strBuf, null);
					((JSFunction)object).toXML(buf);
					inner.add(strBuf.toString());
				}
				else if ( object instanceof Object[])
				{
					_addContent(inner, object);
				}
				else if ( object instanceof String && object != SEP )
					inner.add("'"+object+"'");
				else if ( object instanceof ArrayMethod)
				{
					inner.addAll((ArrayMethod)object);
				}
				else
				{				
					inner.add(object);
				}
			}
		}
		else if (content!=null)
			inner.add(content);
	}
	
	/*******************************************************/
	/**
	 * @param ret
	 * @return
	 */
	protected final JSAny declareTypeAny() {
		JSAny ret =  null;
		try {
			ret = this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return ret;
	}



	 class JsonNumberImpl implements JsonNumber {

		 String value;
		 
		 /**
		 * @param value
		 */
		public JsonNumberImpl(String value) {
			super();
			this.value = value;
		}


		@Override
		 public String toString() {
			 return value;
		 }


		@Override
		public ValueType getValueType() {
			return JsonValue.ValueType.NUMBER;
		}


		@Override
		public boolean isIntegral() {
			return false;
		}


		@Override
		public int intValue() {
			return 0;
		}


		@Override
		public int intValueExact() {
			return 0;
		}


		@Override
		public long longValue() {
			return 0;
		}


		@Override
		public long longValueExact() {
			return 0;
		}


		@Override
		public BigInteger bigIntegerValue() {
			return null;
		}


		@Override
		public BigInteger bigIntegerValueExact() {
			return null;
		}


		@Override
		public double doubleValue() {
			return 0;
		}


		@Override
		public BigDecimal bigDecimalValue() {
			return null;
		}

	}

}
