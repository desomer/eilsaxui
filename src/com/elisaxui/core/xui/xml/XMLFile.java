package com.elisaxui.core.xui.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.elisaxui.core.config.ConfigMgr;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassImpl;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.helper.ReflectionHelper;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapResource = new HashMap<Class<?>, Class<?>>();
	public LinkedList<Object> listParent = new LinkedList<Object>();
	private ConfigMgr configMgr = new ConfigMgr();

	public Map<String, JSClassImpl> listClass = new HashMap<String, JSClassImpl>();

	public JSClassImpl getClassImpl(JSBuilder jsBuilder, Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		JSClassImpl impl = listClass.get(name);
		if (impl == null) {
			impl = jsBuilder.createJSClass();
			impl.setName(cl.getSimpleName());

			listClass.put(name, impl);

//			// init field => chaque attribut contient le nom js de son champs
//			Field[] listField = cl.getDeclaredFields();
//			if (listField != null) {
//				for (Field field : listField) {
//					System.out.println("init var "+ field.getName() + " from "+ cl.getName());
//					
//				//	try {
//						initField(field);
//				//	} catch (Throwable e) {
//				//		// TODO Auto-generated catch block
//				//		e.printStackTrace();
//				//	}
//				}
//				
//			}

			// init constructor
			JSClass inst = XHTMLPart.jsBuilder.getProxy(cl);
			Method[] lism = cl.getDeclaredMethods();
			if (lism != null) {
				for (Method method : lism) {
					if (method.getName().equals("constructor")) {
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
		return impl;
	}
	
	public void initVar(JSBuilder jsBuilder, Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		//JSClassImpl impl = listClass.get(name);
	//	if (impl == null) {
		//	impl = jsBuilder.createJSClass();
		//	impl.setName(cl.getSimpleName());

		//	listClass.put(name, impl);

			// init field => chaque attribut contient le nom js de son champs
			Field[] listField = cl.getDeclaredFields();
			if (listField != null) {
				for (Field field : listField) {
					System.out.println("init var "+ field.getName() + " from "+ cl.getName());
					
				//	try {
						initField(field);
				//	} catch (Throwable e) {
				//		// TODO Auto-generated catch block
				//		e.printStackTrace();
				//	}
				}
				
			}

		
	}
	

	private void initField(Field field) {
		if (JSClass.class.isAssignableFrom(field.getType())) {
			// gestion particuliere d'un proxy pour affecter le nom
			// au proxy
			@SuppressWarnings("unchecked")
			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) field.getType());
			if (field.getName().startsWith("_")) {
				XHTMLPart.jsBuilder.setNameOfProxy("", prox, field.getName().substring(1));
			} else
				XHTMLPart.jsBuilder.setNameOfProxy("this.", prox, field.getName());
			try {
				ReflectionHelper.setFinalStatic(field, prox);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (JSVariable.class.isAssignableFrom(field.getType())) {
			try {
				JSVariable var =(JSVariable) field.getType().newInstance();
				if (field.getName().startsWith("_"))
					var.setName(field.getName().substring(1));
				else
					var.setName("this." + field.getName());
					  
			    ReflectionHelper.setFinalStatic(field, var);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			// affecte le nom de la variable
			try {
				if (field.getName().startsWith("_"))
					ReflectionHelper.setFinalStatic(field, field.getName().substring(1));
				else
					ReflectionHelper.setFinalStatic(field, "this." + field.getName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isXMLPartAlreadyInFile(XMLPart part) {
		return mapResource.put(part.getClass(), part.getClass()) != null;
	}

	public ConfigMgr getConfigMgr() {
		return configMgr;
	}

	private XMLPart root;

	public XMLPart getRoot() {
		return root;
	}

	public void setRoot(XMLPart root) {
		this.root = root;
	}

}
