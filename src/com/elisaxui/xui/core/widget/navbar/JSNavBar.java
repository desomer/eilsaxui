/**
 * 
 */
package com.elisaxui.xui.core.widget.navbar;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.declareType;
import static com.elisaxui.xui.core.toolkit.JQuery.*;
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
	
	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	default Object getData(JSVariable selector) {

		set(aDataSet, _new());
		__(aDataSet.setData("[]"));

		set(aDataDriven, _new(aDataSet));
		
		JSChangeCtx ctx = declareType(JSChangeCtx.class, "ctx");
		JQuery jqdom = declareType(JQuery.class, "jqdom");
		
		__(aDataDriven.onEnter(fct(ctx).__(()->{
			
			_if(ctx.row().attrByString("'_dom_'"),"==null");
			
			    JSonNavBarRow jsnavRow = let(JSonNavBarRow.class, "jsnavRow", ctx.row());
			    
				_if(jsnavRow.type(),"=='"+TYPE_BURGER+"'");
					set(template, ViewNavBar.getTemplateBtnBurger());
					var(jqdom, template.appendInto($(selector)));
					set(ctx.row().attrByString("'_dom_'"), jqdom.get(0));
					
				_elseif(jsnavRow.type(),"=='"+TYPE_TITLE+"'");
					JSonNavBarTitle jsnavTitle= cast(JSonNavBarTitle.class,  ctx.row());
				
					set(template, ViewNavBar.getTemplateName(jsnavTitle.title()));
					var(jqdom, template.appendInto("$(selector)"));
					set(ctx.row().attrByString("'_dom_'"), jqdom.get(0));
					
				_elseif(jsnavRow.type(),"=='"+TYPE_BTN_ACTION+"'");
					JSonNavBarBtnAction jsnavBtn= cast(JSonNavBarBtnAction.class,  ctx.row());
					
					_if($(selector," ", ViewNavBar.rightAction).length(), "==0");
						set(template, ViewNavBar.getTemplateActionBar());
						var(jqdom, template.appendInto($(selector)));
					endif();
					set(template, ViewNavBar.getTemplateAction(jsnavBtn.icon(), jsnavBtn.idAction()));
					var(jqdom, template.appendInto($(selector," ", ViewNavBar.rightAction)));
					set(ctx.row().attrByString("'_dom_'"), jqdom.get(0));
				
				_elseif(jsnavRow.type(),"=='"+TYPE_BACKGROUND+"'");	
				    JSonNavBarBackground jsnavRowBg= cast(JSonNavBarBackground.class,  ctx.row());
				    
				    _if(jsnavRowBg.mode(),"=='granim'");
						set(template, ViewNavBar.getTemplateBgCanvas());
						var(jqdom, template.appendInto($(selector)));
						set(ctx.row().attrByString("'_dom_'"), jqdom.get(0));
					_elseif(jsnavRowBg.mode(),"=='css'");
						set(template, ViewNavBar.getTemplateBgDiv());
						var(jqdom, template.appendInto($(selector)));
						__(jqdom.css("background", jsnavRowBg.css()));
						__(jqdom.css("opacity", jsnavRowBg.opacity()));
						set(ctx.row().attrByString("'_dom_'"), jqdom.get(0));
					endif();
				endif();
				
			endif();
		}
		)))
		
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()))

		.__(aDataDriven.onChange(fct("value")
				._if("value.row['_dom_']!=null && value.property=='idx'")

				.endif()
			))

		;
		
		return aDataSet.getData();
	}

}
