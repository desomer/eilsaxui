/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.perf;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xForceInclude;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.annotation.xVersion;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */

@xFile(id = "ScnPerf")
public class ScnPerf extends XHTMLPart {

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xImport(JSPerfVuesJS.class);
	}

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	@xPriority(1)
	public XMLElement xImport() {
		if (JSPerfVuesJS.isVueJS())
			return xListElem(xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.13/vue.min.js"));
		else if (JSPerfVuesJS.isJQuery())
			return xListElem(xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js"));
		else
			return xListElem();

	}

	public interface A extends JSClass {
		@xStatic(autoCall=true)          // appel automatique de la methode static
		default void main() {
			JSPerfVuesJS p = let("p", newInst(JSPerfVuesJS.class));
			p.doPerf();
		}
	}

	@xTarget(AFTER_CONTENT.class)
	@xRessource // une seule fois par vue
	@xVersion("1.2")  // a terminer
	public XMLElement xDo() {		
		return xListElem(
					xImport(A.class)
				);
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xId(xTxt("app")),
				xSpan("List of users:"),
				xUl(xId(xTxt("users")),
						xLi(xAttr("v-for", xTxt("user in users")), "{{ user.firstName }}")));
	}
}
