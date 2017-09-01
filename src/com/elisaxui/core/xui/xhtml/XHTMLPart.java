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
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
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
	
	public final static XMLElement xHeader(Object... inner) {
		return xElement("header", inner);
	}

	public final static XMLElement xSpan(Object... inner) {
		return xElement("span", inner);
	}
	
	public final static XMLElement xA(Object... inner) {
		return xElement("a", inner);
	}
	
	public final static XMLElement xP(Object... inner) {
		return xElement("p", inner);
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

	/*************************  META **************************************/
	
	public final static XMLElement xTitle(Object... inner) {
		return xElement("title", inner);
	}
	
	public final static XMLElement xMeta(Object... inner) {
		return xElement("meta", inner);
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

	
	/***********************************************************************/
	public final static JSMethodInterface js() {
		return jsBuilder.createJSContent();
	}
	
	public final static JSFunction fct(Object...param)
	{
		return jsBuilder.createJSFunction().setParam(param);
	}
	
	public final static JSFunction fragment()
	{
		return jsBuilder.createJSFunction().setFragment(true);
	}

	public static final String xVar(Object var) {
		return "'+" + var + "+'";
	}
	
	public static JSVariable jsvar(Object... param) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			str.append(param[i]);
		}
		JSVariable var = new JSVariable();
		var.setName(str.toString());
		return var;
	}
	
	public final static XMLElement xScriptJS(Object js) {
		XMLElement t = xElement("script", xAttr("type", "\"text/javascript\""), js);
		return t;
	}
	
//	public final static XMLElement xScriptJSAsync(Object js) {
//		XMLElement t = xElement("script", xAttr("async"), xAttr("type", "\"text/javascript\""), js);
//		return t;
//	}
	
	public final static XMLElement xScriptSrc(Object js) {
		XMLElement t = xElement("script", xAttr("src", "\""+js+"\""));
		return t;
	}
	
	public final static XMLElement xScriptSrcAsync(Object js) {
		XMLElement t = xElement("script", xAttr("src", "\""+js+"\""), xAttr("async"));
		return t;
	}
	
	
	public final static XMLElement xScriptSrcAsync(Object js , Object fct) {
		XMLElement t = xElement("script", xAttr("src", "\""+js+"\""), xAttr("async"), xAttr("onload", txt(fct)));
		return t;
	}

//						+ "<link rel='stylesheet' media='none' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"

	public final static XMLElement xLinkCss(String url) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("href", xTxt(url)));
	}
	
	public final static XMLElement xLinkCssAsync(String url) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("media", xTxt("async")), xAttr("href", xTxt(url)));
	}
	
	public final static XMLElement xLinkCssAsync(String url, Object fct) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("media", xTxt("async")), xAttr("onload", txt(fct)), xAttr("href", xTxt(url)));
	}
	
	public final static CSSBuilder xCss() {
		return new CSSBuilder();
	}
	
	public final static CSSBuilder xCss(Object...path ) {
		return new CSSBuilder().select(path);
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
