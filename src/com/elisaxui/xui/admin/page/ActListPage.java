package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLPart;

public class ActListPage extends XUIViewXHtml {

	@Override
	public void doContent(XMLPart root) {
		xContent( xSpan( "ok", 	
				  xUl( this.getChildren()) ));
		
		xAfter ( xScriptJS("console.debug('ok')"));
	}

}
