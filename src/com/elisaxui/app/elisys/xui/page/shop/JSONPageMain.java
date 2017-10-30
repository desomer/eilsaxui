/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.xui.core.widget.container.JSONPage;

/**
 * @author gauth
 *
 */
public class JSONPageMain extends JSONPage {

	@Override
	public Object getJSON() {
		return page( "Activity1", arr(), obj());
	}

}
