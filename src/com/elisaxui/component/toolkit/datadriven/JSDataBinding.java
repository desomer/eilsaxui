/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSEvent;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeInputElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;
import com.elisaxui.core.xui.xml.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
@xExport
public interface JSDataBinding extends JSClass {

	@xStatic
	default Object initVChangeableText(JSNodeElement domItem, JSAny value, JSString attr) {
		domItem.dataset().attrByString(var("'xui'+",attr)).set(true);
		let("elemText", JSNodeTemplate.MTH_ADD_TEXT+"("+value+")" );
		return "elemText";
	}
	
	@xStatic
	default Object initVChangeableFct(JSNodeElement domItem, JSElement row, JSAny value, JSString attr, JSCallBack fctOnChange) {
		domItem.dataset().attrByString(var("'xui'+",attr)).set(true);
		
		XuiBindInfo bi = newJS(XuiBindInfo.class);
		bi.row().set(row);
		bi.attr().set(attr);
		bi.fct().set(fctOnChange);
		domItem.attr(JSNodeTemplate.ATTR_BIND_INFO).set(bi);

		JSChangeCtx ctx = newJS(JSChangeCtx.class);
		ctx.value().set(value);
		ctx.row().set(row);
		JSChangeCtx changeCtx = let(JSChangeCtx.class, "changeCtx", ctx);
		JSCallBack ret = let(JSCallBack.class, "ret", cast(XuiBindInfo.class, domItem.attr(JSNodeTemplate.ATTR_BIND_INFO)).fct().call(domItem, changeCtx ));
		let("elemText", JSNodeTemplate.MTH_ADD_TEXT+"("+ret+")" );
		return "elemText";
	}
	
	@xStatic
	default void initOnDataChange(JSNodeElement domItem, JSElement row, JSAny value, JSString attr,	JSCallBack fctOnChange) {
		domItem.dataset().attrByString(var("'xui'+",attr)).set(true);
		
		XuiBindInfo bi = newJS(XuiBindInfo.class);
		bi.row().set(row);
		bi.attr().set(attr);
		bi.fct().set(fctOnChange);
		domItem.attr(JSNodeTemplate.ATTR_BIND_INFO).set(bi);

		JSChangeCtx ctx = newJS(JSChangeCtx.class);
		ctx.value().set(value);
		ctx.row().set(row);
		ctx.element().set(domItem);

		JSChangeCtx changeCtx = let(JSChangeCtx.class, "changeCtx", ctx);
		cast(XuiBindInfo.class, domItem.attr(JSNodeTemplate.ATTR_BIND_INFO)).fct().call(domItem, changeCtx );
	}

	@xStatic
	default void initChangeHandler(JSChangeCtx changeCtx, JSNodeElement aDom) {
		/*******************************************************/

		JSString sel = let(JSString.class, "sel", "'[data-xui'+", changeCtx.property(), "+']'");
		JSArray<JSNodeElement> listNode = let(JSArray.class, "listNode", "Array.from(", aDom.querySelectorAll(sel),")");
//		_if(aDom.attr(JSNodeTemplate.ATTR_BIND_INFO).notEqualsJS(null))._then(() -> 
//			listNode.push(aDom)
//		);
		listNode.setArrayType(JSNodeElement.class);
		JSInt i = declareType(JSInt.class, "i");
		_forIdx(i, listNode)._do(() -> {
			JSNodeElement elem = let("elem", listNode.at(i));
			XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", elem.attr(JSNodeTemplate.ATTR_BIND_INFO));
			JSAny valueChange = let("valueChange", changeCtx.value());
			_if(ddi, "!=null &&", ddi.fct().notEqualsJS(null)).then(() -> {
				changeCtx.element().set(elem);
				JSAny rf = let("rf", ddi.fct().call(elem, changeCtx)); // appel de la fct vOnChange( fct )
				_if(rf.equalsJS(null)).then(() -> 
					_continue() // ne change pas la value du text du dom
				);

				valueChange.set(rf); // la fct retour la valeur a changer
			});

			elem.textContent().set(valueChange);
		});
		/*******************************************************/
	}
	
	/*******************************************************/
	@xStatic(autoCall = true)   //TODO 2 true dans la meme classe ne marche pas
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
					JSNodeInputElement inputelem = let(JSNodeInputElement.class, "inputelem",
							event.target());
					XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", inputelem.attr(JSNodeTemplate.ATTR_BIND_INFO));
					_if(ddi, "!=null &&", ddi.row().notEqualsJS(null)).then(() -> {
						_if(inputelem.type().equalsJS("checkbox"), "||", inputelem.type().equalsJS("radio")).then(() -> {
							ddi.row().attrByString(ddi.attr()).set(inputelem.checked());
						})._else(()->{
							ddi.row().attrByString(ddi.attr()).set(inputelem.value());
						});
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
