/**
 * 
 */
package com.elisaxui.component.widget.overlay;

import static com.elisaxui.component.toolkit.JQuery.*;
import static com.elisaxui.component.transition.CssTransition.*;
import static com.elisaxui.component.widget.overlay.ViewOverlay.*;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;

/**
 * @author Bureau
 *
 */
public interface JSOverlay extends JSClass {

	JSInt speed();

	JSFloat opacity();

	default void constructor(JSInt speed, JSFloat opacity) {
		speed().set(speed);
		opacity().set(opacity);
	}

	default void doShowOverlay(JQuery act, Object phase) {

		JQuery overlay = let(JQuery.class, "overlay", act.find(cBlackOverlay));
		_if(phase, "==1")._then(() -> {
			overlay.css("display", "block");
			overlay.css("opacity", 0);
		})._elseif(phase, "==2")._then(() -> {
			overlay.css("transition", txt("opacity ", speed(), "ms linear"));
			overlay.css("opacity", opacity());
		});
	}

	default void doHideOverlay(Object phase) {

		JQuery overlay = let(JQuery.class, "overlay", $(active.descendant(cBlackOverlay)));

		_if(phase, "==1")._then(() -> {
			overlay.css("opacity", 0);
		})._elseif(phase, "==2")._then(() -> {
			overlay.css("display", "none");
			overlay.css("transition", "");
		});
	}

}
