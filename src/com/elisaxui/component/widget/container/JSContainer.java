/**
 * 
 */
package com.elisaxui.component.widget.container;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.component.toolkit.JSFactory;
import com.elisaxui.component.toolkit.TKActivity;
import com.elisaxui.component.toolkit.TKRouterEvent;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.transition.JSTransition;
import com.elisaxui.component.widget.button.ViewFloatAction;
import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.core.xui.xhtml.IXHTMLBuilder;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * @author Bureau
 *
 */
public interface JSContainer extends JSFactory, IXHTMLBuilder {

	JSDataDriven aDataDriven();
	JSDataSet aDataSet();
	JSXHTMLPart template = null;
	JSContainer _this=null;
	JSContainer _self=null;
	
	TKRouterEvent _tkrouter =null;
	JSTransition _tkAnimation = null;
	TKActivity _activityMgr = null;
	
	default Object getSubData(Object ctx)
	{

		_if("ctx.row.html instanceof JSXHTMLPart");
			_var("part", "ctx.row.html");
			_var("ret", xDiv(xVar("part.html")));
			__("ret.js+=part.js");
			__("return ret");
		_else();
			_var("ret", xDiv(xVar("ctx.row.html")));
			_var("js", "ctx.row.js.replace('<script type=\\\"text\\\\/javascript\\\">', '')");
			_set("js", "js.replace('<\\\\/script>', '')");
			__("ret.js=\"<script>\"+js+\"<\\/script>\"");
			__("return ret");
		endif()
		;
		return null;
	}

	JSViewCard _cardFactory = null;
	
	default Object getData(Object selector) {

		ViewCard card = new ViewCard();

		aDataSet().set(_new());
		aDataSet().setData(new JSArray<>().asLitteral());
		aDataDriven().set(_new(aDataSet()));
		let(_self, _this);
		
		aDataDriven().onEnter(funct("ctx").__(()->{
				_if("ctx.row['_dom_']==null");
				
				 	_if("ctx.row.type=='page'");
						_set(template, XHTMLPart.xPart(new ViewPageLayout(xVar("ctx.row.id"))));
						_var("jqdom", template.appendInto("$(selector)"));
						__("ctx.row['_dom_']=jqdom[0]");
						
						// ajoute les enfant
						_for("var i in ctx.row.children");
							_var("child", "ctx.row.children[i]");
							_var("factory", "new (eval(child.factory))()" );
							_var("data", "factory.getData(child.selector)");
							_for("var j in child.rows");
								_var("row", "child.rows[j]");
								_set("row.idx", "j");
//								var("param", "{'data':data, 'row':row , 'idx':j }")
//								__("setTimeout(", fct("param").__("param.data.push(param.row)") , ", 100*j, param)")	
								__("data.push(row)");
							endfor();
						endfor();
						
						_var(_tkrouter, "$xui.tkrouter");
						
						_if("ctx.row.active");		
						  	__(_tkrouter, ".", _tkAnimation.doActivityActive(JQuery.$(var("'#'+ctx.row.id"))));
						  	__(_tkrouter, ".", _tkAnimation.doFixedElemToFixe(JQuery.$(var("'#'+ctx.row.id"))));
						endif();
						
						__("var backupId=", _tkrouter, ".", _activityMgr.getCurrentIDActivity());
						__(_tkrouter, ".", _activityMgr.setCurrentActivity(new JSString()._setValue("ctx.row.id")));
						__(_tkrouter.doEvent(txt(TKActivity.ON_ACTIVITY_CREATE)))	;
						__(_tkrouter, ".", _activityMgr.setCurrentActivity(new JSString()._setValue("backupId")));
						
					_elseif_("ctx.row.type=='card'");
						_var("subData", "{html:'', js:''}");
					    _if("ctx.row.html!=null") ;
					    	_set("subData", _self.getSubData("ctx"));
					    endif();
					    
						_set(template, ViewCard.getTemplate((ViewCard)card.addProperty("childrenCard", xVar("subData.html") )));
						__(template, ".js+=subData.js");
						// ajoute la card
						_var("jqdom", template.appendInto("$(selector)"));
						__("ctx.row['_dom_']=jqdom[0]");
						
						if (true) {
							//------------- anim les card----------	
							__("$(ctx.row['_dom_']).css('visibility','hidden')");
							__("setTimeout(", funct("elem") 
									.__("elem.anim='bounceInUp'")  //zoomInUp  //fadeInUp  //slideInUp //rollIn
								, ", 500 * ctx.row.idx , ctx.row)");
						}
						
						// ajoute les lignes de card 
						_var(_cardFactory, _new() );
						_var("data", _cardFactory.getData("jqdom"));
						_for("var j in ctx.row.rows");
							_var("row", "ctx.row.rows[j]");
							__("data.push(row)")	;
						endfor();

						
					_elseif_("ctx.row.type=='floatAction'");
						_set(template, ViewFloatAction.getTemplate());
						_var("jqdom", template.appendInto("$(selector)"));
						__("ctx.row['_dom_']=jqdom[0]");
	
					_elseif_("ctx.row.type=='action'");

					endif();
				endif();
				})
				);
		
		aDataDriven().onExit(funct("value").__(()->{
				_if("value!=null && value.row['_dom_']!=null");

				endif();
		})
				);

		aDataDriven().onChange(funct("ctx").__(()->{
				_if("ctx.row['_dom_']!=null && ctx.row.type=='card' && ctx.property=='anim'");
					consoleDebug(txt("JSContainer change "), "ctx.value", "ctx");
					_if("!ctx.value==''");
						__("$(ctx.row['_dom_']).css('visibility','')");
						__("$(ctx.row['_dom_']).toggleClass('animated '+ctx.value)");	
					endif();	
				endif();
			})
			);

		_var("json", aDataSet().getData());
		__("return json");

		return null;  // TODO a changer
	}
	
}
