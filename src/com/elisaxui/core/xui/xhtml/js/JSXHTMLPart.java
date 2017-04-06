package com.elisaxui.core.xui.xhtml.js;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;

/**
 * 
 * gestion des template xmlpart + js
 * 
 * @author Bureau
 *
 */
public interface JSXHTMLPart extends JSClass {

	Object html=null;
	Object js=null;
	
	default Object constructor(Object h, Object j) {
		return set(html, h)
			  .set(js, j)
				;
	}
	
	default Object append(Object parent)
	{
		return 
		__(parent,".append($(this.html))")
		.var("c", "$(this.js)")
		.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
		;
	}
	
//	static Object _new() {
//		return JSClass._new(JSXHTMLPart.class);
//	}
}
