package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

@xFile(id = "admin.html")
@xComment("activite d'admin")
public class ScnAdminMain extends XHTMLPart {


	@xTarget(HEADER.class)
	public Element xTitle() {
		return xElement("title", "un titre");
	}

	@xTarget(CONTENT.class)
	public Element xContenu2() {
		return xDiv(xH1(xID("'test'"), "un contenu",
				xPart(new ActListPage()
						.addProperty("name", xDiv("property"))
						.addProperty("test", xDiv("handle")),
						xLi("ligne5"), xLi("ligne6"))));
	}

	// @xTarget(CONTENT.class)
	// public Element xContenu()
	// {
	// return xDiv( xH1( xID("'test'"), "toto"
	// ,xPart(new ActListPage(),
	// xLi("ligne1"),
	// xLi("ligne2"))
	// )
	// );
	// }

	// @xTarget()
	// public void xContent()
	// {
	// vBody(xElement(null, "<!--vBody-->"));
	// vAfterBody(xElement(null,"<!--vAfterBody-->"));
	// vContent(xElement(null,"<!--vContent-->"));
	// vAfterContent(xElement(null,"<!--vAfterContent-->"));
	// }

	@xTarget(AFTER_CONTENT.class)
	public Element xaAddJS() {

		JSTestClass ab = varOfType("ab", JSTestClass.class);
		JSTestClass abc = varOfType("abc", JSTestClass.class);
		
		return xListElement(
				xImport(JSTestClass.class),
				xImport(JSTest2Class.class),
				xElement("/","<script src='https://code.jquery.com/jquery-3.1.1.slim.min.js' "
						+ "integrity='sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n' "
						+ "crossorigin='anonymous'></script>"),
				
				xScriptJS(js()
						._var("a", 12)
						._var("b", xDiv(xPart(new ActListPage(), xLi("ligne ", txtVar("a")))))
						
						.__("$('body').append($(b[0]))")
						._var("c", "$(b[1])")
						.__("$.each( c, function( i, el ) {\n  if (el.nodeName=='SCRIPT') eval(el.text)\n })")
						
						._var(ab, JSTestClass._new())
						._var(abc, JSTestClass._new())
						.__(ab.console("a", "b"))
						.__(abc.console("b", "a"))
						.__(ab.test())

				));
	}
	
	// (function () {/* text + cr +lf */}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]
}
