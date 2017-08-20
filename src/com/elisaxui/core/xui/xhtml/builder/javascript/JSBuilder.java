package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInvocationHandler;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;


/**
 * 
 *  invoke Proxy => xxxx => createJSFunctionImpl
 * 
 * @author Bureau
 *
 */

public class JSBuilder extends XUIFormatManager {    
	

	public JSBuilder() {

	}

	public void setNameOfProxy(String prefix, Object inst, Object name) {
		JSClassInvocationHandler mh = (JSClassInvocationHandler) Proxy.getInvocationHandler(inst);
		mh.setName(prefix + name);
	}


	@SuppressWarnings("unchecked")
	public final <E extends JSClass> E getProxy(final Class<? extends JSClass> cl) {

		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { cl }, new JSClassInvocationHandler(cl, this));
		return (E) proxy;
	}

	
	
	/***************************************************************************/
	
	public static void initJSConstructor(Class<? extends JSClass> cl, String name) {
		// init constructor
		JSClass inst = XHTMLPart.jsBuilder.getProxy(cl);
		Method[] lism = cl.getDeclaredMethods();
		if (lism != null) {
			for (Method method : lism) {
				if (method.getName().equals("constructor")) {
//					System.out.println("[JSBuilder]  include default constructor of class " + name);
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
	
		
	public JSClassImpl getJSClassImpl(Class<? extends JSClass> cl, Object proxy) throws IllegalAccessException {
		JSClassImpl ImplClass = XUIFactoryXHtml.getXHTMLFile().getClassImpl(JSBuilder.this, cl);
//		if (!ImplClass.isInitialized()) {
//
//			ImplClass.setInitialized(true);
//		}
		return ImplClass;
	}
	
	public JSClassImpl createJSClass() {
		return new JSClassImpl(this);
	}

	public JSFunction createJSFunction() {
		return new JSFunction(this);
	}

	public JSContent createJSContent() {
		return new JSContent(this);
	}



//	public static final class JSElem {
//		public Object value;
//	}
}
