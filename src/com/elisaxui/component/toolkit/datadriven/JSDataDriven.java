package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVoid;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
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
@xExport
public interface JSDataDriven extends JSClass {

	JSDataSet dataSet();

	TKPubSub callBackEnter();
	TKPubSub callBackExit();
	TKPubSub callBackChange();
	

	@xStatic
	default void doTemplateDataDriven(JSNodeElement parent, JSArray<?> data, JSAny fctEnter, JSAny fctExit, JSAny fctChange )
	{
		JSChangeCtx ctx = declareType(JSChangeCtx.class, "ctx");
		JSon key = declareType(JSon.class, "key");
		
		JSDataSet aDataSet = let("aDataSet", newJS(JSDataSet.class) );
		aDataSet.setData(data);
	
		JSDataDriven aDataDriven = let("aDataDriven", newJS(JSDataDriven.class, aDataSet) );
		aDataDriven.onEnter(funct(ctx).zzSetComment("onEnter").__(()->{
			ctx.parent().set(parent);
			JSNodeElement dom =let(JSNodeElement.class, "dom", fctEnter.callMth("call", _this(), ctx.row(), ctx)); 
			
			_if("dom instanceof Function").then(() -> {
				__("dom = dom.call(parent, parent)");
			});
			
			ctx.row().attrByString(JSDataSet.ATTR_DOM_LINK).set(dom);

			_for("var ", key ," in ", ctx.row())._do(()->{
				_if(ctx.row().hasOwnProperty(key)).then(() -> {
					JSon attr = let("attr", ctx.row().attrByString(key));
					_if(attr, " instanceof Object").then(() -> {
						attr.attrByString(JSDataSet.ATTR_DOM_LINK).set(dom);
					});

				});
			});
			parent.appendChild( dom );
		}));
		
		aDataDriven.onExit(funct(ctx).zzSetComment("onExit").__(()->{
			ctx.parent().set(parent);
			_if(fctExit, "!=null").then(() -> {
				__(fctExit, ".call(this, ctx.row, ctx.row['"+JSDataSet.ATTR_DOM_LINK+"'], ctx)");
			})._else(()->{
				_if(ctx.row().attrByString(JSDataSet.ATTR_DOM_LINK).notEqualsJS(null)).then(() -> {
					cast(JSNodeElement.class, ctx.row().attrByString(JSDataSet.ATTR_DOM_LINK)).remove();
				});
			});
		}));
		
		_if(fctChange.notEqualsJS(null)).then(() -> {
			aDataDriven.onChange(funct(ctx).zzSetComment("onChange").__(()->{
				_if(ctx.row().attrByString(JSDataSet.ATTR_DOM_LINK).notEqualsJS(null)).then(() -> {
					__(fctChange, ".call(this, ctx)");
				});
		}));
		});
		
		
		/********************************************************************/
		
	}
	
	default void constructor(Object data) {
		dataSet().set(data);
		callBackEnter().set(newJS(TKPubSub.class));
		callBackExit().set(newJS(TKPubSub.class));
		callBackChange().set(newJS(TKPubSub.class));
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
		JSDataDriven self = let(JSDataDriven.class, "self", _this());
		
		JSContentInterface fctChange =  funct("value").__(()->{
			_if("value.ope=='enter'");
				self.doEnter("value");
			endif();
			_if("value.ope=='exit'");
				self.doExit("value");
			endif();
			_if("value.ope=='change'");
				self.doChange("value");
			endif();
		});
		
		/******************************************/
		_var("data", dataSet().getData());
		_var("fctChange", fctChange);
		
		dataSet().onChange("fctChange");
		
		return _void();
	}

}
