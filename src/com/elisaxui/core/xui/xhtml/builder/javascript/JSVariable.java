/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.Array;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSValue;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

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
	protected void registerMethod(Object obj ) {
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
	
	public JSBool isNotEqual(Object obj)
	{
		JSBool ret = new JSBool();
		Array arr = new Array();
		Object content = _getString();
		if (content instanceof Array )
			arr.addAll((Array<?>)content);
		else
			arr.add(content);
		arr.add("!=");
		arr.add(obj);
		ret._setContent(arr);
		return ret;
	}
	
	public JSVoid set(Object... objs)
	{
		JSVoid ret = new JSVoid();
		_doOperator(ret, "=", objs);
		return ret;
	}

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
		
		int i = objs.length;
		
		for (Object obj : objs) {
			if (i==1 && obj instanceof String && this instanceof JSValue)
				obj = "\""+ obj + "\"";
			
			if (obj instanceof Array )
				arr.addAll((Array<?>)obj);
			else
				arr.add(obj);
		}

		ret._setContent(arr);
		registerMethod(ret);
	}
	
}
