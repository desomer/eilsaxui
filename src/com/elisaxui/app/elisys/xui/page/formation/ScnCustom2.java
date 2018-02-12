/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 *    https://localhost:9998/rest/page/fr/fra/id/custom2
 */
@xFile(id = "custom2")
@xComment("activite custom2")     // commentaire a ajouter en prefixe sur les commentaire des methodes
public class ScnCustom2 extends XHTMLPart {

	JSClass1 jsClass1;
	
	@xTarget(HEADER.class)
	@xRessource									// une seule fois par vue
	public XMLElement xImportClass() {
		return xListElem(   // ajout plusieur element sans balise parent
					xImport(JSClass1.class),    // xImport( cl,  INLINE)  OU  xImport( cl,  moduleJS("test.js"))
					
					xScriptJS(js()
							._var(jsClass1, _new())
							.__(jsClass1.doSomething())
							)
					);
	};
		
}
