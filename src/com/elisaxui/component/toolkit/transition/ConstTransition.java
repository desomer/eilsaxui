/**
 * 
 */
package com.elisaxui.component.toolkit.transition;

/**
 * @author Bureau
 *
 */
public class ConstTransition {

	public static final String ANIM_FROM_BOTTOM ="fromBottom";
	public static final String ANIM_FROM_RIPPLE ="ripple";
	
	public static final double COEF_ANIM = 1.5;
	public static final int SPEED_SHOW_ACTIVITY = (int)(200*COEF_ANIM);
	public static final int SPEED_ACTIVITY_TRANSITION_EFFECT = (int)(150*COEF_ANIM);
	public static final int DELAY_SURETE_END_ANIMATION = 50;
	
	public static final String NEXT_FRAME = "'nextFrame'";
	public static final int BUG_NEXT_FRAME_CHROME_V67 = 100;
	
	public static final int SPEED_ANIMATED = (int)(1000*COEF_ANIM);
	
	public static final int SPEED_SHOW_MENU = (int)(150*COEF_ANIM);
	public static final int SPEED_SHOW_MENU_ITEMS = (int)(400*COEF_ANIM);
	public static final int SPEED_SHOW_MENU_ITEMS_ANIM = (int)(50*COEF_ANIM);
	
	public static final int SPEED_RIPPLE_EFFECT =  (int)(300*COEF_ANIM);
	public static final int SPEED_BURGER_EFFECT =  (int)(200*COEF_ANIM);
	
	public static final int SPEED_ANIM_SCROLL =  (int)(150*COEF_ANIM);
	
	public static final String PERFORM_3D = "backface-visibility: hidden; transform: translate3d(0px,0px,0px); will-change:transform;";
	public static final String PERFORM_CHANGE_OPACITY = "will-change:opacity, display;";
	
	public static final int ZINDEX_ANIM_FRONT = 1;
	public static final int ZINDEX_ANIM_BACK = 0;

	public static final int ZINDEX_NAV_BAR = 1;
	public static final int ZINDEX_MENU = 2;
	public static final int ZINDEX_FLOAT = 3;
	public static final int ZINDEX_OVERLAY = 4;	
		
}
