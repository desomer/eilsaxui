/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
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
		if (array != null)
			for (int i = 0; i < array.length; i++) {
				if (array[i] instanceof Class)
					array[i] = XHTMLPart.xInclude((Class) array[i]);
				if (array[i] instanceof XMLPart)
					array[i] = XHTMLPart.vPart((XMLPart) array[i]);
			}

		return xListNode(array);
	}

	default XMLElement xListNode(Object... array) {
		return XMLPart.xListNodeStatic(array);
	}

	static XMLElement xNode(String name, Object... inner) {
		return XMLPart.xNode(name, inner);
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

	/************************* META **************************************/

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

	/************************** ATTR ************************************/
	default XMLAttr xId(Object value) {
		return XMLBuilder.createAttr("id", value);
	}

	/************************* VAR **************************************/
	@Deprecated()
	/**
	 * use vCalc
	 * 
	 * @param var
	 * @return
	 */
	default String xVar(Object var) {
		return "'+" + var + "+'";
	}

	default String vPropCalc(Object... var) {
		List<Object> l = Arrays.asList(var);
		boolean hasVar = false;
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i) instanceof JSElement) {
				hasVar = true;
			}
		}

		StringBuilder str = new StringBuilder();
		boolean lastIsString = false;
		if (hasVar) {

			for (int i = 0; i < l.size(); i++) {
				Object elem = l.get(i);
				boolean isVar = elem instanceof JSElement;
				if (!lastIsString || isVar)
					str.append("'");
				if (i > 0 && lastIsString && isVar)
					str.append("+");

				lastIsString = !isVar;
				str.append(elem);
			}
			if (lastIsString)
				str.append("'");

		} else {
			for (int i = 0; i < l.size(); i++) {
				str.append(l.get(i));
			}
		}
		return str.toString();
	}

}
