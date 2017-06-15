package com.elisaxui.xui.core.transition;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
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
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.admin.page.JSTestDataDriven;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouter;
import com.elisaxui.xui.core.transition.TKTransition;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.chart.ViewJSChart;
import com.elisaxui.xui.core.widget.container.JSContainer;
import com.elisaxui.xui.core.widget.menu.JSMenu;
import com.elisaxui.xui.core.widget.menu.ViewMenu;
import com.elisaxui.xui.core.widget.navbar.JSNavBar;
import com.elisaxui.xui.core.widget.overlay.JSOverlay;


public class CssTransition extends XHTMLPart {


	public static final int widthMenu = 250;
	
	public static final int SPEED_SHOW_ACTIVITY = 200;
	public static final int SPEED_ACTIVITY_TRANSITION_EFFECT = 150;
	public static final int DELAY_SURETE_END_ANIMATION = 50;
	
	public static final String NEXT_FRAME = "'nextFrame'";
	
	public static final int SPEED_SHOW_MENU = 150;
	
	public static final int SPEED_RIPPLE_EFFECT = 300;
	public static final int SPEED_BURGER_EFFECT = 200;;
	
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {
		
		return xCss()			
				.on(".activity.fixedForAnimated", "top:0px; position: fixed; transition:transform "+SPEED_SHOW_ACTIVITY+"ms linear;")   // reste cacher en bas de la page et ne suit pas l'ascenceur

				
				.on(".activity.backToFront", "transition:transform "+(SPEED_SHOW_ACTIVITY+100)+"ms linear;")
				
				.on(".activity.toback", "transition:transform "+(SPEED_SHOW_ACTIVITY+100)+"ms linear; "
						+ "transform:translate3d(0px,0px,0px) scale(0.9); ")
				
				.on(".activity.frontActivity", "z-index:"+ScnStandard.ZINDEX_ANIM_FRONT+";")
				.on(".activity.backActivity", "z-index:"+ScnStandard.ZINDEX_ANIM_BACK+";")
				
				.on(".activity.toHidden", "transform: translate3d(0px,100%,0px);")				
				.on(".activity.tofront", "transform: translate3d(0px,0px,0px);")  
				.on(".activity.nodisplay", "display:none;")
				
				.on(".activity.circleAnim0prt", "clip-path:circle(0.0% at 85vw 90vh); -webkit-clip-path:circle(0.0% at 85vw 90vh); ")
				.on(".activity.circleAnim100prt", "clip-path:circle(80% at center); -webkit-clip-path:circle(80% at center); ")

				.on(".activity.zoom12", "transform:scale3d(1.2,1.2,1)")
				.on(".activity.zoom10", "transform:scale3d(1,1,1)")

				.on(".activity.transitionSpeedSlow", "transition:all "+ CssTransition.SPEED_SHOW_ACTIVITY*5 +"ms linear")
				.on(".activity.transitionSpeed", "transition:all "+ CssTransition.SPEED_SHOW_ACTIVITY +"ms linear")
				.on(".activity.transitionSpeedx2", "transition:all "+ CssTransition.SPEED_ACTIVITY_TRANSITION_EFFECT +"ms linear")
				
				.on(".activityMoveForShowMenu", "transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d("+(widthMenu-100)+"px,0px,0px);")
				.on(".activityMoveForHideMenu", "transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d(0px,0px,0px);")
				;
				
	}


	
}
