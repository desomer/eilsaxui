package com.elisaxui.core.xui;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot;
import com.elisaxui.core.xui.xhtml.application.XHTMLAppScanner;
import com.elisaxui.core.xui.xhtml.application.XHTMLChangeManager;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

@Path("/")
public class XUIFactoryXHtml {

	public static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<>();
	public static final XHTMLChangeManager changeMgr = new XHTMLChangeManager();
	
	
	public static final XMLPart getXMLRoot() {
		return ThreadLocalXUIFactoryPage.get().getRoot();
	}

	public static final XHTMLFile getXHTMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	
	boolean enableCache = true;
	boolean enableDynamic = false;  // si true recherche les nouvelle ressource des nouveau fichier ajouter ( page, js)
	
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

		
		List<String> cacheControl = headers.getRequestHeader("cache-control");
		boolean noCache = cacheControl!=null && cacheControl.get(0).equals("no-cache");
		
		CoreLogger.getLogger(1).info(() -> "cacheControl="+cacheControl);
		
		MultivaluedMap<String, String> param = uri.getQueryParameters();
		
		List<String> p = param.get("compatibility");
		boolean es5 = p!=null && p.get(0).equals("es5");
		
		if (ConfigFormat.getData().isEs5())
			es5=true;
			
		p = param.get("version");
		int version = p==null?ConfigFormat.getData().getVersionTimeline():Integer.parseInt(p.get(0));
		if (ConfigFormat.getData().isReload())
		{
			if (version==0)
				noCache=true;
			ConfigFormat.getData().setReload(false);
		}
		
		
		JSExecutorHelper.setThreadPreprocessor(es5);
				
		// cherche les changements
		initChangeManager();
		
		CacheManager cache = new CacheManager(id, ""+es5);
		cache.getVersion(noCache, version);
				
		if (cache.result == null) {   // si pas en cache
			// recupere la classe de page
			Class<? extends XHTMLPart> xHTMLPartClass = changeMgr.mapClass.get(id);
			if (xHTMLPartClass != null) {
				
				JSExecutorHelper.initThread();
				
				ProxyHandler.ThreadLocalMethodDesc.set(null);
				XHTMLFile fileXML = createXHTMLFile();
				fileXML.setRoot(new XHTMLRoot());
				List<Locale> languages = headers.getAcceptableLanguages();
				Locale loc = languages.get(0);

				// premier passe (execute les annotation)
				initXMLFile(xHTMLPartClass, fileXML, param);

				if (ErrorNotificafionMgr.hasErrorMessage()) {
					// affiche la page d'erreur
					return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
							.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
				}

				// generation page
				CoreLogger.getLogger(1).info(() -> "*******"+ " GENERATE XML File of "	+ xHTMLPartClass + " ********");
				
				// deuxieme passe (execute les toXML)
				String html = toXML(fileXML, loc, null);
				cache.result = html;
				
				HashMap<String, XMLFile> subFile = fileXML.listSubFile;
				for (Map.Entry<String,XMLFile> entry : subFile.entrySet()) {
					    String name = entry.getKey();
					    XMLFile xmlFile = entry.getValue();
						ThreadLocalXUIFactoryPage.set((XHTMLFile)xmlFile);
					    String contentFile = toXML(xmlFile, null, new XMLBuilder(name, new StringBuilder(1000), null).setModeResource(true));
					    
					    String ext = name.substring(name.indexOf(".")+1);
					    
					    if (es5 && ext.equals("js"))
							try {
								contentFile = JSExecutorHelper.doBabel(contentFile);
							} catch (NoSuchMethodException | ScriptException e) {
								e.printStackTrace();
							}
					    
					    name=name.substring(0, name.lastIndexOf('.'));
						CacheManager.resourceDB.put(name, contentFile);
				}

				ThreadLocalXUIFactoryPage.set(fileXML);
				
				cache.storeResultInDb();
				cache.getVersionDB(0);
				
				JSExecutorHelper.stopThread();					
			}
		} else {
			CoreLogger.getLogger(1).info(() -> "get from cache " + cache.idCacheDB);
		}
		
		if (cache.result==null)
			return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
				
		StringBuilder dif = new StringBuilder();
		if (changeMgr.listLineDiff.length()>0)
		{
			dif.append("\n\n<script id='srcdiff' type='application/json'>");
			dif.append(changeMgr.listLineDiff);
			dif.append("\n</script>");
		}
				
		String response = cache.result+dif;
		
		List<String> etag = headers.getRequestHeader("if-none-match");
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
			e.printStackTrace();
		}
		return "?";
	}
	

	/**
	 * @param fileXML
	 * @param loc
	 * @return
	 */
	private static final String toXML(XMLFile fileXML, Locale loc, XMLBuilder xmlBuf) {
		
		if (xmlBuf==null)
		{
			StringBuilder buf = new StringBuilder(1000);
			buf.append("<!doctype html>");
			((XHTMLRoot) fileXML.getRoot()).setLang(loc.toLanguageTag());
			xmlBuf = new XMLBuilder("page", buf, null);
		}
		
		fileXML.getRoot().doContent();
		List<XMLElement> rootContent = fileXML.getRoot().getListElementFromTarget(CONTENT.class);

		for (XMLElement elem : rootContent) {
			elem.toXML(xmlBuf);
		}

		return  xmlBuf.getContent().toString();
	}

	/**
	 * 
	 */
	private synchronized void  initChangeManager() {
		if (changeMgr.mapClass.isEmpty() || enableDynamic)
		{
			XHTMLAppScanner.getMapXHTMLPart(changeMgr);
		}
	}

	private void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file, MultivaluedMap<String, String> p) {
		try {

			CoreLogger.getLogger(1).info(()->"******** INIT XML File of "+pageClass+" ********");
						
			((XHTMLFile)file).setParam(p);
			
			XHTMLPart page = pageClass.newInstance();
			
			((XHTMLFile)file).setScene( page);
			
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
		System.out.println("create XHTML File");
		XHTMLFile file = new XHTMLFile();
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
				XMLFile fileXML = createXHTMLFile();
				XHTMLPart part = new JSServiceWorker();
				fileXML.setRoot(part);
				
				part.doContent();
				rootContent = part.getListElementFromTarget(CONTENT.class);
			}
			else
			{
				XMLElement elem = XHTMLPart.xListNodeStatic(XHTMLPart.js().consoleDebug("'OK "+ name +"'"));
				rootContent= new ArrayList<XMLElement>();		
				rootContent.add(elem);
			}
			

			for (XMLElement elem : rootContent) {
				elem.toXML(new XMLBuilder("page", buf, null));
			}
			
			reponseInCache = buf.toString();
			 CacheManager.resourceDB.put(name, reponseInCache);
		}
		
		List<String> etag = headers.getRequestHeader("if-none-match");
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
				.header("cache-control", "public, max-age=30672000")
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
			List<XMLElement> rootContent = null;
			StringBuilder buf = new StringBuilder(1000);
			
			reponseInCache = buf.toString();
			CacheManager.resourceDB.put(name, reponseInCache);
		}
		
		List<String> etag = headers.getRequestHeader("if-none-match");
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
				.header("cache-control", "public, max-age=30672000")
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
		System.out.println("token ="+token);
		return Response.status(Status.OK)
				.entity("S82b-qkkEFYbLjsTOOoDny1_6ZOJeRTZCrZHrRZBf9Y._kDi-Kd_nFPpc1VeAbWRbNnNofTvn-iQ8CsSM0GceYE").build();
	}
	
}
