/**
 * 
 */
package com.elisaxui.doc.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xComment;

@xResource(id = "ScnTemplate2")
public class ScnTemplate2 extends XHTMLPart {

	
	/********************************************************/
	// la vue
	/********************************************************/
	static CSSClass cMain;

	@xTarget(HEADER.class)
	@xResource // une seule fois par vue
	public XMLElement xImportVue() {
		return xIncludeJS(JSDomBuilder.class);
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("Scn Template 2"), vPart(new CmpButton2().vProperty(CmpButton2.PROPERTY_LABEL, "OK 2")), xArticle(cMain));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xIncludeJS(JSTestTemplate2.class);
	}

	/********************************************************/
	// une class JS
	/********************************************************/
	@xTarget(AFTER_CONTENT.class) // une seule fois par vue car class , a mettre @xTarget sur la JSClass pour retirer l'import
	public interface JSTestTemplate2 extends JSClass, IJSNodeTemplate, IJSONBuilder {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {
			document().querySelector(cMain).appendChild(
					xPicture(JSString.value(
							"https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb")));
		}

		@xStatic
		default JSNodeElement xPicture(JSString url) {
			return createNodeTemplate(xListNode(
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
		@xResource // une seule fois par vue
		public XMLElement xImportCss() {
			return cStyle().path("button").set("background-color: #4CAF50;");
		}

	}
}
