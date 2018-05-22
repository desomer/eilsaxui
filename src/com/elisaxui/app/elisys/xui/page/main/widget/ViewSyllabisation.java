/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.main.widget;

import static com.elisaxui.component.toolkit.transition.CssTransition.cFixedElement;

import com.elisaxui.app.elisys.xui.page.main.JSONPage1;
import com.elisaxui.component.widget.button.ViewBtnCircle;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewSyllabisation extends XHTMLPart {

	static CSSClass cDivSyllabisation;
	static CSSClass cSyllabeMot;
	static CSSClass cSyllabe;
	static CSSClass cSyllabeImpaire;
	
	public static CSSClass cMicro;
	
	@xTarget(HEADER.class)
	@xResource
	public XMLElement xStylePart() {

		return cStyle().path(cDivSyllabisation)
						.set("min-height:20vh; line-height: 45px;"
							//*	+ "font-family: 'Open Sans', sans-serif;"
								+ "    font-size: 25px; font-stretch: expanded;"
								+ " background-color: #feffc7;"
								)
					
					 .path(cSyllabeMot)
					 	.set("margin:10px")
					 	
//					 .select(cMicro)
//					 	.set("width:100%")
						
					 .path(cSyllabeImpaire)
						.set("border: 1px solid rgba(255, 0, 239, 0.56);background: rgba(255, 53, 157, 0.14);") 	
								    
		;
	}
	
	
	@xTarget(CONTENT.class)
	public XMLElement xContenu() {
		return xDiv(  xDiv(cDivSyllabisation) );  
	}
	
	public static XMLElement getMot(Object text) {
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xSpan(cSyllabeMot, xIdAction(JSONPage1.EVT_DO_MOT), xAttr("data-mot", text) , xSpan("  "));
			}
		};
		
		return template.getTemplate();
	}
	
	public static XMLElement getSyl(Object text) {
		IJSDomTemplate template = new IJSDomTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xSpan(cSyllabe, text);
			}
		};
		
		return template.getTemplate();
	}
}
