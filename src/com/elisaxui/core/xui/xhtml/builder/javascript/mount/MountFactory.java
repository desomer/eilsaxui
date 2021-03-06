/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.mount;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent.JSNewLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;

/**
 * @author gauth
 *
 */
public abstract class MountFactory {

	@Deprecated
	public static List<Object> register(XHTMLPart src, Class<? extends MountFactory> mount, JSElement in, JSElement param)
	{		
		Method[] listMth = src.getClass().getDeclaredMethods();
		for (Method method : listMth) {
			xMount aFactory = method.getAnnotation(xMount.class);
			if (aFactory!=null && mount.isAssignableFrom(aFactory.value()))
				{
				   ArrayList<Object> code = new ArrayList<>();
				    try {
						Object ret = method.invoke(src, new Object[] {param});
						JSFunction fct = (JSFunction) new JSFunction().setParam(new Object[] {in})._return(ret);
						code.add("window.xMount."+mount.getSimpleName()+"=");
						code.add(fct);
						return code;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
		}
		return null;
	}
	
	/**
	 * genere le ligne de code du register
	 * @param src
	 * @return
	 */
	public static ArrayList<Object> register(XHTMLPart src)
	{		
		ArrayList<Object> code = new ArrayList<>();
		code.add("if (window.xMount==null) window.xMount={};");
		code.add(JSNewLine.class);
		Method[] listMth = src.getClass().getDeclaredMethods();
		
		List<Method> lm = Arrays.asList(listMth);
		
		lm.sort(Comparator.comparing(Method::getName));
		
		for (Method method : lm) {
			xMount aFactory = method.getAnnotation(xMount.class);
			if (aFactory!=null)
				{
				    try {
				    	
				    	Object[] p = new Object[method.getParameterCount()];

							for (int i = 0; i < method.getParameterCount(); i++) {
								// gestion du typage des parametres
								p[i] = ProxyHandler.getObjectJS(method.getParameterTypes()[i], "", "p"+i);

							}
						Object ret = method.invoke(src, p);
						
						String namec = aFactory.value().getName(); 
						namec = namec.substring(namec.lastIndexOf('.')+1);
						
						JSFunction fct = (JSFunction) new JSFunction().setComment("MountFactory "+method.getName()+ " ("+namec +".java)").setParam(new Object[] {"p0"})._return(ret);
						code.add(JSNewLine.class);
						code.add("window.xMount."+aFactory.value().getSimpleName()+"=");
						code.add(fct);
						code.add(";");
						code.add(JSNewLine.class);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
						e.printStackTrace();
					}
				}
		}
		return code;
	}
	
	
	
	/*
	 * xui().mount("createPage") .container("listePage") .on("append", cMain)
	 */ // "replace"

	/*
	 * xui().container("listePage").set(id);
	 */
}
