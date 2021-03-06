package com.elisaxui.test;

import com.elisaxui.component.toolkit.datadriven.JSDataCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * 
 * @author Bureau
 * 
 *                  http://localhost:8080/rest/page/fr/fra/id/admin.html
 */
@xResource(id = "admin.html")
@xComment("activite d'admin")
public class ScnAdminMain extends XHTMLPart {

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xTitle() {
		return xNode("title", "un titre");
	}

	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImportJQUERY() {
		return	xNode("/","<script  src='http://code.jquery.com/jquery-3.2.1.min.js'"
				+ "  integrity='sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4='  crossorigin='anonymous'></script>"
				+"<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
				+"<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
				);
	}
	
	@xTarget(HEADER.class)
	@xResource
	public XMLElement xImportAllClass() {
		return xListNode(
				xIncludeJS(JSTestClass.class),
				xIncludeJS(JSTest2Class.class),
				xIncludeJS(JSXHTMLPart.class),
				xIncludeJS(JSTestDataDriven.class),
				xIncludeJS(JSDataDriven.class),
				xIncludeJS(JSDataSet.class),
				xIncludeJS(JSDataCtx.class)
				);
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(xH1(xId("'test'"), xAttr("style", "'display:inline-block'"), "un ActListPage :",
				vPart(new ActListPage()
						.vProperty(ActListPage.PROPERTY_NAME, xDiv("property name ok"))
						.vProperty(ViewItem.TEST_HANDLE, xSpan("un example d'handle "))
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
						._var(template, xDiv(vPart(new ActListPage().vProperty(ViewItem.TEST_HANDLE, xSpan(xVar("t1")))
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
						
						
						._var(template, xNode("input", xId("\"test\""), xAttr("type","\"text\"")))
						.__(template.appendInto("$('body')"))	
						
				);		
	}
	
	
	static String panel="\"padding: 5px;margin-bottom: 20px;border-radius: 0;background-color: #FFF;box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12);\"";
	
	public static XMLElement xTemplateDataDriven(Object value, Object value2)
	{
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return 	xDiv(xAttr("style", panel), xVar(value), xSpan("-"), xSpan(xVar(value2)) );	
			}
		};
		
		return template.getTemplate();
	}
}
