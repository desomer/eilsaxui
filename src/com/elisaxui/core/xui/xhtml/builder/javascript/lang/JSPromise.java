/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;

/**
 * @author gauth
 *
 */
public class JSPromise extends JSAny  implements IJSClassInterface {

	public JSPromise then(JSFunction lamba)
	{
		return callMth("then", lamba);
	}
	
}
