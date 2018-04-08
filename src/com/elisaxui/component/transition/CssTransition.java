package com.elisaxui.component.transition;

import static com.elisaxui.component.transition.ConstTransition.*;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.widget.overlay.ViewOverlayRipple;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

@xComment("CssTransition")
public class CssTransition extends XHTMLPart {

	public static final int widthMenu = 250;

	public static CSSClass activity;
	
	static CSSClass animated;
	
	static CSSClass cStateFixedForFreeze;
	public static CSSClass cFixedElement;
	
	public static CSSClass active;
	public static CSSClass inactive;
	public static CSSClass detach;
	
	public static CSSClass circleAnim0prt;
	public static CSSClass circleAnim100prt;
	
	public static CSSClass cStateZoom09;
	public static CSSClass cStateZoom1;
	public static CSSClass cStateZoom12;
	
	public static CSSClass cTransitionSpeed;
	public static CSSClass cTransitionSpeedEffect;


	public static CSSClass activityMoveForShowMenu;
	public static CSSClass activityMoveForHideMenu;
	
	public static CSSClass cStateFrontActivity;
	public static CSSClass cStateBackActivity;
	
	public static CSSClass cStateMoveToBottom;
	public static CSSClass cStateMoveToFront;
	public static CSSClass cStateNoDisplay;
	
	@xTarget(HEADER.class)
	@xResource
	@xPriority(5)
	public XMLElement xStylePart() {

		return cStyle()
				.path(animated).set("animation-duration:"+SPEED_ANIMATED+"ms")
				
				.path(activity)
						.and(cStyle(cStateFixedForFreeze).set("top:0px; position: fixed; overflow:hidden "))
						.and(cStyle(cStateFrontActivity).set("z-index:"+XUIScene.ZINDEX_ANIM_FRONT+";"))	
						.and(cStyle(cStateBackActivity).set("z-index:"+XUIScene.ZINDEX_ANIM_BACK+";"))		
	
				.path(activity.and(cStateMoveToBottom)).set(
						"transform: translate3d(0px,100%,0px);"
						+ "transition:transform "+SPEED_SHOW_ACTIVITY +"ms ease-in-out;")	
				.path(activity.and(cStateMoveToFront)).set(
						"transform: translate3d(0px,0px,0px);"
						+ "transition:transform "+SPEED_SHOW_ACTIVITY +"ms ease-in-out;")  
				.path(activity.and(cStateNoDisplay)).set("display:none;")
				
				.path(activity)
						.and(cStyle(cStateZoom12).set("transform: scale3d(1.2,1.2,1)"))
						.and(cStyle(cStateZoom1).set("transform: scale3d(1,1,1)"))	
						.and(cStyle(cStateZoom09).set("transform: scale3d(0.9,0.9,1)"))
				
				.path(activityMoveForShowMenu).set(
						"transition:transform "+SPEED_SHOW_MENU+"ms ease-out;"
						+ " transform: translate3d("+(widthMenu-100)+"px,0px,0px) "
						+ "scale3d(0.95,0.95,1);")
				.path(activityMoveForHideMenu).set(
						"transition:transform "+SPEED_SHOW_MENU+"ms ease-out;"
						+ " transform: translate3d(0px,0px,0px);")

				.path(activity.and(cTransitionSpeed)).set("transition:all "+ SPEED_SHOW_ACTIVITY +"ms ease-in-out")
				.path(activity.and(cTransitionSpeedEffect)).set("transition:all "+ SPEED_ACTIVITY_TRANSITION_EFFECT +"ms ease-in-out")
								
				.path(circleAnim0prt).set("clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); -webkit-clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); ")
				.path(circleAnim100prt).set("clip-path:circle(80% at center); -webkit-clip-path:circle(80% at center); ")
				;
				
	}


	
}
