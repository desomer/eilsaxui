package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;

@xFile(id = "admin.html")
@xComment("activite d'admin")
public class ScnAdminMain extends XHTMLPart {
	
//	_Construc("a","b")
//	_hMthToto("a","b")
//	_hMthToto("a","b")
//	
//	JSClass aCls = _Class("name",
//			_constructor(_Construc),
//			_fct(_hMthToto),
//			_fct("titi", "b")
//		)
//	
//	.__(inst("a")._new(aCls.class) )   
//	.__(inst("a")._hMthToto.call(12, 13) )	
	
	@xTarget(HEADER.class)
	public Element xTitle()
	{
		return xElement("title", "un titre");
	}
	
	
	@xTarget(CONTENT.class)
	public Element xContenu2()
	{
		return xDiv( xH1( xID("'test'"), "un contenu",
				xPart(new ActListPage()
						.addProperty("name", xDiv("property"))
						.addProperty("test", xDiv("handle"))
						, 
						xLi("ligne5"), 
						xLi("ligne6"))));
	}
	
//	@xTarget(CONTENT.class)
//	public Element xContenu()
//	{
//		return xDiv( xH1( xID("'test'"), "toto"
//				,xPart(new ActListPage(), 
//						xLi("ligne1"), 
//						xLi("ligne2"))
//				)
//				);
//	}
		
//	@xTarget()
//	public void xContent()
//	{
//		vBody(xElement(null, "<!--vBody-->"));
//		vAfterBody(xElement(null,"<!--vAfterBody-->"));
//		vContent(xElement(null,"<!--vContent-->"));
//		vAfterContent(xElement(null,"<!--vAfterContent-->"));
//	}
		
	@xTarget(AFTER_CONTENT.class)
	public Element xaAddJS()
	{
		
		return xListElement(
				 xListElement("\n<script src='https://code.jquery.com/jquery-3.1.1.slim.min.js' "
				 		+ "integrity='sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n' "
				 		+ "crossorigin='anonymous'></script>")	
				,xScriptJS(js()
					._var("a", 12)
					._var("b", xDiv(xPart(new ActListPage(), xLi("ligne ", txtVar("a")) )))
					.__("$('body').append($(b[0]))")
					._var("c", "$(b[1])")
					.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
//					.var("a", a._new())
//					.__(a.doTest(12))
				))
				; 
	}
	// (function () {/* text + cr +lf */}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]
}
