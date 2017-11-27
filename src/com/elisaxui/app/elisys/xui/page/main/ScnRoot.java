/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import com.elisaxui.app.elisys.xui.js.JSHistoireManager;
import com.elisaxui.app.elisys.xui.widget.JSSyllabisation;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.xui.core.page.ConfigScene;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.layout.JSPageLayout;
import com.elisaxui.xui.core.widget.log.ViewLog;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;

/**
 * @author Bureau
 *
 */

@xFile(id = "main")
@xComment("activite standard")
public class ScnRoot extends XUIScene {

	private static final String REST_JSON_MENU_ACTIVITY1 = "/rest/json/menu/activity1";
	

	public static final String[] listPhotos= new String[] {
			"https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/6337/light-coffee-pen-working.jpg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/117729/pexels-photo-117729.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/211122/pexels-photo-211122.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/210745/pexels-photo-210745.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/164482/pexels-photo-164482.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/256417/pexels-photo-256417.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/207665/pexels-photo-207665.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/272228/pexels-photo-272228.jpeg?h=350&auto=compress&cs=tinysrgb",
			"https://images.pexels.com/photos/287336/pexels-photo-287336.jpeg?h=350&auto=compress&cs=tinysrgb"
	}; 
	
	
	@xTarget(AFTER_CONTENT.class) 
	@xRessource
	public XMLElement xImportAfter() {
		return xListElement(
				xImport(JSSyllabisation.class), 
				xImport(JSHistoireManager.class)  
				);
	}
	
	

	@Override
	public JSMethodInterface searchMenu()
	{
	  JSArray jsonMenu = new JSArray()._setName("jsonMenu");	
	  JSArray jsonApi = new JSArray()._setName("jsonApi");	
	  JSInt idx = new JSInt()._setName("idx");
	  
	  return fragment()
			  	// TODO a changer 
				.__("$.getJSON('"+REST_JSON_MENU_ACTIVITY1+"')"
					+ ".done(", fct(jsonApi)    //.consoleDebug("'json menu'", "a")
						._forIdx(idx, jsonApi)
							.__(jsonMenu.push(jsonApi.at(idx)))
						.endfor()
						,")"
					+ ".fail(", fct("xhr","textStatus", "error").consoleDebug("error") ,")")
			  ;
	}
	
	@Override
	public JSMethodInterface loadPage()
	{
	  return fragment()
				.__(tkActivity.createActivity(new JSONPage1().getJSON()))
				.__(tkActivity.prepareActivity(new JSONPage2().getJSON()))
				.__(tkActivity.prepareActivity(new JSONPage3().getJSON()))
			  ;
	}
	
