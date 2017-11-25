package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * class de representation d'une class JS
 * @author Bureau
 *
 */
public class JSClassImpl extends JSContent {
	
	Object name;   // nom de la class

	LinkedList<JSFunction> listFuntion = new LinkedList<>();
	private Map<String, String> listDistinctFct = new HashMap<String, String>();
	
	private LinkedList<MethodDesc> listHandleFuntionPrivate = new LinkedList<>();
	
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
		
//		if (method.getName().equals("activityMgr"))
//			System.out.println("activityMgr activityMgr ok");
		
		List<Object> buf = new Array<Object>();
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
			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) method.getReturnType());
			prox._setContent(buf);
			return prox;
		}
		else if (JSVariable.class.isAssignableFrom(method.getReturnType() ))
		{
			JSVariable ret=null;
			try {
				ret = ((JSVariable)method.getReturnType().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ret._setContent(buf);
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
	public LinkedList<MethodDesc> getListHandleFuntionPrivate() {
		return listHandleFuntionPrivate;
	}

}
