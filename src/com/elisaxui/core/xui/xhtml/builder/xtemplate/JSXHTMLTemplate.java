/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
public interface JSXHTMLTemplate extends JSClass {

	@xStatic
	default void doTemplateDataDriven(JSDomElement parent, JSArray<?> data, JSAny fctEnter, JSAny fctExit, JSAny fctChange )
	{
		JSChangeCtx ctx = declareType(JSChangeCtx.class, "ctx");
		JSDataSet aDataSet = let("aDataSet", newInst(JSDataSet.class) );
		aDataSet.setData(data);
	
		JSDataDriven aDataDriven = let("aDataDriven", newInst(JSDataDriven.class, aDataSet) );
		aDataDriven.onEnter(funct(ctx).__(()->{
			ctx.parent().set(parent);
			JSDomElement dom =let(JSDomElement.class, "dom", fctEnter.call("call", _this(), JSAny.SEP, ctx.row(), JSAny.SEP, ctx));   //, ".call(this, ctx.row, ctx)"
			ctx.row().attrByString(JSDataSet.ATTR_DOM_LINK).set(dom);
			parent.appendChild( dom );
		}));
		
		aDataDriven.onExit(funct(ctx).__(()->{
			ctx.parent().set(parent);
			__(fctExit, ".call(this, ctx.row, ctx.row['"+JSDataSet.ATTR_DOM_LINK+"'], ctx)");
		}));
		
		_if(fctChange, "!=null").then(() -> {
			aDataDriven.onChange(funct(ctx).__(()->{
				//_if("valuectx.row['_dom_']!=null").then(() -> {
					__(fctChange, ".call(this, ctx)");
				//});
		}));
		});
		
		
		/********************************************************************/
		
	}

	@xStatic(autoCall=true)
	default void initMethod() {
		JSInt i = declareType(JSInt.class, "i");
		JSInt j = declareType(JSInt.class, "j");
		JSArray<?> child = declareType(JSArray.class, "child");
		JSon elem = declareType(JSon.class, "elem");
		JSon eldom = declareType(JSon.class, "eldom");
		
		let("doElem", fct(eldom, elem, () -> {
			_if("elem instanceof Element || elem instanceof Text").then(() -> { // nodeType = Node.TextNode
				__("eldom.appendChild(elem)");
			})._elseif("typeof(elem) === 'string' || elem instanceof String").then(() -> {
				__("eldom.appendChild(document.createTextNode(elem))");
			})._elseif("elem instanceof Function").then(() -> {
				let("r", "elem.call(elem, eldom)");
				_if("r!=null").then(() -> {
					__("doElem(eldom, r)");
				});
			})._else(() -> {
				JSArray<?> el = cast(JSArray.class, "elem");
				_forIdx(i, el)._do(() -> {
					let("attr", el.at(i));
					__("eldom.setAttributeNode(attr)");
				});
			});
		}));

		let("e", funct("id", child, "attr").__(() -> {
			JSAny newdom = let(JSAny.class, "newdom", "document.createElement(id)");
			_if(child.isNotEqual(null)).then(() -> {
				_forIdx(j, child)._do(() -> {
					let("elem", child.at(j));
					__("doElem(newdom, elem)");
				});
			});
			_return(newdom);
		}));
		__("window.e=e");

		let("a", fct(child, () -> {
			JSon attr = let(JSon.class, "attr", null);
			JSArray<Object> ret = let("ret", new JSArray<>().asLitteral());
			_forIdx(j, child)._do(() -> {
				let("elem", child.at(j));
				_if(j.modulo(2).isEqual(0)).then(() -> {
					attr.set("document.createAttribute(elem)");
					ret.push(attr);
				})._else(() -> {
					attr.attr("value").set("elem");
				});

			});
			_return(ret);
		}));
		__("window.a=a");

		let("t", fct(child, () -> {
			JSon text = let(JSon.class, "text", null);
			text.set("document.createTextNode(", child, ")");
			_return(text);
		}));
		
		__("window.t=t");
	}
	
}
