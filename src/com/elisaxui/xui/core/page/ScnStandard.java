package com.elisaxui.xui.core.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSInterface;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataCtx;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.admin.page.JSTestDataDriven;
import com.elisaxui.xui.core.toolkit.TKAnimation;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouter;
import com.elisaxui.xui.core.widget.ViewFloatAction;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;
import com.elisaxui.xui.core.widget.overlay.ViewOverlay;

@xFile(id = "standard")
@xComment("activite standard")
public class ScnStandard extends XHTMLPart {

	public static final int heightNavBar = 53;
	public static final int widthMenu = 250;
	
	public static final int SPEED_SHOW_BURGERMENU = 200;
	public static final int SPEED_SHOW_ACTIVITY = 300;
	public static final int SPEED_SHOW_OVERLAY = 400;
	public static final int SPEED_RIPPLE_EFFECT = 300;
	public static final int SPEED_BURGER_EFFECT = 600;
	
	public static final int ZINDEX_NAV_BAR = 1;
	public static final int ZINDEX_MENU = 2;
	public static final int ZINDEX_FLOAT = 3;
	public static final int ZINDEX_OVERLAY = 4;
	
	public static final String bgColor = "background: linear-gradient(to right, rgba(253,94,176,1) 0%, rgba(255,0,136,1) 64%, rgba(239,1,124,1) 100%);";
    public static final String bgColorMenu = "background: linear-gradient(to right, rgba(239,1,124,0.5) 0%, rgba(255,0,136,0.68) 36%, rgba(253,94,176,1) 100%);";

    @xTarget(HEADER.class)
	@xRessource
	public Element xTitle() {
		return xElement("title", "le standard");
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
		return xListElement(
				xPart(new TKQueue()),
				xImport(JSXHTMLPart.class),
				xImport(JSTestDataDriven.class),
				xImport(JSDataDriven.class),
				xImport(JSDataSet.class),
				xImport(JSDataCtx.class),
				xImport(JSOverlay.class),
				xImport(TKRouter.class),
				xImport(TKAnimation.class)
				);
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.on("html", "font-size: 14px;line-height: 1.5;"
						+ "font-family: 'Roboto', sans-serif;font-weight: normal; color: rgba(0,0,0,0.87);")
				.on("body", "background-color: white;margin: 0; top: 0px;left: 0px; right: 0px; bottom: 0px; ")
				.on("*", "-webkit-tap-highlight-color: rgba(0,0,0,0);")

				//----------------------------------------------------------------
				.on(".scene","overflow-x: hidden; background-color: black; "
						+ "min-width: 100%;  min-height: 100%; position: absolute   "   //position: absolute;
						)
				
				//----------------------------------------------------------------
				.on(".activity", "background-color: white;"
						+ "position: absolute;   min-width: 100%;  min-height: 100%;   "// top: 0px; left: 0px; right: 0px; bottom: 0px; "
						+ "backface-visibility: hidden; will-change:overflow,z-index;") //will-change:transform
				
				
				
			//	.on(".activity.active", "z-index:1;")
				
				.on(".activity.inactive.inactivefixed", "position: fixed;") 
				.on(".activity.inactive", "transition:transform "+SPEED_SHOW_ACTIVITY+"ms ease-out; top: 0px;left: 0px; right: 0px; bottom: 0px;"
						+ "backface-visibility: hidden;"
						+ "box-shadow: 3px 3px 3px 0 rgba(0,0,0,.24);")

				.on(".activity.backToFront", "transition:transform "+(SPEED_SHOW_ACTIVITY+100)+"ms ease-out;"
						)
				.on(".activity.toback", "transition:transform "+SPEED_SHOW_ACTIVITY+"ms ease-out; transform:translate3d(0px,0px,0px) scale(0.8); "
						)
				.on(".activity.inactive.toHidden", "transform: translate3d(0px,100%,0px);")
				.on(".activity.inactive.tofront", "transition:transform "+SPEED_SHOW_ACTIVITY+"ms ease-out; transform: translate3d(0px,0px,0px);") //z-inde2x: "+ZINDEX_TO_FRONT+";
				.on(".activity.inactive.tofrontSlow", "transition:transform "+400+"ms ease-out; transform: translate3d(0px,0px,0px);") //z-inde2x: "+ZINDEX_TO_FRONT+";

				.on(".activityMoveForShowMenu", "transition:transform "+SPEED_SHOW_BURGERMENU+"ms ease-out; transform: translate3d("+(widthMenu-100)+"px,0px,0px);")
				.on(".activityMoveForHideMenu", "transition:transform "+SPEED_SHOW_BURGERMENU+"ms ease-out; transform: translate3d(0px,0px,0px);")
				
				//----------------------------------------------------------------
				
				.on(".content", "background-color: white; box-sizing: border-box; min-height:100%; min-width: 100%; position:absolute; padding: 8px; padding-top: " + (heightNavBar + 8) + "px")

				//----------------------------------------------------------------
				.on("#content", "height:3000px; position:relative")
				.on("#content2", "height:2000px; position:relative")
				
				
				.on(".panel", "padding: 15px;"
						+ "margin-bottom: 15px;"
						+ "border-radius: 0;"
						+ "background-color: #FFF;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12)")
				;
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportJQUERY() {
		return xElement("/", "<script  src='http://code.jquery.com/jquery-3.2.1.min.js'></script>"
				+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
			//	+ "<script src='https://cdnjs.cloudflare.com/ajax/libs/js-signals/1.0.0/js-signals.min.js'></script>"
				+ "<script src='http://work.krasimirtsonev.com/git/navigo/navigo.js'></script>"
			//	+ "<script src='https://code.jquery.com/pep/0.4.2/pep.js'></script>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css'>"
				+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/hammer.js/2.0.8/hammer.min.js'></script>"
				+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js'></script>"
				+ "<link href='https://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
				);
	}

	
	@Deprecated
	public Object getStaticPage()
	{
		return  xListElement(xPart(new ViewBtnBurger()) 
		,xDiv(xAttr("class", "'center'"), xDiv(xAttr("class", "'logo'"), "Elisa"))
	    ,xDiv(xAttr("class", "'rightAction'"),
	    		xA( "<i class='actionBtn material-icons'>perm_identity</i>"),
	    		xA( "<i class='actionBtn material-icons'>more_vert</i>"))
	    );

	}

	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'scene'"), 
					xDiv(xId("activity1"), xAttr("class", "'activity active'")
							,xPart(new ViewNavBar().addProperty(ViewNavBar.PROPERTY_NAME, "NavBar1"))
							,xDiv(xAttr("class", "'content'") 
								, xPart(new ViewFloatAction())
								, xDiv(xAttr("id",txt("content2")))
								, xPart(new ViewOverlay())
							)
							
				        )
					,xDiv(xId("activity2"), xAttr("class", "'activity inactive inactivefixed toHidden'")
							, xPart(new ViewNavBar().addProperty(ViewNavBar.PROPERTY_NAME, "NavBar2"))
							, xDiv(xAttr("class", "'content'") 	
								//	, xPart(new ViewFloatAction())
									, xDiv(xAttr("id",txt("content")))
							, xPart(new ViewOverlay())
						   )	
					     )
					,xPart(new ViewMenu())

		);
	}
	
	//var oRect = oElement.getBoundingClientRect();

	JSTestDataDriven testDataDriven;
	
	String NavBarAnimated1="var granimInstance = new Granim({\n"+
			" element: \'#NavBar1\',\n"+
			" name: \'basic-gradient\',\n"+
			" direction: \'diagonal\',\n"+
			" opacity: [1, 1],\n"+
			" isPausedWhenNotInView: false,\n"+
			" states : {\n"+
			" \"default-state\": {\n"+
			" gradients: [\n"+
