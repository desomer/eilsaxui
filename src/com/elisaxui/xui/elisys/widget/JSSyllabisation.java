/**
 * 
 */
package com.elisaxui.xui.elisys.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.xui.admin.AppRoot;
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
	JSInt lastResult = null;
	JSVariable recognition = null;
	JSSyllabisation _self = null;
	JSSyllabisation _this = null;
	JSVariable stop = null;
	
//	recognition.onresult = function (event) {
//	for (i = event.resultIndex; i < event.results.length; i++) {
//			textarea.value += event.results[i][0].transcript + "\n"
//			document.getElementById("span").innerHTML =
//				Math.round(event.results[i][0].confidence * 100) + " %"
//	}
//}
	
	default Object constructor() {
		set(aDataSet, _new());
		set(stop, false);
		return set(lastResult, 0)
				;
	}
	
	
	
	default Object createMicroListener()
	{
		__()
		.var("f", "webkitSpeechRecognition || SpeechRecognition")
		.set(recognition, "new f()")
		.set("this.recognition.continuous", true)
		.set("this.recognition.lang", txt("fr-FR"))
		.var(_self, _this)
		.var("jsonSyllabe", aDataSet.getData())
		.set("this.recognition.onresult", fct("event") 
					._for("var i = event.resultIndex; i < event.results.length; i++")
						.var("time", "event.timeStamp-self.lastResult")
						._if("event.results[i].isFinal && time>500")
							.set("self.lastResult", "event.timeStamp")
//							.__("$.notify(event.results[i][0].transcript, {globalPosition: 'bottom left', className:'success', autoHideDelay: 2000})")
							.__("$.getJSON('"+AppRoot.REST_JSON_SYLLABISATION+"', {text:event.results[i][0].transcript}).done(", fct("a")

									._for("var i = jsonSyllabe.length-1; i >=0; i--")
										.__("jsonSyllabe.splice(i,1);")
									.endfor()
									
									.var("lesmots", "a.mots")
									._for("var i = 0, l = lesmots.length; i < l; i++")
										.__("jsonSyllabe.push(lesmots[i])")
									.endfor()
								,")")
							
						.endif()
					.endfor()
				)
		.set("this.recognition.onend", fct("event")
				._if("window.microlistener.stop")
					.set("window.microlistener.stop", false)
				._else()
					.__("window.microlistener.recognition.start()"))
				.endif()
		
		;

		return _this;
	}
	
	default Object getData()
	{
				
		__(aDataSet.setData("[]"))
		
		.set(aDataDriven, _new(aDataSet))
		
		
		.__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					
					.set(template, ViewSyllabisation.getMot(XHTMLPart.xVar("ctx.row.text")))  
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
	            .endif()
        ))
		.__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")
					.__("$(value.row['_dom_']).hide(50+(value.idx*20),", fct().__("$(this).remove()"), ")")
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
