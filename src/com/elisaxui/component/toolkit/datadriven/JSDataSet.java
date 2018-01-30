package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;

public interface JSDataSet extends JSClass {


	public static final String _DOM = "_dom_";
	
	JSArray data = null;
	JSCallBack callBackChange = null;
	JSInt delayEvent = null;
	JSVariable myProxySet = null;    //WeakSet
	
	JSDataSet _that = null;
//	Object synchroneEvent = null;   // toDo

	default JSVoid constructor(Object d) {
		 _set(data, d);
		 _set(callBackChange, "$.Callbacks()");
		 _set(delayEvent, 0);
		 _set(myProxySet, "new WeakSet()");
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
				_var("changeHandler", "{\n" +
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
	
					       ._if("property!='"+_DOM+"' && target[property]!==value")
					       		//.consoleDebug("'setting'", "property" , "' for '" , "target" , "' with value '" , "value")
								._var("row", "{ ope:'change', row:target, idx:target.idx, property:property, value: value, old: target[property] }")
								._var("fct"," function() {fastdom.mutate(function() {that.callBackChange.fire(row); })}")
								.__("setTimeout(fct, 1)")
					       .endif()
				       	   ._set("target[property]", "value")
					       .__("return true")

//				" set: function(target, property, value, receiver) {\n" +
//				" console.log(\'setting \' , property , \' for \' , target , \' with value \' , value);\n" +
//				" target[property] = value;\n" +
//				" // you have to return true to accept the changes\n" +
//				" return true;\n" +
//				" }\n" +
				,"};")
				
				
				._var("that", "this")
				// observe le push du tableau
				._set("d.push", fct()
						.__("arguments[0]=new Proxy(arguments[0], changeHandler)")
						.__(_that.addProxy("arguments[0]"))
						.__("Array.prototype.push.apply(this, arguments)")		
						._var("row", "{ ope:'enter', row:arguments[0], idx:this.length-1 }")
						// les push sont lancé dans le fast dom 
						._var("fct"," function() {fastdom.mutate(function() {that.callBackChange.fire(row); })}")
						// les push sont executé de facon echelonné dans le temps par intervalle de 2 ms
						._var("t", "that.delayEvent")
						._if("t==0")
							.__("setTimeout(",fct()._set("that.delayEvent",0) ,", 0)")   // remise a zero apres la boucle
						.endif()
						._set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						//.__("window.requestAnimationFrame(fct)")
				)
				._set("d.pop", fct()._var("ret","Array.prototype.pop.apply(this, arguments)")
						._var("row", "{ ope:'exit', row:ret, idx:this.length }")
						._var("fct"," function() {fastdom.mutate(function() {that.callBackChange.fire(row); })}")
						._var("t", "that.delayEvent")
						._if("t==0")
							.__("setTimeout(",fct()._set("that.delayEvent",0) ,", 0)")   // remise a zero apres la boucle
						.endif()
						._set("that.delayEvent", "that.delayEvent+2")
						.__("setTimeout(fct, t)")
						.__("return ret")
				)
				._set("d.splice", fct().__(()->{
						
						_if("arguments.length>2 && !", _that.isProxy("arguments[2]") );
							__("arguments[2]=new Proxy(arguments[2], changeHandler)");
							__(_that.addProxy("arguments[2]"));
						endif();
						
						_var("ret","Array.prototype.splice.apply(this, arguments)");
						_var("row", "null");
						_if("arguments.length>2");
							_set("row", "{ ope:'enter', row:arguments[2], idx:arguments[0] }");
						_else();
							_set("row", "{ ope:'exit', row:ret[0], idx:this.length }");
						endif();
						
						_var("fct"," function() {fastdom.mutate(function() {that.callBackChange.fire(row); })}");
						_var("t", "that.delayEvent");
						_if("t==0");
							__("setTimeout(",fct()._set("that.delayEvent",0) ,", 0)") ;  // remise a zero apres la boucle
						endif();
						_set("that.delayEvent", "that.delayEvent+2");
						__("setTimeout(fct, t)");
						__("return ret");
					})
				)
			   ._set(data, "d")	 
				;
	}

	default JSArray getData() {
		 return data;
	}

}
