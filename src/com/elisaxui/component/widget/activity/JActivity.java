/**
 * 
 */
package com.elisaxui.component.widget.activity;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSBool;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * @author gauth
 *
 */
@Deprecated
public interface JActivity extends JSClass {

	JSString id();
	JSString type();
	JSArray events();
	JSArray children();
	
	JSBool active();
}
