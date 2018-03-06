package com.elisaxui.core.xui.xhtml;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContentInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSListParameter;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.target.AFTER_BODY;
import com.elisaxui.core.xui.xhtml.target.BODY;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLElement;

public abstract class XHTMLPart extends XMLPart implements IXHTMLBuilder {

	private static final String ASYNC = "async";
	private static final String ONLOAD = "onload";
	private static final String STYLESHEET = "stylesheet";
	private static final String SCRIPT = "script";

	public final XMLPart vBody(XMLElement body) {
		XUIFactoryXHtml.getXMLRoot().addElement(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(XMLElement elem) {
		XUIFactoryXHtml.getXMLRoot().addElement(AFTER_BODY.class, elem);
		return this;
	}

	/***********************************************************************/
	/**
	 *  pas dans un XMLPart mais un JSClass
	 *  
	 * @return
	 */
	@Deprecated
	public static final JSContentInterface js() {
		return new JSContent();
	}

	/**
	 *  pas dans un XMLPart mais un JSClass
	 */
	@Deprecated
	public static final JSFunction fct(Object... param) {
		return new JSFunction().setParam(param);
	}

	/**
	 * ou js() mais fragment peux etre conditionnel
	 *   pas dans un XMLPart mais un JSClass
	 *   
	 *   utiliser aussi pour surcharge
	 * @return
	 */
	@Deprecated
	public static final JSFunction fragment() {
		return new JSFunction().setFragment(true);
	}

	/**
	 * 
	 *  pas dans un XMLPart mais un JSClass
	 * 
	 * @param param
	 * @return
	 */
	@Deprecated
	public static JSAny jsvar(Object... param) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			str.append(param[i]);
		}
		JSAny var = new JSAny();
		var._setName(str.toString());
		return var;
	}

	public static final XMLElement xScriptJS(Object js) {
		return xElement(SCRIPT, xAttr("type", "\"text/javascript\""), js);
	}

	public static final XMLElement xScriptSrc(Object js) {
		return xElement(SCRIPT, xAttr("src", "\"" + js + "\""));
	}

	public static final XMLElement xScriptSrcAsync(Object js) {
		return xElement(SCRIPT, xAttr("src", "\"" + js + "\""), xAttr(ASYNC));
	}

	public static final XMLElement xScriptSrcAsync(Object js, Object fct) {
		return xElement(SCRIPT, xAttr("src", "\"" + js + "\""), xAttr(ASYNC), xAttr(ONLOAD, txt(fct)));
	}

	public static final XMLElement xLinkCss(String url) {
		return xElement("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("href", xTxt(url)));
	}

	public static final XMLElement xLinkManifest(String url) {
		return xElement("link", xAttr("rel", xTxt("manifest")), xAttr("href", xTxt(url)));
	}

	public static final XMLElement xLinkIcon(String url) {
		return xElement("link", xAttr("rel", xTxt("icon")), xAttr("href", xTxt(url)));
	}

	public static final XMLElement xLinkCssAsync(String url) {
		return xElement("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("media", xTxt(ASYNC)), xAttr("href", xTxt(url)),
				xAttr(ONLOAD, txt("resLoadedCss(this, 'all');")));
	}

	public static final XMLElement xLinkCssAsync(String url, Object fctTxt) {
		return xElement("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("media", xTxt(ASYNC)), xAttr(ONLOAD, txt(fctTxt)),
				xAttr("href", xTxt(url)));
	}

	public static final CSSBuilder xStyle() {
		return new CSSBuilder();
	}

	public static final CSSBuilder xStyle(Object... path) {
		return new CSSBuilder().path(path);
	}
	
	public static final XMLElement xImport(Class<?>...cl) {
		Object[] r = new Object[cl.length];
		
		for (int i = 0; i < r.length; i++) {
			r[i] = doImport((Class<? extends JSClass>) cl[i]);
		}
		
		return xListElement(r);
	}

	public static final XMLElement xImport(Class<? extends JSClass> cl) {
		return doImport(cl);
	}

	/**
	 * @param cl
	 * @return
	 */
	private static XMLElement doImport(Class<? extends JSClass> cl) {
		JSClassBuilder script = XUIFactoryXHtml.getXHTMLFile().getClassImpl(cl);

		Object autocall = null;
		if (script.getAutoCallMeth() != null) {
			JSClass jsclass = JSContent.declareType(cl, cl.getSimpleName());
			autocall = new JSContent().__(jsclass, ".", script.getAutoCallMeth());
		}

		return xElement(SCRIPT, xAttr("type", "\"text/javascript\""), script, autocall);
	}

	/**************************************************************************/

	public static final XMLAttr xIdAction(Object id) {
		return xAttr("data-x-action", id);
	}

	/****************************************************************************/
	@Deprecated
	public Object _new(Object... param) {
		return new JSListParameter(param);
	}

	public static final String txt(Object var) {
		return "\"" + var + "\"";
	}
}
