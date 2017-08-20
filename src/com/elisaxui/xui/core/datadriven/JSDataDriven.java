package com.elisaxui.xui.core.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSCallBack;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.*;
/**
 * 
 * - gestion par proxy sur json - gestion MutationObserver ?
 * 
 * - enter (object) - exit (object) - change (attribut d'un object) - gestion de
 * plage d'affichage
 * 
 * @author Bureau
 *
 */
public interface JSDataDriven extends JSClass {

	JSDataSet dataSet = defAttr();

	JSCallBack callBackEnter = defAttr();
	JSCallBack callBackExit = defAttr();
	JSCallBack callBackChange = defAttr();
	
	JSDataDriven _this = defVar();
	JSDataDriven _self = defVar();

	default Object constructor(Object data) {
		set(dataSet, data)
				.set(callBackEnter, "$.Callbacks()")
				.set(callBackExit, "$.Callbacks()")
				.set(callBackChange, "$.Callbacks()")
				.__(_this.start());
		return _void();
	}

	default Object onEnter(Object callback) {
		__(callBackEnter, ".add(", callback, ")");
		return  _void();
	}

	default Object onExit(Object callback) {
		__(callBackExit, ".add(", callback, ")");
		return  _void();
	}

	default Object onChange(Object callback) {
		__(callBackChange, ".add(", callback, ")");
		return  _void();
	}

	default Object doEnter(Object row) {
		__(callBackEnter, ".fire(row)");
		return  _void();
	}

	default Object doExit(Object row) {
		__(callBackExit, ".fire(row)");
		return  _void();
	}

	default Object doChange(Object row) {
		__(callBackChange, ".fire(row)");
		return  _void();
	}

	default Object start() {
		
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
