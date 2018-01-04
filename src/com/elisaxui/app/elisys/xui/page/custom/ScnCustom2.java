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
import com.elisaxui.xui.core.widget.menu.JSMenu;

/**
 * @author gauth
 *
 *    https://localhost:9998/rest/page/fr/fra/id/custom2
 */
@xFile(id = "custom2")
@xComment("activite custom2")     // commentaire a ajouter en prefixe sur les commentaire des methodes
public class ScnCustom2 extends XHTMLPart {

	static JSClass1 jsClass1;
	
	@xTarget(HEADER.class)
	@xRessource									// une seule fois par vue
	public XMLElement xImportClass() {
		return xListElement(   // ajout plusieur element sans balise parent
					xImport(JSClass1.class),
					xScriptJS(js()
							.var(jsClass1, _new())
							.__(jsClass1.doSomething())
							)
					);
	};
		
}
