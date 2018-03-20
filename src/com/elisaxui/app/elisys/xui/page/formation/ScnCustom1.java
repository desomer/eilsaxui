/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
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
public class ScnCustom1 extends XHTMLPart implements ICSSBuilder {

	static CSSClass cAB;
	static CSSClass cABC;

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
		return xStyle(sSel("tutu"), () -> css("display:block"));
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
		return xStyle(sSel(".toto"), () -> css("display:block"));
	}

	/***********************************************************/

	@xTarget(AFTER_BODY.class)
	@xRessource
	public XMLElement xStylePart() {

		return xListNode(
				xStyle(sMedia("all and (min-width: 1024px) and (max-width: 1280px) and (orientation: portrait)"), 
						() -> {
							sOn(sSel(cAB, ">", cABC), () ->  css("display:blocka") );
							sOn(sSel(cAB), () -> css("display:blocka") );
						}),
				
				xStyle(sSel(cAB, ">", cABC), () -> {
					css("display:blocka");
					css("display:blockb");
					sOn(sSel("&", cAB, ",", cABC), () -> {
						css("display:blockc");
						css("display:blockd");
						sOn(sSel("div,h1"), () -> {
							css("display:blocked");
							css("display:blockfd");
						});
					});
					sOn(sSel("span"), () -> {
						css("display:blocke");
						css("display:blockf");
					});
				}));
	}
}
