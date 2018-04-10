/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom;

import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;

/**
 * @author gauth
 *
 */
public interface JSEvent extends JSType {
    JSNodeElement target();
    JSString type();
}
