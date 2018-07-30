/**
 * 
 */
package com.elisaxui.component.toolkit.old;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.component.widget.activity.TKRouterEvent;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xml.annotation.xComment;

/**
 * @author Bureau
 *
 */
@Deprecated
public class JXui extends JSAny  implements IJSClassInterface {
	@xComment("_$xui.tkrouter")    //   _ retire le this de  this.$ui  => donne juste $xui.
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
