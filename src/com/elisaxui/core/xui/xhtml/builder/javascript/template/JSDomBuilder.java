/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.template;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
public interface JSDomBuilder extends JSClass {

	@xStatic(autoCall = true)
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
			})._elseif("elem instanceof Attr").then(() -> {
				__("eldom.setAttributeNode(elem)");
			})._else(() -> {
				JSArray<?> el = cast(JSArray.class, "elem");
				// gestion des attribut
				_forIdx(i, el)._do(() -> {
					JSAny attr = let(JSAny.class, "attr", el.at(i));
					__("doElem(eldom, attr)");
				});
			});
		}));

		let("e", funct("id", child).__(() -> {
			JSAny newdom = let(JSAny.class, "newdom", "document.createElement(id)");
			_if(child.notEqualsJS(null)).then(() -> {
				_forIdx(j, child)._do(() -> {
					let("elem", child.at(j));
					__("doElem(newdom, elem)");
				});
			});
			_return(newdom);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_ELEM + "=e");

		let("p", funct(child, "domParent").__(() -> {
			_if(child, ".length==1").then(() -> {
				let("r", "child[0]");
				_if("r instanceof Function && domParent!=null").then(() -> {
					let("v", "r.call(r, domParent)");  // ajoutes les element
					_if("v!=null").then(() -> {
						__("doElem(domParent, v)");
					});
					
					__("r=document.createTextNode('')");  // ne retourne rien de d'affichable
				});
				_return("r");
			});
			_return(child);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_PART + "=p");

		let("a", fct(child, () -> {
			JSon attr = let(JSon.class, "attr", null);
			JSArray<Object> ret = let("ret", new JSArray<>().asLitteral());
			_forIdx(j, child)._do(() -> {
				JSAny elemC = let(JSAny.class, "elemC", child.at(j));
				_if(j.modulo(2).equalsJS(0)).then(() -> {
					attr.set("document.createAttribute(elemC)");
					ret.push(attr);
				})._else(() -> {
					_if(elemC, " instanceof Function").then(() -> {
						attr.attr("_fct_").set(elemC); // attribut de type function comme vBindable qui affecte
						// la valeur et affecte le XuiBindInfo du node dom
					})._else(() -> {
						attr.attr("value").set(elemC);
					});
				});

			});
			_return(ret);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_ATTR + "=a");

		let("t", fct(child, () -> {
			JSon text = let(JSon.class, "text", null);
			text.set("document.createTextNode(", child, ")");
			_return(text);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_TEXT + "=t");

		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSAny row = JSContent.declareType(JSAny.class, "row");
		JSAny value = JSContent.declareType(JSAny.class, "value");
		JSString attr = JSContent.declareType(JSString.class, "attr");

		let("dbb", fct(domItem, row, attr, value, () -> {
			domItem.attr(JSNodeTemplate.ATTR_BIND_INFO).set("{row:", row, ", attr:" + attr + "}");
			_return(value);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_DATA_BINDING + "=dbb");

	}
}
