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

	static XMLElement xElem(String name, Object... inner) {
		return XMLPart.xElement(name, inner);
	}
	
	default XMLElement xList(Object... array) {
		return XMLPart.xListElement(array);
	}
	
	/******************************************************************************/
	default XMLElement xDiv(Object... inner) {
		return xElem("div", inner);
	}
	
	default XMLElement xHeader(Object... inner) {
		return xElem("header", inner);
	}
	
	default XMLElement xNav(Object... inner) {
		return xElem("nav", inner);
	}
	
	default XMLElement xMain(Object... inner) {
		return xElem("main", inner);
	}
	
	default XMLElement xSection(Object... inner) {
		return xElem("section", inner);
	}
	
	default XMLElement xAside(Object... inner) {
		return xElem("aside", inner);
	}
	
	default XMLElement xFooter(Object... inner) {
		return xElem("footer", inner);
	}
	
	default XMLElement xArticle(Object... inner) {
		return xElem("article", inner);
	}

	default XMLElement xSpan(Object... inner) {
		return xElem("span", inner);
	}
	
	default XMLElement xI(Object... inner) {
		return xElem("i", inner);
	}
	
	default XMLElement xA(Object... inner) {
		return xElem("a", inner);
	}
	
	default XMLElement xP(Object... inner) {
		return xElem("p", inner);
	}
	
	default XMLElement xButton(Object... inner) {
		return xElem("button", inner);
	}

	default XMLElement xH1(Object... inner) {
		return xElem("h1", inner);
	}
	
	default XMLElement xH2(Object... inner) {
		return xElem("h2", inner);
	}

	default XMLElement xUl(Object... inner) {
		return xElem("ul", inner);
	}

	default XMLElement xLi(Object... inner) {
		return xElem("li", inner);
	}
	
	default XMLElement xImg(Object... inner) {
		return xElem("img", inner);
	}
	
	default XMLElement xCanvas(Object... inner) {
		return xElem("canvas", inner);
	}
	
	default XMLElement xTextArea(Object... inner) {
		return xElem("textarea", inner);
	}

	/*************************  META **************************************/
	
	default XMLElement xTitle(Object... inner) {
		return xElem("title", inner);
	}
	
	default XMLElement xMeta(Object... inner) {
		return xElem("meta", inner);
	}
	
	default XMLElement xComment(Object... comment) {
		ArrayList<Object> elem = new ArrayList<>();
		elem.add("<!--\n");
		for (Object c : comment) {
			elem.add(xList(c + "\n"));
		}
		elem.add(xList("-->"));
		return xElem(null, elem.toArray());
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
