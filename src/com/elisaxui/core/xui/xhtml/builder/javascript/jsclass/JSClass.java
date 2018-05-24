package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;

/**
 * interface proxy de class JS
 * @author Bureau
 *
 */
public interface JSClass extends JSContentInterface, JSElement, ILitteral  {

	Object _setContent(Object value);
	Object _getContent();
	
	<E> E cast(Class<E> cl, Object obj);
	<E> E callStatic(Class<E> cl);
	<E> E set(Object... value);
	<E> E asLitteral(); 
	<E> E declareType(Class<E> type, Object name);
	<E> JSArray<E> declareArray(Class<E> type, String name);
	/**
	 * execute en mode java => pas de js
	 * @param c
	 */
	void callJava(JSLambda c);
	
	static <E> E defVar()
	{
		return null;
	}
	
	static <E> JSArray<E> defArray(Class<? extends E> cl)
	{
		return new JSArray<E>().setArrayType(cl);
	}
}
