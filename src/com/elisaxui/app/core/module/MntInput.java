/**
 * 
 */
package com.elisaxui.app.core.module;

import com.elisaxui.component.toolkit.datadriven.IJSDataBinding;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.widget.input.ViewCheckRadio;
import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.component.widget.menu.ViewMenuDivider;
import com.elisaxui.component.widget.menu.ViewMenuItems;
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

	public static class MountMenuItem extends MountFactory {
	}
	@xMount(MountMenuItem.class)
	public XMLElement createMountMenuItem(TInput input) {
		return 	xElem(new ViewMenuItems() // template static ou dynamic
						.vProp(ViewMenuItems.PROPERTY_NAME, vChangeable(input.label()))
						.vProp(ViewMenuItems.PROPERTY_ICON, input.iconId())
						.vProp(ViewMenuItems.PROPERTY_ACTION, input.actionId())
						);
	}
	
	public static class MountMenuDivider extends MountFactory {
	}
	@xMount(MountMenuDivider.class)
	public XMLElement createMountMenuDivider(TInput input) {
		return 	xElem(new ViewMenuDivider() );
	}
	/********************************************************/
	// les dto
	/********************************************************/
	public interface TInput extends JSType {
		JSString label();
		JSString implement();
		JSAny value();
		JSString iconId();
		JSString actionId();
	}



}
