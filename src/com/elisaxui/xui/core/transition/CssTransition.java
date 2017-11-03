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
import static com.elisaxui.xui.core.transition.ConstTransition.*;

@xComment("CssTransition")
public class CssTransition extends XHTMLPart {

	public static final int widthMenu = 250;

	public static XClass activity;
	
	static XClass fixedForAnimated;
	static XClass active;
	static XClass animated;
	public static XClass detach;
	
	@xTarget(HEADER.class)
	@xRessource
	@xPriority(5)
	public XMLElement xStyle() {

		return xCss()
				.select(activity.and(fixedForAnimated)).set("top:0px; position: fixed; transition:transform "+SPEED_SHOW_ACTIVITY +"ms linear;")
				.select(animated).set("animation-duration:"+SPEED_ANIMATED+"ms")
				
				.on(".activity.backToFront", "transition:transform "+(SPEED_SHOW_ACTIVITY+100)+"ms linear;")
				
				.on(".activity.toback", "transition:transform "+(SPEED_SHOW_ACTIVITY+100)+"ms linear; "
						+ "transform:translate3d(0px,0px,0px) scale(0.9); ")
				
				.on(".activity.frontActivity", "z-index:"+XUIScene.ZINDEX_ANIM_FRONT+";")
				.on(".activity.backActivity", "z-index:"+XUIScene.ZINDEX_ANIM_BACK+";")
				
				.on(".activity.toHidden", "transform: translate3d(0px,100%,0px);")				
				.on(".activity.tofront", "transform: translate3d(0px,0px,0px);")  
				.on(".activity.nodisplay", "display:none;")
				
				.on(".activity.circleAnim0prt", "clip-path:circle(0.0% at 85vw 90vh); -webkit-clip-path:circle(0.0% at 85vw 90vh); ")
				.on(".activity.circleAnim100prt", "clip-path:circle(80% at center); -webkit-clip-path:circle(80% at center); ")

				.on(".activity.zoom12", "transform:scale3d(1.2,1.2,1)")
				.on(".activity.zoom10", "transform:scale3d(1,1,1)")

				.on(".activity.transitionSpeedSlow", "transition:all "+ SPEED_SHOW_ACTIVITY*5 +"ms linear")
				.on(".activity.transitionSpeed", "transition:all "+ SPEED_SHOW_ACTIVITY +"ms linear")
				.on(".activity.transitionSpeedx2", "transition:all "+ SPEED_ACTIVITY_TRANSITION_EFFECT +"ms linear")
				
				.on(".activityMoveForShowMenu", "transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d("+(widthMenu-100)+"px,0px,0px);")
				.on(".activityMoveForHideMenu", "transition:transform "+SPEED_SHOW_MENU+"ms ease-out; transform: translate3d(0px,0px,0px);")
				;
				
	}


	
}
