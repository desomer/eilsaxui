/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xForceInclude;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xFile(id = "ScnDataDriven")
public class ScnDataDriven extends XHTMLPart {

	static XClass cMain;
	
	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xListElem(
				xImport(JSDataSet.class),   
				xImport(JSDataDriven.class),
				xImport(JSXHTMLTemplate.class),
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.slim.min.js"),
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js")
				);
	}
	
	@xTarget(CONTENT.class) // la vue
	public XMLElement xAppShell() {
		return xDiv(xH1("LOGO"), xArticle(cMain));
	}
	
	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xListElem( xImport(JSTestDataDriven.class)	);
	}
	
	// une class JS
	public interface JSTestDataDriven extends JSClass, IXHTMLTemplate  {
		
			@xStatic(autoCall=true)          // appel automatique de la methode static
			default void main() {
				JSString urlImage = let("urlImage", JSString.value("ii"));
				
				JSTestDataDriven m = let("m", newInst(JSTestDataDriven.class));
			//	m.xTmp(urlImage);
				
				JSVariable template = let(JSVariable.class, "template", xTemplateJS(
						xDiv("OK", 
								xImg(urlImage)
						)));

				__("document.querySelector('.cMain').appendChild(", template, ")");
			}
			
			@xForceInclude
			default Object xTmp(JSString url) {
				JSVariable i = let(JSVariable.class, "i", xImage(url));
				return xTemplateJS(	xDiv("OK", i	));
			}
			
			@xForceInclude
			default Object xImage(JSString url) {
				return xTemplateJS(xImg(url) );
			}
		}
	
}
