package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

public interface JSDataSet extends JSClass {

	Object data = null;
	String callBackChange = null;
	Object delayEvent = null;
	

	default Object constructor(Object d) {
		return set(data, d)
				.set(callBackChange, "$.Callbacks()")
				.set(delayEvent, 1)
				;
	}

	default Object onChange(Object callback) {
		return __(callBackChange,".add(", callback ,")");
				
	}
	
	default Object setData(Object d) {
		//JSDataSet that = i
		return  var("that", "this")
				// observe le push du tableau
				.set("d.push", fct().__("Array.prototype.push.apply(this, arguments)") 
						.var("row", "arguments[0]")
						.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
						.var("t", "that.delayEvent")
						.set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						//.__("window.requestAnimationFrame(fct)")
				)
			   .set(data, "d")	 
				;

//				var("changeHandler", "{\n" +
//				" get: function(target, property) {\n" +
//			//	" console.log(\'getting \' , property , \' for \' , target);\n" +
//				" // property is index in this case\n" +
//				" return target[property];\n" +
//				" },\n"+ 
//				" apply: function(target, thisArg, argumentsList) {\n"+
//				" return thisArg[target].apply(this, argumentList);\n"+
//				" },\n"+
//				" deleteProperty: function(target, property) {\n"+
//				" console.log(\"Deleted %s\", property);\n"+
//				" return true;\n"+
//				" },"+
//				"set:", fct("target", "property", "value", "receiver")
//			       	   .set("target[property]", "value")
//				       ._if("target==d")
//				       		.consoleDebug("'setting'", "property" , "' for '" , "target" , "' with value '" , "value")
//				       .endif()
//				       .__("return true")
//
////				" set: function(target, property, value, receiver) {\n" +
////				" console.log(\'setting \' , property , \' for \' , target , \' with value \' , value);\n" +
////				" target[property] = value;\n" +
////				" // you have to return true to accept the changes\n" +
////				" return true;\n" +
////				" }\n" +
//				,"};")
//
//				.set(data, "new Proxy(d, changeHandler)");
	}

	default Object getData() {
		return __("return ", data);
	}

}
