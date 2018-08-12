/**
 * 
 */
package com.elisaxui.component.toolkit.datadriven;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xInLine;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface IJSMountFactory extends JSClass {
	JSDataBinding dataBinding = JSClass.declareTypeClass(JSDataBinding.class);
	JSDataDriven dataDriven = JSClass.declareTypeClass(JSDataDriven.class);
	JSNodeElement domparent = JSClass.declareType();
	JSChangeCtx ctx = JSClass.declareType();

	@xInLine
	default JSFunction vMount(JSElement aRow, JSString mountId) {
		return fct(() -> _return(dataBinding.mount(mountId, aRow))).setComment("vMount " + mountId);
	}

	@xInLine
	default JSFunction vFor(JSArray<? extends JSElement> data, JSElement aRow, XMLElement elem) {

		JSCallBack c = fct(ctx, () -> {
			dataBinding.initChangeHandler(ctx, cast(JSNodeElement.class, ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK)));
		}).setComment("").toCallBack();

		JSCallBack enter = onEnter(aRow, elem);

		return fct(domparent,
				() -> dataDriven.doTemplateDataDriven(domparent, data, enter, null, c ))
						.setComment("vFor " + data);

	}

	@xInLine
	public default JSCallBack onEnter(JSElement row, Object elem) {
		return fct(row, ctx, () -> _return(elem)).setComment("onEnter " + row).toCallBack();
	}

}
