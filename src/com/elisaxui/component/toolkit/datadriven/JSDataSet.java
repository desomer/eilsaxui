package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;

public interface JSDataSet extends JSClass {

	public static final String ATTR_DOM_LINK = "_dom_";

	JSArray<Object> data();

	TKPubSub callBackChange();

	JSInt delayEvent = null;
	JSAny myProxySet = null; // ajouter le type WeakSet

	JSDataSet _that = null;

	default void constructor(Object d) {
		data().set(d);
		callBackChange().set(newJS(TKPubSub.class));
		_set(delayEvent, 0);
		_set(myProxySet, "new WeakSet()");
	}

	default void onChange(Object callback) {
		callBackChange().subscribe(callback);
	}

	default Object isProxy(Object myObj) {
		return __("return ", myProxySet, ".has(", myObj, ")");
	}

	default Object addProxy(Object myObj) {
		return __(myProxySet, ".add(", myObj, ")");
	}

	default void setData(JSArray<?> d) {

		JSObject target = declareType(JSObject.class, "target");
		JSObject property = declareType(JSObject.class, "property");
		JSObject thisArg = declareType(JSObject.class, "thisArg");
		JSObject argumentsList = declareType(JSObject.class, "argumentsList");
		JSObject value = declareType(JSObject.class, "value");
		JSObject receiver = declareType(JSObject.class, "receiver");
		JSInt idx = declareType(JSInt.class, "idx");
		

		// changeHandler sur attribut d'objet JS
		JSObject changeHandler = let("changeHandler", new JSObject().asLitteral());

		changeHandler.attr("get").set(fct(target, property,
				() -> {
					JSObject obj = let("obj", target.attrByString(property));
					_if("typeof ", obj, "=== 'object' && !(", obj, " instanceof Array) && ", obj, "!==null && ",
							property, "!='", JSDataSet.ATTR_DOM_LINK, "'").then(() -> {
								_return("new Proxy(", obj, ",", changeHandler, ")");
							})._else(() -> {
								_return(obj);
							});
				}));
		changeHandler.attr("apply").set(fct(target, thisArg, argumentsList,
				() -> _return(thisArg.attrByString(target).apply(_this(), argumentsList))));
		changeHandler.attr("deleteProperty").set(fct(target, property,
				() -> {
					consoleDebug(txt("Deleted %s"), property);
					_return(true);
				}));
		changeHandler.attr("set").set(fct(target, property, value, receiver,
				() -> {
					_if("property!='" + ATTR_DOM_LINK + "' && target[property]!==value").then(() -> {
						JSChangeCtx obj = newJS(JSChangeCtx.class).asLitteral();
						obj.ope().set("change");
						obj.row().set(target);
						obj.idx().set(target.attr("idx"));
						obj.property().set(property);
						obj.value().set(value);
						obj.old().set(target.attrByString(property));

						JSChangeCtx row = let("row", obj);
						setTimeout(fct(() -> __("fastdom.mutate(",
								fct(() -> cast(JSDataSet.class, "that").callBackChange().publish(row)), ")")),
								1);
					});

					target.attrByString(property).set(value);
					_return(true);
				}));

		_var("that", _this());
		
		_forIdx(idx, d)._do(() -> {
			__(d.at(idx), "=new Proxy(",d.at(idx),", changeHandler)");
			__(_that.addProxy(d.at(idx)));
			_var("row", "{ ope:'enter', row:",d.at(idx),", idx:",idx," }");
			__("fastdom.mutate(function() {that.callBackChange.publish(row); })");
		});
		
		// observe le push du tableau
		_set("d.push", fct(() -> {
			__("arguments[0]=new Proxy(arguments[0], changeHandler)");
			__(_that.addProxy("arguments[0]"));
			__("Array.prototype.push.apply(this, arguments)");
			_var("row", "{ ope:'enter', row:arguments[0], idx:this.length-1 }");

			_if("window.datadrivensync").then(() -> {
				__("fastdom.mutate(function() {that.callBackChange.publish(row); })");
			})._else(() -> {
				// les push sont lancé dans le fast dom
				_var("fct", " function() {fastdom.mutate(function() {that.callBackChange.publish(row); })}");
				// les push sont executé de facon echelonné dans le temps par intervalle de 2 ms
				_var("t", "that.delayEvent");

				_if("t==0").then(() -> {
					__("setTimeout(", funct()._set("that.delayEvent", 0), ", 0)"); // remise a zero apres la boucle
				});
				_set("that.delayEvent", "that.delayEvent+2");
				__("setTimeout(fct, t)");

				// .__("window.requestAnimationFrame(fct)")
			});
		}));

		_set("d.pop", funct()._var("ret", "Array.prototype.pop.apply(this, arguments)")
				._var("row", "{ ope:'exit', row:ret, idx:this.length }")
				._var("fct", " function() {fastdom.mutate(function() {that.callBackChange.publish(row); })}")
				._var("t", "that.delayEvent")
				._if("t==0")
				.__("setTimeout(", funct()._set("that.delayEvent", 0), ", 0)") // remise a zero apres la boucle
				.endif()
				._set("that.delayEvent", "that.delayEvent+2")
				.__("setTimeout(fct, t)")
				.__("return ret"));

		_set("d.splice", funct().__(() -> {

			_if("arguments.length>2 && !", _that.isProxy("arguments[2]"));
			__("arguments[2]=new Proxy(arguments[2], changeHandler)");
			__(_that.addProxy("arguments[2]"));
			endif();

			_var("ret", "Array.prototype.splice.apply(this, arguments)");
			_var("row", "null");
			_if("arguments.length>2");
			_set("row", "{ ope:'enter', row:arguments[2], idx:arguments[0] }");
			_else();
			_set("row", "{ ope:'exit', row:ret[0], idx:this.length }");
			endif();

			_var("fct", " function() {fastdom.mutate(function() {that.callBackChange.publish(row); })}");
			_var("t", "that.delayEvent");
			_if("t==0");
			__("setTimeout(", funct()._set("that.delayEvent", 0), ", 0)"); // remise a zero apres la boucle
			endif();

			_set("that.delayEvent", "that.delayEvent+2");
			__("setTimeout(fct, t)");
			__("return ret");
		}));

		data().set(d);
	}

	default JSArray<Object> getData() {
		return data();
	}

}
