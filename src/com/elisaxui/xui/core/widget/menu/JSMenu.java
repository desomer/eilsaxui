/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;

/**
 * @author Bureau
 *
 */
public interface JSMenu extends JSClass {

	JSDataDriven aDataDriven = null; 
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	
	default Object getData()
	{
				
		set(aDataSet, _new())
		.__(aDataSet.setData("[]"))
		
		.set(aDataDriven, _new(aDataSet))
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					._if("ctx.row.type=='divider'")
						.set(template, ViewMenu.getTemplateMenuDivider())
			            .var("jqdom", template.append("$('.menu ul')"))
			            .__("ctx.row['_dom_']=jqdom[0]")
					._else()
			            .set(template, ViewMenu.getTemplateMenu("ctx.row.name", "ctx.row.icon", "ctx.row.idAction"))
			            .var("jqdom", template.append("$('.menu ul')"))
			            .__("jqdom.css('visibility','hidden')")
			            .__("ctx.row['_dom_']=jqdom[0]")
		            .endif()
		     //   ._else()
		      //  	.__("$(ctx.row['_dom_']).css('visibility','hidden')")
	            .endif()
        ))
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()
			))
		
		.__(aDataDriven.onChange(fct("ctx")
				._if("ctx.row['_dom_']!=null && ctx.property=='anim'")
					.var("change", "ctx.value")
					._if("!change==''")
						.__("$(ctx.row['_dom_']).css('visibility','')")
						.__("$(ctx.row['_dom_']).toggleClass('animated '+change)")
						.__("setTimeout(\n", fct("elem").__("elem.toggleClass('animated '+change)") ,",500, $(ctx.row['_dom_']))")
//					._elseif()
//						.__("$(ctx.row['_dom_']).css('visibility','hidden')")
					.endif()	
				.endif() 
				))
		
		.var("jsonMenu", aDataSet.getData())
		.__("return jsonMenu")
		
		;
		 
		 return null;
	}
}
