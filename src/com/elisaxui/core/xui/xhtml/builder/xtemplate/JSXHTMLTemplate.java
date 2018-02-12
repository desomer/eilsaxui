/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
public interface JSXHTMLTemplate extends JSClass {

	@xStatic
	default void doTemplateDataDriven(JSon parent, JSArray<?> data, JSVariable fctEnter, JSVariable fctExit )
	{
		JSDataSet aDataSet = let("aDataSet", newInst(JSDataSet.class) );
		aDataSet.setData(data);
	
		JSDataDriven aDataDriven = let("aDataDriven", newInst(JSDataDriven.class, aDataSet) );
		aDataDriven.onEnter(fct("ctx").__(()->{
			__("ctx.parent = parent");
			let(JSon.class, "row", fctEnter, ".call(this, ctx.row, ctx)");
			__("ctx.row['"+JSDataSet.ATTR_DOM_LINK+"']=row");
			__(parent, ".appendChild( row )");
		}));
		aDataDriven.onExit(fct("ctx").__(()->{
			__("ctx.parent = parent");
			__(fctExit, ".call(this, ctx.row, ctx.row['"+JSDataSet.ATTR_DOM_LINK+"'], ctx)");
		}));
		
		
		/********************************************************************/
		
	}

	@xStatic(autoCall=true)
	default void initMethod() {
		JSInt i = JSClass.declareType(JSInt.class, "i");
		JSInt j = JSClass.declareType(JSInt.class, "j");
		JSArray<?> child = JSClass.declareType(JSArray.class, "child");
		JSon elem = JSClass.declareType(JSon.class, "elem");
		JSon eldom = JSClass.declareType(JSon.class, "eldom");
		
		let("doElem", callback(eldom, elem, () -> {
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

		let("e", fct("id", child, "attr").__(() -> {
			JSVariable newdom = let(JSVariable.class, "newdom", "document.createElement(id)");
			_if(child.isNotEqual(null)).then(() -> {
				_forIdx(j, child)._do(() -> {
					let("elem", child.at(j));
					__("doElem(newdom, elem)");
				});
			});
			_return(newdom);
		}));
		__("window.e=e");

		let("a", callback(child, () -> {
			JSon attr = let(JSon.class, "attr", null);
			JSArray<Object> ret = let("ret", new JSArray<>().asLitteral());
			_forIdx(j, child)._do(() -> {
				let("elem", child.at(j));
				_if(j.modulo(2).isEqual(0)).then(() -> {
					attr.set("document.createAttribute(elem)");
					ret.push(attr);
				})._else(() -> {
					attr.get("value").set("elem");
				});

			});
			_return(ret);
		}));
		__("window.a=a");

		let("t", callback(child, () -> {
			JSon text = let(JSon.class, "text", null);
			text.set("document.createTextNode(", child, ")");
			_return(text);
		}));
		
		__("window.t=t");
	}
	
}
