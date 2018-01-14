/**
 * 
 */
package com.elisaxui.app.elisys.xui.widget;

import static com.elisaxui.component.toolkit.JQuery.$;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defAttr;
import static com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass.defVar;

import com.elisaxui.component.toolkit.JQuery;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.Anonym;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSInt;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSXHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xAnonymous;
/**
 * @author Bureau
 *
 */
public interface JSSyllabisation extends JSClass {

	public static final String REST_JSON_SYLLABISATION = "/rest/json/syllabisation/pacahontas";
	
	JSDataDriven aDataDriven = defAttr(); 
	JSDataSet aDataSet();
	JSXHTMLPart template =  defAttr();
	JSInt lastResult =  defAttr();
	JSon recognition =  defAttr();
	JSVariable stop =  defAttr();
	JSVariable isRunning =  defAttr();
	
	JSSyllabisation _self =  defVar();
	JSSyllabisation _this =  defVar();
	
	
	default Object constructor() {
		_set(aDataSet(), _new());
		_set(stop, false);
		_set(isRunning, false);
		return _set(lastResult, 0);
	}
	
	
	@xAnonymous    //TODO a faire fonctionner     pour retirer le boolean testAnonymInProgress dans la class MethodInvocationHandler
	default Object fRecognitionEnd(Object event)
	{
		return  (Anonym) ()-> {
			_if("window.microlistener.stop");
				_set("window.microlistener.stop", false);
			_else();
				__("window.microlistener.recognition.start()");
			endif();
		};
	}
	
	
	default Object createMicroListener()
	{
		_var("f", "webkitSpeechRecognition || SpeechRecognition");
		_set(recognition, "new f()");
		_set(recognition.attr("continuous"), true);
		_set(recognition.attr("lang"), txt("fr-FR"));
		_var(_self, _this);
		
		JSArray jsonSyllable = new JSArray()._setName("jsonSyllable");
		_var(jsonSyllable, aDataSet().getData());

		Anonym onresult = (/*event*/)->{ 			
			_for("var i = event.resultIndex; i < event.results.length; i++");
				_var("time", "event.timeStamp-", "self.lastResult");
				_if("event.results[i].isFinal && time>500");
					_set("self.lastResult", "event.timeStamp");
					
					JSString textEvent = new JSString()._setName("textEvent");
					_var(textEvent, "event.results[i][0].transcript");
					__("window.lastPhrase", "+=' '+", textEvent);
	
					JSon param = new JSon()._setName("param");
					_var(param, "{text:",textEvent,"}");
					
					JSon data = new JSon()._setName("data"); 
					
					Anonym onJsonSyllabysation = (/*data*/)->{	
						JSArray lesmots = new JSArray()._setName("lesmots");
						_var(lesmots, data.attr("mots"));
						JSInt num = new JSInt()._setName("num");
						_forIdx(num, lesmots);
							setTimeout(fct(num).__(jsonSyllable.push(lesmots.at(num))), "50+(20*"+num+")", num);
						endfor();
					};

					__("$.getJSON('"+REST_JSON_SYLLABISATION+"', ",param," ).done(", fct(data).__(onJsonSyllabysation),")");
					
				endif();
			endfor();
		};
		
		Anonym fRecognitionEnd = (/*event*/)->{ 	
			_if("window.microlistener.stop");
				_set("window.microlistener.stop", false);
			_else();
				__("window.microlistener.recognition.start()");
			endif();		
		};	
		
		_set(recognition.attr("onresult"), fct("event") .__(onresult));
		//set(recognition.attr("onend"), fRecognitionEnd("event"));
		_set(recognition.attr("onend"), fct("event") .__(fRecognitionEnd));
		return _this;
	}
		
	default Object getData()
	{
		__(aDataSet().setData("[]"));
		_set(aDataDriven, _new(aDataSet()));
		
		Anonym onEnter = (/*ctx*/)->{
			_if("ctx.row['_dom_']==null");				
				_set(template, ViewSyllabisation.getMot(XHTMLPart.xVar("ctx.row.text")));
				
				JQuery jqdom = new JQuery()._setName("jqdom");
				_var(jqdom, template.appendInto(JQuery.$(ViewSyllabisation.cDivSyllabisation)));
				
				_set("ctx.row['_dom_']", jqdom.get(0));
				
				JSArray<JSArray> sylb = new JSArray()._setName("sylb");
				_var(sylb, "ctx.row.syllabes");
				
	            _forIdx("j", sylb);
	            	_set(template, ViewSyllabisation.getSyl(XHTMLPart.xVar(sylb.at("j").attr("text"))));
	            	
	        		JQuery jqdomSyl = new JQuery()._setName("jqdomSyl");
	            	_var(jqdomSyl, template.appendInto("jqdom"));
	            	
	            	_if("j%2==1");
	            		__(jqdomSyl.addClass(ViewSyllabisation.cSyllabeImpaire)  );
	            	endif();
	            endfor();
	        endif();
		};
								
		Anonym onExit = (/*ctx*/)->{
			_if("ctx!=null && ctx.row['_dom_']!=null");
				__($(var("ctx.row['_dom_']")).hide("50+(ctx.idx*20)", fct().__("$(this).remove()") ));
			endif();
			};
		
		__(aDataDriven.onEnter(fct("ctx").__(onEnter)));
		__(aDataDriven.onExit(fct("ctx").__(onExit)));	
		
		return aDataSet().getData();
	}
}
