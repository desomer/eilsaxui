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
	
	static XClass fixedForAnimated;
	static XClass animated;
	
	public static XClass active;
	public static XClass inactive;
	public static XClass detach;
	
	public static XClass circleAnim0prt;
	public static XClass circleAnim100prt;
	
	public static XClass zoom09;
	public static XClass zoom10;
	public static XClass zoom12;
	
	public static XClass transitionSpeedSlow;
	public static XClass transitionSpeed;
	public static XClass transitionSpeedx2;
	
	public static XClass transformSpeed;

	public static XClass activityMoveForShowMenu;
	public static XClass activityMoveForHideMenu;
	
//	public static XClass backToFront;
//	public static XClass toback;
	public static XClass frontActivity;
	public static XClass backActivity;
	
	public static XClass toBottom;
	public static XClass tofront;
	public static XClass nodisplay;
	
	@xTarget(HEADER.class)
	@xRessource
	@xPriority(5)
	public XMLElement xStyle() {

		return xCss()
				.select(animated).set("animation-duration:"+SPEED_ANIMATED+"ms")
				
				.select(activity.and(fixedForAnimated)).set("top:0px; position: fixed; transition:transform "+SPEED_SHOW_ACTIVITY +"ms linear;")
				
//				.select(activity.and(backToFront)).set("transition:all "+(SPEED_SHOW_ACTIVITY+100)+"ms linear;")		
//				.select(activity.and(toback)).set("transition:all "+(SPEED_SHOW_ACTIVITY+100)+"ms linear; ")
				
				.select(activity.and(frontActivity)).set("z-index:"+XUIScene.ZINDEX_ANIM_FRONT+";")
				.select(activity.and(backActivity)).set("z-index:"+XUIScene.ZINDEX_ANIM_BACK+";")
				
				//.on(".activity.toBottom", "transform: translate3d(100%,0px,0px);")	
				.select(activity.and(toBottom)).set("transform: translate3d(0px,100%,0px);")	
				.select(activity.and(tofront)).set("transform: translate3d(0px,0px,0px);")  
				.select(activity.and(nodisplay)).set("display:none;")
				
				.select(activity.and(circleAnim0prt)).set("clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); -webkit-clip-path:circle(0.0% at "+ViewOverlayRipple.START_POINT+"); ")
				.select(activity.and(circleAnim100prt)).set("clip-path:circle(80% at center); -webkit-clip-path:circle(80% at center); ")

				.select(activity.and(zoom12)).set("transform:translate3d(0px,0px,0px) scale3d(1.2,1.2,1)")
				.select(activity.and(zoom10)).set("transform:translate3d(0px,0px,0px) scale3d(1,1,1)")
				 .select(activity.and(zoom09)).set("transform:translate3d(0px,0px,0px) scale3d(0.9,0.9,1)")

				.select(activity.and(transitionSpeedSlow)).set("transition:all "+ SPEED_SHOW_ACTIVITY*5 +"ms linear")
				.select(activity.and(transitionSpeed)).set("transition:all "+ SPEED_SHOW_ACTIVITY +"ms linear")
				.select(activity.and(transitionSpeedx2)).set("transition:all "+ SPEED_ACTIVITY_TRANSITION_EFFECT +"ms linear")
				
				.select(activityMoveForShowMenu).set("transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d("+(widthMenu-100)+"px,0px,0px);")
				.select(activityMoveForHideMenu).set("transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d(0px,0px,0px);")
				;
				
	}


	
}
