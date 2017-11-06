/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;

public class MethodInvocationHandler implements InvocationHandler {
	
	
	static boolean debug = true;
	static boolean debug2 = false;
	
	
	private Object varname; // nom de la variable qui contient le proxy
	private Class<? extends JSClass> implementClass;   // type js de la class
	private Map<String, JSContent> mapContent = new HashMap<>(); // contenu de la methode
	private JSBuilder jsbuilder;
	
	private String currentFctJSName = null;
	private static boolean testAnonymousInProgress = false;
	

	public MethodInvocationHandler(Class<? extends JSClass> impl, JSBuilder jsbuilder) {
		this.setImplementClass(impl);
		this.jsbuilder=jsbuilder;
	}
	
	/**
	 * interception des appel de methode de interface
	 * 	- creer une JSClassImpl si n'existe pas
	 *  - creer les fct javascript
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (method.getName().equals("toString")) {
			if (getVarName() == null)
				return "???";
			return getVarName().toString();
		}

		String id = JSClassImpl.getMethodId(method, args);
		JSClassImpl implcl = XUIFactoryXHtml.getXHTMLFile().getClassImpl(jsbuilder, getImplementClass());
		boolean isMthAlreadyInClass = implcl.getListDistinctFct().containsKey(id);

		if (!isMthAlreadyInClass) {
			/********************************************************************/
			/* 		CREATION DU CODE DANS LA FCT								*/
			/********************************************************************/
			if (method.isDefault()) {
				
				if (testAnonymousInProgress)
					return JSClassImpl.toJSCallInner(getVarName(), method, args);
				
				/*****  APPEL DES FUNCTION DE LA CLASSE *****/ 
				MethodDesc MthInvoke = new MethodDesc(implcl, proxy, method, args);
				if (currentFctJSName==null)
				{
										
					implcl.getListDistinctFct().put(id, id);
					
					currentFctJSName=id;
					if (debug)
						System.out.println("[JSBuilder]   include mth "+id+" of class " + implcl.getName());
					
					// initialise le proxy
					MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(proxy);
					Object nameProxy = mh.varname;   // recupere le nom de la varibale du proxy
					mh.varname = "this"; // force a this pour appel interne d'autre fct de la classe JS
					
					JSFunction fct = createJSFunctionImpl(MthInvoke, false);    // creer le code
//					JSClassImpl ImplClass = jsbuilder.getJSClassImpl(getImplementClass());
					//ImplClass.addFunction(fct);
					implcl.addFunction(fct);
					
					mh.varname = nameProxy;
				}
				else
				{

					/*********************** APPEL A UNE AUTRE FCT INTERNE => AJOUTE DANS getListHandleFuntionPrivate **************************/

					/**************************     test si utilisation class Anonym **********************************/	
					MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(proxy);
					Object nameProxy = mh.varname;
					mh.varname = "this"; // force a this pour appel interne d'autre fct de la classe JS
					
					JSFunction fctAnomyn = createJSFunctionImpl(MthInvoke, true);    // test si utilisation class Anonym
					
					mh.varname = nameProxy;
					if (fctAnomyn!=null)
						return fctAnomyn;   
					/*******************************************************************************/
					
				    // appel d'une function js de la même class
					if (debug2)
						System.out.println("[JSBuilder]******************************** mth "+id+" of class " + implcl.getName() + " next = "+currentFctJSName);
					
					implcl.getListHandleFuntionPrivate().add(MthInvoke);
					
					return JSClassImpl.toJSCallInner(getVarName(), method, args);
				}
			} 
			else if (! checkMethodExist(method) )
			{
				
				boolean retJSVariable=JSVariable.class.isAssignableFrom(method.getReturnType());
				boolean retJSClass=JSClass.class.isAssignableFrom(method.getReturnType());
				
				if (retJSVariable)
				{
					JSVariable retJSVar = (JSVariable)method.getReturnType().newInstance();
					retJSVar.setName(getVarName() + "." + method.getName());
					return retJSVar;
				}
				else if (retJSClass)
				{
					
					JSClass prox = XHTMLPart.jsBuilder.getProxy( (Class<? extends JSClass>) method.getReturnType());
					XHTMLPart.jsBuilder.setNameOfProxy(getVarName()+".", prox, method.getName());
					return prox;
				}
				else
				{
					// genere le code de call json	
					List<Object> buf = new ArrayList<Object>();
					buf.add(getVarName() + "." + method.getName());
					return buf;
				}
			}
			else	
				{
				/*****  APPEL DES FUNCTION INTERNE (var, set, if) sur la class JSContent*****/ 
				
				// creer le JSContent
				JSContent currentJSContent = mapContent.get(currentFctJSName);
				
				if (currentJSContent==null)
				{
					currentJSContent= jsbuilder.createJSContent();
					if (debug2)
						System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - createJSContent "+currentFctJSName+" of class " + implcl.getName());
					mapContent.put(currentFctJSName, currentJSContent); // creer le contenu
				}

				System.out.println("[JSBuilder]"+System.identityHashCode(currentJSContent)+ " - appel meth "+ method.getName() + "  du JSContent "+currentFctJSName+" of class " + implcl.getName());

				
				// appel l'implementation le methode JSInterface
				Object ret =  method.invoke(currentJSContent, args);

				if (method.getName().equals("fct"))   // gestion appel fct anonyme par fct()
				{
					((JSFunction)ret).proxy=(JSMethodInterface) proxy; 
				}
				
				if (debug2)
				{
					// trace du retour
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

		//  creer le js du call de la fct
		return JSClassImpl.toJSCall(getVarName(), method, args);
	}

	/**
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 */
	private boolean checkMethodExist(Method method) {
		try {
			return JSContent.class.getMethod(method.getName(), method.getParameterTypes())!=null;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	
	private JSFunction createJSFunctionImpl(MethodDesc handle, boolean testAnonymous) 
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
		
		
		Object prevCode = null;
		JSContent code = null;
		
		if (testAnonymous)
		{    
			testAnonymousInProgress = true;
			code= mapContent.get(currentFctJSName);
		    prevCode = code.$$subContent();
		}
		
		// appel la fct la classJS    ==> entrainte les appel  invoke de cette classe
		Object ret = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
				.unreflectSpecial(handle.method, declaringClass).bindTo(handle.proxy).invokeWithArguments(p);
		
		JSFunction fct= null;
		
		if (testAnonymous && ret instanceof Anonym)
		{
			testAnonymousInProgress = false;
			((Runnable)ret).run();
			Object aCode = code.$$gosubContent(prevCode);
			prevCode=null;
			JSContent cont = jsbuilder.createJSContent();
			cont.$$gosubContent(aCode);
			fct = jsbuilder.createJSFunction().setParam(p).setCode(cont);
			
		}
		else if (testAnonymous==false)
		{
			String id = JSClassImpl.getMethodId(handle.method, handle.args);
		    code = mapContent.get(id); 
		    
			//ajouter en fin de methode JS
			if (ret!=null && !(ret instanceof JSContent))
			{
				code._return(ret);
			}
		    
			if (debug2)
				System.out.println(System.identityHashCode(code)+ " - return JSFunction " + id);
			
			fct = jsbuilder.createJSFunction().setName(handle.method.getName()).setParam(p)
					.setCode(code);
			
			// function en cours terminé 
			currentFctJSName = null;
			
			// invoke les methodes interne private a la fin de la creation de la methode
			for(Iterator<MethodDesc> i = handle.implcl.getListHandleFuntionPrivate().iterator(); i.hasNext();) {
					MethodDesc nextHandle = i.next();
				    i.remove();	      					    
				    nextHandle.method.invoke(nextHandle.proxy, nextHandle.args);
			}
		}
		
		if (testAnonymous && prevCode!=null)
		{ 
			code.$$gosubContent(prevCode);
			testAnonymousInProgress = false;
		}
		
		return fct;
	}

	/**
	 * @return the name
	 */
	public Object getVarName() {
		return varname;
	}

	/**
	 * @param name the name to set
	 */
	public void setVarName(Object name) {
		this.varname = name;
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