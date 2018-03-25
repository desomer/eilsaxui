/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;

import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

/**
 * @author gauth
 *
 */
public interface IXHTMLBuilder {
	
	default XMLElement xElem(Object... array) {
		return XMLPart.xListElement(array);
	}
	
	default XMLElement xListNode(Object... array) {
		return XMLPart.xListElement(array);
	}
	
	
	static XMLElement xNode(String name, Object... inner) {
		return XMLPart.xElement(name, inner);
	}
	/******************************************************************************/
	default XMLElement xDiv(Object... inner) {
		return xNode("div", inner);
	}
	
	default XMLElement xHeader(Object... inner) {
		return xNode("header", inner);
	}
	
	default XMLElement xNav(Object... inner) {
		return xNode("nav", inner);
	}
	
	default XMLElement xMain(Object... inner) {
		return xNode("main", inner);
	}
	
	default XMLElement xSection(Object... inner) {
		return xNode("section", inner);
	}
	
	default XMLElement xAside(Object... inner) {
		return xNode("aside", inner);
	}
	
	default XMLElement xFooter(Object... inner) {
		return xNode("footer", inner);
	}
	
	default XMLElement xArticle(Object... inner) {
		return xNode("article", inner);
	}

	default XMLElement xSpan(Object... inner) {
		return xNode("span", inner);
	}
	
	default XMLElement xI(Object... inner) {
		return xNode("i", inner);
	}
	
	default XMLElement xA(Object... inner) {
		return xNode("a", inner);
	}
	
	default XMLElement xP(Object... inner) {
		return xNode("p", inner);
	}
	/************************************************/
	default XMLElement xButton(Object... inner) {
		return xNode("button", inner);
	}
	
	default XMLElement xInput(Object... inner) {
		return xNode("input", inner);
	}
	
	default XMLElement xLabel(Object... inner) {
		return xNode("label", inner);
	}
	/************************************************/
	default XMLElement xH1(Object... inner) {
		return xNode("h1", inner);
	}
	
	default XMLElement xH2(Object... inner) {
		return xNode("h2", inner);
	}

	/************************************************/
	default XMLElement xUl(Object... inner) {
		return xNode("ul", inner);
	}

	default XMLElement xLi(Object... inner) {
		return xNode("li", inner);
	}
	/************************************************/
	default XMLElement xImg(Object... inner) {
		return xNode("img", inner);
	}
	
	default XMLElement xCanvas(Object... inner) {
		return xNode("canvas", inner);
	}
	
	default XMLElement xTextArea(Object... inner) {
		return xNode("textarea", inner);
	}

	/*************************  META **************************************/
	
	default XMLElement xTitle(Object... inner) {
		return xNode("title", inner);
	}
	
	default XMLElement xMeta(Object... inner) {
		return xNode("meta", inner);
	}
	
	default XMLElement xComment(Object... comment) {
		ArrayList<Object> elem = new ArrayList<>();
		elem.add("<!--\n");
		for (Object c : comment) {
			elem.add(xListNode(c + "\n"));
		}
		elem.add(xListNode("-->"));
		return xNode(null, elem.toArray());
	}

	/**************************  ATTR  ************************************/
	default XMLAttr xId(Object value) {
		return XMLBuilder.createAttr("id", value);
	}
	
	
	/*************************  VAR  **************************************/
	default String xVar(Object var) {
		return "'+" + var + "+'";
	}
}
