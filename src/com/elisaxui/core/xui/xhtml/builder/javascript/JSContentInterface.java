package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

public interface JSContentInterface  {
	
	JSContentInterface __(Object... content);
	
	/**********************************************************************/
	JSContentInterface _set(Object name, Object... content);
	JSContentInterface _var(Object name, Object... content);
	
	<E> E let(Class<E> type, Object name, Object... content);
	<E> E let(String name, E content);
	void let(JSContentInterface name, Object... content);
	void let(JSVariable name, Object... content);
	
	JSContentInterface consoleDebug(Object... content);
	JSContentInterface systemDebugIf(Object cond, Object... content);
	/**********************************************************************/
	JSContentInterface _for(Object... content);
	JSContentInterface _forIdxBetween(JSInt idx, int start, int end);
	JSContentInterface _forIdx(Object idx, JSArray<?> array);
	JSContentInterface _do(JSAnonym c);
	@Deprecated
	JSContentInterface endfor();
	/**********************************************************************/
	@Deprecated
	Object _new(Object... param);
	<E> E newInst(Class<E> type, Object... param);
	/**********************************************************************/
	/**
	 * exemple <br>  
	 * txt("do ", var("event"), "on", var("variable")) <br>
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
	JSVariable var(Object... param);
	Object calc(Object... param);
	
	/**********************************************************************/
	
	JSFunction fct(Object... param);
	JSFunction callback( JSAnonym c);
	JSFunction callback(Object param, JSAnonym c);
	
	/**
	 * exemple <br>
	 * 	.__( fragment(debugEvent).consoleDebug(txt("ok")))
	
	 * @param condition
	 * @return
	 */
	JSFunction fragmentIf(Object condition);

	/**********************************************************************/
	JSContentInterface _if(Object... content);
	JSContentInterface then(JSAnonym content);
	
	JSContentInterface _else();
	JSContentInterface _else(JSAnonym content);
	JSContentInterface _elseif(Object... content);
	
	@Deprecated
	JSContentInterface endif();
	@Deprecated
	JSContentInterface _elseif_(Object... content);


	/**********************************************************************/
	JSContentInterface setTimeout(Object... content);
	JSContentInterface setTimeout(JSAnonym a, Object... content);
	
	/**********************************************************************/
	JSContentInterface _return(Object... content);
	
	@Deprecated
	JSVoid _void();
	@Deprecated
	Object _null();
	@Deprecated
	Object _this();
	/*******************************************************************/
	Object $$subContent();
	Object $$gosubContent(Object content);

}