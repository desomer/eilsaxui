/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

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
		return xListNode( // ajout plusieur element sans balise parent
				xTitle("titre"),
				xMeta(xAttr("name", xTxt("author")), xAttr("content", xTxt("desomer"))));
	}

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportCss() {
		return xStyle().path("tutu").set("display: none;");
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
		return xStyle().path("import").set("display: none;");
	}

	/***********************************************************/

	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {
		return xStyle()
				/**********************************/
				.path(".toto1").set("display: none;")
					.and(xStyle(":toto2").set("display: none").set(";display: none")
								.and(xStyle(":toto3").set("display: none;"))
								.andChild(xStyle("img").set("display: none;"))
								.andDirectChild(xStyle("span").set("display: none;"))
						)
					.or(xStyle("titi").or(xStyle("tutu")	
							.set("display: none;")
							.and(xStyle(":titi1").set("display: none;"))
							))
					.or(xStyle("tata").set("display: none;"))
					.andChild(xStyle(".toto5").set("display: none;"))
				/***************************************/
				.path("#toto6").set("display: block2;")
				    .andChild(xStyle(".eee").or(xStyle(".fff")).set("display: block;"))
		
				/****************************************/
				;
	}
}
