package com.elisaxui.core.xui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.elisaxui.component.page.JSServiceWorker;
import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.config.ConfigFormat;
import com.elisaxui.core.xui.config.XHTMLAppScanner;
import com.elisaxui.core.xui.config.XHTMLChangeManager;
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

@Path("/")
public class XUIFactoryXHtml {

	private static final String POINT = ".";
	private static final String IF_NONE_MATCH = "if-none-match";
	private static final String CACHE_CONTROL = "cache-control";
	
	public static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<>();
	public static final XHTMLChangeManager changeMgr = new XHTMLChangeManager();
	boolean enableCache = true;
	
	public static final XMLPart getXHTMLTemplateRoot() {
		return ThreadLocalXUIFactoryPage.get().getXHTMLTemplate();
	}

	public static final XMLFile getXMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}
	
	public static final XHTMLFile getXHTMLFile() {
		return (XHTMLFile)ThreadLocalXUIFactoryPage.get();
	}
	
	@GET
	@Path("/page/{pays}/{lang}/id/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response getHtml(
			@Context HttpServletRequest httpRequest, 
			@Context HttpHeaders headers, 
			@Context UriInfo uri, 
			@PathParam("pays") String pays,
			@PathParam("lang") String lang, 
			@PathParam("id") String id) {

		
		RequestConfig requestConfig = new RequestConfig(headers, uri);
		
		initResquestConfig(requestConfig);
		
		JSExecutorHelper.setThreadPreprocessor(requestConfig.es5);
				
		// cherche les changements
		initChangeManager();
		
		// cherche dans les caches
		CacheManager cache = new CacheManager(id, ""+requestConfig.es5, "html");
		cache.initVersion(requestConfig.noCache, requestConfig.version);
				
		if (cache.result == null) {   // si pas en cache
			// recupere la classe de page
			Class<? extends XHTMLPart> xHTMLPartClass = changeMgr.mapClass.get(id);
			if (xHTMLPartClass != null) {
				Response err = createVersion(requestConfig, cache, xHTMLPartClass);		
				if (err!=null)	return err;
			}
		} else {
			CoreLogger.getLogger(1).info(() -> "****** get cache " + cache.idCacheDB);
		}
		
		if (cache.result==null)
			return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
				
		StringBuilder dif = new StringBuilder();
		if (cache.fileComparator.listLineDiff.length()>0)
		{
			dif.append("\n\n<script id='srcdiff' type='application/json'>");
			dif.append(cache.fileComparator.listLineDiff);
			dif.append("\n</script>");
		}
				
		String response = cache.result+dif;
		
		List<String> etag = headers.getRequestHeader(IF_NONE_MATCH);
		String eTagRequest = etag==null?"":etag.get(0);
		String eTagResponse = calculateEtag(response);
		
		if (eTagRequest.equals(eTagResponse))
		{
			return Response.status(Status.NOT_MODIFIED)
					.header("etag", eTagResponse)
					.build();
		}
		
		return Response.status(Status.OK)
				.entity(response)
				.header("ETag", calculateEtag(response))
				.build();
	}

	/**
	 * @param requestConfig
	 * @param param
	 * @param cache
	 * @param xHTMLPartClass
	 */
	private Response createVersion(RequestConfig requestConfig, CacheManager cache, Class<? extends XHTMLPart> xHTMLPartClass) {
		JSExecutorHelper.initThread();
		
		XHTMLFile fileXML = createXHTMLFile();
		fileXML.setXHTMLTemplate(new XHTMLTemplateRoot());
		
		List<Locale> languages = requestConfig.headers.getAcceptableLanguages();
		Locale loc = languages.get(0);

		CoreLogger.getLogger(1).info(()->"****** GENERATE PHASE 1 : "+xHTMLPartClass.getSimpleName()+" ********");
		// premier passe (execute les annotation)
		initXMLFile(xHTMLPartClass, fileXML, requestConfig.param);

		if (ErrorNotificafionMgr.hasErrorMessage()) {
			// affiche la page d'erreur
			return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
					.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
		}

		// generation page
		CoreLogger.getLogger(1).info(() -> "******"+ " GENERATE PHASE 2 : "	+ xHTMLPartClass.getSimpleName() + " ********");
		
		// deuxieme passe (execute les toXML)
		String html = toXML(fileXML, loc, null);
		cache.result = html;
		
		doSubFile(requestConfig, fileXML);
		
		cache.storeResultInDb();
		cache.getVersionDB(0);   // calcul la difference
		
		JSExecutorHelper.stopThread();
		return null;
	}

	/**
	 * @param headers
	 * @param uri
	 * @param headerConfig
	 * @return
	 */
	private void initResquestConfig(RequestConfig requestConfig) {
		List<String> cacheControl = requestConfig.headers.getRequestHeader(CACHE_CONTROL);
		requestConfig.noCache = cacheControl!=null && cacheControl.get(0).equals("no-cache");
		
		CoreLogger.getLogger(1).info(() -> "cacheControl="+cacheControl);
		
		MultivaluedMap<String, String> param = requestConfig.uri.getQueryParameters();
				
		List<String> p = param.get("compatibility");
		requestConfig.es5 = p!=null && p.get(0).equals("es5");
		if (ConfigFormat.getData().isEs5())
			requestConfig.es5=true;
			
		p = param.get("version");
		requestConfig.version = p==null?ConfigFormat.getData().getVersionTimeline():Integer.parseInt(p.get(0));
		
		if (ConfigFormat.getData().isReload())
		{
			if (requestConfig.version==0)
				requestConfig.noCache=true;
			ConfigFormat.getData().setReload(false);
		}
		
		requestConfig.param = param;
	}

	
	private class RequestConfig
	{
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
		
		boolean es5;
		int version;
		boolean noCache;
		
		MultivaluedMap<String, String> param;
	}
	
	/**
	 * @param es5
	 * @param fileXML
	 */
	private void doSubFile(RequestConfig requestConfig, XHTMLFile fileXML) {
		HashMap<String, XMLFile> subFile = fileXML.listSubFile;
		for (Map.Entry<String,XMLFile> entry : subFile.entrySet()) {
			    String name = entry.getKey();
			    XHTMLFile xmlFile = (XHTMLFile)entry.getValue();
				ThreadLocalXUIFactoryPage.set(xmlFile);
			    String contentFile = toXML(xmlFile, null, new XMLBuilder(name, new StringBuilder(1000), null).setModeResource(true));
			    
			    String ext = name.substring(name.indexOf(POINT)+1);
			    
			    if (requestConfig.es5 && ext.equals("js"))
					try {
						contentFile = JSExecutorHelper.doBabel(contentFile);
					} catch (NoSuchMethodException | ScriptException e) {
						CoreLogger.getLogger(2).log(Level.SEVERE, "PB BABEL",  e );
					}
			    
			    name=name.substring(0, name.lastIndexOf('.'));  // nom sans extension
			    
			    
				CacheManager cache = new CacheManager(xmlFile.getID(), ""+requestConfig.es5, xmlFile.getExtension());
				cache.initVersion(requestConfig.noCache, requestConfig.version);
			    
				cache.result = contentFile;
				cache.storeResultInDb();
				cache.getVersionDB(0);   // calcul la difference
				
				/*********************************************************************/
				StringBuilder dif = new StringBuilder();
				if (cache.fileComparator.listLineDiff.length()>0)
				{
					dif.append("\n\n");
					dif.append(cache.fileComparator.listLineDiff);
					dif.append("\n");
				}
				/*********************************************************************/
				CacheManager.resourceDB.put(name, contentFile+dif);
		}

		ThreadLocalXUIFactoryPage.set(fileXML);
	}


	public static String calculateEtag(final String s) {
	    final java.nio.ByteBuffer buf = java.nio.charset.StandardCharsets.UTF_8.encode(s);
	    java.security.MessageDigest digest;
		try {
			digest = java.security.MessageDigest.getInstance("SHA1");
		    buf.mark();
		    digest.update(buf);
		    buf.reset();
		    return String.format("W/\"%s\"", javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest()));
		} catch (NoSuchAlgorithmException e) {
			CoreLogger.getLogger(2).log(Level.SEVERE, "PB calculateEtag",  e );
		}
		return "?";
	}
	
	/** script src= ... integrity=xxx */
	public static String calculateIntegrity(final String s) {
		// On lit le contenu du fichier :
		byte[] fileContentBytes = s.getBytes();
		// On crée le MessageDigest avec l’algorithme désiré :
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			CoreLogger.getLogger(2).log(Level.SEVERE, "PB calculateIntegrity",  e );
		}
		// On calcule le hash :
		byte[] hash = digest.digest(fileContentBytes ); 
		// Que l'on encode en base64 (java 8 - sinon il faut une solution tierce)
		String encoded = Base64.getEncoder().encodeToString(hash);
		return encoded;
	}
	
	/**
	 * @param fileXML
	 * @param loc
	 * @return
	 */
	private static final String toXML(XHTMLFile fileXML, Locale loc, XMLBuilder xmlBuf) {
		
		if (xmlBuf==null)
		{
			StringBuilder buf = new StringBuilder(1000);
			buf.append("<!doctype html>");
			((XHTMLTemplateRoot) fileXML.getXHTMLTemplate()).setLang(loc.toLanguageTag());
			xmlBuf = new XMLBuilder("page", buf, null);
		}
		
		fileXML.getXHTMLTemplate().doContent();
		List<XMLElement> rootContent = fileXML.getXHTMLTemplate().getListElementFromTarget(CONTENT.class);

		for (XMLElement elem : rootContent) {
			elem.toXML(xmlBuf);
		}

		return  xmlBuf.getContent().toString();
	}

	/**
	 * 
	 */
	private synchronized void  initChangeManager() {
		if (changeMgr.mapClass.isEmpty())
		{
			XHTMLAppScanner.getMapXHTMLPart(changeMgr);
		}
	}

	private void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file, MultivaluedMap<String, String> requestParam) {
		try {
						
			((XHTMLFile)file).setParam(requestParam);
			
			XHTMLPart page = pageClass.newInstance();
			file.setMainXMLPart( page);
			
			xCoreVersion coreVersion = pageClass.getAnnotation(xCoreVersion.class);
			((XHTMLFile)file).setCoreVersion(coreVersion==null?"1":coreVersion.value());
			
			page.doContent();
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

	/**
	 * creer un XHTMLFile vide
	 * @return
	 */
	private XHTMLFile createXHTMLFile() {
		XHTMLFile file = new XHTMLFile();
		ProxyHandler.ThreadLocalMethodDesc.set(null); 
		ThreadLocalXUIFactoryPage.set(file);
		return file;
	}
/*************************************************************************************************/
	
	@GET
	@Path("/js/{name}.js")
	@Produces("application/javascript")
	public Response getJS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		
		String reponseInCache=null;
		if (enableCache)
			reponseInCache = CacheManager.resourceDB.get(name);
		
		
		if (reponseInCache==null) {
			List<XMLElement> rootContent = null;
			StringBuilder buf = new StringBuilder(1000);
			
			if (name.equals("sw"))
			{
				XHTMLFile fileXML = createXHTMLFile();
				JSServiceWorker part = new JSServiceWorker();
				fileXML.setXHTMLTemplate(part);
				
				part.doContent();
				rootContent = part.getListElementFromTarget(CONTENT.class);
			}
			else
			{
				XMLElement elem = XHTMLPart.xListNodeStatic(XHTMLPart.js().consoleDebug("'PB GET JS "+ name +"'"));
				rootContent= new ArrayList<>();		
				rootContent.add(elem);
			}
			
			for (XMLElement elem : rootContent) {
				elem.toXML(new XMLBuilder("page", buf, null));
			}
			
			reponseInCache = buf.toString();
			 CacheManager.resourceDB.put(name, reponseInCache);
		}
		
		List<String> etag = headers.getRequestHeader(IF_NONE_MATCH);
		String eTagRequest = etag==null?"":etag.get(0);
		String eTagResponse = calculateEtag(reponseInCache);
		
		if (eTagRequest.equals(eTagResponse))
		{
			return Response.status(Status.NOT_MODIFIED)				
					.header("etag", eTagResponse)
					.build();
		}
		
		return Response.status(Status.OK)
				.entity(reponseInCache)
				.header(CACHE_CONTROL, "public, max-age=30672000")
				.header("last-modified", getDateHTTP())
				.header("expire", getNextYearDateHTTP())
				.header("etag", eTagResponse)
				.build();
	}
	
	
	@GET
	@Path("/css/{name}.css")
	@Produces("text/css")
	public Response getCSS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		
		String reponseInCache=null;
		if (enableCache)
			reponseInCache = CacheManager.resourceDB.get(name);
		
		if (reponseInCache==null) {
			StringBuilder buf = new StringBuilder(1000);
			
			reponseInCache = buf.toString();
			CacheManager.resourceDB.put(name, reponseInCache);
		}
		
		List<String> etag = headers.getRequestHeader(IF_NONE_MATCH);
		String eTagRequest = etag==null?"":etag.get(0);
		String eTagResponse = calculateEtag(reponseInCache);
		
		if (eTagRequest.equals(eTagResponse))
		{
			return Response.status(Status.NOT_MODIFIED)
					.header("etag", eTagResponse)
					.build();
		}
		
		return Response.status(Status.OK)
				.entity(reponseInCache)
				.header(CACHE_CONTROL, "public, max-age=30672000")
				.header("expire", getNextYearDateHTTP())
				.header("last-modified", getDateHTTP())
				.header("etag", eTagResponse)
				.build();
	}

	private String getNextYearDateHTTP() {
		ZonedDateTime date = ZonedDateTime.now();
		date = date.plus(1, ChronoUnit.YEARS);
		return DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(date);
	}
	
	private String getDateHTTP() {
		ZonedDateTime date = ZonedDateTime.now();
		return DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(date);
	}

	/************************************************************************************************/
	@GET
	@Path("/page/challenge/{token}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getChallenge(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("token") String token) {
		CoreLogger.getLogger(1).info(()->"token = "+token);
		return Response.status(Status.OK)
				.entity("S82b-qkkEFYbLjsTOOoDny1_6ZOJeRTZCrZHrRZBf9Y._kDi-Kd_nFPpc1VeAbWRbNnNofTvn-iQ8CsSM0GceYE").build();
	}
	
}
