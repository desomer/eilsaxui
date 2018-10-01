/**
 * 
 */
package com.elisaxui.core.xui;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.app.CacheManager;
import com.elisaxui.core.xui.app.ConfigFormat;
import com.elisaxui.core.xui.app.XHTMLAppScanner;
import com.elisaxui.core.xui.app.XHTMLChangeManager;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLTemplateRoot;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
public abstract class XUIFactory {

	protected static final String CACHE_CONTROL = "cache-control";
	public static final XHTMLChangeManager changeMgr = new XHTMLChangeManager();
	public static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<>();

	public static final XMLPart getXHTMLTemplateRoot() {
		return ThreadLocalXUIFactoryPage.get().getXHTMLTemplate();
	}

	public static final XMLFile getXMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	public static final XHTMLFile getXHTMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	/**
	 * @param fileXML
	 * @param loc
	 * @return
	 */
	protected static final String toXML(XHTMLFile fileXML, Locale loc, XMLBuilder xmlBuf) {

		if (xmlBuf == null) {
			StringBuilder buf = new StringBuilder(1000);
			buf.append("<!doctype html>");
			((XHTMLTemplateRoot) fileXML.getXHTMLTemplate()).setLang(loc.toLanguageTag());
			xmlBuf = new XMLBuilder("page", buf, null);
		}

		fileXML.getXHTMLTemplate().zzDoContent();
		List<XMLElement> rootContent = fileXML.getXHTMLTemplate().getListElementFromTarget(CONTENT.class);

		for (XMLElement elem : rootContent) {
			elem.toXML(xmlBuf);
		}

		return xmlBuf.getContent().toString();
	}

	/**
	 * 
	 */
	public XUIFactory() {
		super();
	}

	/**
	 * @param headers
	 * @param uri
	 * @param headerConfig
	 * @return
	 */
	protected void initResquestConfig(RequestConfig requestConfig) {
		List<String> cacheControl = requestConfig.headers.getRequestHeader(CACHE_CONTROL);
		requestConfig.noCache = cacheControl != null && cacheControl.get(0).equals("no-cache");

		CoreLogger.getLogger(1).info(() -> "cacheControl=" + cacheControl);

		MultivaluedMap<String, String> param = requestConfig.uri.getQueryParameters();

		List<String> p = param.get("compatibility");
		requestConfig.setEs5(p != null && p.get(0).equals("es5"));
		if (ConfigFormat.getData().isEs5())
			requestConfig.setEs5(true);

		p = param.get("version");
		requestConfig.version = p == null ? ConfigFormat.getData().getVersionTimeline() : Integer.parseInt(p.get(0));

		if (ConfigFormat.getData().isReload()) {
			if (requestConfig.version == 0)
				requestConfig.noCache = true;
			ConfigFormat.getData().setReload(false);
		}

		requestConfig.param = param;
	}

	/**
	 * 
	 */
	protected synchronized void initChangeManager() {
		if (changeMgr.mapClass.isEmpty() || changeMgr.dateInjection == null) {
			XHTMLAppScanner.getMapXHTMLPart(changeMgr);
		}
	}

	/**
	 * creer un XHTMLFile vide
	 * 
	 * @return
	 */
	protected XHTMLFile createXHTMLFile() {
		XHTMLFile file = new XHTMLFile();
		ProxyHandler.ThreadLocalMethodDesc.set(null);
		ThreadLocalXUIFactoryPage.set(file);
		return file;
	}

	protected abstract void doSubFile(RequestConfig requestConfig, XHTMLFile fileXML);

	/**
	 * @param requestConfig
	 * @param param
	 * @param cache
	 * @param xHTMLPartClass
	 */
	protected Response createVersion(RequestConfig requestConfig, CacheManager cache,
			Class<? extends XHTMLPart> xHTMLPartClass) {
		JSExecutorHelper.initThread();

		Locale loc = Locale.FRENCH;
		if (requestConfig.headers != null) {
			List<Locale> languages = requestConfig.headers.getAcceptableLanguages();
			loc = languages.get(0);
		}

		XHTMLFile fileXML = null;
		fileXML = createXHTMLFile();
		fileXML.setXHTMLTemplate(new XHTMLTemplateRoot());
		CoreLogger.getLogger(1).info(() -> "****** GENERATE PHASE 1 : " + xHTMLPartClass.getSimpleName() + " ********");
		// premier passe (execute les annotation)
		initXMLFile(xHTMLPartClass, fileXML, requestConfig.param);

		if (ErrorNotificafionMgr.hasErrorMessage()) {
			// affiche la page d'erreur
			return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
					.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
		}

		// generation page
		CoreLogger.getLogger(1)
				.info(() -> "******" + " GENERATE PHASE 2 : " + xHTMLPartClass.getSimpleName() + " ********");

		// deuxieme passe (execute les toXML)
		String html = toXML(fileXML, loc, null);

		CoreLogger.getLogger(1)
				.info(() -> "******" + " GENERATE PHASE SUBFILE : " + xHTMLPartClass.getSimpleName() + " ********");

		cache.setResult(html);

		// execute les sub fichier
		doSubFile(requestConfig, fileXML);

		if (cache.isStore()) {
			cache.storeResultInDb(false);
			cache.getVersionDB(0); // calcul la difference
		}

		CoreLogger.getLogger(1)
				.info(() -> "******" + " GENERATE PHASE COMMIT : " + xHTMLPartClass.getSimpleName() + " ********");
		cache.commit();
		CoreLogger.getLogger(1)
				.info(() -> "******" + " GENERATE PHASE END COMMIT : " + xHTMLPartClass.getSimpleName() + " ********");

		JSExecutorHelper.stopThread();
		CoreLogger.getLogger(1)
				.info(() -> "******" + " GENERATE PHASE TERNINATED : " + xHTMLPartClass.getSimpleName() + " ********");

		return null;
	}

	protected void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file,
			MultivaluedMap<String, String> requestParam) {
		try {

			((XHTMLFile) file).setParam(requestParam);

			XHTMLPart page = pageClass.newInstance();
			file.setMainXMLPart(page);

			xCoreVersion coreVersion = pageClass.getAnnotation(xCoreVersion.class);
			((XHTMLFile) file).setCoreVersion(coreVersion == null ? "1" : coreVersion.value());

			page.zzDoContent();
			for (XMLElement elem : page.getListElementFromTarget(CONTENT.class)) {
				page.vBody(elem);
			}
			for (XMLElement elem : page.getListElementFromTarget(AFTER_CONTENT.class)) {
				page.vAfterBody(elem);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			ErrorNotificafionMgr.doError("Pb instanciation " + pageClass.getName(), e);
		}
	}

	public class RequestConfig {
		HttpHeaders headers;
		UriInfo uri;

		/**
		 * @param headers
		 * @param uri
		 */
		public RequestConfig(HttpHeaders headers, UriInfo uri) {
			super();
			this.headers = headers;
			this.uri = uri;
		}

		/**
		 * @return the es5
		 */
		public boolean isEs5() {
			return es5;
		}

		/**
		 * @param es5
		 *            the es5 to set
		 */
		public void setEs5(boolean es5) {
			this.es5 = es5;
		}

		private boolean es5;
		int version;
		boolean noCache;

		MultivaluedMap<String, String> param;
	}
}