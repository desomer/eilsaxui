package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;


/**
 * 
 *  invoke Proxy => xxxx => createJSFunctionImpl
 * 
 * @author Bureau
 *
 */

public class JSBuilder extends XUIFormatManager {    
	
	
	static boolean debug = true;
	static boolean debug2 = false;
	
	public JSBuilder() {
		// TODO Auto-generated constructor stub
	}

	public void setNameOfProxy(String prefix, Object inst, Object name) {
		aInvocationHandler mh = (aInvocationHandler) Proxy.getInvocationHandler(inst);
		mh.name = prefix + name;
	}


	abstract class aInvocationHandler implements InvocationHandler {
		// public Object proxy; // le proxy
		public Object name; // nom de la variable qui contient le proxy
		public Class<? extends JSClass> implementClass;   // type js de la class
		public Map<String, JSContent> mapContent = new HashMap<>(); // contenu de la methode
		public String nextName = null;
	}

	public final class MethodInvocationHandle
	{
		public MethodInvocationHandle(JSClassImpl implcl, Object proxy, Method method, Object[] args) {
			super();
			this.implcl = implcl;
			this.proxy = proxy;
			this.method = method;
			this.args = args;
		}
		
		JSClassImpl implcl;
		Object proxy; 
		Method method;
		Object[] args;
	}
	

	@SuppressWarnings("unchecked")
	public final <E extends JSClass> E getProxy(final Class<? extends JSClass> cl) {

		
		class MyInvocationHandler extends aInvocationHandler {

			public MyInvocationHandler(Class<? extends JSClass> impl) {
				this.implementClass = impl;
			}

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				if (method.getName().equals("toString")) {
					if (name == null)
						return "???";
					return name.toString();
				}

				String id = getMethodId(method, args);
				JSClassImpl implcl = XUIFactoryXHtml.getXHTMLFile().getClassImpl(JSBuilder.this, cl);
				boolean isMthAlreadyInClass = implcl.listDistinctFct.containsKey(id);

				if (!isMthAlreadyInClass) {
					if (method.isDefault()) {
						
						MethodInvocationHandle MthInvoke = new MethodInvocationHandle(implcl, proxy, method, args);
						if (nextName==null)
						{
							implcl.listDistinctFct.put(id, id);
	
						//	JSContent r = mapContent.remove(id);					
							nextName=id;
							if (debug)
								System.out.println("[JSBuilder]   include mth "+id+" of class " + implcl.name);
							
							JSFunction fct = createJSFunctionImpl(MthInvoke);    // creer le code
							JSClassImpl ImplClass = getJSClassImpl(cl, proxy);
							ImplClass.addFunction(fct);
						}
						else
						{
						    // appel d'une function js de la même class
							if (debug2)
								System.out.println("[JSBuilder]******************************** mth "+id+" of class " + implcl.name + " next = "+nextName);
							implcl.listHandleFuntionPrivate.add(MthInvoke);
							
							return toJSCallInner(method, args);
						}
					} else {
						
						JSContent currentContent = mapContent.get(nextName);
						if (currentContent==null)
						{
							currentContent=createJSContent();
							if (debug2)
								System.out.println("[JSBuilder]"+System.identityHashCode(currentContent)+ " - createJSContent "+nextName+" of class " + implcl.name);
							mapContent.put(nextName, currentContent); // creer le contenu
						}
						
						// appel l'implementation le methode JSInterface
						Object ret =  method.invoke(currentContent, args);
						if (debug2)
						{
							if (ret instanceof JSContent )
							{
								System.out.println("[JSBuilder]"+System.identityHashCode(currentContent)+ " - appel de la mth "+id+" of class " + implcl.name +" => "+((JSContent)ret).listElem );
							}	
							else
								System.out.println("[JSBuilder]"+System.identityHashCode(currentContent)+ " - [no JSContent] appel de la mth "+id+" of class " + implcl.name +" => "+ret );
	
							System.out.println("[JSBuilder]"+System.identityHashCode(currentContent)+ " - value = " +currentContent.listElem);
						}						
						return ret;
					}
				}

				return toJSCall(method, args);
			}

			/** TODO a deplacer dans JSClassImpl  */
			
			private JSFunction createJSFunctionImpl(MethodInvocationHandle handle) 
							throws NoSuchMethodException, 
							Throwable, 
							IllegalAccessException,	
							InstantiationException, 
							InvocationTargetException {

				final Class<?> declaringClass = handle.method.getDeclaringClass();
				Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
						.getDeclaredConstructor(Class.class, int.class);
				constructor.setAccessible(true);

				Parameter[] param = handle.method.getParameters();
				Object[] p = new Object[param.length];

				if (handle.args != null) {
					for (int i = 0; i < handle.args.length; i++) {
						p[i] = param[i].getName();
					}
				}

				// appel la fct la classJS 
				Object ret = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
						.unreflectSpecial(handle.method, declaringClass).bindTo(handle.proxy).invokeWithArguments(p);

				
				String id = getMethodId(handle.method, handle.args);
			    JSContent code = mapContent.get(id); 
			    
				//ajouter en fin de methode JS
				if (ret!=null && !(ret instanceof JSContent))
				{
					code._return(ret);
				}
			    
				if (debug2)
					System.out.println(System.identityHashCode(code)+ " - return JSFunction " + id);
				
				JSFunction fct = createJSFunction().setName(handle.method.getName()).setParam(p)
						.setCode(code);
				
				nextName = null;
				
				// invoke les methodes private
				for(Iterator<MethodInvocationHandle> i = handle.implcl.listHandleFuntionPrivate.iterator(); i.hasNext();) {
						MethodInvocationHandle nextHandle = i.next();
					    i.remove();	      					    
					    nextHandle.method.invoke(nextHandle.proxy, nextHandle.args);
				}
				
				return fct;
			}

			/**
			 * retourne le call
			 * 
			 * @param method
			 * @param args
			 * @return
			 */
			private List<Object> toJSCall(Method method, Object[] args) {
				List<Object> buf = new ArrayList<Object>();
				buf.add(name + "." + method.getName() + "(");

				int i = 0;
				if (args != null) {
					for (Object p : args) {
						if (i > 0)
							buf.add(", ");
						buf.add(p);
						i++;
					}
				}
				buf.add(")");
				return buf;
			}
			
			/**
			 * retourne le call
			 * 
			 * @param method
			 * @param args
			 * @return
			 */
			private List<Object> toJSCallInner(Method method, Object[] args) {
				List<Object> buf = new ArrayList<Object>();
				buf.add("this." + method.getName() + "(");

				int i = 0;
				if (args != null) {
					for (Object p : args) {
						if (i > 0)
							buf.add(", ");
						buf.add(p);
						i++;
					}
				}
				buf.add(")");
				return buf;
			}
		}

		MyInvocationHandler ih = new MyInvocationHandler(cl);

		Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { cl }, ih);
		// ih.proxy = proxy;
		return (E) proxy;
	}

	
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
	
	/**
	 * id  = nom method + nb argument
	 * @param method
	 * @param args
	 * @return
	 */
	public String getMethodId(Method method, Object[] args) {
		StringBuilder buf = new StringBuilder();
		buf.append(method.getName());
		buf.append(".");
		buf.append(args == null ? 0 : args.length);
		return buf.toString();
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

	public static final class JSNewLine {
	};

	public static final class JSElem {
		public Object value;
	}
}
