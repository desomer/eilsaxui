/**
 * 
 */
package com.elisaxui.xui.core.toolkit.json;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.xui.core.toolkit.TKRouterEvent;

/**
 * @author Bureau
 *
 */
public class JXui extends JSClassMethod {
	@xComment("_$xui.tkrouter")    ////////
	static TKRouterEvent _tkrouter;
	
	public static final JXui $xui()
	{
		JXui ret = new JXui();
		ret.addContent("$xui");
		return ret;
	}
	
	public TKRouterEvent tkrouter()
	{
		///return attr("tkrouter");   //TODO a faire marcher
		return _tkrouter;
	}
	
}
