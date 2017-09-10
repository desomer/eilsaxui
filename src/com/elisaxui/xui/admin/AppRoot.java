/**
 * 
 */
package com.elisaxui.xui.admin;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.admin.test.JSTestDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataCtx;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouterEvent;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.transition.TKTransition;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.chart.ViewJSChart;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.container.JSONPage;
import com.elisaxui.xui.core.widget.container.JSViewCard;
import com.elisaxui.xui.core.widget.layout.JSPageLayout;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;
import com.elisaxui.xui.elisys.widget.JSSyllabisation;
import com.elisaxui.xui.elisys.widget.ViewSyllabisation;

import static com.elisaxui.xui.core.toolkit.TKActivity.*;

/**
 * @author Bureau
 *
 */
public class AppRoot extends XHTMLPart {

	/**
	 * 
	 */
	private static final String REST_JSON_MENU_ACTIVITY1 = "/rest/json/menu/activity1";
	private static final String REST_JSON_SYLLABISATION = "/rest/json/syllabisation/pacahontas";
	static JSMenu jsMenu;
	static JSNavBar jsNavBar;
	static TKActivity tkActivity;
	static JSPageLayout jsPageLayout;
	
	
	

	public static final String[] listPhotos= new String[] {
			"https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/6337/light-coffee-pen-working.jpg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/117729/pexels-photo-117729.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/211122/pexels-photo-211122.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/210745/pexels-photo-210745.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/164482/pexels-photo-164482.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/256417/pexels-photo-256417.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/207665/pexels-photo-207665.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/272228/pexels-photo-272228.jpeg?h=350&auto=compress&cs=tinysrgb"
	}; 
	
	
	@xTarget(AFTER_CONTENT.class)
	@xRessource
	public XMLElement xImportAfter() {
		return xListElement(
				xImport(JSSyllabisation.class)   //TODO A faire marcher et retire du XUIScene
				);
	}
	
	
	@xTarget(AFTER_CONTENT.class)  // dans le header
	@xRessource
	public XMLElement xInitJS() {
		return xScriptJS(getJS())  ;
	}
	
	
	static JSSyllabisation jsSyllabe;
	
