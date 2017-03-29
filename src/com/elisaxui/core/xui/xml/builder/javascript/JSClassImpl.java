package com.elisaxui.core.xui.xml.builder.javascript;

import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLBuilder;

public class JSClassImpl extends JSContent {
	JSClassImpl(JSBuilder jsBuilder) {
		super(jsBuilder);
		// TODO Auto-generated constructor stub
	}

	LinkedList<JSFunction> listFuntion = new LinkedList<>();
	Object name;
	
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
