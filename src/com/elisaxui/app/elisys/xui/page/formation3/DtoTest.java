/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation3;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.json.JSONType;

/**
 * @author gauth
 *
 */
public interface DtoTest extends JSONType {

	JSArray<DtoUser> users();
}
