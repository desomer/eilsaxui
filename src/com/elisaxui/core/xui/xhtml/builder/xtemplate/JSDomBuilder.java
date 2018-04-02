/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEvent;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeHTMLInputElement;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.annotation.xStatic;

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
			})._else(() -> {
				JSArray<?> el = cast(JSArray.class, "elem");
				// gestion des attribut
				_forIdx(i, el)._do(() -> {
					JSAny attr = let(JSAny.class, "attr", el.at(i));
					// _if(attr.attrByString("_fct_"), " instanceof Function").then(() -> { //TODO faire marcher
					_if("attr['_fct_'] instanceof Function").then(() -> {
						let("r", "attr['_fct_'].call(attr, eldom)");
						_if("r!=null").then(() -> {
							__("attr.value=r");
						});
					});

					__("eldom.setAttributeNode(attr)");
				});
			});
		}));

		let("e", funct("id", child, "attr").__(() -> {
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
						attr.attr("_fct_").set(elemC);  // attribut de type function comme vBindable qui affecte 
						//la valeur et affecte le  XuiBindInfo du node dom
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
		JSAny row= JSContent.declareType(JSAny.class, "row"); 
		JSAny value= JSContent.declareType(JSAny.class, "value");
		JSString attr= JSContent.declareType(JSString.class, "attr");
		
		let("dbb", fct(domItem, row, attr, value,  () -> {
			domItem.attr(JSNodeTemplate.ATTR_BIND_INFO).set("{row:", row, ", attr:" + attr + "}");
			_return(value);
		}));
		__("window." + JSNodeTemplate.MTH_ADD_DATA_BINDING + "=dbb");
		
		initChangeEvent();  // TODO a remplacer par autoCall = true
	}

	/*******************************************************/
	@xStatic(autoCall = false)   //TODO 2 true dans la meme classe ne marche pas
	default void initChangeEvent() {

		JSArray<JSString> listEventLitteral = new JSArray<JSString>().asLitteral();
		listEventLitteral.push(JSString.value("change"));
		listEventLitteral.push(JSString.value("click"));
		listEventLitteral.push(JSString.value("keyup"));
		listEventLitteral.push(JSString.value("input"));
		listEventLitteral.push(JSString.value("paste"));
		listEventLitteral.push(JSString.value("blur"));

		JSArray<JSString> listEvent = let("listEvent", listEventLitteral);

		JSEvent event = declareType(JSEvent.class, "event");
		JSInt idx = declareType(JSInt.class, "idx");
		_forIdx(idx, listEvent)._do(() -> {
			document().addEventListener(listEvent.at(idx), fct(event, () -> {
				_if(event.target().nodeName().equalsJS("INPUT")).then(() -> {
					JSNodeHTMLInputElement inputelem = let(JSNodeHTMLInputElement.class, "inputelem",
							event.target());
					XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", inputelem.attr(JSNodeTemplate.ATTR_BIND_INFO));
					_if(ddi, "!=null &&", ddi.row().notEqualsJS(null)).then(() -> {
						ddi.row().attrByString(ddi.attr()).set(inputelem.value());
					});
				});
			}));
		});
	}
	
	/*************************************************/
	public interface XuiBindInfo extends JSType {
		JSString attr();

		JSon row();
		
		JSCallBack fct();
	}
}
