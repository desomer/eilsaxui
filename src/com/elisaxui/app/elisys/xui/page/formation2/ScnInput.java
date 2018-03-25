/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import com.elisaxui.component.widget.input.ViewInputText;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 */
@xFile(id = "ScnInput")
public class ScnInput extends XHTMLPart {

	static CSSClass cMain;
	
	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImportVue() {
		return xListNode(
				xImport(JSDomBuilder.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		return xDiv(xH1("ScnInput"), xArticle(cMain, 
				vPart(new ViewInputText().vProperties(ViewInputText.pLabel, "Color")),
				vPart(new ViewInputText().vProperties(ViewInputText.pLabel, "Font size"))
				)
				);
	}


}
