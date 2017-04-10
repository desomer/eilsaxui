package com.elisaxui.xui.admin.page;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;

public interface JSTestDataDriven extends JSClass {

	JSDataDriven aDataDriven = null; 
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	
//	default Object constructor()
//	{
//		return set(aDataDriven, null)
//			.set(aDataSet, null)	;
//	}
	
	default Object createRow()
	{
		return __("return ",
				fct("value")
	            .set(template, ScnAdminMain.xTemplateDataDriven("value.a", "value.b"))
	            .__(template.append("$('body')"))  		
			);
	}
	
	default Object startTest()
	{
		return var("v", " [ {a:15, b:'12'},{a:21, b:'22'} ]")
				
				.set(aDataSet, _new("v"))
				.set(aDataDriven, _new(aDataSet))
				//.__(aDataDriven.onEnter("function( value ) { console.debug('on entre', value) }"))
				//.__(aDataDriven.onEnter(createRow()))
				.__(aDataDriven.onEnter(fct("value")
			            .set(template, ScnAdminMain.xTemplateDataDriven("value.a", "value.b"))
			            .__(template.append("$('body')"))
	            ))
				.__(aDataDriven.start())
				;
	}
}
