/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassInterface;

/**
 * @author gauth
 *
 */
public class JSWindow extends JSClassInterface {

	
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