	public JSMethodInterface getJS()
	{
		return js()				
				//******************** construction du menu ****************************************************
				.var(jsMenu, _new())
				.var("jsonMenu", jsMenu.getData())
				// TODO a changer par un data sur le menu
				.set("window.jsonMainMenu", "jsonMenu")  // liste des nemu pour animation dans tkAnimation
				
				.var(jsSyllabe, _new())
				.var("jsonSyllabe", jsSyllabe.getData())
				
				/***********************************************************************************************/		

				/**************************************************************/
				.__(TKQueue.startProcessQueued( 100,  fct()
					.__(" $.getScript({url:'https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js',  cache: true})") 
	
					.__("$.getJSON('"+REST_JSON_MENU_ACTIVITY1+"').done(", fct("a").consoleDebug("'json menu'", "a")
							._for("var i = 0, l = a.length; i < l; i++")
								.__("jsonMenu.push(a[i])")
							.endfor()
							,").fail(", fct("xhr","textStatus", "error").consoleDebug("error") ,")")
						
					.var(tkActivity, "$xui.tkrouter.activityMgr")
					.__("$('.",XUIScene.scene.getId(), "').children().remove()")
					.__(tkActivity.createActivity(new JSONPage1().getJSON()))
					.__(tkActivity.prepareActivity(new JSONPage2().getJSON()))
					.__(tkActivity.prepareActivity(new JSONPage3().getJSON()))
				//	.__(tkActivity.setCurrentActivity("'Activity1'"))
					
					 , 1000,  fct()
					 .__(jsSyllabe.getMicro(JQuery.$(ViewSyllabisation.cMicro).get(0)))
					 
					.__("$.getJSON('"+REST_JSON_SYLLABISATION+"').done(", fct("a")
								.consoleDebug(txt("syllabisation"), "a")
								.var("lesmots", "a.mots")
								._for("var i = 0, l = lesmots.length; i < l; i++")
									.__("jsonSyllabe.push(lesmots[i])")
								.endfor()
							,")")
					
					
					))
				/*************************************************************/
				
				
				/**************************************************************/
				
				.set("window.onCreateActivity1", fct()
						.consoleDebug("'on Create Activity1'")
						.__(TKQueue.startProcessQueued( 100,  fct()
//							.var("jCanvasGranim", "$('#NavBarActivity1 .animatedBg')[0]")
//							.__(NavBarAnimated1)
								
//							.__("JSONEditor.defaults.options.theme = 'bootstrap3';")	
//							.__("var editor = new JSONEditor(document.getElementById(\"editor\"), { schema: {} } )")
//							.__("editor.setValue(window.jsonMainMenu)")
//							.__("editor.on('change',", fct()
//									.var("content", "editor.getValue()")
//									.consoleDebug("content")
//									.__("$.post( {"+ 
//											"  type: 'POST'," + 
//											"  url: '/rest/json/save'," + 
//											"  data: JSON.stringify(content)," + 
//											//"  success: success,\r\n" + 
//											"  dataType: 'text'," + 
//										    "  contentType: 'text/plain; charset=utf-8'"+
//											"})")
//								, ")")
							
							)
						)
				)
				
				.set("window.onResumeActivity1", fct().__("alert('ok')") 
						)
				
				/**************************************************************/
				
				.set("window.onCreateActivityDown", fct("json")
						.__(TKQueue.startProcessQueued( 100,  fct()
								.var(jsPageLayout, _new())
								.__(jsPageLayout.setEnableCloseGesture("json.param"))
							)
						)
				)
				
				/**************************************************************/
				
				.set("window.onIndentity", fct("json")
						.__(TKQueue.startProcessQueued( fct()
							    .__("window.recognition.start()")
								.__("$.notify('Micro', {globalPosition: 'bottom left', className:'success', autoHideDelay: 2000})")
							)
						)
				)
				
				/************************************************************/
				.set(jsNavBar, _new())
				.set("jsonNavBar", jsNavBar.getData("'#NavBarActivity2'"))    // bug import mth jsNavBar car pas ajouer si pas appelé
				
				;
	}
	
	/**********************************************************************/
	class JSONPage1 extends JSONPage
	{
		
		public static final String EVT_MORE = "more";
		public static final String EVT_IDENTITY = "identity";
		public static final String EVT_BTN_FLOAT = "BtnFloatMain";
		
		public Object getJSON()
		{
			XMLElement cnt1 = xDiv( ViewRippleEffect.cRippleEffect, 
							xAttr("style", "\"width: 100%; height: 30vh; background:url(" +listPhotos[6] +") center / cover\""),
							xId("test1"), 
							xAttr("data-x-action", "\""+EVT_MORE+"\"")
					 );
				 
			
			XMLElement cnt2 = xPart(new ViewJSChart(xId("test2")));	
			
			XMLElement cntSyllabique =  xPart(new ViewSyllabisation());	
			
			return page( "Activity1", arr( 
						factory("#NavBarActivity1", FACTORY_NAVBAR, arr( backgroundImage(listPhotos[4], 0.3),  
																	 btnBurger(), 
																	 title("Bonjour Elisa"),
																	 btnActionIcon("mic", EVT_IDENTITY),
																	 btnActionIcon("more_vert", EVT_MORE)
																	) )
					 ,  factory("#Activity1 .cArticle", FACTORY_CONTAINER, arr(
							 										cardHtml( cnt1 ), 
							 									//	cardHtml( cnt2 ),
							 										cardHtml( cntSyllabique ),
							 										card( arr( backgroundImage(listPhotos[7], 1),  
							 													backgroundImage(listPhotos[8], 1),  
								 												text("une double image")
								 												)),
							 										card( arr( backgroundImage(listPhotos[2], 1),  
							 												text("un disque dur")
							 												)),
							 										card( arr( backgroundImage(listPhotos[5], 1),  
								 												text("De la monnaie")
								 												))
							 										) )
					 ,  factory("#Activity1 .content", FACTORY_CONTAINER, arr( 
							 										floatAction()))
					)
						/// les event
					, obj( 
							v(EVT_MORE , routeTo( "!route/Activity2?p=1")),
							v(EVT_BTN_FLOAT , routeTo( "!route/Activity3?p=1")),
							v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivity1", null)),
							v(ON_ACTIVITY_RESUME , callbackTo("onResumeActivity1", null)),
							v(EVT_IDENTITY, callbackTo("onIndentity", null))
							));
		}
		
	}
	
