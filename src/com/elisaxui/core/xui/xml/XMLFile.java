package com.elisaxui.core.xui.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.elisaxui.core.config.ConfigMgr;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xml.builder.javascript.JSClassImpl;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapResource = new HashMap<Class<?>, Class<?>>();
	public LinkedList<Object> listParent = new LinkedList<Object>();
	private ConfigMgr configMgr = new ConfigMgr();

	public Map<String, JSClassImpl> listClass = new HashMap<String, JSClassImpl>();

	public JSClassImpl getClassImpl(JSBuilder jsBuilder, Class cl) {
		String name = cl.getSimpleName();
		JSClassImpl impl = listClass.get(name);
		if (impl == null) {
			impl = jsBuilder.createJSClass();
			impl.setName(cl.getSimpleName());

			listClass.put(name, impl);
			// init constructor
			JSClass inst = XHTMLPart.jsBuilder.getProxy(cl);
			Method[] lism = cl.getDeclaredMethods();
			if (lism != null) {
				for (Method method : lism) {
					if (method.getName().equals("constructor")) {
						try {
							method.invoke(inst, new Object[method.getParameterCount()]);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException| SecurityException e) {
							 e.printStackTrace();
						}

					}
				}
			}

		}
		return impl;
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
