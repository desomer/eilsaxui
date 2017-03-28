package com.elisaxui.core.xui.xhtml;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.SCRIPT_AFTER_BODY;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xml.builder.javascript.JSFunction;

public abstract class XHTMLPart extends XMLPart {

	static JSBuilder jsBuilder = new JSBuilder(null, new Object[]{});
	
	public final XMLPart vBody(Element body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(Element elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(SCRIPT_AFTER_BODY.class, elem);
		return this;
	}

	public final Element xDiv(Object... inner) {
		return xElement("div", inner);
	}

	public final Element xSpan(Object... inner) {
		return xElement("span", inner);
	}

	public final Element xH1(Object... inner) {
		return xElement("h1", inner);
	}

	public final Element xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public final Element xLi(Object... inner) {
		return xElement("li", inner);
	}

	public final Element xComment(Object... comment) {
		ArrayList<Object> elem = new ArrayList<>();
		elem.add("<!--\n");
		for (Object c : comment) {
			elem.add(xListElement(c+"\n"));
		}
		elem.add(xListElement("-->"));
		return xElement(null, elem.toArray());
	}
	
	
	public final static JSContent js() {
		return jsBuilder.createJSContent();
	}
	
	
	public final static JSFunction _fct(Object... param) {
		return jsBuilder.createJSFunction().setParam(param);
	}
	
	public String txtVar(Object var)
	{
		return "'+"+var+"+'";
	}
	
	public final Element xScriptJS(Object js) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}
	
	
	public final  JSClass _new()
	{
		return null;
	}
	
	public final  <E> E xImport( Class<?> cl)
	{
		
		class MyInvocationHandler  implements InvocationHandler{
			public Object inst;
			
		    private Object impl;
		    public  MyInvocationHandler(Object obj) {
		        this.impl = obj;
		    }
		    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		    	 if (method.getName().equals("js"))
					    return js();
					 
					 if (method.getName().equals("setName"))
					 {
						 final Class<?> declaringClass = method.getDeclaringClass();
				          Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
				          constructor.setAccessible(true);
						 return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					              .unreflectSpecial(method, declaringClass)
					              .bindTo(proxy)
					              .invokeWithArguments(args);   // args
					 }
					 
					 if(method.isDefault()) {
					     final Class<?> declaringClass = method.getDeclaringClass();
					          Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
					          constructor.setAccessible(true);
							   Parameter[] param =  method.getParameters();	 
					          Object[] p = new Object[param.length];
					          
				        	  StringBuilder txtXML = new StringBuilder(1000);
				      		  StringBuilder txtXMLAfter = new StringBuilder(1000);
				      		  txtXML.append(method.getName());
				      		  txtXML.append("(");
					          
					          for (int i = 0; i < args.length; i++) {
					        	  p[i] = param[i].getName();
					        	  if (i>0)
					        		  txtXML.append(", ");
					        	  txtXML.append(p[i]);
					          }
					          txtXML.append(") {");
					          
					          Object ret = 
					              constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
					              .unreflectSpecial(method, declaringClass)
					              .bindTo(proxy)
					              .invokeWithArguments(p);   // args
					          
					          if (ret instanceof JSContent)
					          {			      		  
					        	 
					        	  ((JSContent)ret).toXML(new XMLBuilder("js", txtXML, txtXMLAfter).setJS(false));
					        	  txtXML.append("\n}");
					        	  
					        	  System.out.println("txt JS = <"+ txtXML + ">");

					          }
					          else
					        	  System.out.println("ret ="+ ret);
					   }
			
					
				   
				   StringBuilder buf = new StringBuilder();
				   buf.append(((JSClass)inst).name);
				   buf.append(".");
				   buf.append(method.getName());
				   buf.append("(");
				   
				   int i = 0;
				  // System.out.println("Proxying: " + method.getName() + " " + Arrays.toString(args));
				   for (Object p : args) {
					   if (i>0)
						   buf.append(", ");
					   buf.append(p);
					   i++;
				   }
				   buf.append(")");
				   
				   System.out.println("txt call = <"+ buf + ">");
				   
				   return buf;
				 }
		}
	
		MyInvocationHandler ih = new MyInvocationHandler(null);
	
		Object a =  Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { cl }, ih);
		ih.inst=a;
		return (E) a;
	}
	

	/**************************************************************************/
	
	public final Attr xID(Object id) {
		Attr attr = xAttr("id", id);
		return attr;
	}

	
}
