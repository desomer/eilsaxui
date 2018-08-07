/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface JSRequestAnimationFrame extends JSClass {

	JSCallBack tick = JSClass.declareType();
	JSInt now = JSClass.declareType();
	JSInt next = JSClass.declareType();

	@xStatic(autoCall = true) // appel automatique de la methode static
	default void main() {
		
		let(next, 0);

		let(tick, fct(now, () -> {
			_if(now.substact(next), ">5000").then(() -> {
				next.set(now);
				consoleDebug("'qf'", now);
			});
			JSWindow.window().requestAnimationFrame(tick);
		}));

		// lance le deamon
		JSWindow.window().requestAnimationFrame(tick);
	}

}
