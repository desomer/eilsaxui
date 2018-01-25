/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */

@xFile(id = "ScnPerf")
public class ScnPerf extends XHTMLPart {

	JSPerfVuesJS perfVuesJS = null;
	
	
	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xImport(JSPerfVuesJS.class);
	}
	
	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return  JSPerfVuesJS.isVueJS() ? 
				xListElement(
					xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.13/vue.min.js")
				): JSPerfVuesJS.isJQuery()?  xListElement(
					xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js"))
					: null
					
				; 
				
	}
	
	
	@xTarget(AFTER_CONTENT.class)
	@xRessource // une seule fois par vue
	public XMLElement xDo() {
		return xListElement(
				xScriptJS(js()
						._var(perfVuesJS, _new())
						.__(perfVuesJS.doPerf())
						)
				); 
				
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv( xId(xTxt("app")), 
				xSpan("List of users:"),
				xUl( xId(xTxt("users")), 
						xLi( xAttr("v-for", xTxt("user in users")), "{{ user.first_name }}"))
				);
	}
}
