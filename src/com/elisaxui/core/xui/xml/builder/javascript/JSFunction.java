package com.elisaxui.core.xui.xml.builder.javascript;

import com.elisaxui.core.xui.xml.builder.XMLBuilder;

public class JSFunction extends JSContent
{
	JSFunction(JSBuilder jsBuilder) {
		super(jsBuilder);
	}

	Object name = null;
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

	Object[] param = null;  
	


	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		// TODO Auto-generated method stub
		return null;
	}

}
