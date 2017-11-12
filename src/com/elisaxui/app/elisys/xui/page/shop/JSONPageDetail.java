/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.shop;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import com.elisaxui.xui.core.widget.activity.JActivity;
import com.elisaxui.xui.core.widget.activity.JSONActivity;

/**
 * @author gauth
 *
 */
public class JSONPageDetail extends JSONActivity {

	@Override
	public JActivity getJSON() {
		JActivity activity = declareType(JActivity.class, "activity"); 
		activity._setContent(activity( "ActivityDetail", arr(
				
				factory("#NavBarActivityDetail", FACTORY_NAVBAR, arr(  // backgroundImage(ScnRoot.listPhotos[4], 0.3),  
						 btnBurger(), 
						 title("Liste")
						) )
				
				), obj()));
		
		return activity;
	}

}
