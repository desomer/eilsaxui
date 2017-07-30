package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.BODY;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSListParameter;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public abstract class XHTMLPart extends XMLPart {

	public static JSBuilder jsBuilder = new JSBuilder();

	public final XMLPart vBody(XMLElement body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(XMLElement elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(AFTER_BODY.class, elem);
		return this;
	}

	/******************************************************************************/
	public final static XMLElement xDiv(Object... inner) {
		return xElement("div", inner);
	}

	public final static XMLElement xSpan(Object... inner) {
		return xElement("span", inner);
	}
	
	public final static XMLElement xA(Object... inner) {
		return xElement("a", inner);
	}
	
	public final static XMLElement xButton(Object... inner) {
		return xElement("button", inner);
	}

	public final static XMLElement xH1(Object... inner) {
		return xElement("h1", inner);
	}
	
	public final static XMLElement xH2(Object... inner) {
		return xElement("h2", inner);
	}


	public final static XMLElement xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public final static XMLElement xLi(Object... inner) {
		return xElement("li", inner);
	}
	
	public final static XMLElement xImg(Object... inner) {
		return xElement("img", inner);
	}
	
	public final static XMLElement xCanvas(Object... inner) {
		return xElement("canvas", inner);
	}

	public final static XMLElement xComment(Object... comment) {
		ArrayList<Object> elem = new ArrayList<>();
		elem.add("<!--\n");
		for (Object c : comment) {
			elem.add(xListElement(c + "\n"));
		}
		elem.add(xListElement("-->"));
		return xElement(null, elem.toArray());
	}

	public final static JSMethodInterface js() {
		return jsBuilder.createJSContent();
	}
	
	public final static JSFunction fct(Object...param)
	{
		return jsBuilder.createJSFunction().setParam(param);
	}

	public static final String xVar(Object var) {
		return "'+" + var + "+'";
	}
	
	
	public final static XMLElement xScriptJS(Object js) {
		XMLElement t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}


	public final static CSSBuilder xCss() {
		return new CSSBuilder();
	}
	
	
	public final static XMLElement xImport(Class<? extends JSClass> cl) {
		XMLElement t = xElement("script", xAttr("type", "\"text/javascript\""), XUIFactoryXHtml.getXHTMLFile().getClassImpl(jsBuilder, cl));
		return t;
	}
	
 
	/**************************************************************************/

	public final static XMLAttr xId(Object id) {
		XMLAttr attr = xAttr("id", id);
		return attr;
	}
	
	public final static XMLAttr xIdAction(Object id) {
		XMLAttr attr = xAttr("data-x-action", id);
		return attr;
	}

	/****************************************************************************/
	public Object _new(Object... param) {
		return new JSListParameter(param);
	}

	public static String txt(Object var) {
		return "\""+ var + "\"";
	}
}
