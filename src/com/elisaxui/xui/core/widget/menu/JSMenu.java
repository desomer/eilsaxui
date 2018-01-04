/**
 * 
 */
package com.elisaxui.xui.core.widget.menu;

import static com.elisaxui.xui.core.transition.ConstTransition.DELAY_SURETE_END_ANIMATION;
import static com.elisaxui.xui.core.transition.ConstTransition.SPEED_SHOW_MENU_ITEMS;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;

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
				
		set(aDataSet, _new());
		__(aDataSet.setData("[]"));
		
		set(aDataDriven, _new(aDataSet));
		__(aDataDriven.onEnter(fct("ctx").__(()->{
				_if("ctx.row['_dom_']==null");
					_if("ctx.row.type=='divider'");
						set(template, ViewMenu.getTemplateMenuDivider());
			            _var("jqdom", template.appendInto("$('.menu ul')"));
			            __("ctx.row['_dom_']=jqdom[0]");
					_else();
			            set(template, ViewMenu.getTemplateMenu("ctx.row.name", "ctx.row.icon", "ctx.row.idAction"));
			            _var("jqdom", template.appendInto("$('.menu ul')"));
			            __("jqdom.css('visibility','hidden')");  // invisible par defaut avant animation
			            __("ctx.row['_dom_']=jqdom[0]");
		            endif();
	            endif();
		  })
        ));
		
		__(aDataDriven.onExit(fct("value").__(()->{
				_if("value!=null && value.row['_dom_']!=null");

				endif();
		 })
			));
		
		__(aDataDriven.onChange(fct("ctx").__(()->{
				_if("ctx.row['_dom_']!=null && ctx.property=='anim'");
					_var("change", "ctx.value");
					_if("!change==''");
						__("$(ctx.row['_dom_']).css('visibility','')");
						__("$(ctx.row['_dom_']).toggleClass('animated '+change)");
						// remise a zero de l'animation
						__("setTimeout(", fct("elem").__("elem.toggleClass('animated '+change)") ,",", SPEED_SHOW_MENU_ITEMS + DELAY_SURETE_END_ANIMATION,", $(ctx.row['_dom_']))");
					endif();
				endif();
		 })
				));
		
		_var("jsonMenu", aDataSet.getData());
		__("return jsonMenu");
		
		;
		 
		 return null;
	}
}
