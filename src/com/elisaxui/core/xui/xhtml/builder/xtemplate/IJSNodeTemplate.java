/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.xtemplate;

import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xml.annotation.xInLine;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IJSNodeTemplate extends JSClass, IXHTMLBuilder {

	default JSNodeTemplate createNodeTemplate(XMLElement xmlElement) {
		return new JSNodeTemplate(xmlElement).setModeJS(true);
	}

	@xInLine
	default JSFunction vBindable(JSElement row, JSAny value) {
		String[] attr = value.toString().split("\\.");
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		return fct(domItem,
				() -> _return(JSNodeTemplate.MTH_ADD_DATA_BINDING, "(", domItem, ",", row, ",'", attr[1], "',", value,
						")")).zzSetComment("vBindable(" + value + ")");
	}

	@xInLine
	default Object vIf(JSFunction bind, Object elem) {
		JSNodeElement domItem = JSContent.declareType(JSNodeElement.class, "domItem");
		JSCallBack f = JSContent.declareType(JSCallBack.class, "" + bind);
		return fct(domItem, () -> _if(f.call(_this(), domItem)).then(() -> _return(elem))).zzSetComment("vIf(...)");
	}

}
