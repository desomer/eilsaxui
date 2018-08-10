/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
public class XHTMLPartMount extends XHTMLPart implements JSClass {

	
	JSClass proxy = null;
	
	public XHTMLPartMount()
	{
		try {
			proxy = (JSClass) ProxyHandler.getObjectJS(JSClass.class, "a", "b");
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#_setContent(java.lang.Object)
	 */
	@Override
	public Object _setContent(Object value) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#_getContent()
	 */
	@Override
	public Object _getContent() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#cast(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <E> E cast(Class<E> cl, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#set(java.lang.Object[])
	 */
	@Override
	public <E> E set(Object... value) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#asLitteral()
	 */
	@Override
	public <E> E asLitteral() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#callStatic(java.lang.Class)
	 */
	@Override
	public <E> E callStatic(Class<E> cl) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#declareType(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <E> E declareType(Class<E> type, Object name) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#declareArray(java.lang.Class, java.lang.String)
	 */
	@Override
	public <E> JSArray<E> declareArray(Class<E> type, String name) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#callJava(com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda)
	 */
	@Override
	public void callJava(JSLambda c) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#__(java.lang.Object[])
	 */
	@Override
	public JSContentInterface __(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_set(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public JSContentInterface _set(Object name, Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_var(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public JSContentInterface _var(Object name, Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#let(java.lang.Class, java.lang.Object, java.lang.Object[])
	 */
	@Override
	public <E> E let(Class<E> type, Object name, Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#let(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E let(String name, E content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#let(com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface, java.lang.Object[])
	 */
	@Override
	public void let(JSContentInterface name, Object... content) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#let(com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny, java.lang.Object[])
	 */
	@Override
	public void let(JSAny name, Object... content) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#consoleDebug(java.lang.Object[])
	 */
	@Override
	public JSContentInterface consoleDebug(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#systemDebugIf(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public JSContentInterface systemDebugIf(Object cond, Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_for(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _for(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_forIdx(com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt, int, int)
	 */
	@Override
	public JSContentInterface _forIdx(JSInt idx, int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_forIdx(java.lang.Object, com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray)
	 */
	@Override
	public JSContentInterface _forIdx(Object idx, JSArray<?> array) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_do(com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda)
	 */
	@Override
	public JSContentInterface _do(JSLambda c) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#endfor()
	 */
	@Override
	public JSContentInterface endfor() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#newJS(java.lang.Class, java.lang.Object[])
	 */
	@Override
	public <E> E newJS(Class<E> type, Object... param) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#txt(java.lang.Object[])
	 */
	@Override
	public JSString txt(Object... param) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#var(java.lang.Object[])
	 */
	@Override
	public JSAny var(Object... param) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#calc(java.lang.Object[])
	 */
	@Override
	public Object calc(Object... param) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#funct(java.lang.Object[])
	 */
	@Override
	public JSFunction funct(Object... param) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public JSFunction fct(JSLambda c) {
		return proxy.fct(c);
	}

	@Override
	public JSFunction fct(JSElement param, JSLambda c) {
		return proxy.fct(param, c);
	}

	@Override
	public JSFunction fct(JSElement param1, JSElement param2, JSLambda c) {
		return proxy.fct(param1, param2, c);
	}

	@Override
	public JSFunction fct(JSElement param1, JSElement param2, JSElement param3, JSLambda c) {
		return proxy.fct(param1, param2, param3, c);
	}

	@Override
	public JSFunction fct(JSElement param1, JSElement param2, JSElement param3, JSElement param4, JSLambda c) {
		return proxy.fct(param1, param2, param3, param4, c);
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#fragmentIf(java.lang.Object)
	 */
	@Override
	public JSFunction fragmentIf(Object condition) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_if(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _if(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#then(com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda)
	 */
	@Override
	public JSContentInterface then(JSLambda content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_else()
	 */
	@Override
	public JSContentInterface _else() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_else(com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda)
	 */
	@Override
	public JSContentInterface _else(JSLambda content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_elseif(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _elseif(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#endif()
	 */
	@Override
	public JSContentInterface endif() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_elseif_(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _elseif_(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#delete(java.lang.Object[])
	 */
	@Override
	public JSContentInterface delete(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#setTimeout(java.lang.Object[])
	 */
	@Override
	public JSContentInterface setTimeout(Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#setTimeout(com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda, java.lang.Object[])
	 */
	@Override
	public JSContentInterface setTimeout(JSLambda a, Object... content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_return(java.lang.Object[])
	 */
	@Override
	public JSContentInterface _return(Object... content) {
		// TODO Auto-generated method stub
		return proxy._return(content);
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_continue()
	 */
	@Override
	public JSContentInterface _continue() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_this()
	 */
	@Override
	public JSAny _this() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_void()
	 */
	@Override
	public JSVoid _void() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#$$subContent()
	 */
	@Override
	public Object $$subContent() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#$$gosubContent(java.lang.Object)
	 */
	@Override
	public Object $$gosubContent(Object content) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ILitteral#getStringJSON()
	 */
	@Override
	public String getStringJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
