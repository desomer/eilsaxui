package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataCtx;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

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
	public Element xTitle() {
		return xElement("title", "un titre");
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportJQUERY() {
		return	xElement("/","<script  src='http://code.jquery.com/jquery-3.2.1.min.js'"
				+ "  integrity='sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4='  crossorigin='anonymous'></script>"
				+"<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
				);
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
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
	public Element xContenu() {
		return xDiv(xH1(xID("'test'"), "un ActListPage :",
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
	
	public static Element xTemplateDataDriven(Object value, Object value2)
	{
		return 	xDiv(xVar(value), xSpan("-"), xVar(value2) );	
	}
	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return 			
				xScriptJS(js()
						
						.__(" (function customSwipe(element) {\n"+
							" element\n"+
							" .animate({\"margin-left\" : \"150px\"}, 1000)\n"+
							" .animate({\"margin-left\" : \"50px\"}, 1000, function(){\n"+
							" setTimeout(function(){\n"+
							" customSwipe(element);\n"+
							" }, 1);\n"+
							" });\n"+
							" })($(\'#test\'))")
						
						.var("a", txt("dyna 1"))
						.var("c", txt("dyna 2"))
						.var("t1", txt("bizaroid que ca marche"))
						// creation d'un template
						.var(template, xDiv(xPart(new ActListPage().addProperty(ViewItem.TEST_HANDLE, xSpan(xVar("t1")))
								, xLi(xAttr("data-d", "d"), "ligne ",  xVar("a"))
								, xLi("ligne ", xVar("c"))
								)))
						.__(template.append("$('body')"))	
						
						.var(ab , _new(15))
						.var(abc, _new())
						
						.__(ab.console("a", "c"))
						.__(abc.console("c", "a"))
						.__(ab.test("'eer'"))
						
						.var(testDataDriven, _new())
						.__(testDataDriven.startTest())
						
						
						.var(template, xElement("input",xAttr("id", "\"test\""), xAttr("type","\"text\"")))
						.__(template.append("$('body')"))	

//						.__( "// select the target node\n"+
//								"var target = document.getElementById(\'test\');\n"+
//								" \n"+
//								"// create an observer instance\n"+
//								"var observer = new MutationObserver(function(mutations) {\n"+
//								" mutations.forEach(function(mutation) {\n"+
//								" console.log(mutation.type);\n"+
//								" }); \n"+
//								"});\n"+
//								" \n"+
//								"// configuration of the observer:\n"+
//								"var config = { attributes: true, childList: true, characterData: true };\n"+
//								" \n"+
//								"// pass in the target node, as well as the observer options\n"+
//								"observer.observe(target, config);\n"+
//								" \n"+
//								"// later, you can stop observing\n"
//								//+"observer.disconnect()"
//								)
						
				);
				
	}
	
	
//	@xTarget(AFTER_CONTENT.class)
	public Element xTest()
	{
		
		return 	xScriptJS(js().var("v", " [ {a:1, b:'12'},{a:2, b:'22'} ]")
				
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
				);
	}
	
	
	
	// (function () {/* text + cr +lf */}).toString().match(/[^]*\\/\\*([^]*)\\*\\/\\}$/)[1]
}
