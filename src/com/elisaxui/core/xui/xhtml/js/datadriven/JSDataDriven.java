package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

/**
 * 
 *   - gestion par proxy sur json
 *   - gestion MutationObserver ?
 * 
 * 	 - enter  (object)
 *   - exit   (object)
 *   - change (attribut d'un object)
 *   - gestion de plage d'affichage
 * 
 * @author Bureau
 *
 */
public interface JSDataDriven extends JSClass { 

	JSDataSet dataSet=null;
	
	String callBackEnter = null;
	String callBackExit = null;
	String callBackChange = null;
	JSDataDriven _this = null;
	
	default Object constructor(Object data)
	{
		return set(dataSet, data)
			   .set(callBackEnter, "$.Callbacks()")	 
			   .set(callBackExit, "$.Callbacks()")
			   .set(callBackChange, "$.Callbacks()")
				;
	}
	
	default Object onEnter(Object callback)
	{
		return __(callBackEnter,".add(", callback ,")");
	}
	
	default Object onExit(Object callback)
	{
		return __(callBackExit,".add(", callback ,")");
	}
	
	
	default Object doEnter(Object row)
	{
		return __(callBackEnter, ".fire(row)");
	}
	
	default Object start()
	{
		return var("data", dataSet.getData())
				._for("var i in data")
				    ._if("typeof data[i] != 'function'")
						.__(_this.doEnter("data[i]"))
					.endif()
			    .endfor()
			    .var("self", "this")
			    .var("fct", fct("value")
			    		.__("self.doEnter(value);"))
			    .__(dataSet.onChange("fct"))
			    ;
	}
	
}
