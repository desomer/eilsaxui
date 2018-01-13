/**
 * 
 */
package com.elisaxui.app.elisys.xui.widget;

import com.elisaxui.app.elisys.xui.page.main.JSONPage1;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

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
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle().path(cDivSyllabisation)
						.add("min-height:20vh; line-height: 45px;"
							//*	+ "font-family: 'Open Sans', sans-serif;"
								+ "    font-size: 25px; font-stretch: expanded;"
								+ " background-color: #feffc7;"
								)
					
					 .path(cSyllabeMot)
					 	.add("margin:10px")
					 	
//					 .select(cMicro)
//					 	.set("width:100%")
						
					 .path(cSyllabeImpaire)
						.add("border: 1px solid rgba(255, 0, 239, 0.56);background: rgba(255, 53, 157, 0.14);") 	
								    
		;
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(  xDiv(cDivSyllabisation) );  
	}
	
	public static XMLElement getMot(Object text) {
		return xSpan(cSyllabeMot, xIdAction(JSONPage1.EVT_DO_MOT), xAttr("data-mot", text) , xSpan("  "));
	}
	
	public static XMLElement getSyl(Object text) {
		return xSpan(cSyllabe, text);
	}
}
