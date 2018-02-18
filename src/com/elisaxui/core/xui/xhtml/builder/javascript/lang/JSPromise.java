/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSLambda;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;

/**
 * @author gauth
 *
 */
public class JSPromise extends JSClassInterface {

	public JSPromise then(JSFunction lamba)
	{
		return (JSPromise) _callMethod(new JSPromise(), "then", lamba);
	}
	
}
