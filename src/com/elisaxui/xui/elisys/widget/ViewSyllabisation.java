/**
 * 
 */
package com.elisaxui.xui.elisys.widget;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author Bureau
 *
 */
public class ViewSyllabisation extends XHTMLPart {

	static XClass cDivSyllabisation;
	static XClass cSyllabeMot;
	static XClass cSyllabe;
	static XClass cSyllabeImpaire;
	
	public static XClass cMicro;
	
	String fontOpendys = "data:application/x-font-woff;charset=utf-8;base64,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";
	

	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss().select(cDivSyllabisation)
						.set("min-height:20vh; line-height: 35px;"
								+ "    font-size: large; font-stretch: expanded;")
					
					 .select(cSyllabeMot)
					 	.set("margin:10px")
						
					 .select(cSyllabeImpaire)
						.set("border: 1px solid rgba(255, 0, 239, 0.56);background: rgba(255, 53, 157, 0.14);") 	
						
					    
					    
		;
	}
	
	
	/*
	 * 
	  var recognition = new webkitSpeechRecognition();
  recognition.continuous = true;
  //recognition.interimResults = true;
  recognition.lang = "fr-FR";
  
   recognition.onresult = function(event) {
    var interim_transcript = '';
    for (var i = event.resultIndex; i < event.results.length; ++i) {
      if (event.results[i].isFinal) {
        final_transcript += event.results[i][0].transcript;
      } else {
        interim_transcript += event.results[i][0].transcript;
      }
    }
  
  
	 */
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv( xDiv(cDivSyllabisation), xElement("textarea", cMicro, xAttr("rows","4"), xAttr("cols","50") ) );
	}
	
	public static XMLElement getMot(Object text) {
		return xSpan(cSyllabeMot, xSpan( text) , xSpan("  "));
	}
	
	public static XMLElement getSyl(Object text) {
		return xSpan(cSyllabe, text);
	}
}