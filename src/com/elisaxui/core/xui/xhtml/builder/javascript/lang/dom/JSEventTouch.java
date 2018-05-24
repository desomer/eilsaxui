/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;

/**
 * @author gauth
 *
 */
public interface JSEventTouch extends JSEvent {
		JSArray<TTouch> touches();
		JSArray<TTouch> changedTouches();
		
		public interface TTouch extends JSType {
			JSInt pageX();
			JSInt pageY();
			JSInt clientX();
			JSInt clientY();
		}
}
