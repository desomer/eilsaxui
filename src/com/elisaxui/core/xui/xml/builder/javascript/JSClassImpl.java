package com.elisaxui.core.xui.xml.builder.javascript;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.elisaxui.core.xui.xml.builder.XMLBuilder;

public class JSClassImpl extends JSContent {
	
	Object name;
	boolean isInitialized = false;
	LinkedList<JSFunction> listFuntion = new LinkedList<>();
	Map<String, String> listDistinctFct = new HashMap<String, String>();
	LinkedList<JSFunction> listVariable = new LinkedList<>();
	
	JSClassImpl(JSBuilder jsBuilder) {
		super(jsBuilder);
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
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
		for (JSFunction jsFunction : listFuntion) {
			jsFunction.toXML(buf);
		}
		jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()-1);
		jsBuilder.newTabulation(buf);
		buf.addContent("}");
		return null;
	}

}
