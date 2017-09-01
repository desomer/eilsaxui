package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * class de representation d'une class JS
 * @author Bureau
 *
 */
public class JSClassImpl extends JSContent {
	
	Object name;

	LinkedList<JSFunction> listFuntion = new LinkedList<>();
	private Map<String, String> listDistinctFct = new HashMap<String, String>();
	
	private LinkedList<MethodInvocationHandle> listHandleFuntionPrivate = new LinkedList<>();
	
	public JSClassImpl(JSBuilder jsBuilder) {
		super(jsBuilder);
	}
	

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}
	
	public JSClassImpl addFunction(JSFunction fct) {
		listFuntion.add(fct);
		return this;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		jsBuilder.newLine(buf);
		jsBuilder.newTabulation(buf);
		buf.addContent("class ");
		buf.addContent(name);
		buf.addContent(" {");
	
		jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()+1);
		jsBuilder.newLine(buf);
		int i=0;
		for (JSFunction jsFunction : listFuntion) {
			i++;
			jsFunction.toXML(buf);
			if (i<listFuntion.size())
				jsBuilder.newLine(buf);
		}
		jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()-1);
		jsBuilder.newLine(buf);
		jsBuilder.newTabulation(buf);
		buf.addContent("}");
		return null;
	}

	
	/**
	 * retourne le call
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public static final  Object toJSCallInner(Object name , Method method, Object[] args) {
		
		if (JSClass.class.isAssignableFrom(method.getReturnType() ))
		{
			// chainage d'attribut
			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) method.getReturnType());
			XHTMLPart.jsBuilder.setNameOfProxy("", prox, name + "." + method.getName());
			return prox;
		}
		else
		{
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
	
	/**
	 * retourne le call
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public static final Object toJSCall(Object name , Method method, Object[] args) {
		
//		if (method.getName().equals("activityMgr"))
//			System.out.println("activityMgr activityMgr ok");
		
		if (JSClass.class.isAssignableFrom(method.getReturnType() ))
		{
			// chainage d'attribut
			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) method.getReturnType());
			XHTMLPart.jsBuilder.setNameOfProxy("", prox, name + "." + method.getName());
			return prox;
		}
		else
		{
			// appel de methode
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
	public LinkedList<MethodInvocationHandle> getListHandleFuntionPrivate() {
		return listHandleFuntionPrivate;
	}

}
