/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.es6;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

/**
 * @author gauth
 *
 */
public class JSPromise extends JSAny  implements IJSClassInterface {

	@Override
	public String zzGetJSClassType() {
		return "Promise";
	}
	
	
	public JSPromise then(JSFunction lamba)
	{
		return callMth("then", lamba);
	}
	
}
