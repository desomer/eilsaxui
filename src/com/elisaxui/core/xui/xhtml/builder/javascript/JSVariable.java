/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSValue;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author Bureau
 *
 */
public class JSVariable {
	
	protected String SEP = ",";
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
	
	protected Object _getString() {
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
	/*************************************************************/
	protected void _registerMethod(Object obj ) {
		 try {
			 MethodDesc currentMethodDesc = MethodInvocationHandler.ThreadLocalMethodDesc.get();
			 MethodInvocationHandler.doLastSourceLineInsered(currentMethodDesc, false);
			 
			 StackTraceElement[]  stack = Thread.currentThread().getStackTrace();
			
			 int numLigne = -1;
			 for (StackTraceElement stackTraceElement : stack) {
				if (JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))  && stackTraceElement.getLineNumber()!=-1 )
				{
					numLigne = stackTraceElement.getLineNumber();
					currentMethodDesc.lastLineNoInsered = numLigne;
					currentMethodDesc.lastMthNoInserted = obj;
					break;
				}
			 }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**************************************************************/
	public JSBool isEqual(Object obj)
	{
		JSBool ret = new JSBool();
		_doOperator(ret, "==", obj);
		return ret;
	}
	
	public JSBool isNotEqual(Object obj)
	{
		JSBool ret = new JSBool();
		_doOperator(ret, "!=", obj);
		return ret;
	}
	
	public JSVoid set(Object... objs)
	{
		JSVoid ret = new JSVoid();
		_doOperator(ret, "=", objs);
		return ret;
	}

	/*******************************************************************************/
	/**
	 * @param ret
	 * @param objs
	 */
	protected void _doOperator(JSVariable ret, String operator,  Object... objs) {
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		
		arr.add(operator);
		
		if (objs!=null)
		{
			int i = objs.length;
			
			for (Object obj : objs) {
				if (i==1 && obj instanceof String && this instanceof JSValue)
					obj = "\""+ obj + "\"";
				
				if (obj instanceof Array )
					arr.addAll((Array<?>)obj);
				else
					arr.add(obj);
			}
		}
		else
		{
			arr.add(null);
		}

		ret._setContent(arr);
		_registerMethod(ret);
	}
	
	protected Object _callMethod(JSVariable ret, String mth,  Object... objs) {
		if (ret==null)
		{
			ret = this;
			if (value==null && this.name!=null)
			{
				// gestion premier appel de variable pour chainage
				ret = ret.declareType();
				ret._setName(this._getName());
			}
		}
		
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		
		arr.add("."+mth+"(");
		_addContent(arr, objs);
		arr.add(")");
		
		ret._setContent(arr);
		_registerMethod(ret);
		return ret;
	}

	
	protected <E> E _addContent(Array inner, Object content) {
		
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
					inner.add(object);
			}
		}
		else if (content!=null)
			inner.add(content);
		return (E)this;
	}
	
	/*******************************************************/
	/**
	 * @param ret
	 * @return
	 */
	protected JSVariable declareType() {
		JSVariable ret =  null;
		try {
			ret = this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
