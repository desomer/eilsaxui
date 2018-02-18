/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import com.elisaxui.component.widget.activity.JActivity;
import com.elisaxui.component.widget.activity.JSONActivity;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;

/**
 * @author gauth
 *
 */
public class JSONPageHome extends JSONActivity {

	public static final String EVT_BTN_FLOAT = "BtnFloatMain";
	
	public JActivity getJSON() {
		JActivity activity = JSContent.declareType(JActivity.class, "activity"); 
		activity._setContent( activity( "Activity1", arr(    //TODO ActivityHome
				
				factory("#NavBarActivity1", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Elisys Memo")
						)), 
				factory("#Activity1 .content", FACTORY_CONTAINER, arr( 
									floatAction(null,null)))		
					
				), obj(
						v(EVT_BTN_FLOAT , routeTo( "!route/ActivityDetail")))));
		
		return activity;
	}

}
