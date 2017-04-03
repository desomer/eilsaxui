package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.BODY;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.SCRIPT_AFTER_BODY;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xml.builder.javascript.JSContent;

public abstract class XHTMLPart extends XMLPart {

	public static JSBuilder jsBuilder = new JSBuilder(null, new Object[] {});

	public final XMLPart vBody(Element body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(Element elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(SCRIPT_AFTER_BODY.class, elem);
		return this;
	}

	public final Element xDiv(Object... inner) {
		return xElement("div", inner);
	}

	public final Element xSpan(Object... inner) {
		return xElement("span", inner);
	}

	public final Element xH1(Object... inner) {
		return xElement("h1", inner);
	}

	public final Element xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public final Element xLi(Object... inner) {
		return xElement("li", inner);
	}

	public final Element xComment(Object... comment) {
		ArrayList<Object> elem = new ArrayList<>();
		elem.add("<!--\n");
		for (Object c : comment) {
			elem.add(xListElement(c + "\n"));
		}
		elem.add(xListElement("-->"));
		return xElement(null, elem.toArray());
	}

	public final static JSContent js() {
		return jsBuilder.createJSContent();
	}

//	public final static JSFunction _fct(Object... param) {
//		return jsBuilder.createJSFunction().setParam(param);
//	}

	public String txtVar(Object var) {
		return "'+" + var + "+'";
	}

	public String txt(Object var) {
		return "'"+ var + "'";
	}
	
	public final Element xScriptJS(Object js) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}

//	public final JSClass _new() {
//		return null;
//	}

	
	public final Element xImport(Class<? extends JSClass> cl) {
		Element t = xElement("script", xAttr("type", "\"text/javascript\""), XUIFactoryXHtml.getXMLFile().getClassImpl(jsBuilder, cl));
		return t;
	}
	
 

	@SuppressWarnings("unchecked")
	public final <E extends JSClass> E varOfType(Object name, Class<? extends JSClass> cl) {

		JSClass inst = jsBuilder.getProxy(cl);
		jsBuilder.setNameOfProxy("",inst, name);
		return (E) inst;
	}

	/**************************************************************************/

	public final Attr xID(Object id) {
		Attr attr = xAttr("id", id);
		return attr;
	}

}
