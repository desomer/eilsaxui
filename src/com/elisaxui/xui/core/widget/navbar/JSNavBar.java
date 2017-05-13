/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.xui.core.page.ScnStandard;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.widget.menu.ViewMenu;

/**
 * @author Bureau
 *
 */
public interface JSNavBar extends JSClass {

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	

	default Object getData(Object selector) {

		set(aDataSet, _new())
				.__(aDataSet.setData("[]"))

				.set(aDataDriven, _new(aDataSet))
				
				.__(aDataDriven.onEnter(fct("ctx")
						._if("ctx.row['_dom_']==null")
							._if("ctx.row.type=='burger'")
								.set(template, ViewNavBar.getTemplateBtnBurger())
								.var("jqdom", template.append("$(selector)"))
								.__("ctx.row['_dom_']=jqdom[0]")
							._elseif("ctx.row.type=='name'")
								.set(template, ViewNavBar.getTemplateName("ctx.row.name"))
								.var("jqdom", template.append("$(selector)"))
								.__("ctx.row['_dom_']=jqdom[0]")
							._elseif("ctx.row.type=='action'")
								._if("$(selector+' .rightAction').length==0")
									.set(template, ViewNavBar.getTemplateActionBar())
									.var("jqdom", template.append("$(selector)"))
								.endif()
								.set(template, ViewNavBar.getTemplateAction("ctx.row.icon", "ctx.row.idAction"))
								.var("jqdom", template.append("$(selector+' .rightAction')"))
								.__("ctx.row['_dom_']=jqdom[0]")
							.endif()
						.endif()))
				
				.__(aDataDriven.onExit(fct("value")
						._if("value!=null && value.row['_dom_']!=null")

						.endif()))

				.__(aDataDriven.onChange(fct("value")
						._if("value.row['_dom_']!=null && value.property=='idx'")

						.endif()
					))

				.var("jsonMenu", aDataSet.getData())
				.__("return jsonMenu")

		;

		return null;
	}

}
