package com.elisaxui.core.xui.xhtml.builder.javascript.template;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;

/**
 * 
 * gestion des template xmlpart + js
 * 
 * @author Bureau
 *
 */
@Deprecated
public interface JSXHTMLPart extends JSClass {

	JSString html=null;
	JSString js=null;
	
	default Object constructor(Object h, Object j) {
		return _set(html, h)
			  ._set(js, j)
				;
	}
	
	/** TODO creer le template en script type = "text/template"   */
	default Object appendInto(Object parent)
	{
		return 
		_var("jqdom", "$(this.html)")		
		.__(parent,".append(jqdom)")
		._var("c", "$(this.js)")
		.__("$.each( c, function( i, el ) {  if (el.nodeName=='SCRIPT') eval(el.text) })")
		.__("return jqdom")
		;
	}
	
	default Object insertAt(Object parent, Object idx)
	{
		return 
		_var("jqdom", "$(this.html)")		
		.__(parent,".insertAt(jqdom, idx)")
		._var("c", "$(this.js)")
		.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
		.__("return jqdom")
		;
	}
	
}
