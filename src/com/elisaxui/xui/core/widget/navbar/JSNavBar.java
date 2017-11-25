/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.*;
import static com.elisaxui.xui.core.toolkit.JQuery.$;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xForceInclude;
import com.elisaxui.xui.core.datadriven.JSChangeCtx;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.toolkit.JSFactory;
import com.elisaxui.xui.core.widget.navbar.JSonNavBar.JSonNavBarBackground;
import com.elisaxui.xui.core.widget.navbar.JSonNavBar.JSonNavBarBtnAction;
import com.elisaxui.xui.core.widget.navbar.JSonNavBar.JSonNavBarRow;
import com.elisaxui.xui.core.widget.navbar.JSonNavBar.JSonNavBarTitle;

/**
 * @author Bureau
 *
 */

@xForceInclude
public interface JSNavBar extends JSFactory {
	
	static final String TYPE_BURGER = "burger";
	static final String TYPE_TITLE = "title";
	static final String TYPE_BTN_ACTION = "action";
	static final String TYPE_BACKGROUND = "background";
	
	JSDataDriven aDataDriven = defVar();
	JSDataSet aDataSet = defVar();
	JSXHTMLPart template = defVar();

	default Object getData(JSVariable selector) {

		set(aDataSet, _new());
		__(aDataSet.setData("[]"));    //cast(JSArray.class,"[]"))

		set(aDataDriven, _new(aDataSet));
		
		JSChangeCtx ctx = declareType(JSChangeCtx.class, "ctx");

		
		__(aDataDriven.onEnter(fct(ctx).__(()->{
			
			_if(ctx.row().attrByString(JSDataSet._DOM).isEqual(null)); 
				JQuery jqdom = let(JQuery.class, "jqdom", "null");
			    JSonNavBarRow jsnavRow = let(JSonNavBarRow.class, "jsnavRow", ctx.row());
			    
				_if (jsnavRow.type().isEqual(txt(TYPE_BURGER)));
					set(template, ViewNavBar.getTemplateBtnBurger());
					jqdom.set(template.appendInto($(selector)));
					ctx.row().attrByString(JSDataSet._DOM).set(jqdom.get(0));
					
				_elseif (jsnavRow.type().isEqual(txt(TYPE_TITLE)));
					JSonNavBarTitle jsnavTitle= cast(JSonNavBarTitle.class,  ctx.row());
				
					set(template, ViewNavBar.getTemplateName(jsnavTitle.title()));
					jqdom.set(template.appendInto($(selector)));
					ctx.row().attrByString(JSDataSet._DOM).set(jqdom.get(0));
					
				_elseif (jsnavRow.type().isEqual(txt(TYPE_BTN_ACTION)));
					JSonNavBarBtnAction jsnavBtn= cast(JSonNavBarBtnAction.class,  ctx.row());
					
					_if($(selector," ", ViewNavBar.rightAction).length().isEqual(0)); 
						set(template, ViewNavBar.getTemplateActionBar());
						jqdom.set(template.appendInto($(selector)));
					endif();
					
					set(template, ViewNavBar.getTemplateAction(jsnavBtn.icon(), jsnavBtn.idAction()));
					jqdom.set(template.appendInto($(selector," ", ViewNavBar.rightAction)));
					ctx.row().attrByString(JSDataSet._DOM).set(jqdom.get(0));
				
				_elseif (jsnavRow.type().isEqual(txt(TYPE_BACKGROUND)));	
				    JSonNavBarBackground jsnavRowBg= cast(JSonNavBarBackground.class,  ctx.row());
				    
				    _if (jsnavRowBg.mode().isEqual(txt("granim")));
						set(template, ViewNavBar.getTemplateBgCanvas());
						jqdom.set(template.appendInto($(selector)));
						ctx.row().attrByString(JSDataSet._DOM).set(jqdom.get(0));
						
					_elseif (jsnavRowBg.mode().isEqual(txt("css")));
						set(template, ViewNavBar.getTemplateBgDiv());
						jqdom.set(template.appendInto($(selector)));
						jqdom.css("background", jsnavRowBg.css());
						jqdom.css("opacity", jsnavRowBg.opacity());
						ctx.row().attrByString(JSDataSet._DOM).set(jqdom.get(0));
						
					endif ();
					
				endif ();
				
			endif ();
		}
		)));
		
		__(aDataDriven.onExit(fct("value").__(()->{
				_if("value!=null && value.row['_dom_']!=null");

				endif();
		})
		));
		
		__(aDataDriven.onChange(fct("value").__(()->{
				_if("value.row['_dom_']!=null && value.property=='idx'");

				endif();
		})
		));
		
		return aDataSet.getData();
	}

}
