/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.xui.core.widget.container.JSONPage;

/**
 * @author gauth
 *
 */
public class JSONPageDetail extends JSONPage {

	@Override
	public Object getJSON() {
		return page( "ActivityDetail", arr(
				
				factory("#NavBarActivityDetail", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Liste")
						) )
				
				), obj());
	}

}
