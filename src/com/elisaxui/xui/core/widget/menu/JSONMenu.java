/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.data.json.JSONBuilder;

public class JSONMenu extends JSONBuilder
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