/**
 * 
 */
package com.elisaxui.app.elisys.xui.js;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;

/**
 * @author gauth
 *
 */
public interface JPhrase extends JSClass {
	
	JSArray mots();
	JSString text();
	JSString type();
}
