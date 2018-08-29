/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.widget.input.ViewCheckRadio;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.XHTMLPartJS;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.mount.MountFactory;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class MntInput extends XHTMLPartJS implements IJSDataDriven, IJSDataBinding {

	public static class MountInputText extends MountFactory {
	}

	@xMount(MountInputText.class)
	public XMLElement createMountInputText(TInput input) {
		return 	xElem(new ViewInputText() // template static ou dynamic
						.vProp(ViewInputText.pLabel, vChangeable(input.label()))
						.vProp(ViewInputText.pValue, vBindable(input, input.value())));
	}

	public static class MountInputCheckBox extends MountFactory {
	}

	@xMount(MountInputCheckBox.class)
	public XMLElement createMountInputCheckBox(TInput input) {
		return 	xElem(new ViewCheckRadio() // template static ou dynamic
						.vProp(ViewCheckRadio.pLabel, vChangeable(input.label()))
						.vProp(ViewCheckRadio.pValue, vIfOnce(vBindable(input, input.value()), xAttr("checked"))));
	}

	/********************************************************/
	// les dto
	/********************************************************/
	public interface TInput extends JSType {
		JSString label();
		JSString implement();
		JSAny value();
	}



}
