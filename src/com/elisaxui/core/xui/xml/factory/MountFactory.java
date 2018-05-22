/**
 * 
 */
package com.elisaxui.core.xui.xml.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.annotation.xFactory;

/**
 * @author gauth
 *
 */
public abstract class MountFactory {

	public static ArrayList register(XHTMLPart src, Class<? extends MountFactory> mount, JSElement in, JSElement param)
	{		
		Method[] listMth = src.getClass().getDeclaredMethods();
		for (Method method : listMth) {
			xFactory aFactory = method.getAnnotation(xFactory.class);
			if (aFactory!=null && mount.isAssignableFrom(aFactory.value()))
				{
				   ArrayList code = new ArrayList<>();
				    try {
						Object ret = method.invoke(src, new Object[] {param});
						JSFunction fct = (JSFunction) new JSFunction().setParam(new Object[] {in})._return(ret);
						code.add("window."+mount.getSimpleName()+"=");
						code.add(fct);
						return code;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
		}
		return null;
	}
	
	
	public static ArrayList register(XHTMLPart src)
	{		
		ArrayList code = new ArrayList<>();
		Method[] listMth = src.getClass().getDeclaredMethods();
		for (Method method : listMth) {
			xFactory aFactory = method.getAnnotation(xFactory.class);
			if (aFactory!=null)
				{
				    try {
				    	
				    	Object[] p = new Object[method.getParameterCount()];

							for (int i = 0; i < method.getParameterCount(); i++) {
								// gestion du typage des parametres
								p[i] = ProxyHandler.getObjectJS(method.getParameterTypes()[i], "", "p"+i);

							}
				    	
						Object ret = method.invoke(src, p);
						JSFunction fct = (JSFunction) new JSFunction().setParam(new Object[] {"p0"})._return(ret);
						code.add("window."+aFactory.value().getSimpleName()+"=");
						code.add(fct);
						code.add(";");
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
						e.printStackTrace();
					}
				}
		}
		return code;
	}
	
	
	
	/*
	 * xui().mount("createPage") .container("listePage") .on("append", main)
	 */ // "replace"

	/*
	 * xui().container("listePage").set(id);
	 */
}
