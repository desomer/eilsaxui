/**
 * 
 */
package com.elisaxui.component.toolkit.old;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
@Deprecated
public interface JIntent extends JSClass {
	
	JSString url();	
	JSString action();
	JSString activity();
	
	JSString nextActivityAnim();
	JSString prevActivity();
}
