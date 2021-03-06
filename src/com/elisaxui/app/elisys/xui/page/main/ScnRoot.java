/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main;

import com.elisaxui.app.elisys.xui.js.JSHistoireManager;
import com.elisaxui.app.elisys.xui.page.main.widget.JSSyllabisation;
import com.elisaxui.component.page.old.ConfigScene;
import com.elisaxui.component.page.old.XUIScene;
import com.elisaxui.component.toolkit.TKQueue;
import com.elisaxui.component.toolkit.old.JQuery;
import com.elisaxui.component.widget.activity.JSPageLayout;
import com.elisaxui.component.widget.activity.TKActivity;
import com.elisaxui.component.widget.log.ViewLog;
import com.elisaxui.component.widget.menu.old.JSMenu;
import com.elisaxui.component.widget.navbar.old.JSNavBar;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;

/**
 * @author Bureau
 *
 */

@xResource(id = "cMain")
@xComment("activite standard")
@xCoreVersion("0")
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
	@xResource
	public XMLElement xImportAfter() {
		return xListNode(
				xIncludeJS(JSSyllabisation.class), 
				xIncludeJS(JSHistoireManager.class),
				xIncludeJS(JSRoot.class)  
				);
	}
	
	

	@Override
	public JSContentInterface searchMenu()
	{
	  JSArray jsonMenu = new JSArray()._setName("jsonMenu");	
	  JSArray jsonApi = new JSArray()._setName("jsonApi");	
	  JSInt idx = new JSInt()._setName("idx");
	  
	  return fragment()
			  	// TODO a changer 
				.__("$.getJSON('"+REST_JSON_MENU_ACTIVITY1+"')"
					+ ".done(", fct(jsonApi)    //.consoleDebug("'json menu'", "a")
						.forIdx(idx, jsonApi)
							.__(jsonMenu.push(jsonApi.at(idx)))
						.endfor()
						,")"
					+ ".fail(", fct("xhr","textStatus", "error").consoleDebug("error") ,")")
			  ;
	}

	@Override
	public JSContentInterface loadPage()
	{
	  return fragment()
			.__(tkActivity.createActivity(new JSONPage1().getJSON()))
	  		.__(tkActivity.prepareActivity(new JSONPage2().getJSON()))
	  		.__(tkActivity.prepareActivity(new JSONPage3().getJSON()))

			  ;
	}
	
	static JSSyllabisation jsSyllabe;
	static JSHistoireManager jsHitoireMgr;
	static JSRoot jsRoot;
	
	@Override
	public JSContentInterface createScene()
	{	
		JSArray jsonSyllabe = new JSArray()._setName("jsonSyllabe");
		return fragment()				
				/*************************************************************/
				
				._var(jsHitoireMgr, _new())
				._var(jsSyllabe, _new())
				._var(jsonSyllabe, jsSyllabe.getData())
				
				/**************************************************************/
				
				._var(jsRoot, _new(jsonSyllabe, jsSyllabe))
				
				/**************************************************************/
				
				._set("window.onMicro", fct("json")
						.__(TKQueue.startProcessQueued( fct()
								._var(jsSyllabe, "window.microlistener")
								._if(jsSyllabe,".isRunning==false")
									._set(jsvar(jsSyllabe,".isRunning"), true)
							    	.__(jsSyllabe,".recognition.start()")
							    ._else()
									._set(jsvar(jsSyllabe,".isRunning"), false)
									._set(jsvar(jsSyllabe,".stop"), true)
									.__(jsSyllabe,".recognition.stop()")
							    .endif()
							)
						)
				)
				
				._set("window.onDelete", fct("json")
						.__(TKQueue.startProcessQueued( fct()
								._set("window.lastPhrase", txt(""))
								._var(jsSyllabe, "window.microlistener")
								._var("jsonSyllabe2", jsvar(jsSyllabe, ".aDataSet.getData()"))
								
								._for("var i = jsonSyllabe2.length-1; i >=0; i--")
									.__("setTimeout(", fct()
													.__(jsonSyllabe.splice(0,1))
										, ",50*i)")			
								.endfor()	
							)
						)
				)
				
				._set("window.onMot", fct("json")
						._var("leMot", "$(window.lastBtn).data('mot')")
						._var("msg", "new SpeechSynthesisUtterance(leMot)")
						._set("msg.lang", "'fr-FR'")
						.__("window.speechSynthesis.speak(msg)")
				)
				
				._set("window.onPhrase", fct("json")
						._var(jsSyllabe, "window.microlistener")
						._if(jsSyllabe,".isRunning")
							._set(jsvar(jsSyllabe,".isRunning"), false)
							._set(jsvar(jsSyllabe,".stop"), true)
							.__(jsSyllabe,".recognition.stop()")
					    .endif()
						
						._if("window.speechSynthesis.speaking")
							.__("window.speechSynthesis.cancel()")
						  	.__("$.notify('stop', {globalPosition: 'bottom left', className:'success', autoHideDelay: 2000})")

						._else()
							._var("msg", "new SpeechSynthesisUtterance(window.lastPhrase)")
							._set("msg.lang", "'fr-FR'")
							._set("msg.rate", 0.9)
							//.set("msg.pitch", 1)
							.__("window.speechSynthesis.speak(msg)")
						.endif()
				)
				
				._set("window.onLoadHistoire", fct("json")
						._var(jsHitoireMgr, _new())
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
