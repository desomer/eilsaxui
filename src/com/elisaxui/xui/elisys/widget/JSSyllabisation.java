/**
 * 
 */
package com.elisaxui.xui.elisys.widget;

import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defAttr;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defVar;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSCode;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;
import com.elisaxui.core.xui.xml.annotation.xAnonymous;
import com.elisaxui.xui.core.datadriven.JSDataDriven;
import com.elisaxui.xui.core.datadriven.JSDataSet;
import com.elisaxui.xui.core.toolkit.JQuery;

import static com.elisaxui.xui.core.toolkit.JQuery.*;
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
	
	
	@xAnonymous
	default Object fRecognitionEnd(Object event)
	{
		return  (Anonym) ()-> {
			_if("window.microlistener.stop");
				set("window.microlistener.stop", false);
			_else();
				__("window.microlistener.recognition.start()");
			endif();
		};
	}
	
	
	default Object createMicroListener()
	{
		var("f", "webkitSpeechRecognition || SpeechRecognition");
		set(recognition, "new f()");
		set(recognition.attr("continuous"), true);
		set(recognition.attr("lang"), txt("fr-FR"));
		var(_self, _this);
		
		JSArray jsonSyllable = new JSArray().setName("jsonSyllable");
		var(jsonSyllable, aDataSet.getData());

		Anonym onresult = (/*event*/)->{ 			
			_for("var i = event.resultIndex; i < event.results.length; i++");
				var("time", "event.timeStamp-", "self.lastResult");
				_if("event.results[i].isFinal && time>500");
					set("self.lastResult", "event.timeStamp");
					
					JSString textEvent = new JSString().setName("textEvent");
					var(textEvent, "event.results[i][0].transcript");
					__("window.lastPhrase", "+=' '+", textEvent);
	
					JSon param = new JSon().setName("param");
					var(param, "{text:",textEvent,"}");
					JSon data = new JSon().setName("data"); 
					
					
					Anonym onJsonSyllabysation = (/*data*/)->{	
						JSArray lesmots = new JSArray().setName("lesmots");
						var(lesmots, data.attr("mots"));
						JSInt num = new JSInt().setName("num");
						_forIdx(num, lesmots);
							setTimeout(fct(num).__(jsonSyllable.push(lesmots.get(num))), "50+(20*"+num+")", num);
						endfor();
					};

					__("$.getJSON('"+REST_JSON_SYLLABISATION+"', ",param," ).done(", fct(data).__(onJsonSyllabysation),")");
					
				endif();
			endfor();
		};
		
		set(recognition.attr("onresult"), fct("event") .__(onresult));
		set(recognition.attr("onend"), fRecognitionEnd("event"));

		return _this;
	}
		
	default Object getData()
	{
		__(aDataSet.setData("[]"));
		set(aDataDriven, _new(aDataSet));
		
		Anonym onEnter = (/*ctx*/)->{
			_if("ctx.row['_dom_']==null");				
				set(template, ViewSyllabisation.getMot(XHTMLPart.xVar("ctx.row.text")));
				JQuery jqdom = new JQuery().setName("jqdom");
				var(jqdom, template.appendInto(JQuery.$(ViewSyllabisation.cDivSyllabisation)));
				set("ctx.row['_dom_']", jqdom.get(0));
				JSArray sylb = new JSArray().setName("sylb");
				var(sylb, "ctx.row.syllabes");
	            _forIdx("j", sylb);
	            	set(template, ViewSyllabisation.getSyl(XHTMLPart.xVar(sylb.get("j").attr("text"))));
	        		JQuery jqdomSyl = new JQuery().setName("jqdomSyl");
	            	var(jqdomSyl, template.appendInto("jqdom"));
	            	_if("j%2==1");
	            		__(jqdomSyl.addClass(ViewSyllabisation.cSyllabeImpaire)  );
	            	endif();
	            endfor();
	        endif();
		};
								
		Anonym onExit = (/*ctx*/)->{
			_if("ctx!=null && ctx.row['_dom_']!=null");
				__($(jsvar("ctx.row['_dom_']")).hide("50+(ctx.idx*20)", fct().__("$(this).remove()") ));
			endif();
			};
		
		__(aDataDriven.onEnter(fct("ctx").__(onEnter)));
		__(aDataDriven.onExit(fct("ctx").__(onExit)));	
		
		return aDataSet.getData();
	}
}
