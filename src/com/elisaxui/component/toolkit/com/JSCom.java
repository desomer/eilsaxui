/**
 * 
 */
package com.elisaxui.component.toolkit.com;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;

/**
 * @author gauth
 *
 */
public class JSCom extends JSAny {

	
	public static final TKCom xuiCom()
	{
		return JSContent.declareType(TKCom.class, "TKCom");
	}
	
}
