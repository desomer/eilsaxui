package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.builder.HashCodeExclude;

import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodDesc;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.MethodInvocationHandler;
import com.elisaxui.core.xui.xml.annotation.xForceInclude;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;


/**
 * 
 *  invoke Proxy => xxxx => createJSFunctionImpl
 * 
 * @author Bureau
 *
 */

public class JSBuilder extends XUIFormatManager {    
	

	/**TODO a mettre dans une autre class */
	public void setNameOfProxy(String prefix, Object inst, Object name) {
		MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(inst);
		mh.setVarName(prefix==null?name :( prefix + name));
	}


	@SuppressWarnings("unchecked")
	/**TODO a mettre dans une autre class */
	public final <E extends JSClass> E getProxy(final Class<? extends JSClass> cl) {

		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { cl }, new MethodInvocationHandler(cl));
		return (E) proxy;
	}

	
	public static JSFunction doFctAnonym(JSFunction f,  Runnable c)
	{
		Object ret = f.proxy.$$subContent();
		
		try {
			 c.run();
			MethodDesc currentMethodDesc = MethodInvocationHandler.ThreadLocalMethodDesc.get();
			MethodInvocationHandler.doLastSourceLineInsered(currentMethodDesc, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		Object sub = f.proxy.$$gosubContent(ret);
		JSContent cont = f.jsBuilder.createJSContent();
		cont.$$gosubContent(sub);
		f.setCode(cont);
		return f;
	}
	
	/***************************************************************************/
	
	public static void initJSConstructor(Class<? extends JSClass> cl, String name) {
		// init constructor
		JSClass inst = XHTMLPart.getJSBuilder().getProxy(cl);
		Method[] lism = cl.getDeclaredMethods();
		
		xForceInclude annInclude = cl.getAnnotation(xForceInclude.class);
		boolean includeAllMth = annInclude != null;
		
		if (lism != null) {
			for (Method method : lism) {
				boolean isPublic = method.isDefault();
				xForceInclude annIncludeMth = method.getAnnotation(xForceInclude.class);
				
				boolean isForceIncluded = annIncludeMth != null;
				boolean includeMth = isPublic && (isForceIncluded || includeAllMth);
				
				if (includeMth || method.getName().equals("constructor")) {
					try {
						method.invoke(inst, new Object[method.getParameterCount()]);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| SecurityException e) {
						ErrorNotificafionMgr.doError("pb constructor sur " + cl.getSimpleName(), e);
						e.printStackTrace();
					}

				}
			}
		}
	}
	
	@Deprecated
	public JSClassImpl createJSClass() {
		return new JSClassImpl();
	}

	@Deprecated
	public JSFunction createJSFunction() {
		return new JSFunction();
	}
	
	@Deprecated
	public JSContent createJSContent() {
		return new JSContent();
	}

}
