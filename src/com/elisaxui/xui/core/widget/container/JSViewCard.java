/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.button.ViewRippleEffect;
import com.elisaxui.xui.core.widget.menu.ViewMenu;

/**
 * @author Bureau
 *
 */
public interface JSViewCard extends JSClass {

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	default Object getData(Object selector) {

		set(aDataSet, _new())
		.__(aDataSet.setData("[]"))

		.set(aDataDriven, _new(aDataSet))
		
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					._if("ctx.row.type=='background'")
							._if("ctx.row.mode=='css'")
								.set(template, ViewCard.getTemplateRichMedia())
								.var("jqdom", template.appendInto("$(selector)"))
								.__("jqdom.css('background', ctx.row.css)")
								.__("jqdom.css('opacity', ctx.row.opacity)")
								.__("ctx.row['_dom_']=jqdom[0]")
							.endif()
					._elseif("ctx.row.type=='text'")
						.set(template, ViewCard.getTemplateText("ctx.row.html"))
						.var("jqdom", template.appendInto("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
						
					._elseif("ctx.row.type=='cardAction'")
						.__("$(selector).attr('data-x-action', ctx.row.idAction )")
						.__(JQuery.$(jsvar("selector")).addClass(ViewRippleEffect.cRippleEffect))
					.endif()	
				.endif()))
		
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()))

		.__(aDataDriven.onChange(fct("value")
				._if("value.row['_dom_']!=null && value.property=='idx'")

				.endif()
			))

		.var("jsonComponent", aDataSet.getData())
		;

		return "jsonComponent";
	}

}
