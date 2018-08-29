/**
 * 
 */
package com.elisaxui.component.toolkit.core;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSCallBack;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSWindow;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSFloat;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 *	gerer une queue de promise
 *
 *		animation1 = aDiv.animate([{ opacity: 1 }, { opacity: 0 }], 1000)
 *
 *		queue.add( animation1.promiseFinish)
 *      queue.wait(100) 
 *      queue.add( animation2.promiseFinish)
 */
@xCoreVersion("1")
public interface JSRequestAnimationFrame extends JSClass {

	JSCallBack tick = JSClass.declareType();
	JSInt now = JSClass.declareType();
	JSInt next = JSClass.declareType();
	
	TKPubSub callBackTick();
	JSRequestAnimationFrame that= JSClass.declareType();
	
	@xStatic(autoCall = true) // appel automatique de la methode static
	default void main() {
		callBackTick().set(newJS(TKPubSub.class));
		
		let(next, 0);
		let(that, _this());

		let(tick, fct(now, () -> {
			_if(now.substact(next), ">", 1000/60).then(() -> {
				next.set(now);
				//consoleDebug("'qf'", now);

				TAnimFrameEvent event = newJS(TAnimFrameEvent.class);
				event.time().set(now);
				
				that.callBackTick().publish(event);
			});
			JSWindow.window().requestAnimationFrame(tick);
		}));

		// lance le deamon
		JSWindow.window().requestAnimationFrame(tick);
	}

	
	@xStatic() // appel automatique de la methode static
	default void addListener(Object callback) {
		callBackTick().subscribe(callback);
	}
	
	public interface TAnimFrameEvent extends JSType {
		JSFloat time();
	}
}
