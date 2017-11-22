/**
 * 
 */
package com.elisaxui.xui.core.widget.activity;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JActivity extends JSClass {

	JSString id();
	JSString type();
	JSArray events();
	JSArray children();
	
	JSBool active();
}
