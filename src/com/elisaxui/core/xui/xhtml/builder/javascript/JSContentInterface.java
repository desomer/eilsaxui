package com.elisaxui.core.xui.xhtml.builder.javascript;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

public interface JSContentInterface extends JSElement  {
	
	JSContentInterface __(Object... content);
	
	/**********************************************************************/
	JSContentInterface _set(Object name, Object... content);
	JSContentInterface _var(Object name, Object... content);
	
	@Deprecated
	<E> E let(Class<E> type, Object name, Object... content);
	@Deprecated
	<E> E let(String name, E content);
	void let(JSContentInterface name, Object... content);
	void let(JSAny name, Object... content);
	
	JSContentInterface consoleDebug(Object... content);
	JSContentInterface systemDebugIf(Object cond, Object... content);
	/**********************************************************************/
	JSContentInterface _for(Object... content);
	JSContentInterface _forIdx(JSInt idx, int start, int end);
	JSContentInterface _forIdx(Object idx, JSArray<?> array);
	JSContentInterface _do(JSLambda c);
	@Deprecated
	JSContentInterface endfor();
	/**********************************************************************/
	@Deprecated
	Object _new(Object... param);
	<E> E newJS(Class<E> type, Object... param);
	/**********************************************************************/
	/**
	 * exemple <br>  
	 * txt("do ", var("event"), "on", var("variable")) <br>
	 * consoleDebug(txt("do event typed ", var("event")), txt(" on "), "jsonAct")
	 * @param param
	 * @return
	 */
	JSString txt(Object... param);
	
	/**
	 * creation d'un JSAny <br>
	 * exemple   var("name", "[", 1 ,"]") 
	 * @param param
	 * @return
	 */
	JSAny var(Object... param);
	/**
	 * idem var sauf ajout parenthese  ex :calc(toto, "+12")   =>   (toto+12)
	 * @param param
	 * @return
	 */
	Object calc(Object... param);
	
	/**********************************************************************/
	
	JSFunction funct(Object... param);
	JSFunction fct( JSLambda c);
	JSFunction fct(JSElement param, JSLambda c);
	JSFunction fct(JSElement param1, JSElement param2, JSLambda c);
	JSFunction fct(JSElement param1, JSElement param2, JSElement param3, JSLambda c);
	JSFunction fct(JSElement param1, JSElement param2, JSElement param3, JSElement param4, JSLambda c);
	
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
	/********************************************************************/
	JSContentInterface delete(Object... content);

	/**********************************************************************/
	JSContentInterface setTimeout(Object... content);
	JSContentInterface setTimeout(JSLambda a, Object... content);
	
	/**********************************************************************/
	JSContentInterface _return(Object... content);
	JSContentInterface _continue();
	JSAny _this();
	
	@Deprecated
	JSVoid _void();

	/*******************************************************************/
	Object $$subContent();
	Object $$gosubContent(Object content);

}