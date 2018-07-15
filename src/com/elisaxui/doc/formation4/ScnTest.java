/**
 * 
 */
package com.elisaxui.doc.formation4;

import java.util.ArrayList;

import com.elisaxui.component.toolkit.transition.ConstTransition;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnTest")
@xComment("Scene test")
public class ScnTest extends XHTMLPart implements ICSSBuilder {

	@xTarget(HEADER.class)
	public XMLElement xCss()
	{
		return xElem(
				
				xStyle(() -> {
					sOn(sSel("header"), () -> {
						css("top: 0px; width: 100%; position: fixed");
						css("height:50px");
						css("background: linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%);");
						css("box-shadow: 20px 6px 12px 9px rgba(0, 0, 0, 0.22), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2)");
						css("transition: all " + ConstTransition.SPEED_ANIM_SCROLL + "ms linear"); // ease-in-out 
					});
					
					sOn(sSel("footer"), () -> {
						css("bottom: 0px; width: 100%; position: fixed");
						css("height:50px");
						css("background: linear-gradient(to bottom, rgb(255, 191, 97) 0%, rgb(255, 152, 0) 64%, rgb(241, 197, 133) 100%);");
						css("box-shadow: 16px -14px 20px 0 rgba(0, 0, 0, 0.21), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);");
						css("transition: all " + ConstTransition.SPEED_ANIM_SCROLL + "ms linear"); // ease-in-out 
					});
				})
				
				);
	}
	
	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xRoot()
	{
		ArrayList<XMLElement> array = new ArrayList<>();
		
		for (int i = 0; i < 100; i++) {
			array.add(xDiv("A"+i));
		}
		
		return xDiv( 
				xHeader("HEADER"),
				array,
				xFooter("FOOTER")
				);
	}
	
	@xTarget(AFTER_BODY.class)
	public XMLElement xTest()
	{
		return xElem(JSRequestAnimationFrame2.class);
	}
	
}
