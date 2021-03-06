package com.elisaxui.core.xui.xhtml;

import com.elisaxui.component.toolkit.core.JSActionManager;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSElement;
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
	
	public static final String SCRIPT = "script";
	public static final String STYLE = "style";
	
	/****************************************************************/
	
	public final XMLPart vBody(XMLElement body) {
		XUIFactoryXHtml.getXHTMLTemplateRoot().addElementOnTarget(BODY.class, body);
		return this;
	}

	public final XMLPart vAfterBody(XMLElement elem) {
		XUIFactoryXHtml.getXHTMLTemplateRoot().addElementOnTarget(AFTER_BODY.class, elem);
		return this;
	}

	/***********************************************************************/
	/**
	 *  pas dans un XMLPart mais un JSClass
	 *  
	 * @return
	 */
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
		JSAny var = new JSAny();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			str.append(param[i]);
		}
		var._setName(str.toString());	
		return var;
	}

	/**
	 * 
	 * xScriptJS(loadResourceFromURL("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"))
	 * xScriptJS(js().__("var a=1"))
	 * 
	 * @param js
	 * @return
	 */
	public static final XMLElement xScriptJS(Object js) {
		return xNode(SCRIPT, xAttr("type", "\"text/javascript\""), js);
	}

	public static final XMLElement xScriptSrc(Object js) {
		return xNode(SCRIPT, xAttr("src", "\"" + js + "\""));
	}
	
	public static final XMLElement xScriptModule(Object js) {
		return xNode(SCRIPT, xAttr("type","'module'"), xAttr("src", "\"" + js + "\""));
	}

	public static final XMLElement xScriptSrcAsync(Object js) {
		return xNode(SCRIPT, xAttr("src", "\"" + js + "\""), xAttr(ASYNC));
	}

	public static final XMLElement xScriptSrcAsync(Object js, Object fct) {
		return xNode(SCRIPT, xAttr("src", "\"" + js + "\""), xAttr(ASYNC), xAttr(ONLOAD, txt(fct)));
	}

	public static final XMLElement xLinkCss(String url) {
		return xNode("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("href", xTxt(url)));
	}

	public static final XMLElement xLinkModulePreload(String url) {
		return xNode("link", xAttr("rel", xTxt("modulepreload")), xAttr("href", xTxt(url)));
	}
	
	public static final XMLElement xLinkPrerender(String url) {
		return xNode("link", xAttr("rel", xTxt("prerender")), xAttr("href", xTxt(url)));
	}
	
	public static final XMLElement xLinkManifest(String url) {
		return xNode("link", xAttr("rel", xTxt("manifest")), xAttr("href", xTxt(url)));
	}

	public static final XMLElement xLinkIcon(String url) {
		return xNode("link", xAttr("rel", xTxt("icon")), xAttr("href", xTxt(url)));
	}

	@Deprecated
	public static final XMLElement xLinkCssAsync(String url) {
		return xNode("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("media", xTxt(ASYNC)), xAttr("href", xTxt(url)),
				xAttr(ONLOAD, xTxt("resLoadedCss(this, 'all');")));
	}

	
//	public static final XMLElement xLinkCssAsync(String url, Object fctTxt) {
//		return xNode("link", xAttr("rel", xTxt(STYLESHEET)), xAttr("media", xTxt(ASYNC)), xAttr(ONLOAD, xTxt(fctTxt)),
//				xAttr("href", xTxt(url)));
//	}
	
	public static final XMLElement xLinkCssPreload(String url) {
		return xNode("link", xAttr("rel", xTxt("preload")), xAttr(ONLOAD, xTxt("this.rel='stylesheet'")),
				xAttr("href", xTxt(url)), xAttr("as", xTxt(STYLE)));
	}

	@Deprecated
	public static final CSSElement cStyle() {
		return new CSSElement();
	}

	@Deprecated
	public static final CSSElement cStyle(Object... path) {
		return new CSSElement().path(path);
	}
	
	/**
	 *  use xElem(aJSClass.class, ...)
	 * @param cl
	 * @return
	 */
	@Deprecated
	public static final XMLElement xIncludeJS(Class<? extends JSClass> cl) {
		return doInclude(cl);
	}

	/**
	 * @param cl
	 * @return
	 */
	private static XMLElement doInclude(Class<? extends JSClass> cl) {
		JSClassBuilder script = XUIFactoryXHtml.getXHTMLFile().getClassImpl(cl, true);

		Object autocall = null;
		if (script.getAutoCallMeth() != null) {
			JSClass jsclass = JSContent.declareType(cl, cl.getSimpleName());
			autocall = new JSContent().__(jsclass, ".", script.getAutoCallMeth());
		}

		return xNode(SCRIPT, xAttr("type", "\"text/javascript\""), script, autocall);
	}

	/**************************************************************************/
	/** TODO a metter dans une interface IJSAction  */
	public static final XMLAttr xIdAction(Object id) {
		return xAttr(JSActionManager.DATA_X_ACTION, id);
	}

	/****************************************************************************/
	@Deprecated
	// newJS
	public Object _new(Object... param) {
		return new JSListParameter(param);
	}
	
	@Deprecated
	/** xTxt */
	public static final String txt(Object var) {
		return "\"" + var + "\"";
	}
}
