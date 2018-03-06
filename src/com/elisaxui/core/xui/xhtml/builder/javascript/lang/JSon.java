package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

public class JSon extends JSAny  implements IJSClassInterface {
	
     
	public JSBool hasOwnProperty(Object attr)
	{
		return callTyped(new JSBool(), "hasOwnProperty", attr);
	}
	
}
