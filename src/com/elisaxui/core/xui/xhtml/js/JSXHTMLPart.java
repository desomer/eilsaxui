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
	/** TODO creer le template en script type = "text/template"   */
	default Object append(Object parent)
	{
		return 
		var("jqdom", "$(this.html)")		
		.__(parent,".append(jqdom)")
		.var("c", "$(this.js)")
		.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
		.__("return jqdom")
		;
	}
	
	default Object insertAt(Object parent, Object idx)
	{
		return 
		var("jqdom", "$(this.html)")		
		.__(parent,".insertAt(jqdom, idx)")
		.var("c", "$(this.js)")
		.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
		.__("return jqdom")
		;
	}
	
//	static Object _new() {
//		return JSClass._new(JSXHTMLPart.class);
//	}
}
