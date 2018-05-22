/**
 * 
 */
package com.elisaxui.doc.formation3;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
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

@xResource(id = "ScnPerf")
public class ScnPerf extends XHTMLPart {

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportVue() {
		return xListNode(
				xInclude(JSPerfVuesJS.class), 
				xInclude(JSDataSet.class),
				xInclude(JSDataDriven.class),
				xInclude(JSDomBuilder.class),
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js"),
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js")
				);
	}

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	@xPriority(1)
	public XMLElement xImport() {
		if (JSPerfVuesJS.isVueJS())
			return xListNode(xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.13/vue.min.js"));
	//	else if (JSPerfVuesJS.isJQuery())
	//		return xListElem(xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js"));
		else
			return xListNode();

	}

	// une class JS
	public interface A extends JSClass {
		@xStatic(autoCall=true)          // appel automatique de la methode static
		default void main() {
			JSPerfVuesJS p = let("p", newJS(JSPerfVuesJS.class));
			p.doPerf();
		}
	}

	@xTarget(AFTER_CONTENT.class)
	@xResource // une seule fois par vue
	@xVersion("appli")  // a terminer
	public XMLElement xDo() {		
		return xListNode(
					xInclude(A.class)
				);
	}

	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xId(xTxt("app")),  // TODO retirer xTxt   exemple xId
				xComment("ok"),
				xSpan("List of users:"),
				xUl(xId(xTxt("users")),
						xLi(xAttr("v-for", xTxt("user in users")), "{{ user.firstName }}")));
	}
}
