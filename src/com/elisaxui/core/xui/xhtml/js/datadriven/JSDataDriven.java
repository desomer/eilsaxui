package com.elisaxui.core.xui.xhtml.js.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

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

	JSDataSet dataSet = null;

	String callBackEnter = null;
	String callBackExit = null;
	String callBackChange = null;
	JSDataDriven _this = null;
	JSDataDriven _self = null;

	default Object constructor(Object data) {
		set(dataSet, data)
				.set(callBackEnter, "$.Callbacks()")
				.set(callBackExit, "$.Callbacks()")
				.set(callBackChange, "$.Callbacks()")
				.__(_this.start());
		return null;
	}

	default Object onEnter(Object callback) {
		__(callBackEnter, ".add(", callback, ")");
		return null;
	}

	default Object onExit(Object callback) {
		__(callBackExit, ".add(", callback, ")");
		return null;
	}

	default Object onChange(Object callback) {
		__(callBackChange, ".add(", callback, ")");
		return null;
	}

	default Object doEnter(Object row) {
		return __(callBackEnter, ".fire(row)");
	}

	default Object doExit(Object row) {
		__(callBackExit, ".fire(row)");
		return null;
	}

	default Object doChange(Object row) {
		__(callBackChange, ".fire(row)");
		return null;
	}

	default Object start() {
		var("data", dataSet.getData())
				.var(_self, _this)

				// ._for("var i in data")
				// ._if("typeof data[i] != 'function'")
				// .var("row", "{ ope:'enter', row:data[i], idx:i }")
				// .__(_self.doEnter("row"))
				// .endif()
				// .endfor()

				.var("fctChange", fct("value")
						._if("value.ope=='enter'")
						.__(_self.doEnter("value"))
						.endif()
						._if("value.ope=='exit'")
						.__(_self.doExit("value"))
						.endif()
						._if("value.ope=='change'")
						.__(_self.doChange("value"))
						.endif())
				.__(dataSet.onChange("fctChange"));
		return null;
	}

}
