/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

/**
 * @author gauth
 *
 */
public class JSDocument extends JSDomElement  implements IJSClassInterface {

	
	public static final JSDocument document()
	{
		JSDocument ret = new JSDocument();
		ret.addContent("document");
		return ret;
	}
	
	
}
