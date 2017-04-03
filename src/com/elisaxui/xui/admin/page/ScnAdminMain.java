package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
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
						.addProperty("testHandle", xDiv("un example d'handle "))
						,xLi("ligne5"), xLi("ligne6"))
				   )
				);
	}

	@xTarget(CONTENT.class)
	@xRessource
	public Element xResource() {
		return xListElement(
				xImport(JSTestClass.class),
				xImport(JSTest2Class.class),
				xImport(JSXHTMLPart.class)
				);
	}
	
	@xTarget(AFTER_CONTENT.class)
	public Element xaAddJS() {

		JSTestClass ab = varOfType("ab", JSTestClass.class);
		JSTestClass abc = varOfType("abc", JSTestClass.class);
		JSXHTMLPart template = varOfType("template", JSXHTMLPart.class);

		return xListElement(
				xElement("/","<script src='https://code.jquery.com/jquery-3.1.1.slim.min.js' "
						+ "integrity='sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n' "
						+ "crossorigin='anonymous'></script>"),
				
				xScriptJS(js()
						.var("a", 12)
						.var("c", txt("ok"))
						// creation d'un template
						.var(template, xDiv(xPart(new ActListPage()
								, xLi("ligne ", xVar("a"))
								, xLi("ligne ", xVar("c"))
								)))
						.__(template.append("$('body')"))	
						
						.var(ab, JSTestClass._new(15))
						.var(abc, JSTestClass._new())
						
						.__(ab.console("a", "c"))
						.__(abc.console("c", "a"))
						.__(ab.test())

				));
	}
	
	// (function () {/* text + cr +lf */}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]
}
