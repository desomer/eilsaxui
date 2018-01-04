package com.elisaxui.xui.core.transition;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.widget.overlay.ViewOverlayRipple;

import static com.elisaxui.xui.core.transition.ConstTransition.*;

@xComment("CssTransition")
public class CssTransition extends XHTMLPart {

	public static final int widthMenu = 250;

	public static XClass activity;
	
	static XClass animated;
	
	static XClass cStateFixedForFreeze;
	public static XClass cFixedElement;
	
	public static XClass active;
	public static XClass inactive;
	public static XClass detach;
	
	public static XClass circleAnim0prt;
	public static XClass circleAnim100prt;
	
	public static XClass cStateZoom09;
	public static XClass cStateZoom1;
	public static XClass cStateZoom12;
	
	public static XClass transitionSpeed;
	public static XClass transitionSpeedx2;


	public static XClass activityMoveForShowMenu;
	public static XClass activityMoveForHideMenu;
	
	public static XClass cStateFrontActivity;
	public static XClass cStateBackActivity;
	
	public static XClass cStateMoveToBottom;
	public static XClass cStateMoveToFront;
	public static XClass cStateNoDisplay;
	
	@xTarget(HEADER.class)
	@xRessource
	@xPriority(5)
	public XMLElement xStylePart() {

		return xStyle()
				.path(animated).set("animation-duration:"+SPEED_ANIMATED+"ms")
				
				.path(activity)
						.andPath(xStyle(cStateFixedForFreeze).set("top:0px; position: fixed; overflow:hidden "))
						.andPath(xStyle(cStateFrontActivity).set("z-index:"+XUIScene.ZINDEX_ANIM_FRONT+";"))	
						.andPath(xStyle(cStateBackActivity).set("z-index:"+XUIScene.ZINDEX_ANIM_BACK+";"))		
	
				.path(activity.and(cStateMoveToBottom)).set(
						"transform: translate3d(0px,100%,0px);"
						+ "transition:transform "+SPEED_SHOW_ACTIVITY +"ms ease-in-out;")	
				.path(activity.and(cStateMoveToFront)).set(
						"transform: translate3d(0px,0px,0px);"
						+ "transition:transform "+SPEED_SHOW_ACTIVITY +"ms ease-in-out;")  
				.path(activity.and(cStateNoDisplay)).set("display:none;")
				
				.path(activity)
						.andPath(xStyle(cStateZoom12).set("transform: scale3d(1.2,1.2,1)"))
						.andPath(xStyle(cStateZoom1).set("transform: scale3d(1,1,1)"))	
						.andPath(xStyle(cStateZoom09).set("transform: scale3d(0.9,0.9,1)"))
				
				.path(activityMoveForShowMenu).set(
						"transition:transform "+SPEED_SHOW_MENU+"ms ease-out;"
						+ " transform: translate3d("+(widthMenu-100)+"px,0px,0px) "
						+ "scale3d(0.95,0.95,1);")
				.path(activityMoveForHideMenu).set(
						"transition:transform "+SPEED_SHOW_MENU+"ms ease-out;"
						+ " transform: translate3d(0px,0px,0px);")

				.path(activity.and(transitionSpeed)).set("transition:all "+ SPEED_SHOW_ACTIVITY +"ms ease-in-out")
				.path(activity.and(transitionSpeedx2)).set("transition:all "+ SPEED_ACTIVITY_TRANSITION_EFFECT +"ms ease-in-out")
								
				.path(circleAnim0prt).set("clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); -webkit-clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); ")
				.path(circleAnim100prt).set("clip-path:circle(80% at center); -webkit-clip-path:circle(80% at center); ")
				;
				
	}


	
}
