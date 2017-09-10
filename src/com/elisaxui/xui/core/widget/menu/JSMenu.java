/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.transition.CssTransition;

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
			            .var("jqdom", template.appendInto("$('.menu ul')"))
			            .__("ctx.row['_dom_']=jqdom[0]")
					._else()
			            .set(template, ViewMenu.getTemplateMenu("ctx.row.name", "ctx.row.icon", "ctx.row.idAction"))
			            .var("jqdom", template.appendInto("$('.menu ul')"))
			            .__("jqdom.css('visibility','hidden')")  // invisible par defaut avant animation
			            .__("ctx.row['_dom_']=jqdom[0]")
		            .endif()
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
						// remise a zero de l'animation
						.__("setTimeout(\n", fct("elem").__("elem.toggleClass('animated '+change)") ,",", CssTransition.SPEED_SHOW_MENU_ITEMS+CssTransition.DELAY_SURETE_END_ANIMATION,", $(ctx.row['_dom_']))")
					.endif()	
				.endif() 
				))
		
		.var("jsonMenu", aDataSet.getData())
		.__("return jsonMenu")
		
		;
		 
		 return null;
	}
}
