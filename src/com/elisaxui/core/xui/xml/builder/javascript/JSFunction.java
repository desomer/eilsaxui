package com.elisaxui.core.xui.xml.builder.javascript;

import com.elisaxui.core.xui.xml.builder.XMLBuilder;

public class JSFunction extends JSContent
{
	//"use strict"
	
	JSFunction(JSBuilder jsBuilder) {
		super(jsBuilder);
	}

	Object name = null;
	Object[] param = null; 
	JSContent code;
	
	public JSInterface getCode() {
		return code;
	}

	public JSFunction setCode(JSContent code) {
		this.code = code;
		return this;
	}

	public Object getName() {
		return name;
	}

	public JSFunction setName(Object name) {
		this.name = name;
		return this;
	}

	public Object[] getParam() {
		return param;
	}

	public JSFunction setParam(Object[] param) {
		this.param = param;
		return this;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
//		jsBuilder.newLine(buf);
		jsBuilder.newTabulation(buf);
		buf.addContent(name);
		buf.addContent("(");

		for (int i = 0; i < param.length; i++) {
			if (i > 0)
				buf.addContent(", ");
			buf.addContent(param[i]);
		}
		buf.addContent(")");
		buf.addContent(" {");
		jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()+1);
		code.toXML(buf);
		jsBuilder.setNbInitialTab(jsBuilder.getNbInitialTab()-1);
		jsBuilder.newLine(buf);
		jsBuilder.newTabulation(buf);
		buf.addContent("}");
		jsBuilder.newLine(buf);
		return buf;
	}

}
