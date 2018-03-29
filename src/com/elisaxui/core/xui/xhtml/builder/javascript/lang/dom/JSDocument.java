/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;

/**
 * @author gauth
 *
 */
public class JSDocument extends JSNodeElement  implements IJSClassInterface {

	
	public static final JSDocument document()
	{
		JSDocument ret = new JSDocument();
		ret.addContent("document");
		return ret;
	}
	
	
}
