package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassInterface;

public class JSon extends JSClassInterface {
	
     public JSon get(Object attr)
     {
    	 return attr(""+attr);
     }
     
}
