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
	JSDataDriven _self = null;
	
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
	
	default Object onChange(Object callback)
	{
		return __(callBackChange,".add(", callback ,")");
	}
	
	default Object doEnter(Object row)
	{
		return __(callBackEnter, ".fire(row)");
	}
	
	default Object doExit(Object row)
	{
		return __(callBackExit, ".fire(row)");
	}
	
	default Object doChange(Object row)
	{
		return __(callBackChange, ".fire(row)");
	}
	
	default Object start()
	{
		return var("data", dataSet.getData())
			    .var("self", "this")
				
//				._for("var i in data")
//				    ._if("typeof data[i] != 'function'")
//				   		.var("row", "{ ope:'enter', row:data[i], idx:i }")
//						.__(_self.doEnter("row"))
//					.endif()
//			    .endfor()

			    .var("fctChange", fct("value")
			    		._if("value.ope=='enter'")
			    			.__(_self.doEnter("value"))
			    		.endif()
			    		._if("value.ope=='exit'")
		    				.__(_self.doExit("value"))
		    			.endif()
		    			._if("value.ope=='change'")
	    					.__(_self.doChange("value"))
	    				.endif()
		    		)
			    .__(dataSet.onChange("fctChange"))
			    ;
	}
	
}
