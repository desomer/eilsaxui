/**
 * 
 */
package com.elisaxui.component.widget.menu;

import com.elisaxui.core.xui.xhtml.builder.json.JSONBuilder;

public class JSONMenu implements JSONBuilder
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