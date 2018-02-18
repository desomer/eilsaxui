package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;

public interface JSContentInterface  {
	
	JSContentInterface __(Object... content);
	
	/**********************************************************************/
	JSContentInterface _set(Object name, Object... content);
	JSContentInterface _var(Object name, Object... content);
	
	<E> E let(Class<E> type, Object name, Object... content);
	<E> E let(String name, E content);
	void let(JSContentInterface name, Object... content);
	void let(JSAny name, Object... content);
	
	JSContentInterface consoleDebug(Object... content);
	JSContentInterface systemDebugIf(Object cond, Object... content);
	/**********************************************************************/
	JSContentInterface _for(Object... content);
	JSContentInterface _forIdxBetween(JSInt idx, int start, int end);
	JSContentInterface _forIdx(Object idx, JSArray<?> array);
	JSContentInterface _do(JSLambda c);
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
	JSString txt(Object... param);
	
	/**
	 * creation d'un JSVariable <br>
	 * exemple   jsvar("name", "[", 1 ,"]") 
	 * @param param
	 * @return
	 */
	JSAny var(Object... param);
	Object calc(Object... param);
	
	/**********************************************************************/
	
	JSFunction fct(Object... param);
	JSFunction callback( JSLambda c);
	JSFunction callback(JSElement param, JSLambda c);
	JSFunction callback(JSElement param1, JSElement param2, JSLambda c);
	JSFunction callback(JSElement param1, JSElement param2, JSElement param3, JSLambda c);
	JSFunction callback(JSElement param1, JSElement param2, JSElement param3, JSElement param4, JSLambda c);
	
	/**
	 * exemple <br>
	 * 	.__( fragment(debugEvent).consoleDebug(txt("ok")))
	
	 * @param condition
	 * @return
	 */
	JSFunction fragmentIf(Object condition);

	/**********************************************************************/
	JSContentInterface _if(Object... content);
	JSContentInterface then(JSLambda content);
	
	JSContentInterface _else();
	JSContentInterface _else(JSLambda content);
	JSContentInterface _elseif(Object... content);
	
	@Deprecated
	JSContentInterface endif();
	@Deprecated
	JSContentInterface _elseif_(Object... content);


	/**********************************************************************/
	JSContentInterface setTimeout(Object... content);
	JSContentInterface setTimeout(JSLambda a, Object... content);
	
	/**********************************************************************/
	JSContentInterface _return(Object... content);
	Object _this();
	
	@Deprecated
	JSVoid _void();

	/*******************************************************************/
	Object $$subContent();
	Object $$gosubContent(Object content);

}