/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;

public class MethodInvocationHandler implements InvocationHandler {

	private static final String DU_JS_CONTENT = "  du JSContent ";
	private static final String OF_CLASS = " of class ";
	
	static boolean debug = false;
	static boolean debug2 = false;
	static boolean debug3 = false;

	private Object varname; // nom de la variable du proxy
	private Object varContent; // contenu code du proxy
	private Class<? extends JSClass> implementClass; // type js de la class

	private Map<String, MethodDesc> mapContentMthBuildByProxy = new HashMap<>(); // contenu des methodes crée par proxy

	private String currentFctBuildByProxy = null; //
	private boolean testAnonymInProgress = false; // test si la methode doit retourner une fonction anonym

	// methode en attente d'ajout dans le code
	public static final ThreadLocal<MethodDesc> ThreadLocalMethodDesc = new ThreadLocal<>(); 

	public MethodInvocationHandler(Class<? extends JSClass> impl) {
		this.setImplementClass(impl);
	}

	/**
	 * interception des appel de methode de interface - creer une JSClassImpl si
	 * n'existe pas - creer les fct javascript
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (method.getName().equals("toString")) {
			return getProxyContent();
		}

		if (method.getName().equals("_getContent")) {
			return varContent;
		}

		if (method.getName().equals("_setContent")) {
			varContent = args[0];
			return proxy;
		}

		if (method.getName().equals("set")) {
			Object[] param = (Object[]) args[0];
			return ThreadLocalMethodDesc.get().content._set(proxy, param);
		}

		if (method.getName().equals("cast")) {
			return cast((Class<?>) args[0], args[1]);
		}

		String id = JSClassImpl.getMethodId(method, args);
		JSClassImpl implcl = XUIFactoryXHtml.getXHTMLFile().getClassImpl(XHTMLPart.jsBuilder, getImplementClass());
		boolean isMthAlreadyInClass = implcl.getListDistinctFct().containsKey(id);

		if (isMthAlreadyInClass) {
			// creer le js du call de la fct
			Object ret = JSClassImpl.toJSCall(getProxyContent(), method, args); // utilise le nom du proxy
			if (!testAnonymInProgress) {
				registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
			}
			return ret;
		} else {
			/********************************************************************/
			/* CREATION DU CODE DANS LA FCT */
			/********************************************************************/
			if (method.isDefault()) {

				if (testAnonymInProgress)
					return JSClassImpl.toJSCall("/*ww4*/this", method, args); // ne creer pas la fct en testAnonymInProgress

				MethodDesc mthInvoke = new MethodDesc(implcl, proxy, method, args);

				if (currentFctBuildByProxy == null) {
					/*******************************************/
					/***** APPEL DES FUNCTION DE LA CLASSE *****/
					/*******************************************/
					implcl.getListDistinctFct().put(id, id);
					currentFctBuildByProxy = id;

					if (debug)
						println("  include mth " + id + OF_CLASS + implcl.getName());

					// initialise le proxy
					MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(proxy);
					Object nameProxy = mh.varname; // recupere le nom de la variable du proxy
					mh.varname = "/*ww2*/this"; // force a this pour appel interne d'autre fct de la classe JS

					// creer le JSContent
					MethodDesc currentMethodDesc = getMethodDesc(implcl, proxy, currentFctBuildByProxy);
					MethodDesc lastMethodDesc = ThreadLocalMethodDesc.get();
					ThreadLocalMethodDesc.set(currentMethodDesc);

					if (debug3)
						println(System.identityHashCode(currentMethodDesc.content)
										+ " -add ThreadLocalMethodDesc meth " + method.getName() + DU_JS_CONTENT
										+ currentFctBuildByProxy + OF_CLASS + implcl.getName());

					// creer le code
					JSFunction fct = createJSFunctionImpl(mthInvoke, false);
					implcl.addFunction(fct);

					ThreadLocalMethodDesc.set(lastMethodDesc);
					if (debug3)
						println(System.identityHashCode(currentMethodDesc.content)
										+ " -restore ThreadLocalMethodDesc meth " + method.getName() + DU_JS_CONTENT
										+ currentFctBuildByProxy + OF_CLASS + implcl.getName());

					mh.varname = nameProxy;

					// creer le js du call de la fct
					Object ret = JSClassImpl.toJSCall(getProxyContent(), method, args);
					registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
					return ret;
				} else {

					/*****************************************************
					 * APPEL A UNE AUTRE FCT INTERNE A LA FCT EN COURS DU PROXY => AJOUTE DANS getListHandleFuntionPrivate
					 *****************************************************/

					/**************************
					 * test si utilisation class Anonym
					 **********************************/
					MethodInvocationHandler mh = (MethodInvocationHandler) Proxy.getInvocationHandler(proxy);
					Object nameProxy = mh.varname;
					mh.varname = "/*ww3*/this"; // force a this pour appel interne d'autre fct de la classe JS

					/*******************************************************************************/
					// test si utilisation class Anonym
					/*******************************************************************************/
					JSFunction fctAnomyn = createJSFunctionImpl(mthInvoke, true);

					mh.varname = nameProxy;
					if (fctAnomyn != null) {
						return fctAnomyn;
					}
					/*******************************************************************************/
					// appel d'une function js de la même class
					if (debug2)
						println("** mth " + id + OF_CLASS + implcl.getName() + " next = " + currentFctBuildByProxy);

					implcl.getListHandleFuntionPrivate().add(mthInvoke);

					Object ret = JSClassImpl.toJSCall("/*ww1*/this", method, args);

					// registerMethod
					registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
					return ret;
				}
			} else if (!checkMethodExist(method)) {
				/******************************************************************************/
				/***** INSERT le nom de la methode abstract de type Interface de variable ****/
				/******************************************************************************/
				Object objName = currentFctBuildByProxy==null?getProxyContent():"/*ww4*/this";
				Object ret = getObjectJS(method.getReturnType(), objName + ".", method.getName());

				if (ret instanceof JSArray) {
					initTypeOfJSArray(method, ret);
				}
				return ret;
			} else {
				/******************************************************************************/
				/***** APPEL DES FUNCTION INTERNE (var, set, if) sur la class JSContent *****/
				/******************************************************************************/
				// creer le JSContent
				MethodDesc currentMethodDesc = getMethodDesc(implcl, proxy, currentFctBuildByProxy);
				JSContent currentJSContent = currentMethodDesc.content;

				if (!testAnonymInProgress)
					doLastSourceLineInsered(ThreadLocalMethodDesc.get(), false);

				if (debug2)
					println(System.identityHashCode(currentJSContent)
							+ " - appel meth " + method.getName() + DU_JS_CONTENT + currentFctBuildByProxy
							+ OF_CLASS + implcl.getName());

				// appel l'implementation le methode JSInterface
				Object ret = method.invoke(currentJSContent, args);

				if (ret instanceof JSFunction) // gestion appel fct anonyme par fct() ou fragment()
				{
					((JSFunction) ret).proxy = (JSMethodInterface) proxy;
				}

				if (debug2) {
					// trace du retour
					if (ret instanceof JSContent) {
						println(System.identityHashCode(currentJSContent)
								+ " - appel de la mth " + id + OF_CLASS + implcl.getName() + " => "
								+ ((JSContent) ret).getListElem());
					} else
						println(System.identityHashCode(currentJSContent)
								+ " - [no JSContent] appel de la mth " + id + OF_CLASS + implcl.getName() + " => "
								+ ret);

					println(System.identityHashCode(currentJSContent)
							+ " - value = " + currentJSContent.getListElem());
				}

				return ret;
			}
		}

	}

	/**
	 * @param method
	 * @param ret
	 * @throws ClassNotFoundException
	 */
	private void initTypeOfJSArray(Method method, Object ret) throws ClassNotFoundException {
		// affecte le type de l'array
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) returnType;
			Type[] argTypes = paramType.getActualTypeArguments();
			if (argTypes.length > 0) {
				((JSArray<?>) ret)._type = Class.forName(argTypes[0].getTypeName());
			}
		}
	}

	public static void println(String t)
	{
		System.out.println("[MethodInvocationHandler]" + t);
	}
	
	/**
	 * @param jc
	 * @param args
	 * @return
	 */
	public static Object cast(Class<?> cl, Object args) {
		Object jc = declareType(cl, null);
		if (jc instanceof JSVariable)
			((JSVariable) jc)._setContent(args);
		else
			((JSClass) jc)._setContent(args);
		return jc;
	}

	/**
	 * @param implcl
	 * @return
	 * @throws ClassNotFoundException
	 */
	private MethodDesc getMethodDesc(JSClassImpl implcl, Object proxy, String mthName) {

		MethodDesc currentMethodDesc = null;

		currentMethodDesc = mapContentMthBuildByProxy.get(mthName);

		if (currentMethodDesc == null) {
			JSContent jsc = XHTMLPart.jsBuilder.createJSContent();
			if (debug2)
				println(System.identityHashCode(jsc) + " - createJSContent "
						+ mthName + OF_CLASS + implcl.getName());

			currentMethodDesc = new MethodDesc(jsc);
			currentMethodDesc.proxy = proxy;
			mapContentMthBuildByProxy.put(mthName, currentMethodDesc); // creer le contenu
		}

		return currentMethodDesc;
	}

	/**
	 * @param method
	 * @throws ClassNotFoundException
	 */
	private void registerCallMethodJS(Object method, MethodDesc currentMethodDesc) throws ClassNotFoundException {
		if (currentMethodDesc==null) return;
		
		doLastSourceLineInsered(currentMethodDesc, false);

		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		int numLigne = -1;
		for (StackTraceElement stackTraceElement : stack) {
			if (stackTraceElement.getLineNumber() != -1
					&& JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))) {
				numLigne = stackTraceElement.getLineNumber();
				currentMethodDesc.lastLineNoInsered = numLigne;
				currentMethodDesc.lastMthNoInserted = method;
				break;
			}
		}
	}

	/**
	 * @throws ClassNotFoundException
	 */
	public static void doLastSourceLineInsered(MethodDesc currentMethodDesc, boolean isInFct)
			throws ClassNotFoundException {

		if (currentMethodDesc != null && currentMethodDesc.lastLineNoInsered > 0) {

			StackTraceElement[] stack = Thread.currentThread().getStackTrace();

			String className = null;
			int numLigne = -1;
			for (StackTraceElement stackTraceElement : stack) {
				if (stackTraceElement.getLineNumber() != -1
						&& JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))) {
					className = stackTraceElement.getClassName();
					numLigne = stackTraceElement.getLineNumber();
					break;
				}
			}

			if (isInFct && numLigne <= currentMethodDesc.lastLineNoInsered) {
				numLigne = currentMethodDesc.lastLineNoInsered + 1; // permet d'inserer la derniere ligne d'une fct
																	// anonym
			}

			if (numLigne <= currentMethodDesc.lastLineNoInsered) {
				currentMethodDesc.lastMthNoInserted = null; // plus
				ThreadLocalMethodDesc.get().lastMthNoInserted = null;
			} else {

				if (currentMethodDesc.lastMthNoInserted != null) {
					// INSERE LA LIGNE
					currentMethodDesc.content.__(currentMethodDesc.lastMthNoInserted);
					if (debug3)
						println("--------- add source line " + className + ":"+ currentMethodDesc.lastLineNoInsered + " > " + currentMethodDesc.lastMthNoInserted);

					currentMethodDesc.lastMthNoInserted = null;
				}
			}

			currentMethodDesc.lastLineNoInsered = -1;
			ThreadLocalMethodDesc.get().lastLineNoInsered = -1;
		}
	}

	/**
	 * @return
	 */
	private Object getProxyContent() {
		if (varContent != null)
			return varContent.toString();

		if (getVarName() == null)
			return "???";
		return getVarName().toString();
	}

	/**
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 */
	private boolean checkMethodExist(Method method) {
		try {
			return JSContent.class.getMethod(method.getName(), method.getParameterTypes()) != null;
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
	}

	private JSFunction createJSFunctionImpl(MethodDesc handle, boolean testAnonymous) throws Throwable {

		final Class<?> declaringClass = handle.method.getDeclaringClass();
		Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class,	int.class);
		constructor.setAccessible(true);

		Parameter[] param = handle.method.getParameters();
		Object[] p = getTypedParam(handle, param);

		Object prevCode = null;
		JSContent codeAnonym = null;

		if (testAnonymous) {
			testAnonymInProgress = true;
			codeAnonym = mapContentMthBuildByProxy.get(currentFctBuildByProxy).content;
			prevCode = codeAnonym.$$subContent();
		}

		// appel la fct default de la proxy classJS ==> entrainte les appel invoke de cette classe
		Object retProxyMth = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
				.unreflectSpecial(handle.method, declaringClass).bindTo(handle.proxy).invokeWithArguments(p);

		doLastSourceLineInsered(ThreadLocalMethodDesc.get(), true);

		JSFunction fct = null;

		if (testAnonymous && retProxyMth instanceof Anonym) {
			testAnonymInProgress = false;
			((Runnable) retProxyMth).run();   // execute la function qui retourne l'anonym

			Object aCode = codeAnonym.$$gosubContent(prevCode);
			prevCode = null;
			JSContent cont = XHTMLPart.jsBuilder.createJSContent();
			cont.$$gosubContent(aCode);
			fct = XHTMLPart.jsBuilder.createJSFunction().setParam(p).setCode(cont);

		} else if (!testAnonymous) {
			String id = JSClassImpl.getMethodId(handle.method, handle.args);

			MethodDesc methDesc = mapContentMthBuildByProxy.get(id);

			JSContent code = addReturnInCode(retProxyMth, methDesc);

			if (debug2)
				println(System.identityHashCode(code) + " - return JSFunction " + id);

			fct = XHTMLPart.jsBuilder.createJSFunction()
					.setName(handle.method.getName())
					.setParam(p)
					.setCode(code);

			// function en cours terminé
			currentFctBuildByProxy = null;

			// invoke les methodes interne private a la fin de la creation de la methode
			invokeInternalClassMethod(handle);
		}

		boolean putLastCode = testAnonymous && prevCode != null;
		if (putLastCode) {
			codeAnonym.$$gosubContent(prevCode);
			testAnonymInProgress = false;
		}

		return fct;
	}

	/**
	 * @param ret
	 * @param methDesc
	 * @return
	 */
	private JSContent addReturnInCode(Object ret, MethodDesc methDesc) {
		JSContent code;
		code = methDesc == null ? null : methDesc.content;

		// ajouter en fin de methode JS
		if (ret != null && !(ret instanceof JSContent)) {
			if (code == null)
				code = XHTMLPart.jsBuilder.createJSContent();

			code._return(ret);
		}
		return code;
	}

	/**
	 * @param handle
	 * @param param
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Object[] getTypedParam(MethodDesc handle, Parameter[] param) throws InstantiationException, IllegalAccessException {
		Object[] p = new Object[param.length];

		if (handle.args != null) {
			for (int i = 0; i < handle.args.length; i++) {
				// gestion du typage des parametres
				p[i] = getObjectJS(param[i].getType(), "", param[i].getName());

			}
		}
		return p;
	}

	/**
	 * @param handle
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void invokeInternalClassMethod(MethodDesc handle) throws IllegalAccessException, InvocationTargetException {
		// invoke les methodes interne private a la fin de la creation de la methode
		for (Iterator<MethodDesc> i = handle.implcl.getListHandleFuntionPrivate().iterator(); i.hasNext();) {
			MethodDesc nextHandle = i.next();
			i.remove();
			nextHandle.method.invoke(nextHandle.proxy, nextHandle.args);
		}
	}

	/**
	 * @param param
	 * @param p
	 * @param i
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getObjectJS(Class<?> type, String prefix, Object name) throws InstantiationException, IllegalAccessException {

		boolean retJSVariable = JSVariable.class.isAssignableFrom(type);
		boolean retJSClass = JSClass.class.isAssignableFrom(type);
		if (retJSVariable) {
			JSVariable retJSVar = (JSVariable) type.newInstance();
			retJSVar._setName(prefix + name);
			return retJSVar;
		} else if (retJSClass) {

			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) type);
			XHTMLPart.jsBuilder.setNameOfProxy(prefix, prox, name);
			return prox;
		} else
			return prefix + name;
	}

	/**
	 * @return the name
	 */
	public Object getVarName() {
		return varname;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param implementClass
	 *            the implementClass to set
	 */
	public void setImplementClass(Class<? extends JSClass> implementClass) {
		this.implementClass = implementClass;
	}
}