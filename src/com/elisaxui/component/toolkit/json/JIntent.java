/**
 * 
 */
package com.elisaxui.component.toolkit.json;

import com.elisaxui.component.widget.activity.JActivity;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
public interface JIntent extends JSClass {
	
	JSString url();	
	JSString action();
	JSString activity();
	
	JSString nextActivityAnim();
	JSString prevActivity();
}
