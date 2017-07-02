/**
 * 
 */
package com.elisaxui.xui.core.widget.container;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.builder.css.CSSClass;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLElement;
import com.elisaxui.xui.core.widget.button.ViewBtnBurger;

/**
 * @author Bureau
 *
 */
public class ViewCard extends XHTMLPart {

	CSSClass cPanel;
	
	@xTarget(HEADER.class)
	@xRessource
	public XMLElement xStyle() {

		return xCss()
				.on(cPanel, "padding: 15px;"
						+ "margin-bottom: 15px;"
						+ "border-radius: 0;"
						+ "background-color: #FFF;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.14), 0 3px 1px -2px rgba(0,0,0,.2), 0 1px 5px 0 rgba(0,0,0,.12);"
					//	+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12)"
						)
				;			
	}
	
	@xTarget(CONTENT.class)
	public XMLElement xViewPanel() {
		return xDiv(cPanel, this.getChildren());
	}
	
	public static XMLElement getTemplate(ViewCard card) {
		return xListElement(xPart(card, vHandle("childrenCard") ));
	}
}
