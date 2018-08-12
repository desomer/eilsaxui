/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.beans.PropertyWrapper;

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
			proxy = (JSClass) ProxyHandler.getObjectJS(JSClass.class, "", "");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#_setContent(java.lang.Object)
	 */
	@Override
	public Object _setContent(Object value) {
		return null;
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass#_getContent()
	 */
	@Override
	public Object _getContent() {
		return null;
	}

	@Override
	public <E> E cast(Class<E> cl, Object obj) {
		return proxy.cast(cl, obj);
	}

	@Override
	public <E> E set(Object... value) {
		return proxy.set(value);
	}

	@Override
	public <E> E asLitteral() {
		return proxy.asLitteral();
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

	@Override
	public JSContentInterface __(Object... content) {
		return proxy.__(content);
	}

	@Override
	public JSContentInterface _set(Object name, Object... content) {
		return proxy._set(name, content);
	}

	@Override
	public JSContentInterface _var(Object name, Object... content) {
		return proxy._var(name, content);
	}

	@Override
	public <E> E let(Class<E> type, Object name, Object... content) {
		return proxy.let(type, name, content);
	}


	@Override
	public <E> E let(String name, E content) {
		return proxy.let(name, content);
	}


	@Override
	public void let(JSContentInterface name, Object... content) {
		proxy.let(name, content);
	}

	@Override
	public void let(JSAny name, Object... content) {
		proxy.let(name, content);
	}

	@Override
	public JSContentInterface consoleDebug(Object... content) {
		return proxy.consoleDebug(content);
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


	@Override
	public JSString txt(Object... param) {
		return proxy.txt(param);
	}

	@Override
	public JSAny var(Object... param) {
		return proxy.var(param);
	}

	@Override
	public Object calc(Object... param) {
		return proxy.calc(param);
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

	@Override
	public JSContentInterface _if(Object... content) {
		return proxy._if(content);
	}


	@Override
	public JSContentInterface then(JSLambda content) {
		return proxy.then(content);
	}


	/* (non-Javadoc)
	 * @see com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface#_else()
	 */
	@Override
	public JSContentInterface _else() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public JSContentInterface _else(JSLambda content) {
		return proxy._else(content);
	}

	@Override
	public JSContentInterface _elseif(Object... content) {
		return proxy._elseif(content);
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


	@Override
	public JSContentInterface delete(Object... content) {
		return proxy.delete(content);
	}

	@Override
	public JSContentInterface setTimeout(Object... content) {
		return proxy.setTimeout(content);
	}

	@Override
	public JSContentInterface setTimeout(JSLambda a, Object... content) {
		return proxy.setTimeout(a, content);
	}


	@Override
	public JSContentInterface _return(Object... content) {
		return proxy._return(content);
	}


	@Override
	public JSContentInterface _continue() {
		return proxy._continue();
	}


	@Override
	public JSAny _this() {
		return proxy._this();
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
