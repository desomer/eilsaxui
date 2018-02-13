/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSONType;

/**
 * @author gauth
 *
 */
public interface DtoUser extends JSONType {

	JSString firstName();
	JSString lastName();
	
}
