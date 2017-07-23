package com.elisaxui.xui.core.page;

import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffect;
import static com.elisaxui.xui.core.widget.button.ViewRippleEffect.cRippleEffectShow;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataCtx;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.admin.AppRoot;
import com.elisaxui.xui.admin.page.JSTestDataDriven;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouter;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.transition.TKTransition;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.container.JSPageLayout;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;

@xFile(id = "standard")
@xComment("activite standard")
public class ScnStandard extends XHTMLPart {

	public static final int heightNavBar = 53*2;
	public static final int widthMenu = 250;
	

	public static final double OVERLAY_OPACITY_MENU = 0.5;
	public static final double OVERLAY_OPACITY_BACK = 0.7;
	
	public static final int ZINDEX_ANIM_FRONT = 1;
	public static final int ZINDEX_ANIM_BACK = 0;
	
	public static final int ZINDEX_NAV_BAR = 1;
	public static final int ZINDEX_MENU = 2;
	public static final int ZINDEX_FLOAT = 3;
	public static final int ZINDEX_OVERLAY = 4;

	public static final String bgColorScene = "#333333";
	public static final String bgColorTheme = "#ff359d";
	public static final String bgColorThemeOpacity = "rgba(255,0,136,1)";
	public static final String bgColor = "background: linear-gradient(to right, rgba(253,94,176,1) 0%, rgba(255,0,136,1) 64%, rgba(239,1,124,1) 100%);";
    public static final String bgColorMenu = "background: linear-gradient(to right, rgba(239,1,124,0.5) 0%, rgba(255,0,136,0.68) 36%, rgba(253,94,176,1) 100%);";
    public static final String bgColorContent = "rgb(245, 243, 237)";
    
    public static final String PREF_3D= "backface-visibility: hidden;"
    		//+ " transform-style:preserve-3d;"
    		;
    

    
	//var oRect = oElement.getBoundingClientRect();
    
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xImport() {
		return xListElement(
				
				xElement("title", "le standard"),
				xElement("meta", xAttr("name", xTxt("theme-color")), xAttr("content", xTxt(bgColorTheme))),
				
				xElement("/", "<script  src='http://code.jquery.com/jquery-3.2.1.min.js'></script>"
						+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
					//	+ "<script src='https://cdnjs.cloudflare.com/ajax/libs/js-signals/1.0.0/js-signals.min.js'></script>"
						+ "<script src='http://work.krasimirtsonev.com/git/navigo/navigo.js'></script>"
					//	+ "<script src='https://code.jquery.com/pep/0.4.2/pep.js'></script>"
						+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
						+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css'>"
						+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/hammer.js/2.0.8/hammer.min.js'></script>"
						+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js'></script>"
						+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/granim/1.0.6/granim.min.js'></script>"
						+ "<link href='https://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
						)
				
				
				
				);
	}
		
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xImportAfter() {
		return xListElement(
				xPart(new TKQueue()),
				xImport(JSXHTMLPart.class),
				xImport(JSTestDataDriven.class),
				xImport(JSDataDriven.class),
				xImport(JSDataSet.class),
				xImport(JSDataCtx.class),
				xImport(JSOverlay.class),
				xImport(JSContainer.class),
				xImport(JSPageLayout.class),
				xImport(TKRouter.class),
				xPart(new CssTransition()),
				xImport(TKTransition.class),
				xImport(JSMenu.class),
				xImport(TKActivity.class));
	}
				
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {
		
		return xCss()
				.on("html", "font-size: 14px; line-height: 1.5;"
						+ "font-family: 'Roboto', sans-serif;font-weight: normal;")    //color: rgba(0,0,0,0.87);
				
				.on("body", "background-color: "+bgColorScene+"; margin: 0; ")
				.on("*", "-webkit-tap-highlight-color: rgba(0,0,0,0);")  // pas de coulour au click => ripple a la place

				//----------------------------------------------------------------
				.on(".scene","overflow-x: hidden; background-color: "+bgColorScene+";"   // overflow: auto; -webkit-overflow-scrolling: auto; 
						+ "min-width: 100vw;  min-height: 100vh; "   //position: absolute;
						)
				//----------------------------------------------------------------
				.on(".activity", "background-color: white;"
						+ PREF_3D
						+ " will-change:overflow,z-index;") //will-change:transform
				
				//----------------------------------------------------------------
				.on(".activity .content", " min-height: 100vh; min-width: 100vw; background-color:"+bgColorContent+";will-change:contents")  // changement durant le freeze du contenu de l'activity
				.on(".content", "box-sizing: border-box;"
						+ "min-height:100%; min-width: 100%; max-width: 100%; "
						+ "padding: 8px; padding-top: " + (heightNavBar + 8) + "px")	  //position:absolute;		
				;
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return 
			xListElement(
				xPart(new ViewMenu()),
				xDiv(xAttr("class", "'scene'")
				)
		);
	}
	
	
	@xTarget(AFTER_CONTENT.class)
	@xRessource
	public XMLElement xImportStart() {
		return xListElement(
				xScriptJS(js()
						// a mettre dans TKConfig
						.set("$xui", "{ intent: { nextActivityAnim : 'fromBottom' }, config:{ } }")  
						
						/**********************************************/
						.__("(",getEventManager(),")()")
						.__("(",getMoveManager(),")()")
						.__("(",getStateManager(),")()")
						
						/***************************************************/
						)
				
				,xPart(new AppRoot())
				);
	}
		
