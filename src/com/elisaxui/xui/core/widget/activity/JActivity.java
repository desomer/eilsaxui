/**
 * 
 */
package com.elisaxui.xui.core.widget.activity;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSBoolean;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;

/**
 * @author gauth
 *
 */
public interface JActivity extends JSClass {

	JSString id();
	JSString type();
	JSArray events();
	JSArray children();
	
	JSBoolean active();
}
