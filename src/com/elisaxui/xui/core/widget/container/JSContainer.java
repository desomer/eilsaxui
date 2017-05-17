/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.xui.core.widget.button.ViewFloatAction;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;

/**
 * @author Bureau
 *
 */
public interface JSContainer extends JSClass {

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	JSContainer _this=null;
	JSContainer _self=null;
	
	default Object getSubData(Object ctx)
	{
		__("return ", XHTMLPart.xDiv(XHTMLPart.xVar("ctx.row.html")))
		;
		return null;
	}

	default Object getData(Object selector) {

		ViewCard card = new ViewCard();
		
		set(aDataSet, _new())
		.__(aDataSet.setData("[]"))

		.set(aDataDriven, _new(aDataSet))
		.var(_self, _this)
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					._if("ctx.row.type=='card'")
					    .var("subData", _self.getSubData("ctx"))
						.set(template, ViewCard.getTemplate((ViewCard)card.addProperty("childrenCard", XHTMLPart.xVar("subData.html") )))
						.var("jqdom", template.append("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
					._elseif("ctx.row.type=='floatAction'")
						.set(template, ViewFloatAction.getTemplate())
						.var("jqdom", template.append("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
					._elseif("ctx.row.type=='action'")
//								._if("$(selector+' .rightAction').length==0")
//									.set(template, ViewNavBar.getTemplateActionBar())
//									.var("jqdom", template.append("$(selector+' .navbar')"))
//								.endif()
//								.set(template, ViewNavBar.getTemplateAction("ctx.row.icon", "ctx.row.idAction"))
//								.var("jqdom", template.append("$(selector+' .rightAction')"))
//								.__("ctx.row['_dom_']=jqdom[0]")
					.endif()
				.endif()))
		
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()))

		.__(aDataDriven.onChange(fct("value")
				._if("value.row['_dom_']!=null && value.property=='idx'")

				.endif()
			))

		.var("json", aDataSet.getData())
		.__("return json")

		;

		return null;
	}
	
}