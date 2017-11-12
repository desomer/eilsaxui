/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;
import static com.elisaxui.xui.core.toolkit.TKActivity.ON_ACTIVITY_CREATE;

import com.elisaxui.xui.core.widget.activity.JActivity;
import com.elisaxui.xui.core.widget.activity.JSONActivity;

/**********************************************************************************/
class JSONPage3 extends JSONActivity
{
	
	public static final String EVT_MORE = "more";
	public static final String EVT_SEARCH = "search";
	public static final String EVT_BACK = "back";
	public static final String EVT_BTN_FLOAT = "BtnFloatMain";
	public static final String EVT_HEADER_SWIPE_DOWN =  "HeaderSwipeDown";
			
	public JActivity getJSON()
	{
		JActivity activity = declareType(JActivity.class, "activity"); 
		activity._setContent(activity( "Activity3", arr( 
					factory("#NavBarActivity3", FACTORY_NAVBAR, arr(  
																 btnBurger(), 
																 title("Recherche"),
																 btnActionIcon("arrow_back", EVT_BACK),
																 btnActionIcon("search", EVT_SEARCH)
																) )
				 ,  factory("#Activity3 .cArticle", FACTORY_CONTAINER, arr(
						 										card( arr( backgroundImage(ScnRoot.listPhotos[3], 1),  
						 													text("En construction")
						 												))
						 										) )
				 
				 ,  factory("#Activity3 .content", FACTORY_CONTAINER, arr( 
							floatAction(null,null)))
				)
					/// les event
				, obj( 
						v(EVT_SEARCH , routeTo( "!route/Activity1")),
						v(EVT_HEADER_SWIPE_DOWN , goBack()),
						v(EVT_BACK , goBack()),
						v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivityDown", "#NavBarActivity3")),
						v(EVT_BTN_FLOAT , routeTo( "!route/Activity2?p=ert"))
						)));
		
		return activity;
	}
	
}