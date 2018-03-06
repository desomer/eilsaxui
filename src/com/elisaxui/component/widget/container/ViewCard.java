/**
 * 
 */
package com.elisaxui.component.widget.container;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public class ViewCard extends XHTMLPart {

	XClass cCard;
	static XClass cCardText;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStylePart() {

		return xStyle()
				.path(cCard).add("padding: 10px;"
					//	+ "opacity:0;"
						+ "margin: 10px 5px 0 5px;"
						+ "border-radius: 4px;"
						+ "background-color: #FFF;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.14), 0 3px 1px -2px rgba(0,0,0,.2), 0 1px 5px 0 rgba(0,0,0,.12);"
						).childPath(xStyle(cCardText).add("user-select: none;font-size: 14px;display: block; "
								+ "left: 0;right: 0;top: 100px; padding: 16px; margin: 0;"
								+ "line-height: 1.6; po2sition: absolute; color: #000;"
								+ "overflow: hidden;")
								)

				;			
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		return xDiv(cCard, this.getChildren());
	}
	
	public static XMLElement getTemplate(ViewCard card) {
		return xListElement(xPart(card, searchProperty("childrenCard") ));
	}
	
	public static XMLElement getTemplateRichMedia() {
		IXHTMLTemplate template = new IXHTMLTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xDiv(xAttr("style","\"width: 100%; height: 30vh\""));
			}
		};
		
		return template.getTemplate();
	}
	
	public static XMLElement getTemplateText(Object text) {
		IXHTMLTemplate template = new IXHTMLTemplate() {
			@Override
			public XMLElement getTemplate()
			{
				return xP(cCardText, xVar(text));
			}
		};
		
		return template.getTemplate();
	}
	
}
