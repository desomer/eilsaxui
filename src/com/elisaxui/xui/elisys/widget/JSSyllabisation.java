/**
 * 
 */
package com.elisaxui.xui.elisys.widget;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.*;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JQuery;
/**
 * @author Bureau
 *
 */
public interface JSSyllabisation extends JSClass {

	public static final String REST_JSON_SYLLABISATION = "/rest/json/syllabisation/pacahontas";
	
	JSDataDriven aDataDriven = defAttr(); 
	JSDataSet aDataSet =  defAttr();
	JSXHTMLPart template =  defAttr();
	JSInt lastResult =  defAttr();
	JSon recognition =  defAttr();
	JSVariable stop =  defAttr();
	JSVariable isRunning =  defAttr();
	
	JSSyllabisation _self =  defVar();
	JSSyllabisation _this =  defVar();
	
	
	default Object constructor() {
		set(aDataSet, _new());
		set(stop, false);
		set(isRunning, false);
		return set(lastResult, 0);
	}
	
	
	
	
	default Object createMicroListener()
	{

		JSArray jsonSyllable = new JSArray().setName("jsonSyllable");
		JSArray lesmots = new JSArray().setName("lesmots");
		JSInt num = new JSInt().setName("num");
		JSon data = new JSon().setName("data");  
		
//		Callable monTraitement = () -> { var("a", 0); 
//										 set("a", 1);
//										 return _void();
//		};
		
	//	Supplier<JSFunction> fct = () -> { return fct(); };
		
		var("f", "webkitSpeechRecognition || SpeechRecognition");
		set(recognition, "new f()");
		set(recognition.attr("continuous"), true);
		set(recognition.attr("lang"), txt("fr-FR"));
		var(_self, _this);
		var(jsonSyllable, aDataSet.getData());
		
		var("a", fct("p").__(JSBuilder.classfct(  () -> { 
								var("a", 0); 
		 						set("a", 1);
		 						return _void();} 
		)));
		
		set(recognition.attr("onresult"), fct("event") 
					._for("var i = event.resultIndex; i < event.results.length; i++")
						.var("time", "event.timeStamp-self.lastResult")
						._if("event.results[i].isFinal && time>500")
							.set("self.lastResult", "event.timeStamp")
							.__("window.lastPhrase", "+=' '+", "event.results[i][0].transcript")

							.__("$.getJSON('"+REST_JSON_SYLLABISATION+"', {text:event.results[i][0].transcript} ).done(", fct(data)
									.var(lesmots, data.attr("mots"))
									._for("var i = 0, l =", lesmots.length() ,"; i < l; i++")
										.__("setTimeout(", fct(num)
											.__(jsonSyllable.push(lesmots.get(num)))
										, ",50+(20*i), i)")	
									.endfor()
								,")")
							
						.endif()
					.endfor()
				);
		
		set("this.recognition.onend", fct("event")
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
				
		__(aDataSet.setData("[]"));
		set(aDataDriven, _new(aDataSet));
		
		__(aDataDriven.onEnter(fct("ctx")
				._if("ctx.row['_dom_']==null")
					
					.set(template, ViewSyllabisation.getMot(XHTMLPart.xVar("ctx.row.text")))  
					.var("jqdom", template.appendInto(JQuery.$(ViewSyllabisation.cDivSyllabisation)))
		            .__("ctx.row['_dom_']=jqdom[0]")
		            .var("sylb", "ctx.row.syllabes")
		            ._for("var j = 0, lb = sylb.length; j< lb; j++")
		                
		            	.set(template, ViewSyllabisation.getSyl(XHTMLPart.xVar("sylb[j].text")))
		            	.var("jqdomSyl", template.appendInto("jqdom"))
					//	.__(JQuery.$((JSVariable)jsvar("jqdomSyl")), ".hide()")
					//	.__(JQuery.$((JSVariable)jsvar("jqdomSyl")), ".show(50+ (ctx.idx*50))")
		            	._if("j%2==1")
		            		.__(JQuery.$((JSVariable)jsvar("jqdomSyl")).addClass(ViewSyllabisation.cSyllabeImpaire)  )
		            	.endif()
		            .endfor()
	            .endif()
        ));
		__(aDataDriven.onExit(fct("value")
				._if("value!=null && value.row['_dom_']!=null")
					.__("$(value.row['_dom_']).hide(50+(value.idx*20),", fct().__("$(this).remove()"), ")")
				.endif()
			));
		
		__(aDataDriven.onChange(fct("ctx")

				));
		
		var("jsonMenu", aDataSet.getData());
		__("return jsonMenu");
		 
		 return null;
	}
}
