/**
 * 
 */
package com.elisaxui.component.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */

public class CssReset extends XHTMLPart implements ICSSBuilder {

	@xTarget(HEADER.class)
	@xResource()
	@xPriority(5)
	public XMLElement xStylePart() {
		return xStyle(sMedia("all"), () -> {
			sOn(sSel("*, *:before, *:after"), () -> { 
				css("box-sizing:border-box"); 
				css("-webkit-tap-highlight-color: rgba(0,0,0,0)"); 
				css("margin:0;padding : 0"); 
			});
			
			sOn(sSel("html"), () -> { 
				css("line-height: 1.15"); 
			});
		});
	}
	
}
