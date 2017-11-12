package com.elisaxui.xui.core.datadriven;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defAttr;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defVar;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSVoid;
/**
 * 
 * - gestion par proxy sur json 
 * - gestion MutationObserver ?
 * 
 * - enter (object) - exit (object) - change (attribut d'un object) 
 * - gestion de plage d'affichage
 * 
 * @author Bureau
 *
 */
public interface JSDataDriven extends JSClass {

	JSDataSet dataSet = null;

	JSCallBack callBackEnter = null;
	JSCallBack callBackExit = null;
	JSCallBack callBackChange = null;
	
	JSDataDriven _this = null;
	JSDataDriven _self = null;

	default JSVoid constructor(Object data) {
		set(dataSet, data)
				.set(callBackEnter, "$.Callbacks()")
				.set(callBackExit, "$.Callbacks()")
				.set(callBackChange, "$.Callbacks()")
				.__(_this.start());
		return _void();
	}

	default JSVoid onEnter(Object callback) {
		__(callBackEnter, ".add(", callback, ")");
		return  _void();
	}

	default JSVoid onExit(Object callback) {
		__(callBackExit, ".add(", callback, ")");
		return  _void();
	}

	default JSVoid onChange(Object callback) {
		__(callBackChange, ".add(", callback, ")");
		return  _void();
	}

	default JSVoid doEnter(Object row) {
		__(callBackEnter, ".fire(row)");
		return  _void();
	}

	default JSVoid doExit(Object row) {
		__(callBackExit, ".fire(row)");
		return  _void();
	}

	default JSVoid doChange(Object row) {
		__(callBackChange, ".fire(row)");
		return  _void();
	}

	default JSVoid start() {
		
		JSMethodInterface fctChange =  fct("value")
			._if("value.ope=='enter'")
				.__(_self.doEnter("value"))
			.endif()
			._if("value.ope=='exit'")
				.__(_self.doExit("value"))
			.endif()
			._if("value.ope=='change'")
				.__(_self.doChange("value"))
			.endif()
			;
		
		/******************************************/
		var("data", dataSet.getData())
		.var(_self, _this)
		.var("fctChange", fctChange)
		
		.__(dataSet.onChange("fctChange"));
		
		return _void();
	}

}
