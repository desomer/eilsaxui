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
		return page( "Activity1", arr(
				
				factory("#NavBarActivity1", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Elisys Memo")
						) )
				
				), obj());
	}

}
