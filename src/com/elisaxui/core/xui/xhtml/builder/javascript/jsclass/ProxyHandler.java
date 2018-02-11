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

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSAnonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.json.JSONBuilder;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.builder.XUIFormatManager;

public final class ProxyHandler implements InvocationHandler {

	private static final String DU_JS_CONTENT = "  du JSContent ";
	private static final String OF_CLASS = " of class ";
	private static final boolean TEST_ANONYM = true;
	private static final Object NOT_USED = "NOT_USED";
	private static final JSContent NOT_USED_CONTENT = new JSContent();

	static boolean debug = false;
	static boolean debug2 = false;
	static boolean debug3 = false;

	private Object varname; // nom de la variable du proxy
	private Object varContent; // contenu code du proxy
	private Class<? extends JSClass> implementClass; // type js de la class

	private JsonObjectBuilder jsonBuilder = null;

	/**
	 * @return the jsonBuilder
	 */
	public final JsonObjectBuilder getJsonBuilder() {
		return jsonBuilder;
	}

	/**
	 * @param jsonBuilder
	 *            the jsonBuilder to set
	 */
	public final void setJsonBuilder(JsonObjectBuilder jsonBuilder) {
		this.jsonBuilder = jsonBuilder;
	}

	private String currentFctBuildByProxy = null; //
	private boolean testAnonymInProgress = false; // test si la methode doit retourner une fonction anonym

	// methode en attente d'ajout dans le code
	public static final ThreadLocal<ProxyMethodDesc> ThreadLocalMethodDesc = new ThreadLocal<>(); // regrouper le 2
																									// ThreadLocal
	public static final ThreadLocal<Boolean> ThreadLocalMode = new ThreadLocal<>();
	public static final ThreadLocal<XUIFormatManager> ThreadLocalXUIFormatManager = new ThreadLocal<>();

	public static XUIFormatManager getFormatManager() {
		XUIFormatManager jsb = ThreadLocalXUIFormatManager.get();
		if (jsb == null) {
			jsb = new XUIFormatManager();
			ThreadLocalXUIFormatManager.set(jsb);
		}
		return jsb;
	}

	private Map<String, ProxyMethodDesc> mapContentMthBuildByProxy = new HashMap<>(); // contenu des methodes crée par
																						// proxy , ThreadLocal?

	public ProxyHandler(Class<? extends JSClass> impl) {
		this.setImplementClass(impl);
	}

	public static final boolean isModeJava() {
		Boolean b = ThreadLocalMode.get();
		return b == null ? false : b;
	}

