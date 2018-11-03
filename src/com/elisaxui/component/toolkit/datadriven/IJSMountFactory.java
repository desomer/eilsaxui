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
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
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
	JSChangeCtx ctxChange = JSClass.declareType();

	@xInLine
	default JSFunction vMountChangeable(JSElement data, JSFunction fct) {
		String[] attr = data.toString().split("\\.");
		
		return fct(domparent, () -> {
			dataBinding.initOnDataChange(domparent, cast(JSon.class, attr[0]), cast(JSon.class, data), JSString.value(attr[1]), fct(ctxChange, ()->{
				consoleDebug("'vMountChangeable '",data, ctxChange);	
				consoleDebug("'a faire marcher : vider le dom et rappeler la fct'");
			}).toCallBack());
			_return(fct);
		}).setComment("vMountChangeable " + data);
	}

	@xInLine
	default JSFunction vMount(JSElement aRow, JSString mountId) {
		return fct(domparent, () -> _return(dataBinding.mount(mountId, aRow))).setComment("vMount " + mountId);
	}

	/** TODO vFor( control type , ()->{}) */
	@xInLine
	default JSFunction vFor(JSArray<? extends JSElement> data, JSElement aRow, XMLElement elem) {

		JSCallBack change = onChange(aRow, elem);
		JSCallBack enter = onEnter(aRow, elem);

		return fct(domparent,
				() -> dataDriven.doTemplateDataDriven(domparent, data, enter, null, change))
						.setComment("vFor " + data);

	}

	@xInLine
	public default JSCallBack onEnter(JSElement aRow, Object elem) {
		return fct(aRow, ctxChange, () -> _return(elem)).setComment("onEnter " + aRow).toCallBack();
	}

	@xInLine
	public default JSCallBack onChange(JSElement aRow, Object elem) {
		return fct(ctxChange, () -> {
			dataBinding.initChangeHandler(ctxChange, cast(JSNodeElement.class, ctxChange.row().attrByStr(JSDataSet.ATTR_DOM_LINK)));
		}).setComment("onChange " + aRow).toCallBack();
	}

}
