/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.XHTMLPartMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.mount.MountFactory;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public class MntInput extends XHTMLPartMount implements IJSDataDriven, IJSDataBinding {

	public static class MountInputText extends MountFactory {
	}

	@xMount(MountInputText.class)
	public XMLElement createMain(TInput input) {
		return 	xElem(new ViewInputText() // template static ou dynamic
						.vProp(ViewInputText.pLabel, vChangeable(input.label()))
						.vProp(ViewInputText.pValue, vBindable(input, input.value())));
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
