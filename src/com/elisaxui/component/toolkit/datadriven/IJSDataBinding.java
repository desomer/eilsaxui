/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSNodeTemplate;
import com.elisaxui.core.xui.xml.annotation.xInLine;

/**
 * @author gauth
 *
 */
public interface IJSDataBinding extends JSClass {

	@xInLine
	default JSFunction vChangeable(JSAny value) {
		String[] attr = value.toString().split("\\.");
		JSNodeElement domItem = declareType(JSNodeElement.class, "domItem");
		return fct(domItem,
				() -> _return(
						callStatic(JSDataBinding.class).initVChangeableText(domItem, value, JSString.value(attr[1]))))
								.zzSetComment("vChangeable(" + value + ")");
	}

	@xInLine
	default JSFunction vChangeable(JSElement row, JSAny value, JSFunction fctOnChange) {
		String[] attr = value.toString().split("\\.");
		JSNodeElement domItem = declareType(JSNodeElement.class, "domItem");
		fctOnChange.zzSetComment(""); // pas de commentaire
		
		return fct(domItem,
				() -> _return(
						callStatic(JSDataBinding.class).initVChangeableFct(domItem, row, value, JSString.value(attr[1]), cast(JSCallBack.class ,fctOnChange))))
								.zzSetComment("vChangeable(" + row + "," + value + ", ()->...)");
	}

	@xInLine
	default JSFunction vOnDataChange(JSElement row, JSAny value, JSFunction fctOnChange) {
		JSNodeElement domItem = declareType(JSNodeElement.class, "domItem");
		String[] attr = value.toString().split("\\.");
		fctOnChange.zzSetComment(""); // pas de commentaire
		
		return fct(domItem,
				() ->
						callStatic(JSDataBinding.class).initOnDataChange(domItem, row, value, JSString.value(attr[1]), cast(JSCallBack.class ,fctOnChange)))
								.zzSetComment("vOnDataChange(" + row + "," + value + ", ()->...)");
	}
	
	@xInLine
	default JSFunction vBindable(JSElement row, JSAny value) {
		String[] attr = value.toString().split("\\.");
		JSNodeElement domItem = declareType(JSNodeElement.class, "domItem");
		return fct(domItem,
				() -> _return(JSNodeTemplate.MTH_ADD_DATA_BINDING, "(", domItem, ",", row, ",'", attr[1], "',", value,
						")")).zzSetComment("vBindable(" + value + ")");
	}

	@xInLine
	default Object vIf(JSFunction bind, Object elem) {
		JSNodeElement domItem = declareType(JSNodeElement.class, "domItem");
		JSCallBack f = declareType(JSCallBack.class, "" + bind);
		return fct(domItem, () -> _if(f.call(_this(), domItem)).then(() -> _return(elem))).zzSetComment("vIf(...)");
	}

}
