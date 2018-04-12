/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder.XuiBindInfo;
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
	default void initVChangeableText(JSNodeElement domItem, JSAny value, JSString attr) {
		__(domItem, ".dataset['xui'+", attr, "]=true");
		_return(JSNodeTemplate.MTH_ADD_TEXT+"("+value+")");
	}
	
	@xStatic
	default void initVChangeableFct(JSNodeElement domItem, JSAny row, JSAny value, JSString attr, JSCallBack fctOnChange) {
		__(domItem, ".dataset['xui'+", attr, "]=true");
		__(domItem, ".XuiBindInfo={row:", row, ", attr:", attr, ", fct:", fctOnChange, "}");

		JSChangeCtx ctx = newJS(JSChangeCtx.class).asLitteral();
		ctx.value().set(value);
		ctx.row().set(row);
		JSChangeCtx changeCtx = let(JSChangeCtx.class, "changeCtx", ctx);
		__("let ret = domItem.XuiBindInfo.fct.call(",domItem,",",changeCtx,")");
		_return(JSNodeTemplate.MTH_ADD_TEXT+"(ret)");
	}
	
	@xStatic
	default void initOnDataChange(JSNodeElement domItem, JSAny row, JSAny value, JSString attr,	JSCallBack fctOnChange) {
		__(domItem, ".dataset['xui'+", attr, "]=true");
		__(domItem, ".XuiBindInfo={row:", row, ", attr:", attr, ", fct:", fctOnChange, "}");

		JSChangeCtx ctx = newJS(JSChangeCtx.class).asLitteral();
		ctx.value().set(value);
		ctx.row().set(row);
		ctx.element().set(domItem);

		JSChangeCtx changeCtx = let(JSChangeCtx.class, "changeCtx", ctx);
		__("domItem.XuiBindInfo.fct.call(", domItem, ",", changeCtx, ")");
	}

	@xStatic
	default void initChangeHandler(JSChangeCtx changeCtx, JSNodeElement aDom) {
		/*******************************************************/

		JSString sel = let(JSString.class, "sel", "'[data-xui'+", changeCtx.property(), "+']'");
		JSArray<JSNodeElement> listNode = let(JSArray.class, "listNode", "Array.from(", aDom.querySelectorAll(sel),
				")");
		_if(aDom, ".XuiBindInfo!=null")._then(() -> {
			listNode.push(aDom);
		});
		listNode.setArrayType(JSNodeElement.class);
		JSInt i = declareType(JSInt.class, "i");
		_forIdx(i, listNode)._do(() -> {
			JSNodeElement elem = let("elem", listNode.at(i));
			XuiBindInfo ddi = let(XuiBindInfo.class, "ddi", elem + ".XuiBindInfo");
			JSAny valueChange = let("valueChange", changeCtx.value());
			_if(ddi, "!=null &&", ddi.fct().notEqualsJS(null))._then(() -> {
				changeCtx.element().set(elem);
				JSAny rf = let("rf", ddi.fct().call(elem, changeCtx)); // appel de la fct vOnChange( fct )
				_if(rf.equalsJS(null))._then(() -> {
					__("continue"); // ne change pas la value du text du dom
				});

				valueChange.set(rf); // la fct retour la valeur a changer
			});

			elem.textContent().set(valueChange);
		});
		;
		/*******************************************************/
	}

}
