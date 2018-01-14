/**
 * 
 */
package com.elisaxui.component.widget.container;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.component.toolkit.JSFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.component.widget.button.ViewRippleEffect;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;

/**
 * @author Bureau
 *
 */
public interface JSViewCard extends JSFactory {

	static final String TYPE_BACKGROUND = "background";
	static final String TYPE_TEXT = "text";
	static final String TYPE_CARD_ACTION = "cardAction";

	JSDataDriven aDataDriven = null;
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;

	default Object getData(Object selector) {

		_set(aDataSet, _new());
		__(aDataSet.setData("[]"));

		_set(aDataDriven, _new(aDataSet));

		__(aDataDriven.onEnter(fct("ctx").__(() -> {
			_if("ctx.row['_dom_']==null").then(() -> {
				_if("ctx.row.type== " + txt(TYPE_BACKGROUND)).then(() -> {
					_if("ctx.row.mode=='css'").then(() -> {
						_set(template, ViewCard.getTemplateRichMedia());
						_var("jqdom", template.appendInto("$(selector)"));
						__("jqdom.css('background', ctx.row.css)");
						__("jqdom.css('opacity', ctx.row.opacity)");
						__("ctx.row['_dom_']=jqdom[0]");
					});
				});

				_elseif("ctx.row.type==" + txt(TYPE_TEXT)).then(() -> {
					_set(template, ViewCard.getTemplateText("ctx.row.html"));
					_var("jqdom", template.appendInto("$(selector)"));
					__("ctx.row['_dom_']=jqdom[0]");
				});
				_elseif("ctx.row.type==" + txt(TYPE_CARD_ACTION)).then(() -> {
					__("$(selector).attr('data-x-action', ctx.row.idAction )");
					__(JQuery.$(var("selector")).addClass(ViewRippleEffect.cRippleEffect));
				});
			});
		})));

		__(aDataDriven.onExit(fct("value").__(() -> {
			_if("value!=null && value.row['_dom_']!=null");

			endif();
		})));

		__(aDataDriven.onChange(fct("value").__(() -> {
			_if("value.row['_dom_']!=null && value.property=='idx'");

			endif();
		})));

		_return(aDataSet.getData());
		return null;
	}

}
