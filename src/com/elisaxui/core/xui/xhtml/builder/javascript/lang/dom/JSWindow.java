/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;

/**
 * @author gauth
 *
 */
public class JSWindow extends JSAny  implements IJSClassInterface {

	
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
	
	public JSInt pageYOffset()
	{
		return castAttr(new JSInt(), "pageYOffset");
	}
	
	public JSInt innerHeight()
	{
		return castAttr(new JSInt(), "innerHeight");
	}
	
	public JSWindow scrollTo(Object... value)
	{
		return callMth("scrollTo", value);
	}
	
}
