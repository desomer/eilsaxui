package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLPart;

public class ActListPage extends XUIViewXHtml {

	@Override
	public void doContent(XMLPart root) {
		vContent( xSpan( "ok", 	
				  xUl( this.getChildren()) ).setComment("ActListPage") );
		
		vBody(xSpan("super"));
		
		vAfter ( xScriptJS("console.debug('ok')").setComment("ActListPage"));
	}

}
