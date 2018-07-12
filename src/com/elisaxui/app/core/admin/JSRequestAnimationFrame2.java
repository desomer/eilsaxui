/**
 * 
 */
package com.elisaxui.app.core.admin;

import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xCoreVersion("1")
public interface JSRequestAnimationFrame2 extends JSClass {

	JSCallBack tick = JSClass.declareType();
	JSCallBack tick2 = JSClass.declareType();
	JSInt now = JSClass.declareType();
	JSInt next = JSClass.declareType();
	JSInt next2 = JSClass.declareType();
	JSNodeElement document = JSClass.declareType();

	@xStatic(autoCall = true) // appel automatique de la methode static
	default void main() {
		
		let(next, 0);

		let(tick, fct(now, () -> {
			_if(now.substact(next), ">1000").then(() -> {
				next.set(now);
				document.querySelector("header").firstNodeValue().set(now);
			});
			JSWindow.window().requestAnimationFrame(tick);
		}));

		JSWindow.window().requestAnimationFrame(tick);

		
		let(next2, "Date.now()");
		let(tick2, fct(now, () -> {
			document.querySelector("footer").firstNodeValue().set("Date.now()-next2");
			setTimeout(tick2, 1000);
		}));

		setTimeout(tick2, 1000);

	}

}
