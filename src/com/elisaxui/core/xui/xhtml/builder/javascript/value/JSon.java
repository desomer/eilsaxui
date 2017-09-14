package com.elisaxui.core.xui.xhtml.builder.javascript.value;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;

public class JSon extends JSClassMethod {
	
     public JSon get(Object attr)
     {
    	 return attr(""+attr);
     }
}