	/**********************************************************************************/
	class JSONPage2 extends JSONPage
	{
		
		public static final String EVT_MORE = "more";
		public static final String EVT_SEARCH = "search";
		public static final String EVT_BACK = "back";
		public static final String EVT_BTN_FLOAT = "BtnFloatMain";
		public static final String EVT_HEADER_SWIPE_DOWN =  "HeaderSwipeDown";
				
		public Object getJSON()
		{
			
			return page( "Activity2", arr( 
						factory("#NavBarActivity2", FACTORY_NAVBAR, arr( backgroundImage(listPhotos[5], 0.3),  
																	 btnBurger(), 
																	 title("Liste exercice"),
																	 btnActionIcon("arrow_back", EVT_BACK),
																	 btnActionIcon("search", EVT_SEARCH)
																	) )
					 ,  factory("#Activity2 .cArticle", FACTORY_CONTAINER, arr(
							 										card( arr( backgroundImage(listPhotos[1], 1),  
							 													text("Un café")
							 												)),
							 										card( arr( backgroundImage(listPhotos[2], 1),  
						 													text("Un disque dur")
						 												)),
							 										card( arr( backgroundImage(listPhotos[0], 1),  
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
							));
		}
		
	}
	
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
							 										card( arr( backgroundImage(listPhotos[3], 1),  
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
	
	/**********************************************************************************************/
	
	static final String NavBarAnimated1="var granimInstance = new Granim({\n"+
			" element: jCanvasGranim,\n"+    //NavBarActivity1
			" name: \'basic-gradient\',\n"+
			" direction: \'diagonal\',\n"+
			" opacity: [1, 1, 1],\n"+
			" isPausedWhenNotInView: false,\n"+
			//" stateTransitionSpeed: 500,\n"+
			" states : {\n"+
			" \"default-state\": {\n"+
			" transitionSpeed: 2000,"+
			" gradients: [\n"+
//			    " ['#f5c7df', '#dc0172'], ['#ff79be', '#ff79be'], ['#dc0172', '#f5c7df']" +
			
//				" [\'#AA076B\', \'#61045F\'],\n"+
//				" [\'#02AAB0\', \'#00CDAC\'],\n"+
//				" [\'#DA22FF\', \'#9733EE\']\n"+

			"[\'#EB3349\', \'#F45C43\'],\n"+
			" [\'#FF8008\', \'#FFC837\'],\n"+
			" [\'#4CB8C4\', \'#3CD3AD\'],\n"+
			" [\'#24C6DC\', \'#514A9D\'],\n"+
			" [\'#FF512F\', \'#DD2476\'],\n"+
			" [\'#DA22FF\', \'#9733EE\']\n"+
			" ]\n"+
			" }\n"+
			" }\n"+
			"});"
			;
	/*
	static final String NavBarAnimated2="var granimInstance = new Granim({\n"+
			" element: \'#NavBar2\',\n"+
			" name: \'basic-gradient\',\n"+
			" direction: \'diagonal\',\n"+
			" opacity: [1, 1],\n"+
			" isPausedWhenNotInView: false,\n"+
			" states : {\n"+
			" \"default-state\": {\n"+
			" gradients: [\n"+
//			    " ['#FFF', '#000'], ['#000', '#FFF']" +
			
				" [\'#AA076B\', \'#61045F\'],\n"+
				" [\'#02AAB0\', \'#00CDAC\'],\n"+
				" [\'#DA22FF\', \'#9733EE\']\n"+

			" ]\n"+
			" }\n"+
			" }\n"+
			"});";
	*/
}
