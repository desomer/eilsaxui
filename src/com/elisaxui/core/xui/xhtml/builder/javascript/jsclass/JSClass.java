package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Proxy;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;

/**
 * interface proxy de class JS
 * @author Bureau
 *
 */
public interface JSClass extends JSMethodInterface  {


	
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
	public static <E> E declareType(Class type, Object name) {
		
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
			JSClass prox = XHTMLPart.jsBuilder.getProxy( (Class<? extends JSClass>) type);
			XHTMLPart.jsBuilder.setNameOfProxy(null, prox, name);	
			return (E)prox;
		}
		
		return (E)name;
	}
	
	
	static Object _new(Class<? extends JSClass> cl, Object...param ) {
		StringBuilder buf = new StringBuilder();
		buf.append("new "+cl.getSimpleName() +"(");
		if (param!=null)
		{
			int i=0;
			for (Object object : param) {
				if (i>0)
					buf.append(", ");
				buf.append(object);
				i++;
			}
		}
		buf.append(")");
		return buf.toString() ;
	}
	
	
	Object _setContent(Object value);
	Object _getContent();
	<E> E cast(Class<?> cl, Object obj);
	
}
