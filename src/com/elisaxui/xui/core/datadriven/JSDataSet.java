package com.elisaxui.xui.core.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;

public interface JSDataSet extends JSClass {

	JSArray data = null;
	JSCallBack callBackChange = null;
	JSInt delayEvent = null;
	JSVariable myProxySet = null;    //WeakSet
	
	JSDataSet _that = null;
//	Object synchroneEvent = null;   // toDo

	default JSVoid constructor(Object d) {
		 set(data, d);
		 set(callBackChange, "$.Callbacks()");
		 set(delayEvent, 0);
		 set(myProxySet, "new WeakSet()");
		 return _void();
	}

	default Object onChange(Object callback) {
		return __(callBackChange,".add(", callback ,")");
				
	}
	
	default Object isProxy(Object myObj)
	{
		return __("return ",myProxySet,".has(",myObj,")");
	}
	
	default Object addProxy(Object myObj)
	{
		return __(myProxySet,".add(",myObj,")");
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
					       		//.consoleDebug("'setting'", "property" , "' for '" , "target" , "' with value '" , "value")
								.var("row", "{ ope:'change', row:target, idx:target.idx, property:property, value: value, old: target[property] }")
								.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
								.__("setTimeout(fct, 1)")
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
						.__(_that.addProxy("arguments[0]"))
						.__("Array.prototype.push.apply(this, arguments)")		
						.var("row", "{ ope:'enter', row:arguments[0], idx:this.length-1 }")
						// les push sont lancé dans le fast dom 
						.var("fct"," function() {\nfastdom.mutate(function() {\nthat.callBackChange.fire(row); })}")
						// les push sont executé de facon echelonné dans le temps par intervalle de 2 ms
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
				.set("d.splice", fct()
						
						._if("arguments.length>2 && !", _that.isProxy("arguments[2]") )
							.__("arguments[2]=new Proxy(arguments[2], changeHandler)")
							.__(_that.addProxy("arguments[2]"))
						.endif()
						
						.var("ret","Array.prototype.splice.apply(this, arguments)")
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

	default Object getData() {	  //TODO JSArray
		 return data;
	}

}
