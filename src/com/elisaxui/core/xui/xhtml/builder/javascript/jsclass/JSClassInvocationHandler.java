/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;

public class JSClassInvocationHandler implements InvocationHandler {
	
	
	static boolean debug = true;
	static boolean debug2 = false;
	
	
	private Object name; // nom de la variable qui contient le proxy
	private Class<? extends JSClass> implementClass;   // type js de la class
	private Map<String, JSContent> mapContent = new HashMap<>(); // contenu de la methode
	private JSBuilder jsbuilder;
	
	private String nextName = null;
	

	public JSClassInvocationHandler(Class<? extends JSClass> impl, JSBuilder jsbuilder) {
		this.setImplementClass(impl);
		this.jsbuilder=jsbuilder;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (method.getName().equals("toString")) {
			if (getName() == null)
				return "???";
			return getName().toString();
		}

		String id = JSClassImpl.getMethodId(method, args);
		JSClassImpl implcl = XUIFactoryXHtml.getXHTMLFile().getClassImpl(jsbuilder, getImplementClass());
		boolean isMthAlreadyInClass = implcl.getListDistinctFct().containsKey(id);

		if (!isMthAlreadyInClass) {
			if (method.isDefault()) {
				
				MethodInvocationHandle MthInvoke = new MethodInvocationHandle(implcl, proxy, method, args);
				if (nextName==null)
				{
					implcl.getListDistinctFct().put(id, id);
					
					nextName=id;
					if (debug)
						System.out.println("[JSBuilder]   include mth "+id+" of class " + implcl.getName());
					
					JSClassInvocationHandler mh = (JSClassInvocationHandler) Proxy.getInvocationHandler(proxy);
					
					Object nameProxy = mh.name;
					mh.name = "this"; // force a this pour appel interne d'autre fct de la classe JS
					
					JSFunction fct = createJSFunctionImpl(MthInvoke);    // creer le code
					JSClassImpl ImplClass = jsbuilder.getJSClassImpl(getImplementClass(), proxy);
					ImplClass.addFunction(fct);
					
					mh.name = nameProxy;
				}
				else
				{
				    // appel d'une function js de la même class
					if (debug2)
						System.out.println("[JSBuilder]******************************** mth "+id+" of class " + implcl.getName() + " next = "+nextName);
					implcl.getListHandleFuntionPrivate().add(MthInvoke);
					
					return JSClassImpl.toJSCallInner(method, args);
				}
			} else {
				// creer le JSContent
				JSContent currentJSContent = mapContent.get(nextName);
				if (currentJSContent==null)
				{
					currentJSContent= jsbuilder.createJSContent();
					if (debug2)
						System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - createJSContent "+nextName+" of class " + implcl.getName());
					mapContent.put(nextName, currentJSContent); // creer le contenu
				}

				System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - appel meth "+ method.getName() + "  du JSContent "+nextName+" of class " + implcl.getName());

				
				// appel l'implementation le methode JSInterface
				Object ret =  method.invoke(currentJSContent, args);
				
				if (debug2)
				{
					// trace du retour du retour
					if (ret instanceof JSContent )
					{
						System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - appel de la mth "+id+" of class " + implcl.getName() +" => "+((JSContent)ret).getListElem() );
					}	
					else
						System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - [no JSContent] appel de la mth "+id+" of class " + implcl.getName() +" => "+ret );

					System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - value = " +currentJSContent.getListElem());
				}						
				return ret;
			}
		}

		return JSClassImpl.toJSCall(getName(), method, args);
	}

	/** TODO a deplacer dans JSClassImpl  */			
	private JSFunction createJSFunctionImpl(MethodInvocationHandle handle) 
					throws Throwable {

		final Class<?> declaringClass = handle.method.getDeclaringClass();
		Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
		constructor.setAccessible(true);

		Parameter[] param = handle.method.getParameters();
		Object[] p = new Object[param.length];

		if (handle.args != null) {
			for (int i = 0; i < handle.args.length; i++) {
				p[i] = param[i].getName();
			}
		}
		
		// appel la fct la classJS    ==> entrainte les appel  invoke de cette classe
		Object ret = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
				.unreflectSpecial(handle.method, declaringClass).bindTo(handle.proxy).invokeWithArguments(p);
		
		String id = JSClassImpl.getMethodId(handle.method, handle.args);
	    JSContent code = mapContent.get(id); 
	    
		//ajouter en fin de methode JS
		if (ret!=null && !(ret instanceof JSContent))
		{
			code._return(ret);
		}
	    
		if (debug2)
			System.out.println(System.identityHashCode(code)+ " - return JSFunction " + id);
		
		JSFunction fct = jsbuilder.createJSFunction().setName(handle.method.getName()).setParam(p)
				.setCode(code);
		
		nextName = null;
		
		// invoke les methodes private
		for(Iterator<MethodInvocationHandle> i = handle.implcl.getListHandleFuntionPrivate().iterator(); i.hasNext();) {
				MethodInvocationHandle nextHandle = i.next();
			    i.remove();	      					    
			    nextHandle.method.invoke(nextHandle.proxy, nextHandle.args);
		}
		
		return fct;
	}

	/**
	 * @return the name
	 */
	public Object getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(Object name) {
		this.name = name;
	}

	/**
	 * @return the implementClass
	 */
	public Class<? extends JSClass> getImplementClass() {
		return implementClass;
	}

	/**
	 * @param implementClass the implementClass to set
	 */
	public void setImplementClass(Class<? extends JSClass> implementClass) {
		this.implementClass = implementClass;
	}
}