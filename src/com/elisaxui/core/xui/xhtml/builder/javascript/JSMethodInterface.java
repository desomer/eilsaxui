package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;

public interface JSMethodInterface  {
	
	JSMethodInterface __(Object... content);
	
	JSMethodInterface _return(Object... content);

	JSMethodInterface set(Object name, Object... content);

	JSMethodInterface var(Object name, Object... content);
	<E extends JSVariable> E let(Class<? extends E > type, Object name, Object... content);
	
	JSMethodInterface consoleDebug(Object... content);
	JSMethodInterface systemDebugIf(Object cond, Object... content);
	
	JSMethodInterface _for(Object... content);
	JSMethodInterface _forIdx(Object idx, JSArray array);
	
	JSMethodInterface endfor();

	Object _new(Object... param);
	
	/**
	 * exemple <br>  
	 * txt("do ", jsvar("event"), "on", jsvar("variable")) <br>
	 * consoleDebug(txt("do event typed ", jsvar("event")), txt(" on "), "jsonAct")
	 * @param param
	 * @return
	 */
	Object txt(Object... param);
	
	/**
	 * creation d'un JSVariable <br>
	 * exemple   jsvar("name", "[", 1 ,"]") 
	 * @param param
	 * @return
	 */
	Object jsvar(Object... param);

	JSFunction fct(Object... param);
	
	/**
	 * exemple <br>
	 * 	.__( fragment(debugEvent).consoleDebug(txt("ok")))
	
	 * @param condition
	 * @return
	 */
	JSFunction fragmentIf(Object condition);

	JSMethodInterface _if(Object... content);

	JSMethodInterface _else();
	
	JSMethodInterface endif();

	JSMethodInterface _elseif(Object... content);

	JSMethodInterface setTimeout(Object... content);
	
	/**********************************************************************/
	Object _void();
	Object _null();
	Object _this();
	/*******************************************************************/
	Object $$subContent();
	Object $$gosubContent(Object content);
}