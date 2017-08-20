/**
 * 
 */
package com.elisaxui.xui.admin;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.chart.ViewJSChart;
import com.elisaxui.xui.core.widget.container.JSONPage;
import com.elisaxui.xui.core.widget.layout.JSPageLayout;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;
import static com.elisaxui.xui.core.toolkit.TKActivity.*;

/**
 * @author Bureau
 *
 */
public class AppRoot extends XHTMLPart {

	static JSMenu jsMenu;
	static JSNavBar jsNavBar;
	static TKActivity tkActivity;
	static JSPageLayout jsPageLayout;
	
	public static final String IMAGE1 = "https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static final String IMAGE2 = "https://images.pexels.com/photos/6337/light-coffee-pen-working.jpg?h=350&auto=compress&cs=tinysrgb";
	public static final String IMAGE3 = "https://images.pexels.com/photos/117729/pexels-photo-117729.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static final String IMAGE4 = "https://images.pexels.com/photos/211122/pexels-photo-211122.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static final String IMAGE5 = "https://images.pexels.com/photos/210745/pexels-photo-210745.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static final String IMAGE6 = "https://images.pexels.com/photos/164482/pexels-photo-164482.jpeg?h=350&auto=compress&cs=tinysrgb";
	
	@xTarget(AFTER_CONTENT.class)  // dans le header
	@xRessource
	public XMLElement xInitJS() {
		return xScriptJS(getJS())  ;
	}
	
	
	
