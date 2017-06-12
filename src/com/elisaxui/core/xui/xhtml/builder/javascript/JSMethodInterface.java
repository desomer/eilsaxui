package com.elisaxui.core.xui.xhtml.builder.javascript;

public interface JSMethodInterface {

	
	JSMethodInterface __(Object... content);
	
	JSMethodInterface _return(Object... content);

	JSMethodInterface set(Object name, Object... content);

	JSMethodInterface var(Object name, Object... content);
	
	JSMethodInterface consoleDebug(Object... content);
	
	JSMethodInterface _for(Object... content);
	
	JSMethodInterface endfor();

	Object _new(Object... param);
	
	Object txt(Object... param);
	Object jsvar(Object... param);

	JSFunction fct(Object... param);

	JSMethodInterface _if(Object... content);

	JSMethodInterface _else();
	
	JSMethodInterface endif();

	JSMethodInterface _elseif(Object... content);

	Object _void();
	
	Object _null();
	
	Object _this();

}