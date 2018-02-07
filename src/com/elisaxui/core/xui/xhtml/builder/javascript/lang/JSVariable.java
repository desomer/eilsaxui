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
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyMethodDesc;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author Bureau
 *
 */
public class JSVariable {
	
	protected static final String SEP = ",";
	protected Object name;
	protected Object value;
	protected Object parent;
	
	
	public String _getClassType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @return the parent
	 */
	public final Object _getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public final void _setParent(Object parent) {
		this.parent = parent;
	}

	public final Object _getName() {
		return name;
	}

	public final <E extends JSVariable> E _setName(Object name) {
		this.name = name;
		return (E)this;
	}


	public Object _getValue() {
		return value;
	}
	
	public final <E extends JSVariable> E _setValue(Object value) {
		this.value = value;
		return (E)this;
	}
	
	public  Object _getValueOrName() {
		if (_getValue()==null)
			return name==null?"":""+_getName();
		return _getValue();
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
					//	e.printStackTrace();
				}
			 }
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
	
	public final JSVoid set(Object... objs)
	{
		ProxyHandler mh = (ProxyHandler)this.parent;
		boolean isLitteral = mh!=null && mh.getJsonBuilder()!=null;
		
		if (isLitteral || ProxyHandler.isModeJava()  ) {
			if (mh.getJsonBuilder()==null)
				mh.setJsonBuilder(Json.createObjectBuilder());
			
			String attr = ""+this.name;
			attr = attr.substring(attr.lastIndexOf('.')+1);
			
			mh.getJsonBuilder().add(attr, new JsonNumberImpl(objs[0].toString()));
		}
		
		JSVoid ret = new JSVoid();
		if (!isLitteral)
			_doOperator(ret, "=", objs);
		return ret;
	}

	/*******************************************************************************/
	/**
	 * @param ret
	 * @param objs
	 */
	protected final void _doOperator(JSVariable ret, String operator,  Object... objs) {
		
		if (ProxyHandler.isModeJava()) {
			
			
		} else {
			Array arr = new Array();
			Object content = _getValueOrName();
			if (content instanceof Array)
				arr.addAll((Array<?>) content);
			else
				arr.add(content);

			arr.add(operator);

			if (objs != null) {
				int i = objs.length;

				for (Object obj : objs) {

					if (i == 1 && obj instanceof String && this instanceof JSValue)
						obj = "\"" + obj + "\"";

					if (obj instanceof Array)
						arr.addAll((Array<?>) obj);
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
	
	protected final Object _callMethod(JSVariable ret, String mth, Object... objs) {
		if (ret == null) {
			ret = this;
			if (value == null && this.name != null) {
				// gestion premier appel de variable pour chainage
				ret = ret.declareType();
				ret._setName(this._getName());
			}
		}
		
		if (ProxyHandler.isModeJava()) {
			return ret;
		} else {

			Array arr = new Array();
			Object content = _getValueOrName();
			if (content instanceof Array)
				arr.addAll((Array<?>) content);
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

	
	protected static final void _addContent(Array inner, Object content) {
		
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
				else
				{
				//	if (object instanceof Proxy)
					
					inner.add(object);
				}
			}
		}
		else if (content!=null)
			inner.add(content);
		//return (E)this;
	}
	
	/*******************************************************/
	/**
	 * @param ret
	 * @return
	 */
	protected final JSVariable declareType() {
		JSVariable ret =  null;
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
