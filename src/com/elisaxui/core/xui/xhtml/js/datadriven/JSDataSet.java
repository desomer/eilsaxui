package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

public interface JSDataSet extends JSClass {

	Object data = null;
	String callBackChange = null;
	Object delayEvent = null;
	

	default Object constructor(Object d) {
		return set(data, d)
				.set(callBackChange, "$.Callbacks()")
				.set(delayEvent, 0)
				;
	}

	default Object onChange(Object callback) {
		return __(callBackChange,".add(", callback ,")");
				
	}
	
	default Object setData(Object d) {
		//JSDataSet that = i
		return  
				var("changeHandler", "{\n" +
				" get: function(target, property) {\n" +
//				" console.log(\'getting \' , property , \' for \' , target);\n" +
				" // property is index in this case\n" +
				" return target[property];\n" +
				" },\n"+ 
				" apply: function(target, thisArg, argumentsList) {\n"+
				" return thisArg[target].apply(this, argumentList);\n"+
				" },\n"+
				" deleteProperty: function(target, property) {\n"+
				" console.log(\"Deleted %s\", property);\n"+
				" return true;\n"+
				" },"+
				"set:", fct("target", "property", "value", "receiver")

				       ._if("property!='_dom_' && target[property]!==value")
				       		.consoleDebug("'setting'", "property" , "' for '" , "target" , "' with value '" , "value")
				       .endif()
			       	   .set("target[property]", "value")
				       .__("return true")

//				" set: function(target, property, value, receiver) {\n" +
//				" console.log(\'setting \' , property , \' for \' , target , \' with value \' , value);\n" +
//				" target[property] = value;\n" +
//				" // you have to return true to accept the changes\n" +
//				" return true;\n" +
//				" }\n" +
				,"};")
				
				
				.var("that", "this")
				// observe le push du tableau
				.set("d.push", fct()
						.__("arguments[0]=new Proxy(arguments[0], changeHandler)")
						.__("Array.prototype.push.apply(this, arguments)")		
						.var("row", "{ ope:'enter', row:arguments[0], idx:this.length-1 }")
						.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
						.var("t", "that.delayEvent")
						._if("t==0")
							.__("setTimeout(",fct().set("that.delayEvent",0) ,", 0)")   // remise a zero apres la boucle
						.endif()
						.set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						//.__("window.requestAnimationFrame(fct)")
				)
				.set("d.pop", fct().var("ret","Array.prototype.pop.apply(this, arguments)")
						.var("row", "{ ope:'exit', row:ret, idx:this.length }")
						.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
						.var("t", "that.delayEvent")
						._if("t==0")
							.__("setTimeout(",fct().set("that.delayEvent",0) ,", 0)")   // remise a zero apres la boucle
						.endif()
						.set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						.__("return ret")
				)
				.set("d.splice", fct().var("ret","Array.prototype.splice.apply(this, arguments)")
						.var("row", "null")
						._if("arguments.length>2")
							.set("row", "{ ope:'enter', row:arguments[2], idx:arguments[0] }")
						._else()
							.set("row", "{ ope:'exit', row:ret[0], idx:this.length }")
						.endif()
						
						.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
						.var("t", "that.delayEvent")
						._if("t==0")
							.__("setTimeout(",fct().set("that.delayEvent",0) ,", 0)")   // remise a zero apres la boucle
						.endif()
						.set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						.__("return ret")
				)
			   .set(data, "d")	 
				;



//				.set(data, "new Proxy(d, changeHandler)");
	}

	default Object getData() {
		return __("return ", data);
	}

}
