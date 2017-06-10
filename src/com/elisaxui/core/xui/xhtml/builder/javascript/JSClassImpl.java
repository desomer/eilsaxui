package com.elisaxui.core.xui.xhtml.builder.javascript;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder.MethodInvocationHandle;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

public class JSClassImpl extends JSContent {
	
	Object name;
//	boolean isInitialized = false;
	LinkedList<JSFunction> listFuntion = new LinkedList<>();
	Map<String, String> listDistinctFct = new HashMap<String, String>();
	
	LinkedList<MethodInvocationHandle> listHandleFuntionPrivate = new LinkedList<>();
	
	JSClassImpl(JSBuilder jsBuilder) {
		super(jsBuilder);
	}
	
//	public boolean isInitialized() {
//		return isInitialized;
//	}
//
//	public void setInitialized(boolean isInitialized) {
//		this.isInitialized = isInitialized;
//	}

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

}
