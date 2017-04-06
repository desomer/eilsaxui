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
	@xRessource
	public Element xTitle() {
		return xElement("title", "un titre");
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportJQUERY() {
		return		xElement("/","<script src='https://code.jquery.com/jquery-3.1.1.slim.min.js' "
						+ "integrity='sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n' "
						+ "crossorigin='anonymous'></script>");
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
		return xListElement(
				xImport(JSTestClass.class),
				xImport(JSTest2Class.class),
				xImport(JSXHTMLPart.class)
				);
	}
	
	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xH1(xID("'test'"), "un ActListPage :",
				xPart(new ActListPage()
						.addProperty("name", xDiv("property name ok"))
						.addProperty("testHandle", xSpan("un example d'handle "))
						,xLi("ligne5"), xLi("ligne6"))
				   )
				);
	}
	
	
	JSTestClass ab;   
	JSTestClass abc; 
	JSXHTMLPart template; 
	
	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xListElement(				
				xScriptJS(js()
						.var("a", txt("dyna 1"))
						.var("c", txt("dyna 2"))
						.var("t1", txt("bizaroid que ca marche"))
						// creation d'un template
						.var(template, xDiv(xPart(new ActListPage().addProperty("testHandle", xSpan(xVar("t1")))
								, xLi(xAttr("data-d", "d"), "ligne ",  xVar("a"))
								, xLi("ligne ", xVar("c"))
								)))
						.__(template.append("$('body')"))	
						
						.var(ab , _new(15))
						.var(abc, _new())
						
						.__(ab.console("a", "c"))
						.__(abc.console("c", "a"))
						.__(ab.test("'eer'"))

				),
				
				xScriptJS(js().var("v", " [ {a:1, b:'12'},{a:2, b:'22'} ]")
						
						.__("var changeHandler = {\n"+
								" get: function(target, property) {\n"+
								" console.log(\'getting \' , property , \' for \' , target);\n"+
								" // property is index in this case\n"+
								" return target[property];\n"+
								" },\n"+
								" set: function(target, property, value, receiver) {\n"+
								" console.log(\'setting \' , property , \' for \' , target , \' with value \' , value);\n"+
								" target[property] = value;\n"+
								" // you have to return true to accept the changes\n"+
								" return true;\n"+
								" }\n"+
								"};")
						
						.var("arrayToObserve", "new Proxy(v, changeHandler)")
						.var("objs", "new Proxy({a:3, b:'23'}, changeHandler)")
						.__("arrayToObserve.push( objs )")
						.__("objs.a=55")
						)
				
				);
		
		
	}
	
	// (function () {/* text + cr +lf */}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]
}
