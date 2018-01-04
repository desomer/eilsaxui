package com.elisaxui.xui.admin.test;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.xui.core.datadriven.JSDataCtx;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;

/**
 * 
 * @author Bureau
 * 
 *                  http://localhost:8080/rest/page/fr/fra/id/admin.html
 */
@xFile(id = "admin.html")
@xComment("activite d'admin")
public class ScnAdminMain extends XHTMLPart {

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xTitle() {
		return xElement("title", "un titre");
	}

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xImportJQUERY() {
		return	xElement("/","<script  src='http://code.jquery.com/jquery-3.2.1.min.js'"
				+ "  integrity='sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4='  crossorigin='anonymous'></script>"
				+"<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
				+"<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
				);
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xImportAllClass() {
		return xListElement(
				xImport(JSTestClass.class),
				xImport(JSTest2Class.class),
				xImport(JSXHTMLPart.class),
				xImport(JSTestDataDriven.class),
				xImport(JSDataDriven.class),
				xImport(JSDataSet.class),
				xImport(JSDataCtx.class)
				);
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xH1(xId("'test'"), xAttr("style", "'display:inline-block'"), "un ActListPage :",
				xPart(new ActListPage()
						.addProperty(ActListPage.PROPERTY_NAME, xDiv("property name ok"))
						.addProperty(ViewItem.TEST_HANDLE, xSpan("un example d'handle "))
						,xLi("ligne5"), xLi("ligne6"))
				   )
				);
	}
	
	
	JSTestClass ab;   
	JSTestClass abc; 
	JSXHTMLPart template; 
	JSTestDataDriven testDataDriven;
	
	@xTarget(AFTER_CONTENT.class)
	public XMLElement xAddJS() {
		return 			
				xScriptJS(js()
						
						.__(" $(\'#test\').addClass('animated pulse infinite')")
						
						.__("$.fn.insertAt = function(elements, index){\n"+
							"\tvar children = this.children();\n"+
							"\tif(index >= children.length){\n"+
							"\t\tthis.append(elements);\n"+
							"\t\treturn this;\n"+
							"\t}\n"+
							"\tvar before = children.eq(index);\n"+
							"\t$(elements).insertBefore(before);\n"+
							"\treturn this;\n"+
							"};")
						
						._var("a", txt("dyna 1"))
						._var("c", txt("dyna 2"))
						._var("t1", txt("bizaroid que ca marche"))
						
						// creation d'un template
						._var(template, xDiv(xPart(new ActListPage().addProperty(ViewItem.TEST_HANDLE, xSpan(xVar("t1")))
								, xLi(xAttr("data-d", "d"), "ligne ",  xVar("a"))
								, xLi("ligne ", xVar("c"))
								), xDiv(xAttr("style", txt("height: 800px; width:400px")),xAttr("id",txt("content")))))
						.__(template.appendInto("$('body')"))	
						
						._var(ab , _new(15))
						._var(abc, _new())
						
						.__(ab.console("a", "c"))
						.__(abc.console("c", "a"))
						.__(ab.test("'eer'"))
						
						._var(testDataDriven, _new())
						.__(testDataDriven.startTest())
						
						
						._var(template, xElement("input", xId("\"test\""), xAttr("type","\"text\"")))
						.__(template.appendInto("$('body')"))	
						
				);		
	}
	
	
	static String panel="\"padding: 5px;margin-bottom: 20px;border-radius: 0;background-color: #FFF;box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12);\"";
	
	public static XMLElement xTemplateDataDriven(Object value, Object value2)
	{
		return 	xDiv(xAttr("style", panel), xVar(value), xSpan("-"), xSpan(xVar(value2)) );	
	}
}
