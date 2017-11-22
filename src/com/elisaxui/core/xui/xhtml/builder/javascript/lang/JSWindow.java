/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;

/**
 * @author gauth
 *
 */
public class JSWindow extends JSClassMethod {

	
	public static final JSWindow window()
	{
		JSWindow ret = new JSWindow();
		ret.addContent("window");
		return ret;
	}
	
	public JSWindow requestAnimationFrame(Object value)
	{
		return callMth("requestAnimationFrame", value);
	}
	
	
}
