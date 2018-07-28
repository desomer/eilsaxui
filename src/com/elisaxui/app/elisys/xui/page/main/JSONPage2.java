/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import static com.elisaxui.component.widget.activity.TKActivity.ON_ACTIVITY_CREATE;

import com.elisaxui.component.widget.activity.JActivity;
import com.elisaxui.component.widget.activity.JSONActivity;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;

/**********************************************************************************/
class JSONPage2 extends JSONActivity
{
	
	public static final String EVT_MORE = "more";
	public static final String EVT_SEARCH = "search";
	public static final String EVT_BACK = "back";
	public static final String EVT_BTN_FLOAT = "BtnFloatMain";
	public static final String EVT_HEADER_SWIPE_DOWN =  "HeaderSwipeDown";
			
	public JActivity getJSON()
	{
		/*TODO  a documenter et retier _setContent */
		JActivity activity = JSContent.declareType(JActivity.class, "activity"); 
		
		activity._setContent(
		  activity( "Activity2", arr( 
					factory("#NavBarActivity2", FACTORY_NAVBAR, arr( 
																 backgroundImage(ScnRoot.listPhotos[5], 0.3),  
																 btnBurger(), 
																 title("Liste exercice"),
																 btnActionIcon("arrow_back", EVT_BACK),
																 btnActionIcon("search", EVT_SEARCH)
																) )
				 ,  factory("#Activity2 .cArticle", FACTORY_CONTAINER, arr(
						 										card( arr( backgroundImage(ScnRoot.listPhotos[1], 1),  
						 													text("Un café")
						 												)),
						 										card( arr( backgroundImage(ScnRoot.listPhotos[2], 1),  
					 													text("Un disque dur")
					 												)),
						 										card( arr( backgroundImage(ScnRoot.listPhotos[0], 1),  
					 													text("Un dessin")
					 												))
						 										) )
				)
					/// les event
				, obj( 
						v(EVT_SEARCH , routeTo( "!route/Activity3?p=12")),
						v(EVT_HEADER_SWIPE_DOWN , goBack()),
						v(EVT_BACK , goBack()),
						v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivityDown", "#NavBarActivity2"))
						)));
		
		return activity;
	}
	
}