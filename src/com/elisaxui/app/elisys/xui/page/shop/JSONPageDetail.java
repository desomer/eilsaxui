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
public class JSONPageDetail extends JSONActivity {

	public JActivity getJSON() {
		JActivity activity = JSContent.declareType(JActivity.class, "activity"); 
		activity._setContent(activity( "ActivityDetail", arr(
				
				factory("#NavBarActivityDetail", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Liste")
						) )
				
				), obj()));
		
		return activity;
	}

}
