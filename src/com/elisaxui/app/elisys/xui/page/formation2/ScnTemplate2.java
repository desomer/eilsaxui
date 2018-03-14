/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;

@xFile(id = "ScnTemplate2")
public class ScnTemplate2 extends XHTMLPart {

	
	/********************************************************/
	// la vue
	/********************************************************/
	static CSSClass cMain;

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xImport(JSDomBuilder.class);
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("Scn Template 2"), vPart(new CmpButton2().vProperty(CmpButton2.PROPERTY_LABEL, "OK 2")), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTestTemplate2.class);
	}

	/********************************************************/
	// une class JS
	/********************************************************/
	@xTarget(AFTER_CONTENT.class) // une seule fois par vue car class , a mettre @xTarget sur la JSClass pour retirer l'import
	public interface JSTestTemplate2 extends JSClass, IJSDomTemplate, IJSONBuilder {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			document().querySelector(cMain).appendChild(
					xPicture(JSString.value(
							"https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb")));
		}

		@xStatic
		default JSDomElement xPicture(JSString url) {
			return createDomTemplate(xListNode(
					vPart(new CmpButton2().vProperty(CmpButton2.PROPERTY_LABEL, "OK")),
					xImg(xAttr("src", url))));
		}

	}
	/********************************************************/
	// un component
	/********************************************************/
	static class CmpButton2 extends XHTMLPart {

		@xComment("change-Nom")
		CSSClass cCmpButton;

		public static final String PROPERTY_LABEL = "PROPERTY_LABEL";

		@xTarget(CONTENT.class)
		public XMLElement xBtn() {
			return xButton(cCmpButton, this.<Object>vProperty(PROPERTY_LABEL));
		}

		@xTarget(HEADER.class)
		@xRessource // une seule fois par vue
		public XMLElement xImportCss() {
			return xStyle().path("button").set("background-color: #4CAF50;");
		}

	}
}
