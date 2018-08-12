/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xInLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface IJSDataBinding extends JSClass {

	JSNodeElement domItem = JSClass.declareType();
	JSDataBinding jsDataBinding = JSClass.declareTypeClass(JSDataBinding.class);

	/**
	 * xDiv(vChangeable(item.value())) different de : xDiv(item.value())
	 * 
	 * @param value
	 * @return
	 */
	@xInLine
	default JSFunction vChangeable(JSAny value) {
		String[] attr = value.toString().split("\\.");

		return fct(domItem, () -> {
			_return(jsDataBinding.initVChangeableText(domItem, value, JSString.value(attr[1])));
		}).setComment("vChangeable(" + value + ")");
	}

	/**
	 * xSpan(vChangeable(item, item.value(), fctReverse))
	 * 
	 * @param row
	 * @param value
	 * @param fctOnChange
	 * @return
	 */
	@xInLine
	default JSFunction vChangeable(JSElement row, JSAny value, JSFunction fctOnChange) {
		String[] attr = value.toString().split("\\.");
		JSCallBack cbOnChange = cast(JSCallBack.class, fctOnChange.setComment("")); // pas de commentaire

		return fct(domItem, () -> _return(jsDataBinding.initVChangeableFct(domItem, row, value,
				JSString.value(attr[1]), cbOnChange)))
						.setComment("vChangeable(" + row + "," + value + ", ()->...)");
	}

	/**
	 * 
	 * xDiv("Plus de 10 char", vOnDataChange(item, item.value(), fct(changeCtx, ()
	 * -> _if(changeCtx.value().toStringJS().length(), ">10") .then(() ->
	 * changeCtx.element().classList().add(cSizeOk)) ._else(() ->
	 * changeCtx.element().classList().remove(cSizeOk))))),
	 * 
	 * @param row
	 * @param value
	 * @param fctOnChange
	 * @return
	 */
	@xInLine
	default JSFunction vOnDataChange(JSElement row, JSAny value, JSFunction fctOnChange) {
		String[] attr = value.toString().split("\\.");
		JSCallBack cbOnChange = cast(JSCallBack.class, fctOnChange.setComment("")); // pas de commentaire

		return fct(domItem, () -> jsDataBinding.initOnDataChange(domItem, row, value, JSString.value(attr[1]), cbOnChange))
				.setComment("vOnDataChange(" + row + "," + value + ", ()->...)");
	}

	/**
	 * donne au dom l'attribut json a changer
	 * 
	 * xInput(xAttr("value", vBindable(item, item.value()))))
	 * 
	 * xInput(vIfOnce(vBindable(item, item.singleFile()), xAttr("checked")))
	 * 
	 * @param row
	 * @param value
	 * @return
	 */
	@xInLine
	default JSFunction vBindable(JSElement row, JSAny value) {
		String[] attr = value.toString().split("\\.");
		JSCallBack dbb = cast(JSCallBack.class, JSDomBuilder.MTH_ADD_DATA_BINDING);
		return fct(domItem, () -> _return(dbb.invoke(domItem, row, JSString.value(attr[1]), value)))
				.setComment("vBindable(" + value + ")");
	}

	/**
	 * ajoute l'elem si la fct (souvent vBindable) return true
	 * 
	 * @param bindFct
	 * @param elem
	 * @return
	 */
	@xInLine
	default Object vIfOnce(JSFunction bindFct, Object elem) {
		JSCallBack f = cast(JSCallBack.class, "" + bindFct);
		return fct(domItem, () -> _if(f.call(_this(), domItem)).then(() -> _return(elem))).setComment("vIf(...)");
	}

}
