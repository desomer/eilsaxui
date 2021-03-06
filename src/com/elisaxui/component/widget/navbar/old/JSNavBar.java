/**
 * 
 */
package com.elisaxui.component.widget.navbar.old;

import static com.elisaxui.component.toolkit.old.JQuery.$;

import com.elisaxui.component.toolkit.datadriven.JSChangeCtx;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.toolkit.old.JQuery;
import com.elisaxui.component.widget.navbar.ViewNavBar;
import com.elisaxui.component.widget.navbar.old.JSonNavBar.JSonNavBarBackground;
import com.elisaxui.component.widget.navbar.old.JSonNavBar.JSonNavBarBtnAction;
import com.elisaxui.component.widget.navbar.old.JSonNavBar.JSonNavBarRow;
import com.elisaxui.component.widget.navbar.old.JSonNavBar.JSonNavBarTitle;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xForceInclude;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * @author Bureau
 *
 */

@xForceInclude
@Deprecated
public interface JSNavBar extends JSClass {
	
	static final String TYPE_BURGER = "burger";
	static final String TYPE_TITLE = "title";
	static final String TYPE_BTN_ACTION = "action";
	static final String TYPE_BACKGROUND = "background";
	
	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	default Object getData(JSAny selector) {

		_set(aDataSet, _new());
		aDataSet.setData(new JSArray<>().asLitteral()); 

		_set(aDataDriven, _new(aDataSet));
		
		JSChangeCtx ctx = declareType(JSChangeCtx.class, "ctx");
		JQuery jqdom = declareType(JQuery.class, "jqdom");
		
		aDataDriven.onEnter(funct(ctx).__(()->{
			
			_if(ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).equalsJS(null)); 
			
			    JSonNavBarRow jsnavRow = let(JSonNavBarRow.class, "jsnavRow", ctx.row());
			    
				_if (jsnavRow.type().equalsJS(txt(TYPE_BURGER)));
					_set(template, ViewNavBar.getTemplateBtnBurger());
					_var(jqdom, template.appendInto($(selector)));
					ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).set(jqdom.get(0));
					
				_elseif_ (jsnavRow.type().equalsJS(txt(TYPE_TITLE)));
					JSonNavBarTitle jsnavTitle= cast(JSonNavBarTitle.class,  ctx.row());
				
					_set(template, ViewNavBar.getTemplateName(jsnavTitle.title()));
					_var(jqdom, template.appendInto($(selector," ", ViewNavBar.descBar)));
					ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).set(jqdom.get(0));
					
				_elseif_ (jsnavRow.type().equalsJS(txt(TYPE_BTN_ACTION)));
					JSonNavBarBtnAction jsnavBtn= cast(JSonNavBarBtnAction.class,  ctx.row());
					
					_if($(selector," ", ViewNavBar.rightAction).length().equalsJS(0)); 
						_set(template, ViewNavBar.getTemplateActionBar());
						_var(jqdom, template.appendInto($(selector)));
					endif();
					
					_set(template, ViewNavBar.getTemplateAction(jsnavBtn.icon(), jsnavBtn.idAction()));
					_var(jqdom, template.appendInto($(selector," ", ViewNavBar.rightAction)));
					ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).set(jqdom.get(0));
				
				_elseif_ (jsnavRow.type().equalsJS(txt(TYPE_BACKGROUND)));	
				    JSonNavBarBackground jsnavRowBg= cast(JSonNavBarBackground.class,  ctx.row());
				    
				    _if (jsnavRowBg.mode().equalsJS(txt("granim")));
						_set(template, ViewNavBar.getTemplateBgCanvas());
						_var(jqdom, template.appendInto($(selector)));
						ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).set(jqdom.get(0));
						
					_elseif_ (jsnavRowBg.mode().equalsJS(txt("css")));
						_set(template, ViewNavBar.getTemplateBgDiv());
						_var(jqdom, template.appendInto($(selector," ", ViewNavBar.descBar)));
						__(jqdom.css("background", jsnavRowBg.css()));   //TODO retirer le __
						__(jqdom.css("opacity", jsnavRowBg.opacity()));
						__(ctx.row().attrByStr(JSDataSet.ATTR_DOM_LINK).set(jqdom.get(0)));
						
					endif ();
					
				endif ();
				
			endif ();
		}
		));
		
		aDataDriven.onExit(funct("value").__(()->{
				_if("value!=null && value.row['_dom_']!=null");

				endif();
		})
		);
		
		aDataDriven.onChange(funct("value").__(()->{
				_if("value.row['_dom_']!=null && value.property=='idx'");

				endif();
		})
		);
		
		return aDataSet.getData();
	}

}