	/**
	 * interception des appel de methode de interface - creer une JSClassImpl si
	 * n'existe pas - creer les fct javascript
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		
		ProxyMethodDesc mthInvoke = new ProxyMethodDesc(null, null, proxy, method, args);
		
		Object ret = invokeDirectMethod(mthInvoke);
		
		if (ret != null)
			return ret == NOT_USED ? null : ret;

		String idMeth = JSClassBuilder.getMethodId(method, args);
		JSClassBuilder implcl = XUIFactoryXHtml.getXHTMLFile().getClassImpl(getImplementClass());

		mthInvoke.idMeth = idMeth;
		mthInvoke.implcl = implcl;

		boolean isMthAlreadyInClass = implcl.getListDistinctFct().containsKey(idMeth);

		if (isMthAlreadyInClass) {
			/********************************************************************/
			// creer le js du call de la fct
			/********************************************************************/
			ret = JSClassBuilder.toJSCall(getStringProxyContent(), method, args); // utilise le nom du proxy
			if (!testAnonymInProgress) {
				registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
			}
		} else {
			/********************************************************************/
			/* CREATION DU CODE DANS LA FCT */
			/********************************************************************/
			if (method.isDefault()) {
				ret = doCallImplement(mthInvoke);

			} else if (!checkMethodExist(method)) {
				/******************************************************************************/
				/***** INSERT le nom de la methode abstract de type Interface de variable ****/
				/******************************************************************************/
				ret = doCallAttribut(method);

			} else {
				/******************************************************************************/
				/***** APPEL DES FUNCTION INTERNE (var, set, if) sur la class JSContent *****/
				/******************************************************************************/
				ret = doCallInternalImplement(mthInvoke);

			}
		}

		return ret;
	}

	private Object doCallImplement(ProxyMethodDesc mthInvoke) throws Throwable {
		Object ret = null;
		if (testAnonymInProgress) {
			ret = JSClassBuilder.toJSCall("/*ww4*/this", mthInvoke.method, mthInvoke.args); // ne creer pas la fct en
			// testAnonymInProgress
		} else {

			if (currentFctBuildByProxy == null) { // test si meth en cours de build
				/*******************************************/
				/***** APPEL DES FUNCTION DE LA CLASSE *****/
				/*******************************************/
				ret = doCallMethod(mthInvoke);

			} else {
				/*****************************************************
				 * APPEL A UNE AUTRE FCT INTERNE A LA FCT EN COURS DU PROXY => AJOUTE DANS
				 * getListHandleFuntionPrivate
				 *****************************************************/
				ret = doCallInternalMethod(mthInvoke);
			}
		}
		return ret;
	}

	private Object doCallMethod(ProxyMethodDesc mthInvoke) throws Throwable {
		mthInvoke.implcl.getListDistinctFct().put(mthInvoke.idMeth, mthInvoke.idMeth);
		currentFctBuildByProxy = mthInvoke.idMeth;

		traceDebug("include mth", mthInvoke.idMeth, mthInvoke.implcl, NOT_USED_CONTENT, NOT_USED);

		Object nameProxy = this.varname; // recupere le nom de la variable du proxy
		this.varname = "/*ww2*/this"; // force a this pour appel interne d'autre fct de la classe JS

		// creer le JSContent
		ProxyMethodDesc currentMethodDesc = getMethodDesc(mthInvoke.implcl, mthInvoke.proxy, currentFctBuildByProxy);
		ProxyMethodDesc lastMethodDesc = ThreadLocalMethodDesc.get();
		ThreadLocalMethodDesc.set(currentMethodDesc);

		traceDebug("begin create mth", mthInvoke.idMeth, mthInvoke.implcl, currentMethodDesc.content, NOT_USED);

		// creer le code en appelant la fct
		JSFunction fct = createJSFunctionImpl(mthInvoke, !TEST_ANONYM);
		mthInvoke.implcl.addFunction(fct);
		ThreadLocalMethodDesc.set(lastMethodDesc);

		traceDebug("end create mth", mthInvoke.idMeth, mthInvoke.implcl, currentMethodDesc.content, NOT_USED);

		this.varname = nameProxy;

		// creer le js du call de la fct
		Object ret = JSClassBuilder.toJSCall(getStringProxyContent(), mthInvoke.method, mthInvoke.args);
		registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
		return ret;
	}

	private Object doCallInternalMethod(ProxyMethodDesc mthInvoke) throws Throwable {
		Object ret = null;

		boolean isJSClass = JSClass.class.isAssignableFrom(mthInvoke.method.getDeclaringClass());

		if (!isJSClass) {
			// appel la fct default de la proxy JSONBuilder ou XHTMLElement

			final Class<?> declaringClass = mthInvoke.method.getDeclaringClass();
			Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(
					Class.class,
					int.class);
			constructor.setAccessible(true);

			ret = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					.unreflectSpecial(mthInvoke.method, declaringClass).bindTo(mthInvoke.proxy)
					.invokeWithArguments(mthInvoke.args);

		} else {
			JSFunction anon = isAnonymous(mthInvoke);
			if (anon != null)
				ret = anon;
			else {
				/*******************************************************************************/
				traceDebug("call other mth same class", mthInvoke.idMeth, mthInvoke.implcl, NOT_USED_CONTENT, NOT_USED);

				// ajoute une methode de la meme class à creer par la suite
				mthInvoke.implcl.getListHandleFuntionPrivate().add(mthInvoke);

				// creer le js du call de la fct
				ret = JSClassBuilder.toJSCall("/*ww1*/this", mthInvoke.method, mthInvoke.args);

				// registerMethod
				registerCallMethodJS(ret, ThreadLocalMethodDesc.get());
			}
		}
		
		return ret;
	}

	/**
	 * @param mthInvoke
	 * @throws Throwable
	 */
	private JSFunction isAnonymous(ProxyMethodDesc mthInvoke) throws Throwable {
		/**************************
		 * test si utilisation class Anonym
		 **********************************/
		Object nameProxy = this.varname;
		this.varname = "/*ww3*/this"; // force a this pour appel interne d'autre fct de la classe JS

		/*******************************************************************************/
		// test si utilisation class Anonym
		/*******************************************************************************/
		JSFunction fctAnomyn = createJSFunctionImpl(mthInvoke, TEST_ANONYM);
		this.varname = nameProxy;

		if (fctAnomyn != null) {
			return fctAnomyn; // retourne anonym
		}

		return null;
	}

	/**
	 * @param method
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private Object doCallAttribut(Method method)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Object objName = currentFctBuildByProxy == null ? getStringProxyContent() : "/*ww4*/this";
		Object ret = getObjectJS(method.getReturnType(), objName + ".", method.getName());

		if (ret instanceof JSVariable) {
			if (ret instanceof JSArray) {
				initTypeOfJSArray(method, ret);
			}

			((JSVariable) ret)._setParent(this);
		}
		return ret;
	}

	private Object doCallInternalImplement(ProxyMethodDesc mthInvoke)
			throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {

		ProxyMethodDesc currentMethodDesc = ThreadLocalMethodDesc.get();
		JSContent currentJSContent = currentMethodDesc.content;

		if (!testAnonymInProgress)
			doLastSourceLineInsered(false);

		traceDebug("begin call jscontent", mthInvoke.idMeth, mthInvoke.implcl, currentJSContent, NOT_USED);

		// appel l'implementation le methode JSInterface (JSContent)
		Object ret = mthInvoke.method.invoke(currentJSContent, mthInvoke.args);

		if (ret instanceof JSFunction) // gestion appel fct anonyme par fct() ou fragment()
		{
			((JSFunction) ret).setProxy((JSContentInterface) mthInvoke.proxy);
		}

		traceDebug("end call jscontent", mthInvoke.idMeth, mthInvoke.implcl, currentJSContent, ret);
		return ret;
	}

	/**
	 * @param proxy
	 * @param method
	 * @param args
	 */
	private Object invokeDirectMethod(ProxyMethodDesc mthInvoke) {
		if (mthInvoke.method.getName().equals("toString")) {
			return getStringProxyContent();
		}

		if (mthInvoke.method.getName().equals("_getContent")) {
			if (jsonBuilder != null)
				return jsonBuilder.build().toString();

			if (varContent == null)
				return NOT_USED;
			return varContent;
		}

		if (mthInvoke.method.getName().equals("_setContent")) {
			varContent = mthInvoke.args[0];
			return mthInvoke.proxy;
		}

		if (mthInvoke.method.getName().equals("set")) {
			Object[] param = (Object[]) mthInvoke.args[0];
			return ThreadLocalMethodDesc.get().content._set(mthInvoke.proxy, param);
		}

		if (mthInvoke.method.getName().equals("cast")) {
			return cast((Class<?>) mthInvoke.args[0], mthInvoke.args[1]);
		}

		if (mthInvoke.method.getName().equals("asLitteral")) {
			this.jsonBuilder = Json.createObjectBuilder();
			return mthInvoke.proxy;
		}

		if (mthInvoke.method.getName().equals("callJava")) {
			ThreadLocalMode.set(Boolean.TRUE);
			((JSAnonym) mthInvoke.args[0]).run();
			ThreadLocalMode.set(Boolean.FALSE);
			return mthInvoke.proxy;
		}

		return null;
	}

	/**
	 * @param id
	 * @param implcl
	 * @param currentJSContent
	 * @param ret
	 */
	private void traceDebug(String mode, String idmth, JSClassBuilder implcl, JSContent currentJSContent, Object ret) {

		switch (mode) {

		case "begin create mth":
			if (debug3)
				println(System.identityHashCode(currentJSContent)
						+ " -add ThreadLocalMethodDesc meth " + idmth + DU_JS_CONTENT
						+ currentFctBuildByProxy + OF_CLASS + implcl.getName());
			break;
		case "end create mth":
			if (debug3)
				println(System.identityHashCode(currentJSContent)
						+ " -restore ThreadLocalMethodDesc meth " + idmth + DU_JS_CONTENT
						+ currentFctBuildByProxy + OF_CLASS + implcl.getName());
			break;

		case "include mth":
			if (debug)
				println("  include mth " + idmth + OF_CLASS + implcl.getName());

			break;

		case "call other mth same class":
			if (debug2)
				println("** mth " + idmth + OF_CLASS + implcl.getName() + " prev = " + currentFctBuildByProxy);
			break;

		case "begin call jscontent":
			if (debug2)
				println(System.identityHashCode(currentJSContent)
						+ " - appel meth " + idmth + DU_JS_CONTENT + currentFctBuildByProxy
						+ OF_CLASS + implcl.getName());
			break;

		case "end call jscontent":
			if (debug2) {
				// trace du retour
				if (ret instanceof JSContent) {
					println(System.identityHashCode(currentJSContent)
							+ " - appel de la mth " + idmth + OF_CLASS + implcl.getName() + " => "
							+ ((JSContent) ret).getListElem());
				} else
					println(System.identityHashCode(currentJSContent)
							+ " - [no JSContent] appel de la mth " + idmth + OF_CLASS + implcl.getName() + " => "
							+ ret);

				println(System.identityHashCode(currentJSContent)
						+ " - value = " + currentJSContent.getListElem());
			}
			break;

		default:
			break;
		}
	}

	/**
	 * @param method
	 * @param ret
	 * @throws ClassNotFoundException
	 */
	private static final void initTypeOfJSArray(Method method, Object ret) throws ClassNotFoundException {
		// affecte le type de l'array
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) returnType;
			Type[] argTypes = paramType.getActualTypeArguments();
			if (argTypes.length > 0 ) {
				String cl = argTypes[0].getTypeName();
				if (!cl.equals("?"))
					((JSArray<?>) ret)._type = Class.forName(cl);
			}
		}
	}

	public static final void println(String t) {
		CoreLogger.getLogger(1).fine(t);
	}

	/**
	 * @param jc
	 * @param args
	 * @return
	 */
	public static Object cast(Class<?> cl, Object args) {
		Object jc = declareType(cl, null);
		if (jc instanceof JSVariable)
			((JSVariable) jc)._setValue(args)._setName(args);
		else
			((JSClass) jc)._setContent(args);
		return jc;
	}

	/**
	 * @param implcl
	 * @return
	 * @throws ClassNotFoundException
	 */
	private ProxyMethodDesc getMethodDesc(JSClassBuilder implcl, Object proxy, String mthName) {

		ProxyMethodDesc currentMethodDesc = null;

		currentMethodDesc = mapContentMthBuildByProxy.get(mthName);

		if (currentMethodDesc == null) {
			JSContent jsc = new JSContent();
			if (debug2)
				println(System.identityHashCode(jsc) + " - createJSContent "
						+ mthName + OF_CLASS + implcl.getName());

			currentMethodDesc = new ProxyMethodDesc(jsc);
			currentMethodDesc.proxy = proxy;
			mapContentMthBuildByProxy.put(mthName, currentMethodDesc); // creer le contenu
		}

		return currentMethodDesc;
	}

	/**
	 * @param method
	 * @throws ClassNotFoundException
	 */
	private static final void registerCallMethodJS(Object method, ProxyMethodDesc currentMethodDesc)
			throws ClassNotFoundException {

		if (currentMethodDesc == null)
			return;

		doLastSourceLineInsered(false);

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
	public static final void doLastSourceLineInsered(boolean isInFct)
			throws ClassNotFoundException {

		ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();

		if (currentMethodDesc != null && currentMethodDesc.lastLineNoInsered > 0) {

			ProxyMethodDesc methodDesc = getMethodDescFromStacktrace();

			if (isInFct && methodDesc.lastLineNoInsered <= currentMethodDesc.lastLineNoInsered) {
				methodDesc.lastLineNoInsered = currentMethodDesc.lastLineNoInsered + 1;
				// permet d'inserer la derniere ligne d'une fct anonym
			}

			if (methodDesc.lastLineNoInsered <= currentMethodDesc.lastLineNoInsered) {
				currentMethodDesc.lastMthNoInserted = null; // plus
				ThreadLocalMethodDesc.get().lastMthNoInserted = null;
			} else {

				if (currentMethodDesc.lastMthNoInserted != null) {
					// INSERE LA LIGNE
					currentMethodDesc.content.__(currentMethodDesc.lastMthNoInserted);
					if (debug3)
						println("--------- add source line " + methodDesc.lastMthNoInserted + ":"
								+ currentMethodDesc.lastLineNoInsered + " > " + currentMethodDesc.lastMthNoInserted);

					currentMethodDesc.lastMthNoInserted = null;
				}
			}

			currentMethodDesc.lastLineNoInsered = -1;
			ThreadLocalMethodDesc.get().lastLineNoInsered = -1;
		}
	}

	/**
	 * @param methodDesc
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static final ProxyMethodDesc getMethodDescFromStacktrace() throws ClassNotFoundException {
		ProxyMethodDesc methodDesc = new ProxyMethodDesc(null);

		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTraceElement : stack) {
			if (stackTraceElement.getLineNumber() != -1
					&& JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))) {
				methodDesc.lastMthNoInserted = stackTraceElement.getClassName();
				methodDesc.lastLineNoInsered = stackTraceElement.getLineNumber();
				break;
			}
		}
		return methodDesc;
	}

	/**
	 * @return
	 */
	private String getStringProxyContent() {

		if (varContent != null)
			return varContent.toString();

		if (getVarName() == null) {
			return "???";
		}
		return getVarName().toString();
	}

	/**
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 */
	private static final boolean checkMethodExist(Method method) {
		try {
			return JSContent.class.getMethod(method.getName(), method.getParameterTypes()) != null;
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
	}

	private JSFunction createJSFunctionImpl(ProxyMethodDesc handle, boolean testAnonymous) throws Throwable {

		final Class<?> declaringClass = handle.method.getDeclaringClass();
		Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class,
				int.class);
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

		// appel la fct default de la proxy classJS ==> entrainte les appel invoke de
		// cette classe
		Object retProxyMth = constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
				.unreflectSpecial(handle.method, declaringClass).bindTo(handle.proxy).invokeWithArguments(p);

		doLastSourceLineInsered(true);

		JSFunction fct = null;

		if (testAnonymous && retProxyMth instanceof JSAnonym) {
			testAnonymInProgress = false;
			((Runnable) retProxyMth).run(); // execute la function qui retourne l'anonym

			Object aCode = codeAnonym.$$gosubContent(prevCode);
			prevCode = null;
			JSContent cont = new JSContent();
			cont.$$gosubContent(aCode);
			fct = new JSFunction().setParam(p).setCode(cont);

		} else if (!testAnonymous) {
			String id = JSClassBuilder.getMethodId(handle.method, handle.args);

			ProxyMethodDesc methDesc = mapContentMthBuildByProxy.get(id);

			JSContent code = addReturnInCode(retProxyMth, methDesc);

			if (debug2)
				println(System.identityHashCode(code) + " - return JSFunction " + id);

			xStatic annStaticMth = handle.method.getAnnotation(xStatic.class);

			fct = new JSFunction()
					.setStatic(annStaticMth != null)
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
	private static final JSContent addReturnInCode(Object ret, ProxyMethodDesc methDesc) {
		JSContent code;
		code = methDesc == null ? null : methDesc.content;

		// ajouter en fin de methode JS
		if (ret != null && !(ret instanceof JSContent)) {
			if (code == null)
				code = new JSContent();

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
	private static final Object[] getTypedParam(ProxyMethodDesc handle, Parameter[] param)
			throws InstantiationException, IllegalAccessException {
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
	private static final void invokeInternalClassMethod(ProxyMethodDesc handle)
			throws IllegalAccessException, InvocationTargetException {
		// invoke les methodes interne private a la fin de la creation de la methode
		for (Iterator<ProxyMethodDesc> i = handle.implcl.getListHandleFuntionPrivate().iterator(); i.hasNext();) {
			ProxyMethodDesc nextHandle = i.next();
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
	private static final Object getObjectJS(Class<?> type, String prefix, Object name)
			throws InstantiationException, IllegalAccessException {

		boolean retJSVariable = JSVariable.class.isAssignableFrom(type);
		boolean retJSClass = JSClass.class.isAssignableFrom(type);
		if (retJSVariable) {
			JSVariable retJSVar = (JSVariable) type.newInstance();
			retJSVar._setName(prefix + name);
			return retJSVar;
		} else if (retJSClass) {

			@SuppressWarnings("unchecked")
			JSClass prox = getProxy((Class<? extends JSClass>) type);
			setNameOfProxy(prefix, prox, name);
			return prox;
		} else
			return prefix + name;
	}

	/**
	 * @return the name
	 */
	private final Object getVarName() {
		return varname;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setVarName(Object name) {
		this.varname = name;
	}

	/**
	 * @return the implementClass
	 */
	public final Class<? extends JSClass> getImplementClass() {
		return implementClass;
	}

	/**
	 * @param implementClass
	 *            the implementClass to set
	 */
	public final void setImplementClass(Class<? extends JSClass> implementClass) {
		this.implementClass = implementClass;
	}

	public static final void setNameOfProxy(String prefix, Object inst, Object name) {
		ProxyHandler mh = (ProxyHandler) Proxy.getInvocationHandler(inst);
		mh.setVarName(prefix == null ? name : (prefix + name));
	}

	@SuppressWarnings("unchecked")
	public static final <E extends JSClass> E getProxy(final Class<? extends JSClass> cl) {

		Object proxy = Proxy.newProxyInstance(ProxyHandler.class.getClassLoader(), new Class[] { cl },
				new ProxyHandler(cl));
		return (E) proxy;
	}
}