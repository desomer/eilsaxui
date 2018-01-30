package com.elisaxui.core.xui.xhtml;

import java.util.ArrayList;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSListParameter;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public abstract class XHTMLPart extends XMLPart {

	public static final ThreadLocal<JSBuilder> ThreadLocalJSBuilder = new ThreadLocal<>(); 
	
	public static JSBuilder getJSBuilder()
	{
		JSBuilder jsb = ThreadLocalJSBuilder.get();
		if (jsb==null)
		{
			jsb = new JSBuilder();
			ThreadLocalJSBuilder.set(jsb);
		}
		return jsb;
	}
	


	public final XMLPart vBody(XMLElement body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(XMLElement elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(AFTER_BODY.class, elem);
		return this;
	}

	/******************************************************************************/
	public static final XMLElement xDiv(Object... inner) {
		return xElement("div", inner);
	}
	
	public static final XMLElement xHeader(Object... inner) {
		return xElement("header", inner);
	}
	
	public static final XMLElement xFooter(Object... inner) {
		return xElement("footer", inner);
	}

	public static final XMLElement xSpan(Object... inner) {
		return xElement("span", inner);
	}
	
	public static final XMLElement xI(Object... inner) {
		return xElement("i", inner);
	}
	
	public static final XMLElement xA(Object... inner) {
		return xElement("a", inner);
	}
	
	public static final XMLElement xP(Object... inner) {
		return xElement("p", inner);
	}
	
	public static final XMLElement xButton(Object... inner) {
		return xElement("button", inner);
	}

	public static final XMLElement xH1(Object... inner) {
		return xElement("h1", inner);
	}
	
	public static final XMLElement xH2(Object... inner) {
		return xElement("h2", inner);
	}

	public static final XMLElement xUl(Object... inner) {
		return xElement("ul", inner);
	}

	public static final XMLElement xLi(Object... inner) {
		return xElement("li", inner);
	}
	
	public static final XMLElement xImg(Object... inner) {
		return xElement("img", inner);
	}
	
	public static final XMLElement xCanvas(Object... inner) {
		return xElement("canvas", inner);
	}
	
	public static final XMLElement xTextArea(Object... inner) {
		return xElement("textarea", inner);
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
	
	public static final JSMethodInterface js() {
		return getJSBuilder().createJSContent();
	}
	
	@Deprecated
	public static final JSFunction fct(Object...param)
	{
		return getJSBuilder().createJSFunction().setParam(param);
	}
	
	/**
	 *     ou js()   mais fragment peux etre conditionnel
	 * @return
	 */
	@Deprecated
	public final static JSFunction fragment()
	{
		return getJSBuilder().createJSFunction().setFragment(true);
	}

	public static final String xVar(Object var) {
		return "'+" + var + "+'";
	}
	
	@Deprecated
	public static JSVariable jsvar(Object... param) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			str.append(param[i]);
		}
		JSVariable var = new JSVariable();
		var._setName(str.toString());
		return var;
	}
	
	public static final XMLElement xScriptJS(Object js) {
		return xElement("script", xAttr("type", "\"text/javascript\""), js);
	}
	
//	public final static XMLElement xScriptJSAsync(Object js) {
//		XMLElement t = xElement("script", xAttr("async"), xAttr("type", "\"text/javascript\""), js);
//		return t;
//	}
	
	public static final XMLElement xScriptSrc(Object js) {
		return xElement("script", xAttr("src", "\""+js+"\""));
	}
	
	public final static XMLElement xScriptSrcAsync(Object js) {
		XMLElement t = xElement("script", xAttr("src", "\""+js+"\""), xAttr("async"));
		return t;
	}
	
	
	public final static XMLElement xScriptSrcAsync(Object js , Object fct) {
		XMLElement t = xElement("script", xAttr("src", "\""+js+"\""), xAttr("async"), xAttr("onload", txt(fct)));
		return t;
	}

	public final static XMLElement xLinkCss(String url) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("href", xTxt(url)));
	}
	public final static XMLElement xLinkManifest(String url) {
		return xElement("link", xAttr("rel", xTxt("manifest")), xAttr("href", xTxt(url)));
	}
	public final static XMLElement xLinkIcon(String url) {
		return xElement("link", xAttr("rel", xTxt("icon")), xAttr("href", xTxt(url)));
	}
	
	public final static XMLElement xLinkCssAsync(String url) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("media", xTxt("async")), xAttr("href", xTxt(url)), xAttr("onload", txt("resLoadedCss(this, 'all');")));
	}
	
	public final static XMLElement xLinkCssAsync(String url, Object fctTxt) {
		return xElement("link", xAttr("rel", xTxt("stylesheet")), xAttr("media", xTxt("async")), xAttr("onload", txt(fctTxt)), xAttr("href", xTxt(url)));
	}
	
	public final static CSSBuilder xStyle() {
		return new CSSBuilder();
	}
	
	public final static CSSBuilder xStyle(Object...path ) {
		return new CSSBuilder().path(path);
	}
	
	
	public final static XMLElement xImport(Class<? extends JSClass> cl) {
		JSClassImpl script = XUIFactoryXHtml.getXHTMLFile().getClassImpl(getJSBuilder(), cl);
		
		XMLElement t = xElement("script", xAttr("type", "\"text/javascript\""), script);
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
	@Deprecated
	public Object _new(Object... param) {
		return new JSListParameter(param);
	}

	public static String txt(Object var) {
		return "\""+ var + "\"";
	}
}
