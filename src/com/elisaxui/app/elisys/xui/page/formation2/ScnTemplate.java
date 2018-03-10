/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSONType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth 
 * TODO xImport( Module("mod.js", JSDataSet, JSDataDriven,
 *         JSXHTMLTemplate ) )
 */
@xFile(id = "ScnTemplate")
public class ScnTemplate extends XHTMLPart {

	static XClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xList(
				xImport(JSXHTMLTemplate.class)
				);
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("LOGO"), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTestTemplate.class);
	}

	// une class JS
	@xTarget(AFTER_CONTENT.class)   // une seule fois par vue car class  ,  a mettre @xTarget sur la JSClass pour retirer l'import
	public interface JSTestTemplate extends JSClass, IXHTMLTemplate, IJSONBuilder {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			ImgType data = let("data", newJS(ImgType.class).asLitteral());   // affecter literal dans le newInst
			data.name().set("Votre creation");
			data.urlImage().set("https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb");			
			
			document().querySelector(cMain).appendChild(xImageOK(data.name(), data.urlImage()));
		}

		@xStatic
		default Object xImageOK(JSAny text, JSString url) {
			return jsTemplate(xDiv(text, xPicture(url) ));
		}

		@xStatic
		default Object xPicture(JSString url) {
			return jsTemplate(xImg(xAttr("src", url)));
		}
	}

	public interface ImgType extends JSONType {
		JSString urlImage();
		JSString name();
	}
	
}
