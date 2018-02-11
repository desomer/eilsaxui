package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Proxy;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSAnonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;

/**
 * interface proxy de class JS
 * @author Bureau
 *
 */
public interface JSClass extends JSContentInterface, JSElement  {

	/**
	 * sert a ne pas avoir de warning
	 * @return
	 */
	@Deprecated
	public static <E> E defVar() {
		return null;
	}
	
	/**
	 * sert a ne pas avoir de warning
	 * @return
	 */
	@Deprecated
	public static <E> E defAttr() {
		return null;
	}
	
	/**
	 * sert a ne pas avoir de warning
	 * @return
	 */
	public static <E> E declareType(Class<E> type, Object name) {
		
		boolean retJSVariable=JSVariable.class.isAssignableFrom(type);
		boolean retJSClass=JSClass.class.isAssignableFrom(type);
		
		if (retJSVariable)
		{
			JSVariable v =null;
			try {
				v = ((JSVariable)type.newInstance());
				v._setName(name) ;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
	
			return (E)v;
		}
		
		if (retJSClass)
		{
			JSClass prox = ProxyHandler.getProxy( (Class<? extends JSClass>) type);
			ProxyHandler.setNameOfProxy(null, prox, name);	
			return (E)prox;
		}
		
		return (E)name;
	}
	
	
	
	Object _setContent(Object value);
	Object _getContent();
	
	<E> E cast(Class<?> cl, Object obj);
	<E> E set(Object... value);
	<E> E asLitteral(); 
	
	/**
	 * execute en mode java => pas de js
	 * @param c
	 */
	void callJava(JSAnonym c);
}
