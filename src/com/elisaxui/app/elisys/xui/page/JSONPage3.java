/**
 * 
 */
package com.elisaxui.app.elisys.xui.page;

import static com.elisaxui.xui.core.toolkit.TKActivity.ON_ACTIVITY_CREATE;

import com.elisaxui.xui.core.widget.container.JSONPage;

/**********************************************************************************/
class JSONPage3 extends JSONPage
{
	
	public static final String EVT_MORE = "more";
	public static final String EVT_SEARCH = "search";
	public static final String EVT_BACK = "back";
	public static final String EVT_BTN_FLOAT = "BtnFloatMain";
	public static final String EVT_HEADER_SWIPE_DOWN =  "HeaderSwipeDown";
			
	public Object getJSON()
	{
		
		return page( "Activity3", arr( 
					factory("#NavBarActivity3", FACTORY_NAVBAR, arr(  
																 btnBurger(), 
																 title("Recherche"),
																 btnActionIcon("arrow_back", EVT_BACK),
																 btnActionIcon("search", EVT_SEARCH)
																) )
				 ,  factory("#Activity3 .cArticle", FACTORY_CONTAINER, arr(
						 										card( arr( backgroundImage(AppRoot.listPhotos[3], 1),  
						 													text("En construction")
						 												))
						 										) )
				 
				 ,  factory("#Activity3 .content", FACTORY_CONTAINER, arr( 
							floatAction()))
				)
					/// les event
				, obj( 
						v(EVT_SEARCH , routeTo( "!route/Activity1")),
						v(EVT_HEADER_SWIPE_DOWN , goBack()),
						v(EVT_BACK , goBack()),
						v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivityDown", "#NavBarActivity3")),
						v(EVT_BTN_FLOAT , routeTo( "!route/Activity2?p=ert"))
						));
	}
	
}