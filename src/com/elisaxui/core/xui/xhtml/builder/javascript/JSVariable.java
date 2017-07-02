package com.elisaxui.core.xui.xhtml.builder.javascript;


/**
 * represente une liste de parametre (pour le new XXX(... liste variable ...) )  ou une variable
 * @author Bureau
 *
 */
public class JSVariable  {
//	public boolean newint = true;
	public Object[] param;
	
	public JSVariable(Object... param) {
		super();
		this.param = param;
	}
}
