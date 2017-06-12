package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public abstract class XHTMLPart extends XMLPart {

	public static JSBuilder jsBuilder = new JSBuilder(null, new Object[] {});

	public final XMLPart vBody(Element body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(Element elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(AFTER_BODY.class, elem);
		return this;
	}

	/******************************************************************************/
	public final static Element xDiv(Object... inner) {
		return xElement("div", inner);
	}

	public final static Element xSpan(Object... inner) {
		return xElement("span", inner);
	}
	
	public final static Element xA(Object... inner) {
		return xElement("a", inner);
	}
	
	public final static Element xButton(Object... inner) {
		return xElement("button", inner);
	}

	public final static Element xH1(Object... inner) {
		return xElement("h1", inner);
	}
	
	public final static Element xH2(Object... inner) {
		return xElement("h2", inner);
	}


	public final static Element xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public final static Element xLi(Object... inner) {
		return xElement("li", inner);
	}
	
	public final static Element xImg(Object... inner) {
		return xElement("img", inner);
	}
	
	public final static Element xCanvas(Object... inner) {
		return xElement("canvas", inner);
	}

	public final static Element xComment(Object... comment) {
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

//	public final static JSFunction _fct(Object... param) {
//		return jsBuilder.createJSFunction().setParam(param);
//	}

	public static final String xVar(Object var) {
		return "'+" + var + "+'";
	}
	
	
	public final static Element xScriptJS(Object js) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}


	public final static CSSBuilder xCss() {
		return new CSSBuilder();
	}
	
	
	public final static Element xImport(Class<? extends JSClass> cl) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), XUIFactoryXHtml.getXMLFile().getClassImpl(jsBuilder, cl));
		return t;
	}
	
 
	@Deprecated
	@SuppressWarnings("unchecked")
	public final <E extends JSClass> E varOfType(Object name, Class<? extends JSClass> cl) {

		JSClass inst = jsBuilder.getProxy(cl);
		jsBuilder.setNameOfProxy("",inst, name);
		return (E) inst;
	}

	/**************************************************************************/

	public final static Attr xId(Object id) {
		Attr attr = xAttr("id", id);
		return attr;
	}
	
	public final static Attr xIdAction(Object id) {
		Attr attr = xAttr("data-x-action", id);
		return attr;
	}

	/****************************************************************************/
	public Object _new(Object... param) {
		return new JSVariable(param);
	}

	public static String txt(Object var) {
		return "\""+ var + "\"";
	}
}
