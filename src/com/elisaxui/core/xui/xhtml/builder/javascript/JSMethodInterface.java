package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

public interface JSMethodInterface  {
	
	JSMethodInterface __(Object... content);
	
	/**********************************************************************/
	JSMethodInterface set(Object name, Object... content);
	JSMethodInterface _var(Object name, Object... content);
	
	<E> E let(Class<? extends E > type, Object name, Object... content);
	<E> E let(String name, E content);
	
	JSMethodInterface consoleDebug(Object... content);
	JSMethodInterface systemDebugIf(Object cond, Object... content);
	/**********************************************************************/
	JSMethodInterface _for(Object... content);
	JSMethodInterface _forIdx(Object idx, JSArray array);
	
	JSMethodInterface endfor();
	/**********************************************************************/
	Object _new(Object... param);
	
	/**********************************************************************/
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
	Object var(Object... param);
	Object calc(Object... param);
	
	/**********************************************************************/
	
	JSFunction fct(Object... param);
	JSFunction callback( Anonym c);
	JSFunction callback(Object param, Anonym c);
	
	/**
	 * exemple <br>
	 * 	.__( fragment(debugEvent).consoleDebug(txt("ok")))
	
	 * @param condition
	 * @return
	 */
	JSFunction fragmentIf(Object condition);

	/**********************************************************************/
	JSMethodInterface _if(Object... content);
	JSMethodInterface then(Anonym content);
	
	JSMethodInterface _else();
	JSMethodInterface _else(Anonym content);
	
	JSMethodInterface endif();

	JSMethodInterface _elseif(Object... content);

	/**********************************************************************/
	JSMethodInterface setTimeout(Object... content);
	
	/**********************************************************************/
	JSMethodInterface _return(Object... content);
	JSVoid _void();
	Object _null();
	Object _this();
	/*******************************************************************/
	Object $$subContent();
	Object $$gosubContent(Object content);

}