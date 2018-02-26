/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;

/**
 * @author gauth
 *
 */
public class JSPromise extends JSClassInterface {

	public JSPromise then(JSFunction lamba)
	{
		return callTyped(new JSPromise(), "then", lamba);
	}
	
}
