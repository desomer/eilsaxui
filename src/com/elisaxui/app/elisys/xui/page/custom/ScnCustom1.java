/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.custom;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 *         https://localhost:9998/rest/page/fr/fra/id/custom1
 */
@xFile(id = "custom1")
@xComment("activite custom1") // commentaire a ajouter en prefixe sur les commentaire des nom de methodes
public class ScnCustom1 extends XHTMLPart {

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportMeta() {
		return xListElement( // ajout plusieur element sans balise parent
				xTitle("titre"),
				xMeta(xAttr("name", xTxt("author")), xAttr("content", xTxt("desomer"))));
	}

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportCss() {
		return xStyle().path("tutu").add("display: none;");
	}

	@xTarget(BODY.class)
	@xRessource
	public XMLElement xBody() {
		return xDiv("body");
	}

	/********************** AJOUT DANS PARENT : ICI BODY ******************/
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xH1("test1");
	}

	@xTarget(CONTENT.class)
	@xPriority(1) // ordre dans le content par defaut 100
	public XMLElement xContenu2() {
		return xH1("test2");
	}

	@xTarget(AFTER_CONTENT.class)
	@xRessource
	@xPriority(200) // par defaut 100
	public XMLElement xImportStyle() {
		return xStyle().path("import").add("display: none;");
	}

	/***********************************************************/

	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {
		return xStyle()
				.path(".toto1").add("display: none;")
				.andPath(xStyle(":toto2").add("display: none").add(";display: none")
						.andPath(xStyle(":toto3").add("display: none;"))
						.childPath(xStyle("img").add("display: none;")))
				.childPath(xStyle(".toto5").add("display: none;"))
				.path("#toto6").add("display: none;");
	}
}
