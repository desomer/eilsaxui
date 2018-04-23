package com.elisaxui.core.xui.xhtml;

import java.util.Collections;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.module.ImportDesc;
import com.elisaxui.core.xui.xhtml.builder.module.ModuleDesc;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;


public class XHTMLRootResource extends XHTMLPart {

	public void addElementOnModule(ModuleDesc moduleDesc, XMLElement elem)
	{
		if (moduleDesc.isES6Module())
		{
			int i=0;
			for (ImportDesc aImport : moduleDesc.getListImport()) {
				String newLine = "";
				if (i>0 && XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCrXML())
					newLine = "\n";
				i++;
				addElementOnTarget(HEADER.class, xNode(null, newLine+"import {"+aImport.getExport()+"} from '/rest/js/"+aImport.getURI()+"';") ); 
			}
		}
		addElementOnTarget(BODY.class, elem);
	}
	
	@xTarget(CONTENT.class)
	@xResource
	public XMLElement xGetContent() {
		
		List<XMLElement> body = getListElementFromTarget(BODY.class);
		List<XMLElement> afterBody = getListElementFromTarget(AFTER_BODY.class);
		List<XMLElement> header = getListElementFromTarget(HEADER.class);
		
		Collections.sort(header, new XHTMLRoot.XMLElementComparator() );
		Collections.sort(body, new XHTMLRoot.XMLElementComparator() );
		Collections.sort(afterBody, new XHTMLRoot.XMLElementComparator() );
		
		return xListNode( header, body, afterBody );
	}

}
