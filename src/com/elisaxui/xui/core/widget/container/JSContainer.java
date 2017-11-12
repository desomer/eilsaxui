/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JSFactory;
import com.elisaxui.xui.core.toolkit.TKActivity;
import com.elisaxui.xui.core.toolkit.TKQueue;
import com.elisaxui.xui.core.toolkit.TKRouterEvent;
import com.elisaxui.xui.core.transition.CssTransition;
import com.elisaxui.xui.core.transition.TKTransition;
import com.elisaxui.xui.core.widget.button.ViewFloatAction;
import com.elisaxui.xui.core.widget.layout.ViewPageLayout;
import com.elisaxui.xui.core.widget.navbar.ViewNavBar;

/**
 * @author Bureau
 *
 */
public interface JSContainer extends JSFactory {

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	JSContainer _this=null;
	JSContainer _self=null;
	
	TKRouterEvent _tkrouter =null;
	TKTransition _tkAnimation = null;
	TKActivity _activityMgr = null;
	
	default Object getSubData(Object ctx)
	{
		__()
		._if("ctx.row.html instanceof JSXHTMLPart")
			.var("part", "ctx.row.html")
			.var("ret", XHTMLPart.xDiv(XHTMLPart.xVar("part.html")))
			.__("ret.js+=part.js")
			.__("return ret")
		._else()
			.var("ret", XHTMLPart.xDiv(XHTMLPart.xVar("ctx.row.html")))
			.var("js", "ctx.row.js.replace('<script type=\\\"text\\\\/javascript\\\">', '')")
			.set("js", "js.replace('<\\\\/script>', '')")
			.__("ret.js=\"<script>\"+js+\"<\\/script>\"")
			.__("return ret")
		.endif()
		;
		return null;
	}

	JSViewCard _cardFactory = null;
	
	default Object getData(Object selector) {

		ViewCard card = new ViewCard();
		
		set(aDataSet, _new())
		.__(aDataSet.setData("[]"))

		.set(aDataDriven, _new(aDataSet))
		.var(_self, _this)
		
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
				
				 	._if("ctx.row.type=='page'")
						.set(template, XHTMLPart.xPart(new ViewPageLayout(XHTMLPart.xVar("ctx.row.id"))))
						.var("jqdom", template.appendInto("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
						
						// ajoute les enfant
						._for("var i in ctx.row.children")
							.var("child", "ctx.row.children[i]")
							.var("factory", "new (eval(child.factory))()" )
							.var("data", "factory.getData(child.selector)")
							._for("var j in child.rows")
								.var("row", "child.rows[j]")
								.set("row.idx", "j")
//								.var("param", "{'data':data, 'row':row , 'idx':j }")
//								.__("setTimeout(", fct("param").__("param.data.push(param.row)") , ", 100*j, param)")	
								.__("data.push(row)")
							.endfor()
						.endfor()
						
						.var(_tkrouter, "$xui.tkrouter")
						
						._if("ctx.row.active")
						  	.__(_tkrouter, ".", _tkAnimation.doActivityActive("'#'+ctx.row.id"))
						  	.__(_tkrouter, ".", _tkAnimation.doNavBarToBody())
						.endif()
						
						.__("var backupId=", _tkrouter, ".", _activityMgr.getCurrentIDActivity())
						.__(_tkrouter, ".", _activityMgr.setCurrentActivity(new JSString()._setContent("ctx.row.id")))
						.__(_tkrouter.doEvent(txt(TKActivity.ON_ACTIVITY_CREATE)))	
						.__(_tkrouter, ".", _activityMgr.setCurrentActivity(new JSString()._setContent("backupId")))
						
					._elseif("ctx.row.type=='card'")
						.var("subData", "{html:'', js:''}")
					    ._if("ctx.row.html!=null") 
					    	.set("subData", _self.getSubData("ctx"))
					    .endif()
					    
						.set(template, ViewCard.getTemplate((ViewCard)card.addProperty("childrenCard", XHTMLPart.xVar("subData.html") )))
						.__(template, ".js+=subData.js")
						
						.var("jqdom", template.appendInto("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
						.__("$(ctx.row['_dom_']).css('visibility','hidden')")
						
						.var(_cardFactory, _new() )
						.var("data", _cardFactory.getData("jqdom"))
						
						._for("var j in ctx.row.rows")
							.var("row", "ctx.row.rows[j]")
							.__("data.push(row)")	
						.endfor()
						
						//------------- anim des item de menu----------	
						.__("setTimeout(", fct("elem") 
								.__("elem.anim='bounceInUp'")  //zoomInUp  //fadeInUp  //slideInUp //rollIn
							, ", 500 * ctx.row.idx , ctx.row)")
						
						
						
					._elseif("ctx.row.type=='floatAction'")
						.set(template, ViewFloatAction.getTemplate())
						.var("jqdom", template.appendInto("$(selector)"))
						.__("ctx.row['_dom_']=jqdom[0]")
	
					._elseif("ctx.row.type=='action'")

					.endif()
				.endif()))
		
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()))

		.__(aDataDriven.onChange(fct("ctx")
				._if("ctx.row['_dom_']!=null && ctx.row.type=='card' && ctx.property=='anim'")
					.consoleDebug(txt("JSContainer change "), "ctx.value", "ctx")
					._if("!ctx.value==''")
						.__("$(ctx.row['_dom_']).css('visibility','')")
						.__("$(ctx.row['_dom_']).toggleClass('animated '+ctx.value)")
						//.__("setTimeout(\n", fct("elem").__("elem.toggleClass('animated '+ctx.value)") ,",500, $(ctx.row['_dom_']))")
					.endif()	
				.endif()
			))

		.var("json", aDataSet.getData())
		.__("return json")

		;

		return null;
	}
	
}
