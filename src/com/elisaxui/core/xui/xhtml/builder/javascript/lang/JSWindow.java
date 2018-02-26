/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

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
		return call("requestAnimationFrame", value);
	}
	
	
}