	@xTarget(AFTER_CONTENT.class)
	public XMLElement xAddJS() {
		
		return xScriptJS(js()
				
	   		   	.set("navigator.vibrate", "navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate")
	   		   	
				.__("$.fn.insertAt = function(elements, index){\n"+   // utiliser dans le core 
						"\tvar children = this.children();\n"+
						"\tif(index >= children.length){\n"+
						"\t\tthis.append(elements);\n"+
						"\t\treturn this;\n"+
						"\t}\n"+
						"\tvar before = children.eq(index);\n"+
						"\t$(elements).insertBefore(before);\n"+
						"\treturn this;\n"+
						"};")
								
			);
	}
	

	TKRouter tkrouter;

	public JSMethodInterface getEventManager()
	{
	  return fct()
			   .consoleDebug("'ok EventManager'") 
			   .__("$('body').on('touchstart',", fct('e')
					   .var("btn", "$(e.target).closest('[data-x-action]')")
					  
					   .var("ripple", "btn")
					   .var("event", "btn.data('x-action')")
					   
					   // recherche le ripple btn
					   ._if( "! ripple.hasClass('",  cRippleEffect.getId(), "')")
					   		.set("ripple", "btn.closest('.",  cRippleEffect.getId(), "')")
							._if( "! ripple.hasClass('",  cRippleEffect.getId(), "')")
					   			.set("ripple", "btn.children('.",  cRippleEffect.getId(), "')")
					   		.endif()	
					   .endif()
					   					   
					   	.__("$xui.intent.nextActivityAnim= 'fromBottom' ")	
					   				   	
					   ._if( "ripple.length>0") 
					   
					   	   ._if("ripple.hasClass('cBtnCircle')")
					   	   	    .__("$xui.intent.nextActivityAnim= 'opacity'")
					   			.__(tkrouter.doEvent("event"))
						   ._else()
						   	   // ripple sans ouverture d'activity		   
							   .__(TKQueue.startAlone(fct()
						   		      		.__(tkrouter.doEvent("event"))
						   		      , CssTransition.NEXT_FRAME	, fct()  // attente ripple effect
							   				.__("ripple.addClass('", cRippleEffectShow.getId() ,"')")
									 //,CssTransition.SPEED_RIPPLE_EFFECT/3, 

									 ,CssTransition.SPEED_RIPPLE_EFFECT, fct()  // attente ripple effect
								   		    .__("ripple.removeClass('", cRippleEffectShow.getId() ,"')")     	
									   )
								)  
						   .endif()
					   ._else()
					   		// pas de ripple effect
					   		.__(tkrouter.doEvent("event"))
					   .endif()
				, ")")
			;
	}
	
	public JSMethodInterface getMoveManager()
	{
		// gestion deplacement menu et fermeture par gesture 
	  return fct().consoleDebug("'ok MoveManager'") 
			  			  
				.__("var mc = new Hammer($('body')[0])")    //, {touchAction: 'auto'}
			//	.__("mc.get('pinch').set({ enable: true })")
				.__("mc.get('pan').set({ enable: true, direction: Hammer.DIRECTION_HORIZONTAL })")  //DIRECTION_ALL
				.var("anim", true)
				
				.__("mc.on('hammer.input',", fct("ev")
//						.__("$('#content')[0].innerHTML = [ev.srcEvent.type, ev.pointers.length, ev.isFirst, ev.isFinal, ev.deltaX, ev.deltaY, ev.distance, ev.velocity, ev.deltaTime, ev.offsetDirection, ev.target].join('<br>');")
						//**************************** gestion swipe anim du menu *************************/
						._if("$(ev.target).closest('.menu').length > 0")
							._if("ev.deltaX>-100 && ev.offsetDirection==2 && ev.velocity>-1 ")
								._if("anim==true")
									.__("$('.menu').css('transition', '' )")
									.__("$('.menu').css('transform', 'translate3d('+ev.deltaX+'px,'+$('body').scrollTop()+'px,0px)' )")
								.endif()
							._elseif("anim==true && ev.offsetDirection==2 ")
								.set("anim", "false")
								.__("$('.menu').css('transition', 'transform "+(CssTransition.SPEED_SHOW_MENU+50)+"ms ease-out' )")
								.__(tkrouter.doEvent("'Overlay'"))
							.endif()
							
							._if("ev.isFinal")
								._if("anim==true")
									.__("$('.menu').css('transition', 'transform "+(CssTransition.SPEED_SHOW_MENU+50)+"ms ease-out' )")
									.__("$('.menu').css('transform', 'translate3d(0px,'+$('body').scrollTop()+'px,0px)' )")
								.endif()
								.set("anim", "true")
							.endif()
						.endif()
						//***************************************************************************************/
					,")")
			;
	}
 
	public JSMethodInterface getStateManager()
	{
	  return fct()
		.consoleDebug("'ok StateManager'") 
		
		.set("nav", "new Navigo(null,true)")   //   null,true,'!#')")
		.var(tkrouter , _new("nav"))
		.set("window.tkrouter", tkrouter)  // a mettre dans TKConfig
		;
			  
	}
	
}
