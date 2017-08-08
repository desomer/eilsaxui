/**
 * 
 */
package com.elisaxui.xui.core.toolkit.json;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.xui.core.toolkit.TKRouter;

/**
 * @author Bureau
 *
 */
public class JXui extends JSClassMethod {
	@xComment("_$xui.tkrouter")    ////////
	static TKRouter _tkrouter;
	
	public static final JXui $xui()
	{
		JXui ret = new JXui();
		ret.addContent("$xui");
		return ret;
	}
	
	public TKRouter tkrouter()
	{
		attr("tkrouter");
		return _tkrouter;
	}
	
}
