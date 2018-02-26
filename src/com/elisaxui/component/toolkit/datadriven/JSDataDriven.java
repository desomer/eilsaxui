package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
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

	JSDataSet dataSet();

	TKPubSub callBackEnter();
	TKPubSub callBackExit();
	TKPubSub callBackChange();
	
	JSDataDriven _this = null;
	JSDataDriven _self = null;

	default void constructor(Object data) {
		dataSet().set(data);
		callBackEnter().set(newInst(TKPubSub.class));
		callBackExit().set(newInst(TKPubSub.class));
		callBackChange().set(newInst(TKPubSub.class));
		start();
	}

	default void onEnter(Object callback) {
		callBackEnter().subscribe(callback);
	}

	default void onExit(Object callback) {
		callBackExit().subscribe(callback);
	}

	default void onChange(Object callback) {
		callBackChange().subscribe(callback);
	}

	default void doEnter(Object row) {
		callBackEnter().publish(row);
	}

	default void doExit(Object row) {
		callBackExit().publish(row);
	}

	default void doChange(Object row) {
		callBackChange().publish(row);
	}

	default JSVoid start() {
		
		JSContentInterface fctChange =  funct("value").__(()->{
			_if("value.ope=='enter'");
				_self.doEnter("value");
			endif();
			_if("value.ope=='exit'");
				_self.doExit("value");
			endif();
			_if("value.ope=='change'");
				_self.doChange("value");
			endif();
		});
		
		/******************************************/
		_var("data", dataSet().getData());
		_var(_self, _this);
		_var("fctChange", fctChange);
		
		dataSet().onChange("fctChange");
		
		return _void();
	}

}