	public JSMethodInterface getJS()
	{
		return js()
		
				
				//******************** construction du menu ****************************************************
				.var(jsMenu, _new())
				.var("jsonMenu", jsMenu.getData())
				// TODO a changer par un data sur le menu
				.set("window.jsonMainMenu", "jsonMenu")  // liste des nemu pour animation dans tkAnimation
				
				.__("$.getJSON('/rest/json/menu/activity1').done(", fct("a").consoleDebug("a")
						._for("var i = 0, l = a.length; i < l; i++")
							.__("jsonMenu.push(a[i])")
						.endfor()
						,").fail(", fct("xhr","textStatus", "error").consoleDebug("error") ,")")
				
				/***********************************************************************************************/		
				.var("declaration0",  new JSONPage1().getJSON())
				
				.var("name", txt("Activity1"))
				 
				.var("cnt1", xDiv(xId("test1")/*,  ViewRippleEffect.cRippleEffect()*/, xAttr("data-x-action", "\"more\""),  xAttr("style", "\"width: 100%; height: 30vh; background:url(" +IMAGE6 +") center / cover\"")))
//				.var("chart",  xPart(new ViewJSChart(xId("test")))	)
				.var("cnt2", xDiv(xAttr("style", "\"width: 100%; height: 50vh; background:url(" +IMAGE3 +") center / cover\"")))
				.var("cnt3", xDiv(xId("editor")))
				
				
//				// toute les factory doivent avoir une methode getData
//				
//				.var("declaration1","{type:'page', id:name , children : ["
//						/********************************************************************/				
//						+ "{ selector: '#NavBar'+name, factory:'JSNavBar', rows : [ "
////						+ "				{type:'background', mode:'granim', fct:"fct"},"
//						+ "				{type:'background', mode:'css', css:'url(" +IMAGE5 +") center / cover no-repeat' , opacity:0.3},"   //center / cover
//						+ "				{type:'burger' },"
//						+ "				{type:'name', name:'Elisa'},"
//						+ "				{type:'action', icon:'perm_identity', idAction:'identity'},"
//						+ "				{type:'action', icon:'more_vert', idAction:'more'}"
//						+ "  ]  "
//						+ "},"
//						/************************** AJOUTE DANS ARTICLE **********************/				
//						+ "{ selector: '#'+name+' .article', factory:'JSContainer', rows : [ "
//						+ "				{type:'card', html:cnt1},"
//						+ "				{type:'card', html:chart },"
//						+ "				{type:'card', html:cnt2 },"
//						+ "				{type:'card', html:cnt3 }"
//						+ "  ]  "
//						+ "},"		
//						/********************************************************************/				
//						+ "{ selector: '#'+name+' .content', factory:'JSContainer', rows : [ "
//						+ "				{type:'floatAction' }"
//						+ "  ]  "
//						+ "}"
//						/********************************************************************/					
//						+ "]"
//						+ ", events: { more: { action:'route' , url: '!route/Activity2?p=1'} ,"
//						+ "				BtnFloatMain : { action:'route' , url: '!route/Activity3?p=12'} , "
//						+ "			   "+TKActivity.ON_ACTIVITY_CREATE+" : { action:'callback', fct:'onCreateActivity1'}, "
//						+ "			   "+TKActivity.ON_ACTIVITY_RESUME+" : { action:'callback', fct:'onResumeActivity1'} "
//						+ "  }"
//						+ "}") 
				
				/********************************************************************/
				.set("name", txt("Activity2"))
				
				.set("cnt1", xDiv( xAttr("data-x-action", "\"link\""),  xAttr("style", "\"width: 100%; height: 30vh; background:url(" +IMAGE2 +") center / cover\"")))
				.set("cnt2", xDiv(xAttr("style", "\"width: 100%; height: 50vh; background:url(" +IMAGE3 +") center / cover\"")))
				.var("cnt3", xDiv(xAttr("style", "\"width: 100%; height: 30vh; background:url(" +IMAGE1 +") center / cover\"")))
				.var("cnt4", xDiv(xAttr("style", "\"width: 100%; height: 30vh; background:url(" +IMAGE4 +") center / cover\"")))

				.var("declaration2","{type:'page', id:name, active:false , children : ["
						/********************************************************************/				
						+ "{ selector: '#NavBar'+name, factory:'JSNavBar', rows : [ "
						+ "				{type:'background', mode:'css', css:'url(" +IMAGE6 +") center / cover no-repeat' , opacity:0.3},"   //center / cover
						+ "				{type:'burger' },"
						+ "				{type:'name', name:'Detail'},"
						+ "				{type:'action', icon:'search', idAction:'search'},"
						+ "				{type:'action', icon:'more_vert', idAction:'more'}"
						+ "  ]  "
						+ "},"
						/********************************************************************/				
						+ "{ selector: '#'+name+' .cArticle', factory:'JSContainer', rows : [ "
						+ "				{type:'card', html:cnt1},"
						+ "				{type:'card', html:cnt2 },"
						+ "				{type:'card', html:cnt3 }"
						+ "  ]  "
						+ "},"		
						/********************************************************************/					
						+ "]"
						+ ", events: { more: { action:'route' , url: '!route/Activity1'} ,"
						+ "			   search : { action:'route' , url: '!route/Activity3?p=12'} ,"
						+ "			   HeaderSwipeDown: { action:'back' }, "
						+ "			   "+TKActivity.ON_ACTIVITY_CREATE+" : { action:'callback', fct:'onCreateActivityDown', param:'#NavBarActivity2'} "	
						+ "  }"
						+ "}") 
				
				/********************************************************************/
				.set("name", txt("Activity3"))
				.var("declaration3","{type:'page', id:name, active:false , children : ["
						/********************************************************************/				
						+ "{ selector: '#NavBar'+name, factory:'JSNavBar', rows : [ "
						+ "				{type:'burger' },"
						+ "				{type:'name', name:'Vide'},"
						+ "				{type:'action', icon:'search', idAction:'search'},"
						+ "				{type:'action', icon:'more_vert', idAction:'more'}"
						+ "  ]  "
						+ "},"
						/********************************************************************/				
						+ "{ selector: '#'+name+' .cArticle', factory:'JSContainer', rows : [ "
						+ "				{type:'card', html:cnt4},"
//						+ "				{type:'card', html:cnt2 },"
//						+ "				{type:'card', html:cnt3 }"
						+ "  ]  "
						+ "},"		
						/********************************************************************/				
						+ "{ selector: '#'+name+' .content', factory:'JSContainer', rows : [ "
						+ "				{type:'floatAction' }"
						+ "  ]  "
						+ "}"
						/********************************************************************/						
						+ "]"
						+ ", events: { more: { action:'back' },"
						+ "			   BtnFloatMain : { action:'route' , url: '!route/Activity2?p=22'},"
						+ "			   search : { action:'route' , url: '!route/Activity2?p=24'}, " 
						+ "			   HeaderSwipeDown: { action:'back' }, "
						+ "			   "+TKActivity.ON_ACTIVITY_CREATE+" : { action:'callback', fct:'onCreateActivityDown', param:'#NavBarActivity3'} "
						+ "  }"
						+ "}") 
				
				/**************************************************************/
				.var(tkActivity, "$xui.tkrouter.activityMgr")
				.__(tkActivity.createActivity("declaration0"))
				.__(tkActivity.prepareActivity("declaration2"))
				.__(tkActivity.prepareActivity("declaration3"))
				
				
				/*************************************************************/
				
				
				/**************************************************************/
				
				.set("window.onCreateActivity1", fct()
						.consoleDebug("'onCreateActivity1'")
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
				
				/************************************************************/
				.set(jsNavBar, _new())
				.set("jsonNavBar", jsNavBar.getData("'#NavBarActivity2'"))    // bug import mth jsNavBar car pas ajouer si pas appelé
				
				;
	}
	
	
	class JSONPage1 extends JSONPage
	{
		
		public static final String EVT_MORE = "more";
		public static final String EVT_IDENTITY = "identity";
		public static final String EVT_BTN_FLOAT = "BtnFloatMain";
		
		public Object getJSON()
		{
			XMLElement cnt1 = xDiv(
					 xId("test1"), 
					// xAttr("data-x-action", "\""+EVT_MORE+"\""),  
					 xAttr("style", "\"width: 100%; height: 30vh; background:url(" +IMAGE6 +") center / cover\""));
				 
			
			XMLElement cnt2 = xPart(new ViewJSChart(xId("test2")));	
			
			return page( "Activity1", arr( 
						factory("#NavBarActivity1", FACTORY_NAVBAR, arr( backgroundImage(IMAGE5, 0.3),  
																	 btnBurger(), 
																	 title("Elisa"),
																	 btnActionIcon("perm_identity", EVT_IDENTITY),
																	 btnActionIcon("more_vert", EVT_MORE)
																	) )
					 ,  factory("#Activity1 .cArticle", FACTORY_CONTAINER, arr(
							 										card( arr( backgroundImage(IMAGE3, 1),  
							 											//	backgroundImage(IMAGE4, 1),
							 												text("un text")
							 												)),
							 										cardHtml( cnt1 ), 
							 										cardHtml( cnt2 ) 
							 										) )
					 ,  factory("#Activity1 .content", FACTORY_CONTAINER, arr( 
							 										floatAction()))
					)
						/// les event
					, obj( 
							v(EVT_MORE , routeTo( "!route/Activity2?p=1")),
							v(EVT_BTN_FLOAT , routeTo( "!route/Activity3?p=1")),
							v(ON_ACTIVITY_CREATE , callbackTo("onCreateActivity1")),
							v(ON_ACTIVITY_RESUME , callbackTo("onResumeActivity1"))
							));
		}
		
	}
	
	
	
	
	/*
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
			"});";
	
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
