/**
 * 
 */
package com.elisaxui.component.widget.overlay;

import static com.elisaxui.component.widget.overlay.ViewOverlay.cBlackOverlay;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
@xExport
public interface JSOverlay extends JSClass {

	JSInt speed();
	JSFloat opacity();
	JSNodeElement anOverlay = JSClass.declareType();

	default void constructor(JSInt speed, JSFloat opacity) {
		speed().set(speed);
		opacity().set(opacity);
	}

	default void doShowOverlay(JSNodeElement act1, JSInt phase) {

		let(anOverlay, act1.querySelector(cBlackOverlay));
		anOverlay.style().attr("display").set(txt("block"));
		anOverlay.style().attr("opacity").set(opacity());
		
//		JQuery overlay = let(JQuery.class, "overlay", act.find(cBlackOverlay));
//		_if(phase.equalsJS(1)).then(() -> {
//			overlay.css("display", "block");
//			overlay.css("opacity", 0);
//		})._elseif(phase.equalsJS(2)).then(() -> {
//			overlay.css("transition", txt("opacity ", speed(), "ms linear"));
//			overlay.css("opacity", opacity());
//		});
	}
	
}