//			    " ['#FFF', '#000'], ['#000', '#FFF']" +
			
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
	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xScriptJS(
				js()
				.__("$.fn.insertAt = function(elements, index){\n"+
						"\tvar children = this.children();\n"+
						"\tif(index >= children.length){\n"+
						"\t\tthis.append(elements);\n"+
						"\t\treturn this;\n"+
						"\t}\n"+
						"\tvar before = children.eq(index);\n"+
						"\t$(elements).insertBefore(before);\n"+
						"\treturn this;\n"+
						"};")
				
				.var(testDataDriven, _new())
			//	.__(testDataDriven.startTest())				
				
				
				//.__(NavBarAnimated1)
				//.__(NavBarAnimated2)	
			);
	}
	
	JSMenu jsMenu;
	JSNavBar jsNavBar;
	TKRouter tkrouter;

	public JSInterface getActionManager()
	{
	  return fct()
			   .consoleDebug("'ok ActionManager'") 
			   .__("$('.scene').on('touchstart',", fct('e')
					   .var("btn", "$(e.target).closest('[data-x-action]')")
					  
					   .var("ripple", "btn")
					   .var("event", "btn.data('x-action')")
					   ._if( "! ripple.hasClass('",  ViewRippleEffect.cRippleEffect().getId(), "')")
					   		.set("ripple", "btn.closest('.",  ViewRippleEffect.cRippleEffect().getId(), "')")
							._if( "! ripple.hasClass('",  ViewRippleEffect.cRippleEffect().getId(), "')")
					   			.set("ripple", "btn.children('.",  ViewRippleEffect.cRippleEffect().getId(), "')")
					   		.endif()	
					   .endif()
					   
					   ._if("$('#activity1').hasClass('active')")
					   		.__("$xui.config.nextActivityAnim= 'fromBottom' ")
					   .endif()	
					   
					   ._if( "ripple.length>0") 
					   
					   	   ._if("ripple.hasClass('cBtnCircle')")
					   	   	   .__("$xui.config.nextActivityAnim= 'opacity'")
						   	  // .__(TKQueue.startAlone(fct()
					   			//	.__("ripple.addClass('cBtnCircleChangeForm')")
					   			//	,ScnStandard.SPEED_SHOW_ACTIVITY/2, fct()
					   	   	   
					   					.__(tkrouter.doEvent("event"))
					   					
					   		//	    ,ScnStandard.SPEED_SHOW_ACTIVITY+100, fct()
					   			//    	.__("ripple.removeClass('cBtnCircleChangeForm')")
					   			//	,100,fct() 
						   		   
						   		//    )
						   	//	)
						   ._else()
						   	   					   
							   .__(TKQueue.startAlone(fct()
							   				.__("ripple.addClass('", ViewRippleEffect.cRippleEffectShow().getId() ,"')")
									 ,SPEED_RIPPLE_EFFECT/3, fct()  // attente ripple effect
							   		      	.__(tkrouter.doEvent("event"))
									 ,SPEED_RIPPLE_EFFECT*2, fct()  // attente ripple effect
								   		    .__("ripple.removeClass('", ViewRippleEffect.cRippleEffectShow().getId() ,"')")     	
									   )
								)
						   
						   .endif()
					   ._else()
					   		.__(tkrouter.doEvent("event"))
					   .endif()
				, ")")
			;
	}
	
	public JSInterface getMoveManager()
	{
		// gestion deplacement menu et fermeture par gesture 
	  return fct().consoleDebug("'ok MoveManager'") 
				.__("var mc = new Hammer($('.scene')[0])")    //, {touchAction: 'auto'}
			//	.__("mc.get('pinch').set({ enable: true })")
				.__("mc.get('pan').set({ enable: true, direction: Hammer.DIRECTION_HORIZONTAL })")  //DIRECTION_ALL
				.var("anim", true)
				.__("mc.on('hammer.input',", fct("ev")
//						.__("$('#content')[0].innerHTML = [ev.srcEvent.type, ev.pointers.length, ev.isFirst, ev.isFinal, ev.deltaX, ev.deltaY, ev.distance, ev.velocity, ev.deltaTime, ev.offsetDirection, ev.target].join('<br>');")
						._if("$(ev.target).closest('.menu').length > 0")
							._if("ev.deltaX>-100 && ev.offsetDirection==2 && ev.velocity>-1 ")
								._if("anim==true")
									.__("$('.menu').css('transition', '' )")
									.__("$('.menu').css('transform', 'translate3d('+ev.deltaX+'px,'+$('.scene').scrollTop()+'px,0px)' )")
								.endif()
							._elseif("anim==true && ev.offsetDirection==2 ")
								.set("anim", "false")
								.__("$('.menu').css('transition', 'transform "+(SPEED_SHOW_BURGERMENU+50)+"ms ease-out' )")
								.__(tkrouter.doEvent("'Overlay'"))
							.endif()
							
							._if("ev.isFinal")
								._if("anim==true")
									.__("$('.menu').css('transition', 'transform "+(SPEED_SHOW_BURGERMENU+50)+"ms ease-out' )")
									.__("$('.menu').css('transform', 'translate3d(0px,'+$('.scene').scrollTop()+'px,0px)' )")
								.endif()
								.set("anim", "true")
							.endif()
						.endif()
						
					,")")
			 
			;
	}
 
	public JSInterface getStateManager()
	{
	  return fct()
		.consoleDebug("'ok StateManager'") 
		
		.set("nav", "new Navigo(null,true)")   //   null,true,'!#')")
		.var(tkrouter , _new("nav"))
		.set("window.tkrouter", tkrouter)
		;
			  
	}
	
	@xTarget(AFTER_CONTENT.class)
	public Element xInitJS() {
		return xScriptJS(js()
   		   	   .set("navigator.vibrate", "navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate")
		
				.var(jsMenu, _new())
				.var("jsonMenu", jsMenu.getData())
				.__("jsonMenu.push({name:'Paramètres', icon:'settings', idAction:'setting'} )")
				.__("jsonMenu.push({name:'Configuration', icon:'build', idAction:'config'} )")
				.__("jsonMenu.push({type:'divider' })")
				.__("jsonMenu.push({name:'Aide', icon:'help_outline', idAction:'help'} )")
				.set("window.jsonMainMenu", "jsonMenu")
				
				.var(jsNavBar, _new())
				.var("jsonNavBar", jsNavBar.getData("'#activity1'"))
				.__("jsonNavBar.push({type:'burger' })")
				.__("jsonNavBar.push({type:'name', name:'Elisa' })")
				.__("jsonNavBar.push({type:'action', icon:'perm_identity', idAction:'identity'})")
				.__("jsonNavBar.push({type:'action', icon:'more_vert', idAction:'more'})")
				
				.set(jsNavBar, _new())
				.set("jsonNavBar", jsNavBar.getData("'#activity2'"))
				.__("jsonNavBar.push({type:'burger' })")
				.__("jsonNavBar.push({type:'name', name:'Detail' })")
				.__("jsonNavBar.push({type:'action', icon:'search', idAction:'search'})")
				.__("jsonNavBar.push({type:'action', icon:'more_vert', idAction:'more'})")
				
				
				.set("$xui", "{ config: { delayWaitForShowMenu : 0, nextActivityAnim : 'fromBottom' } }")
				
				.__("(",getActionManager(),")()")
				.__("(",getMoveManager(),")()")
				.__("(",getStateManager(),")()")

				);
	}
	
}
