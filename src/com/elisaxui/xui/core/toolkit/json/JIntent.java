/**
 * 
 */
package com.elisaxui.xui.core.toolkit.json;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;

/**
 * @author gauth
 *
 */
public interface JIntent extends JSClass {
	
	JSString url();
	JSString action();
	JActivity activity();
}
