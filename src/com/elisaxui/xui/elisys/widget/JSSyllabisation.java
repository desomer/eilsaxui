/**
 * 
 */
package com.elisaxui.xui.elisys.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JQuery;
import com.elisaxui.xui.core.transition.CssTransition;

/**
 * @author Bureau
 *
 */
public interface JSSyllabisation extends JSClass {

	JSDataDriven aDataDriven = null; 
	JSDataSet aDataSet = null;
	JSXHTMLPart template = null;
	
	
//	recognition.onresult = function (event) {
//	for (i = event.resultIndex; i < event.results.length; i++) {
//			textarea.value += event.results[i][0].transcript + "\n"
//			document.getElementById("span").innerHTML =
//				Math.round(event.results[i][0].confidence * 100) + " %"
//	}
//}
	
	default Object getMicro(Object textarea)
	{
		__()
		.var("f", "webkitSpeechRecognition || SpeechRecognition")
		.set("window.recognition", "new f()")
		.set("recognition.continuous", true)
		.set("recognition.lang", txt("fr-FR"))
		.set("recognition.onresult", fct("event") 
					._for("var i = event.resultIndex; i < event.results.length; i++")
						._if("event.results[i].isFinal")
							.__("$.notify('time '+ event.timeStamp, {globalPosition: 'bottom left', className:'success', autoHideDelay: 2000})")
							.__("textarea.value += event.results[i][0].transcript")
						.endif()
					.endfor()
					.__("textarea.value +='\\n'")
				);

		return "recognition";
	}
	
	default Object getData()
	{
				
		set(aDataSet, _new())
		.__(aDataSet.setData("[]"))
		
		.set(aDataDriven, _new(aDataSet))
		
		
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					
					.set(template, ViewSyllabisation.getMot(""))   //XHTMLPart.xVar("ctx.row.text")
					.var("jqdom", template.appendInto(JQuery.$(ViewSyllabisation.cDivSyllabisation)))
		            .__("ctx.row['_dom_']=jqdom[0]")
		            .var("sylb", "ctx.row.syllabes")
		            ._for("var j = 0, lb = sylb.length; j< lb; j++")
		                
		            	.set(template, ViewSyllabisation.getSyl(XHTMLPart.xVar("sylb[j].text")))
		            	.var("jqdomSyl", template.appendInto("jqdom"))
		            	._if("j%2==1")
		            		.__(JQuery.$((JSVariable)jsvar("jqdomSyl")).addClass(ViewSyllabisation.cSyllabeImpaire)  )
		            	.endif()
		            .endfor()
		            
		            
//					._if("ctx.row.type=='divider'")
//					//	.set(template, ViewMenu.getTemplateMenuDivider())
//			            .var("jqdom", template.appendInto("$('.menu ul')"))
//			            .__("ctx.row['_dom_']=jqdom[0]")
//					._else()
//			          //  .set(template, ViewMenu.getTemplateMenu("ctx.row.name", "ctx.row.icon", "ctx.row.idAction"))
//			            .var("jqdom", template.appendInto("$('.menu ul')"))
//			            .__("jqdom.css('visibility','hidden')")  // invisible par defaut avant animation
//			            .__("ctx.row['_dom_']=jqdom[0]")
//		            .endif()
	            .endif()
        ))
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")

				.endif()
			))
		
		.__(aDataDriven.onChange(fct("ctx")
//				._if("ctx.row['_dom_']!=null && ctx.property=='anim'")
//					.var("change", "ctx.value")
//					._if("!change==''")
//						.__("$(ctx.row['_dom_']).css('visibility','')")
//						.__("$(ctx.row['_dom_']).toggleClass('animated '+change)")
//						// remise a zero de l'animation
//						.__("setTimeout(\n", fct("elem").__("elem.toggleClass('animated '+change)") ,",", CssTransition.SPEED_SHOW_MENU_ITEMS+CssTransition.DELAY_SURETE_END_ANIMATION,", $(ctx.row['_dom_']))")
//					.endif()	
//				.endif() 
				))
		
		.var("jsonMenu", aDataSet.getData())
		.__("return jsonMenu")
		
		;
		 
		 return null;
	}
}
