/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import com.elisaxui.app.elisys.xui.widget.JSSyllabisation;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.widget.layout.JSPageLayout;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JSRoot extends JSClass {

	default void constructor(JSArray<?> jsonSyllabe, JSSyllabisation jsSyllabe) {

		// TODO callback("json", ()->{  
		var("window.onCreateActivity1").set(fct("json").__(()->{  

			consoleDebug("'on Create Activity1'", "json");
			__(TKQueue.startProcessQueued(100, callback(() -> {  
				//TODO retirer le __( avant  TKQueue

				JSPageLayout jsPageLayout = let("jsPageLayout", newInst(JSPageLayout.class)); 
				jsPageLayout.hideOnScroll(JSString.value("#Activity1"));
				
				// TODO a changer : mettre dans une queue avec priorité (avec image) et gestion
				// de promise d'attente
				// .__("
				// $.getScript({url:'https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js',
				// cache: true}).done("
				// , fct()
				// .var("jCanvasGranim", "$('#NavBarActivity1 .animatedBg')[0]")
				// .__(NavBarAnimated1)
				// ,")")
				
				
			}), 500, callback(() -> {
				_set("window.microlistener", jsSyllabe.createMicroListener());
			})));
		}));

		_set("window.onResumeActivity1", fct().__("alert('ok')"));

		/**************************************************************/
		// gestion du slidedown pour fermer
		_set("window.onCreateActivityDown", fct("json")
				.__(TKQueue.startProcessQueued(100, fct().__(() -> {
					JSPageLayout jsPageLayout = let( "jsPageLayout", newInst(JSPageLayout.class));
					jsPageLayout.setEnableCloseGesture(cast(JSString.class, "json.param"));
				}))));
	}

}
