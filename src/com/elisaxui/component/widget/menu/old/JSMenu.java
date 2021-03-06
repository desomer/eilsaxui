/**
 * 
 */
package com.elisaxui.component.widget.menu.old;

import static com.elisaxui.component.toolkit.transition.ConstTransition.DELAY_SURETE_END_ANIMATION;
import static com.elisaxui.component.toolkit.transition.ConstTransition.SPEED_SHOW_MENU_ITEMS;

import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * @author Bureau
 *
 */
@Deprecated
public interface JSMenu extends JSClass {

	JSDataDriven aDataDriven = null; 
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	
	default Object getData()
	{
				
		_set(aDataSet, _new());
		aDataSet.setData(new JSArray<>().asLitteral());
		
		_set(aDataDriven, _new(aDataSet));
		
		aDataDriven.onEnter(funct("ctx").__(()->{
				_if("ctx.row['_dom_']==null");
					_if("ctx.row.type=='divider'");
						_set(template, ViewMenuOld.getTemplateMenuDivider());
			            _var("jqdom", template.appendInto("$('.menu ul')"));
			            __("ctx.row['_dom_']=jqdom[0]");
					_else();
			            _set(template, ViewMenuOld.getTemplateMenu("ctx.row.name", "ctx.row.icon", "ctx.row.idAction"));
			            _var("jqdom", template.appendInto("$('.menu ul')"));
			            __("jqdom.css('visibility','hidden')");  // invisible par defaut avant animation
			            __("ctx.row['_dom_']=jqdom[0]");
		            endif();
	            endif();
		  })
        );
		
		aDataDriven.onExit(funct("value").__(()->{
				_if("value!=null && value.row['_dom_']!=null");

				endif();
		 })
			);
		
		aDataDriven.onChange(funct("ctx").__(()->{
				_if("ctx.row['_dom_']!=null && ctx.property=='anim'");
					_var("change", "ctx.value");
					_if("!change==''");
						__("$(ctx.row['_dom_']).css('visibility','')");
						__("$(ctx.row['_dom_']).toggleClass('animated '+change)");
						// remise a zero de l'animation
						__("setTimeout(", funct("elem").__("elem.toggleClass('animated '+change)") ,",", SPEED_SHOW_MENU_ITEMS + DELAY_SURETE_END_ANIMATION,", $(ctx.row['_dom_']))");
					endif();
				endif();
		 })
				);
		
		 
		 return aDataSet.getData();
	}
}
