/**
 * 
 */
package com.elisaxui.component.widget.menu;

import com.elisaxui.core.data.JSONBuilder;

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

	/* (non-Javadoc)
	 * @see com.elisaxui.core.data.json.JSONBuilder#getJSON()
	 */
	@Override
	public Object getJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
}