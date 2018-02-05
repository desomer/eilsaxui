/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import static com.elisaxui.component.toolkit.TKActivity.ON_ACTIVITY_CREATE;
import static com.elisaxui.component.toolkit.TKActivity.ON_ACTIVITY_RESUME;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;

import com.elisaxui.app.elisys.xui.widget.ViewSyllabisation;
import com.elisaxui.component.widget.activity.JActivity;
import com.elisaxui.component.widget.activity.JSONActivity;
import com.elisaxui.component.widget.button.ViewRippleEffect;
import com.elisaxui.component.widget.log.ViewLog;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**********************************************************************/
	public class JSONPage1 extends JSONActivity
	{
		public static final String EVT_CLEAR = "clear";
		public static final String EVT_MORE = "more";
		public static final String EVT_IDENTITY = "identity";
		public static final String EVT_BTN_FLOAT = "BtnFloatMain";
		public static final String EVT_DO_MOT = "doMot";
		public static final String EVT_DO_PHRASE = "doPhrase";
		public static final String EVT_DO_LOAD_HISTOIRE = "doLoadHistoire";
		
		public JActivity getJSON()
		{
			

			
			XMLElement cnt1 = XHTMLPart.xDiv(   ViewRippleEffect.cRippleEffect,                             //6
							XHTMLPart.xAttr("style", "\"width: 100%; height: 30vh; background:url(" +ScnRoot.listPhotos[9] +") center / cover\""),
							XHTMLPart.xId("test1"), 
							XHTMLPart.xAttr("data-x-action", "\""+EVT_DO_PHRASE+"\"")
					 );
				 
			
			//XMLElement cnt2 = xPart(new ViewJSChart(xId("test2")));	
			
			XMLElement cntSyllabique =  XHTMLPart.xPart(new ViewSyllabisation());	
			
			XMLElement cntLogWorker =  XHTMLPart.xPart(new ViewLog());	
			
			JActivity activity = declareType(JActivity.class, "activity"); 
			activity._setContent( activity( "Activity1", arr( 
						factory("#NavBarActivity1", FACTORY_NAVBAR, arr( 
																	 //backgroundGradiant(),
																	 backgroundImage(ScnRoot.listPhotos[4], 0.3),  
																	 btnBurger(), 
																	 title("Bonjour Elisa"),
																	 btnActionIcon("delete", EVT_CLEAR),
																	 btnActionIcon("mic", EVT_IDENTITY),
																	 btnActionIcon("more_vert", EVT_MORE)
																	) )
					 ,  factory("#Activity1 .cArticle", FACTORY_CONTAINER, arr(
							 										cardHtml( cnt1 ), 
							 									//	cardHtml( cnt2 ),
							 										cardHtml( cntSyllabique ),
							 										card( arr( backgroundImage(ScnRoot.listPhotos[7], 1),
							 												    cardAction(EVT_DO_LOAD_HISTOIRE), 
								 												text("Paconhontas")
								 												)) ,
							 										cardHtml( cntLogWorker )
//							 										card( arr( backgroundImage(listPhotos[2], 1),  
//					 															backgroundImage(listPhotos[8], 1), 
//							 												text("un disque dur")
//							 												)),
//							 										card( arr( backgroundImage(listPhotos[5], 1),  
//								 												text("De la monnaie")
//								 												))
							 										) )
					 ,  factory("#Activity1 .content", FACTORY_CONTAINER, arr( 
							 										floatAction(null,null)))
					)
						/// les event
					, obj( 
							v(EVT_MORE , routeTo( "!route/Activity2?p=1")),
							v(EVT_BTN_FLOAT , routeTo( "!route/Activity3?p=1")),
							v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivity1", null)),
							v(ON_ACTIVITY_RESUME , callbackTo("onResumeActivity1", null)),
							v(EVT_IDENTITY, callbackTo("onMicro", null)),
							v(EVT_CLEAR, callbackTo("onDelete", null)),
							v(EVT_DO_MOT, callbackTo("onMot", null)),
							v(EVT_DO_PHRASE, callbackTo("onPhrase", null)),
							v(EVT_DO_LOAD_HISTOIRE, callbackTo("onLoadHistoire", null))
							
							)));
			
			return activity;
		}
		
	}