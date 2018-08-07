/**
 * 
 */
package com.elisaxui.component.config;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;

/**
 * @author Bureau
 *
 */
@Deprecated
public interface TKCoreConfig extends JSClass {

	public static final boolean debugDoEvent = true;
	public static final boolean debugDoAction = true;
	public static final boolean debugPushState = true;
	public static final boolean debugDoNavigate = true;
	public static final boolean debugAsyncResource = true;
	
}
