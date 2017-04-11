package com.elisaxui.core.xui.xhtml.builder.javascript;

public interface JSInterface {

	JSInterface __(Object... content);

	JSInterface set(Object name, Object... content);

	JSInterface var(Object name, Object... content);
	
	JSInterface consoleDebug(Object... content);
	
	JSInterface _for(Object... content);
	
	JSInterface endfor();

	Object _new(Object... param);
	
	Object txt(Object... param);

	JSFunction fct(Object... param);

	JSInterface _if(Object... content);

	JSInterface _else();
	
	JSInterface endif();
}