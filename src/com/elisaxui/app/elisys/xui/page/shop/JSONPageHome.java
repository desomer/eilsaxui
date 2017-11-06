/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.xui.core.widget.container.JSONPage;

/**
 * @author gauth
 *
 */
public class JSONPageHome extends JSONPage {

	public static final String EVT_BTN_FLOAT = "BtnFloatMain";
	
	@Override
	public Object getJSON() {
		return page( "Activity1", arr(    //TODO ActivityHome
				
				factory("#NavBarActivity1", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Elisys Memo")
						)), 
				factory("#Activity1 .content", FACTORY_CONTAINER, arr( 
									floatAction(null,null)))		
					
				), obj(
						v(EVT_BTN_FLOAT , routeTo( "!route/ActivityDetail"))));
	}

}
