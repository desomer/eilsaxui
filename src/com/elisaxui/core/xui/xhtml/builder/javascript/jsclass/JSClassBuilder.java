package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xml.annotation.xForceInclude;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * class de representation d'une class JS
 * @author Bureau
 *
 */
public final class JSClassBuilder extends JSContent {
	
	Object name;   // nom de la class
	
	LinkedList<JSFunction> listFunction = new LinkedList<>();
	private Map<String, String> listDistinctFct = new HashMap<>();
	
	private LinkedList<ProxyMethodDesc> listHandleFuntionPrivate = new LinkedList<>();
	public Map<String, ProxyMethodDesc> mapContentMthBuildByProxy = new HashMap<>(); // contenu des methodes crée par proxy , ThreadLocal?
	
	
	Object autoCallMeth;
	
	/**
	 * @return the autoCallMeth
	 */
	public final Object getAutoCallMeth() {
		return autoCallMeth;
	}


	/**
	 * @param autoCallMeth the autoCallMeth to set
	 */
	public final void setAutoCallMeth(Object autoCallMeth) {
		this.autoCallMeth = autoCallMeth;
	}

	
	public static Object initJSConstructor(Class<? extends JSClass> cl) {
		Object autoCall = null;
		
		// init constructor
		JSClass inst = ProxyHandler.getProxy(cl);
		Method[] lism = cl.getDeclaredMethods();
		
		xForceInclude annInclude = cl.getAnnotation(xForceInclude.class);
		boolean includeAllMth = annInclude != null;
		
		if (lism != null) {
			for (Method method : lism) {
				boolean isPublic = method.isDefault();
				xForceInclude annIncludeMth = method.getAnnotation(xForceInclude.class);
				xStatic annStaticMth = method.getAnnotation(xStatic.class);
				
				boolean isForceIncluded = annIncludeMth != null;
				boolean isStatic = annStaticMth != null;
				boolean includeMth = isPublic && (isForceIncluded || includeAllMth || isStatic);
				
				if (isStatic && annStaticMth.autoCall())
					autoCall = method.getName()+"()";
				
				if (includeMth || method.getName().equals("constructor")) {
					try {
						method.setAccessible(true);
						method.invoke(inst, new Object[method.getParameterCount()]);
						ProxyHandler.doLastSourceLineInsered(false);
					} catch (Throwable e) {
						ErrorNotificafionMgr.doError("pb constructor sur " + cl.getSimpleName(), e);
					}

				}
			}
		}
		
		return autoCall;
	}
	
	
	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}
	
	public JSClassBuilder addFunction(JSFunction fct) {
		listFunction.add(fct);
		return this;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		
		XMLBuilder oldBuf = null;
		StringBuilder txtJS = null;
		if (JSExecutorHelper.isWithPreprocessor())
		{
			txtJS = new StringBuilder(1000);
			StringBuilder txtJSAfter = new StringBuilder();
			
			XMLBuilder bufJS =  new XMLBuilder("js", txtJS, txtJSAfter);
			oldBuf = buf;
			buf = bufJS;
		}
		
		ProxyHandler.getFormatManager().newLine(buf);
		ProxyHandler.getFormatManager().newTabInternal(buf);
		buf.addContentOnTarget("class ");
		buf.addContentOnTarget(name);
		buf.addContentOnTarget(" {");
	
		ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine()+1);
		ProxyHandler.getFormatManager().newLine(buf);
		int i=0;
		
		listFunction.sort((JSFunction o1, JSFunction o2)-> o1.getName().toString().compareTo(o2.getName().toString()));
		
		for (JSFunction jsFunction : listFunction) {
			i++;
			jsFunction.toXML(buf);
			if (i<listFunction.size())
				ProxyHandler.getFormatManager().newLine(buf);
		}
		ProxyHandler.getFormatManager().setTabForNewLine(ProxyHandler.getFormatManager().getTabForNewLine()-1);
		ProxyHandler.getFormatManager().newLine(buf);
		ProxyHandler.getFormatManager().newTabInternal(buf);
		buf.addContentOnTarget("}");
		
		if (txtJS!=null)
		{
			try {
				String str = JSExecutorHelper.doBabel(txtJS.toString());
				
				oldBuf.addContentOnTarget(str);
			} catch (ScriptException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	
//	/**
//	 * retourne le call
//	 * 
//	 * @param method
//	 * @param args
//	 * @return
//	 */
//	@Deprecated
//	public static final  Object toJSCallInner(Object name , Method method, Object[] args) {
//				
//		return toJSCall("this", method, args);
//		
//	}
	

	
	/**
	 * retourne le call
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public static final Object toJSCall(Object name , Method method, Object[] args) {
		
		List<Object> buf = new ArrayMethod<>();
		buf.add(name + "." + method.getName() + "(");

		int i = 0;
		if (args != null) {
			for (Object p : args) {
				if (i > 0)
					buf.add(", ");
				buf.add(p);  // peut etre des string, fct, etc...
				i++;
			}
		}
		buf.add(")");
		
		if (JSClass.class.isAssignableFrom(method.getReturnType() ))
		{
			// chainage d'attribut
			JSClass prox = ProxyHandler.getProxy((Class<? extends JSClass>) method.getReturnType());
			prox._setContent(buf);
			return prox;
		}
		else if (JSAny.class.isAssignableFrom(method.getReturnType() ))
		{
			JSAny ret=null;
			try {
				ret = ((JSAny)method.getReturnType().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			ret._setValue(buf);
			return ret;
		}
		else
		{
			// appel de methode
			return buf;
		}

			
	}
	
	/**
	 * id  = nom method + nb argument
	 * @param method
	 * @param args
	 * @return
	 */
	public static final  String  getMethodId(Method method, Object[] args) {
		StringBuilder buf = new StringBuilder();
		buf.append(method.getName());
		buf.append(".");
		buf.append(args == null ? 0 : args.length);
		return buf.toString();
	}

	/**
	 * @return the listDistinctFct
	 */
	public Map<String, String> getListDistinctFct() {
		return listDistinctFct;
	}

	/**
	 * @return the listHandleFuntionPrivate
	 */
	public LinkedList<ProxyMethodDesc> getListHandleFuntionPrivate() {
		return listHandleFuntionPrivate;
	}

}