	static JSSyllabisation jsSyllabe;
	static JSHistoireManager jsHitoireMgr;
	
	
	@Override
	public JSMethodInterface createScene()
	{	
		JSArray jsonSyllabe = new JSArray()._setName("jsonSyllabe");
		return fragment()				
				/*************************************************************/
				
				.var(jsHitoireMgr, _new())
				.var(jsSyllabe, _new())
				.var(jsonSyllabe, jsSyllabe.getData())
				
				/**************************************************************/
				
				.set("window.onCreateActivity1", fct("json")
						.consoleDebug("'on Create Activity1'")
						.__(TKQueue.startProcessQueued( 100,  fct()
								.var(jsPageLayout, _new())
								.__(jsPageLayout.hideOnScroll(new JSString()._setContent("'#Activity1'")))
								
// 					TODO a changer : mettre dans une queue avec priorité (avec image) et gestion de promise d'attente 								
//								.__(" $.getScript({url:'https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js',  cache: true}).done("
//								    , fct() 
//								    	.var("jCanvasGranim", "$('#NavBarActivity1 .animatedBg')[0]")
//								    	.__(NavBarAnimated1)
//								    ,")") 

							, 500,  fct()
						 		.set("window.microlistener", jsSyllabe.createMicroListener())								
							)
						)
				)
				
				.set("window.onResumeActivity1", fct().__("alert('ok')") 
						)
				
				/**************************************************************/
				// gestion du slidedown pour fermer
				.set("window.onCreateActivityDown", fct("json")
						.__(TKQueue.startProcessQueued( 100,  fct()
								.var(jsPageLayout, _new())
								.__(jsPageLayout.setEnableCloseGesture("json.param"))
							)
						)
				)
				
				/**************************************************************/
				
				.set("window.onMicro", fct("json")
						.__(TKQueue.startProcessQueued( fct()
								.var(jsSyllabe, "window.microlistener")
								._if(jsSyllabe,".isRunning==false")
									.set(jsvar(jsSyllabe,".isRunning"), true)
							    	.__(jsSyllabe,".recognition.start()")
							    ._else()
									.set(jsvar(jsSyllabe,".isRunning"), false)
									.set(jsvar(jsSyllabe,".stop"), true)
									.__(jsSyllabe,".recognition.stop()")
							    .endif()
							)
						)
				)
				
				.set("window.onDelete", fct("json")
						.__(TKQueue.startProcessQueued( fct()
								.set("window.lastPhrase", txt(""))
								.var(jsSyllabe, "window.microlistener")
								.var("jsonSyllabe2", jsvar(jsSyllabe, ".aDataSet.getData()"))
								
								._for("var i = jsonSyllabe2.length-1; i >=0; i--")
									.__("setTimeout(", fct()
													.__(jsonSyllabe.splice(0,1))
										, ",50*i)")			
								.endfor()	
							)
						)
				)
				
				.set("window.onMot", fct("json")
						.var("leMot", "$(window.lastBtn).data('mot')")
						.var("msg", "new SpeechSynthesisUtterance(leMot)")
						.set("msg.lang", "'fr-FR'")
						.__("window.speechSynthesis.speak(msg)")
				)
				
				.set("window.onPhrase", fct("json")
						.var(jsSyllabe, "window.microlistener")
						._if(jsSyllabe,".isRunning")
							.set(jsvar(jsSyllabe,".isRunning"), false)
							.set(jsvar(jsSyllabe,".stop"), true)
							.__(jsSyllabe,".recognition.stop()")
					    .endif()
						
						._if("window.speechSynthesis.speaking")
							.__("window.speechSynthesis.cancel()")
						  	.__("$.notify('stop', {globalPosition: 'bottom left', className:'success', autoHideDelay: 2000})")

						._else()
							.var("msg", "new SpeechSynthesisUtterance(window.lastPhrase)")
							.set("msg.lang", "'fr-FR'")
							.set("msg.rate", 0.9)
							//.set("msg.pitch", 1)
							.__("window.speechSynthesis.speak(msg)")
						.endif()
				)
				
				.set("window.onLoadHistoire", fct("json")
						.var(jsHitoireMgr, _new())
						.__(jsHitoireMgr.getHistoire())
				)
		;
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

	
//	.__("JSONEditor.defaults.options.theme = 'bootstrap3';")	
//	.__("var editor = new JSONEditor(document.getElementById(\"editor\"), { schema: {} } )")
//	.__("editor.setValue(window.jsonMainMenu)")
//	.__("editor.on('change',", fct()
//			.var("content", "editor.getValue()")
//			.consoleDebug("content")
//			.__("$.post( {"+ 
//					"  type: 'POST'," + 
//					"  url: '/rest/json/save'," + 
//					"  data: JSON.stringify(content)," + 
//					//"  success: success,\r\n" + 
//					"  dataType: 'text'," + 
//				    "  contentType: 'text/plain; charset=utf-8'"+
//					"})")
//		, ")")
	
    /****************************************************************************************/
	ConfigScene conf = new ConfigScene();
	
	@Override
	public ConfigScene getConfigScene() {
		return conf;
	}
}
