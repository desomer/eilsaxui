/**
 * 
 */
package com.elisaxui.component.widget.menu.old;

import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;

@Deprecated
public class JSONMenu implements IJSONBuilder
{
	public Object item(String name, String icon, String idAction)
	{
		return  obj( v("name", name), 
					 v("icon", icon), 
					 v("idAction", idAction));
	}
	
	public Object divider()
	{
		return  obj( v("type", "divider"));
	}
	
}