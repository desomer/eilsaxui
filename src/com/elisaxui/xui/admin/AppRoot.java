/**
 * 
 */
package com.elisaxui.xui.admin;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.widget.chart.ViewJSChart;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;

/**
 * @author Bureau
 *
 */
public class AppRoot extends XHTMLPart {

	JSMenu jsMenu;
	JSNavBar jsNavBar;
	TKActivity tkActivity;
	
	public static String image1 = "https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static String image2 = "https://images.pexels.com/photos/6337/light-coffee-pen-working.jpg?h=350&auto=compress&cs=tinysrgb";
	public static String image3 = "https://images.pexels.com/photos/117729/pexels-photo-117729.jpeg?h=350&auto=compress&cs=tinysrgb";
	public static String image4 = "https://images.pexels.com/photos/211122/pexels-photo-211122.jpeg?h=350&auto=compress&cs=tinysrgb";
	
	
	@xTarget(AFTER_CONTENT.class)  // dans le header
	@xRessource
	public Element xInitJS() {
		return xScriptJS(js()
		
				//******************** construction du menu ****************************************************
				.var(jsMenu, _new())
				.var("jsonMenu", jsMenu.getData())
				.__("jsonMenu.push({name:'Paramètres', icon:'settings', idAction:'setting'} )")
				.__("jsonMenu.push({name:'Configuration', icon:'build', idAction:'config'} )")
				.__("jsonMenu.push({type:'divider' })")
				.__("jsonMenu.push({name:'Aide', icon:'help_outline', idAction:'help'} )")
				.set("window.jsonMainMenu", "jsonMenu")  // liste des nemu pour animation dans tkAnimation
				
				/***********************************************************************************************/		
				.var("name", txt("Activity1"))
				 
				.var("cnt1", XHTMLPart.xDiv(XHTMLPart.xId("test1"), XHTMLPart.xAttr("style", "\"width: 100%; height: 30vh; background:url(" +image1 +") center / cover\"")))
				.var("chart",  XHTMLPart.xElementPart(new ViewJSChart(XHTMLPart.xId("test")))	)
				.var("cnt2", XHTMLPart.xDiv(XHTMLPart.xAttr("style", "\"width: 100%; height: 50vh; background:url(" +image3 +") center / cover\"")))
				 
				.var("declaration1","{type:'page', id:name , children : ["
						/********************************************************************/				
						+ "{ selector: '#NavBar'+name, factory:'JSNavBar', rows : [ "
						+ "				{type:'burger' },"
						+ "				{type:'name', name:'Elisa'},"
						+ "				{type:'action', icon:'perm_identity', idAction:'identity'},"
						+ "				{type:'action', icon:'more_vert', idAction:'more'}"
						+ "  ]  "
						+ "},"
						/********************************************************************/				
						+ "{ selector: '#'+name+' .article', factory:'JSContainer', rows : [ "
						+ "				{type:'card', html:cnt1},"
						+ "				{type:'card', html:chart },"
						+ "				{type:'card', html:cnt2 }"
						+ "  ]  "
						+ "},"		
						/********************************************************************/				
						+ "{ selector: '#'+name+' .content', factory:'JSContainer', rows : [ "
						+ "				{type:'floatAction' }"
						+ "  ]  "
						+ "}"
						/********************************************************************/					
						+ "]"
						+ ", events: { more: { action:'route' , url: 'route/Activity2?p=1'} ,"
						+ "				BtnFloatMain : { action:'route' , url: 'route/Activity3?p=12'} , "
						+ "			   "+TKActivity.ON_ACTIVITY_CREATE+" : { action:'callback', fct:'onCreateActivity1'}, "
						+ "			   "+TKActivity.ON_ACTIVITY_RESUME+" : { action:'callback', fct:'onResumeActivity1'} "
						+ "  }"
						+ "}") 
				
				/********************************************************************/
				.set("name", txt("Activity2"))
				
				.set("cnt1", xDiv(xAttr("style", "\"width: 100%; height: 30vh; background:url(" +image2 +") center / cover\"")))
				.set("cnt2", xDiv(xAttr("style", "\"width: 100%; height: 50vh; background:url(" +image3 +") center / cover\"")))
				.var("cnt3", xDiv(xAttr("style", "\"width: 100%; height: 30vh; background:url(" +image1 +") center / cover\"")))
				.var("cnt4", xDiv(xAttr("style", "\"width: 100%; height: 30vh; background:url(" +image4 +") center / cover\"")))

				.var("declaration2","{type:'page', id:name, active:false , children : ["
						/********************************************************************/				
						+ "{ selector: '#NavBar'+name, factory:'JSNavBar', rows : [ "
						+ "				{type:'burger' },"
						+ "				{type:'name', name:'Detail'},"
						+ "				{type:'action', icon:'search', idAction:'search'},"
						+ "				{type:'action', icon:'more_vert', idAction:'more'}"
						+ "  ]  "
						+ "},"
						/********************************************************************/				
						+ "{ selector: '#'+name+' .article', factory:'JSContainer', rows : [ "
						+ "				{type:'card', html:cnt1},"
						+ "				{type:'card', html:cnt2 },"
						+ "				{type:'card', html:cnt3 }"
						+ "  ]  "
						+ "},"		
						/********************************************************************/					
						+ "]"
						+ ", events: { more: { action:'route' , url: 'route/Activity1'} ,"
						+ "			   search : { action:'route' , url: 'route/Activity3?p=12'} ,"
					//	+ "			   "+TKActivity.ON_ACTIVITY_CREATE+" : { action:'callback', fct:'c'} "	
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
						+ "{ selector: '#'+name+' .article', factory:'JSContainer', rows : [ "
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
						+ "			   BtnFloatMain : { action:'route' , url: 'route/Activity2?p=22'},"
						+ "			   search : { action:'route' , url: 'route/Activity2?p=24'} " 
						+ "  }"
						+ "}") 
				
				/**************************************************************/
				.var(tkActivity, "window.tkrouter.activityMgr")
				.__(tkActivity.createActivity("declaration1"))
				.__(tkActivity.prepareActivity("declaration2"))
				.__(tkActivity.prepareActivity("declaration3"))
				
				/**************************************************************/
				
				.set("window.onCreateActivity1", fct()
					//	.var("jCanvasGranim", "$('#NavBarActivity1 .animatedBg')[0]")
					//	.__(NavBarAnimated1)
				)
				
				.set("window.onResumeActivity1", fct().__("alert('ok')") 
						)
				
				/************************************************************/
				.set(jsNavBar, _new())
				.set("jsonNavBar", jsNavBar.getData("'#NavBarActivity2'"))    // bug import mth jsNavBar
				
				);
	}
	
	
	
	
	String NavBarAnimated1="var granimInstance = new Granim({\n"+
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
	
	String NavBarAnimated2="var granimInstance = new Granim({\n"+
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
	
}
