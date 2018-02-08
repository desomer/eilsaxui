package com.elisaxui.core.xui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import org.mapdb.Atomic.Var;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import com.elisaxui.component.page.JSServiceWorker;
import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.XHTMLAppBuilder;
import com.elisaxui.core.xui.xhtml.XHTMLChangeManager;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

import difflib.Delta;
import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;

@Path("/")
public class XUIFactoryXHtml {

	private static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<XHTMLFile>();

	public static XHTMLChangeManager changeMgr = new XHTMLChangeManager();
	
	private static Map<String, ArrayList<String>> listeVersionId = null; 
	
	private static HashMap<String, String> pageCacheMem = new HashMap<>() ;

	private static DB db = null;
	private static HTreeMap<String, String> pageCache = null;
	private static  Var<Object> listeDico= null;
	private static  Var<Object> lastDateFile= null;

	public static final long getLastDate() {
		Long r = (Long) lastDateFile.get();
		if (r==null)
		{
			lastDateFile.set(System.currentTimeMillis());
			r = (Long) lastDateFile.get();
		}
		return r;
	}

	/**
	 * @param lastDate the lastDate to set
	 */
	public static final void setLastDate(long lastDate) {
		lastDateFile.set(lastDate);
		db.commit();
	}

	static {	
			String home = System.getProperty("user.home");
			String pathdb = home+ File.separator+"fileMapdb.db";
			
			CoreLogger.getLogger(1).info(()->"start db at "+pathdb);
			
			db = DBMaker.fileDB(pathdb).checksumHeaderBypass().closeOnJvmShutdown().make();	
			pageCache = db.hashMap("mapFileHtml",Serializer.STRING,Serializer.STRING).createOrOpen();
			listeDico = db.atomicVar("pageDico").createOrOpen();
			lastDateFile = db.atomicVar("lastDate").createOrOpen();
			
			listeVersionId = (Map<String, ArrayList<String>>) listeDico.get();
			if (listeVersionId==null)
				listeVersionId = new HashMap<>();
	}
	
	public static final XMLPart getXMLRoot() {
		return ThreadLocalXUIFactoryPage.get().getRoot();
	}

	public static final XHTMLFile getXHTMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	
	boolean enableCache = true;
	boolean enableDynamic = true;
	
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
		
		p = param.get("version");
		int version = p==null?0:Integer.parseInt(p.get(0));
		
		JSExecutorHelper.setThreadPreprocessor(es5);
				
		// cherche les changements
		initChangeManager();
		
		/********************************************************/
		String htmlInCache=null;
		String idCache = null;
		String idCacheVersionning = id+es5;
		
	
		if (enableCache)
		{
			idCache = id+changeMgr.lastOlderFile+es5;
			if (!noCache)
			{
				if (version<0)
				{
					String htmlInCacheOriginal = pageCache.get(idCache);
					ArrayList<String> version1 = listeVersionId.get(idCacheVersionning);
					if (version1!=null &&  -version<version1.size())
						idCache = version1.get(version1.size()+version-1);
					
					htmlInCache = pageCache.get(idCache);
					
					if (htmlInCacheOriginal!=null ) {
						changeMgr.initLineDiff( htmlInCache, htmlInCacheOriginal);
					}
				}
				else
				{
					htmlInCache = pageCacheMem.computeIfAbsent(idCache, k-> pageCache.get(k));
				}
			}
			
		}
		
		if (htmlInCache == null) {   // si pas en cache
			// recupere la classe de page
			Class<? extends XHTMLPart> pageClass = changeMgr.mapClass.get(id);
			if (pageClass != null) {
				
				JSExecutorHelper.initThread();
				
				XMLFile fileXML = createXHTMLFile();
				fileXML.setRoot(new XHTMLRoot());
				List<Locale> languages = headers.getAcceptableLanguages();
				Locale loc = languages.get(0);

				// premier passe
				initXMLFile(pageClass, fileXML, param);

				if (ErrorNotificafionMgr.hasErrorMessage()) {
					// affiche la page d'erreur
					return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
							.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
				}

				// generation page
				CoreLogger.getLogger(1).info(() -> "*******"
						+ " GENERATE XML File of "
						+ pageClass + " ********");
				
				String html = createXMLText(fileXML, loc);
				addInCache(idCache, idCacheVersionning, html);

				JSExecutorHelper.stopThread();					
				htmlInCache = html;
			}
		} else {
			CoreLogger.getLogger(1).info(() -> "get from cache " + idCacheVersionning);
		}
		
		if (htmlInCache==null)
			return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
		
		if ( version==0)
		{
			version = -1;
			ArrayList<String> version1 = listeVersionId.get(idCacheVersionning);
			if (version1!=null &&  -version<version1.size())
				idCache = version1.get(version1.size()+version-1);
			
			String htmlInCacheOld = pageCache.get(idCache);
			if (htmlInCacheOld!=null ) {
				changeMgr.initLineDiff(htmlInCacheOld ,  htmlInCache);
			}
		}
		
		
		StringBuilder dif = new StringBuilder("\n\n<script id='srcdiff' type='application/json'>");  //\r\n
		dif.append(changeMgr.listLineDiff);
		dif.append("\n</script>");
		
//		return Response.status(Status.OK) // .type(MediaType.TEXT_HTML)
//		.entity(html+dif)
//		.header("XUI", "ok")
//		//.header("Access-Control-Allow-Origin", "*")
//		.build();
		
		return Response.status(Status.OK)
				.entity(htmlInCache+dif)
				.header("XUI", "ok")
				.build();
	}


	/**
	 * @param idCache
	 * @param idCacheVersionning
	 * @param html
	 */
	private void addInCache(String idCache, String idCacheVersionning, String html) {
		if (idCache!=null)
		{
			CoreLogger.getLogger(1).info(()->" add cache "+idCache);
			
			ArrayList<String> version1 = listeVersionId.get(idCacheVersionning);
			if (version1==null)
			{
				version1=new ArrayList<>();
				listeVersionId.put(idCacheVersionning, version1);
			}
			version1.add(idCache);
			
			pageCache.put(idCache, html);
			listeDico.set(listeVersionId);
			db.commit();
			
			pageCacheMem.put(idCache, html);
		}
	}

	/**
	 * @param fileXML
	 * @param loc
	 * @return
	 */
	private String createXMLText(XMLFile fileXML, Locale loc) {
		StringBuilder buf = new StringBuilder(1000);
		buf.append("<!doctype html>");
		((XHTMLRoot) fileXML.getRoot()).setLang(loc.toLanguageTag());
		fileXML.getRoot().doContent(null);
		ArrayList<XMLElement> rootContent = fileXML.getRoot().getListElement(CONTENT.class);

		for (XMLElement elem : rootContent) {
			elem.toXML(new XMLBuilder("page", buf, null));
		}

		return  buf.toString();
	}

	/**
	 * 
	 */
	private synchronized void  initChangeManager() {
		if (changeMgr.mapClass==null || enableDynamic)
		{
			XHTMLAppBuilder.getMapXHTMLPart(changeMgr);  // synchronized
		}
	}

	private void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file, MultivaluedMap<String, String> p) {
		try {

			CoreLogger.getLogger(1).info(()->"******** INIT XML File of "+pageClass+" ********");
						
			((XHTMLFile)file).setParam(p);
			
			XHTMLPart page = pageClass.newInstance();
			if (page instanceof XUIScene)
				((XHTMLFile)file).setScene((XUIScene) page);
			
			page.doContent(file.getRoot());
			for (XMLElement elem : page.getListElement(CONTENT.class)) {
				page.vBody(elem);
			}
			for (XMLElement elem : page.getListElement(AFTER_CONTENT.class)) {
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
		ThreadLocalXUIFactoryPage.set(file);
		return file;
	}

	
	@GET
	@Path("/js/{name}.js")
	@Produces("application/javascript")
	public Response getJS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		
		String reponseInCache=null;
		if (enableCache)
			reponseInCache = pageCache.get(name);
		
		if (reponseInCache==null) {
			XMLFile fileXML = createXHTMLFile();
			
			XHTMLPart part = new JSServiceWorker();
			
			fileXML.setRoot(part);
			
			StringBuilder buf = new StringBuilder(1000);
			part.doContent(null);
			ArrayList<XMLElement> rootContent = part.getListElement(CONTENT.class);
	
			for (XMLElement elem : rootContent) {
				elem.toXML(new XMLBuilder("page", buf, null));
			}
			
			reponseInCache = buf.toString();
			pageCache.put(name, reponseInCache);
		}
		
		return Response.status(Status.OK)
				.entity(reponseInCache).header("content-type","application/javascript").header("XUI", "ok").build();
	}
	
	@GET
	@Path("/page/challenge/{token}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getChallenge(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("token") String token) {
		System.out.println("token ="+token);
		return Response.status(Status.OK)
				.entity("ow8QTs-0I4ywbbrmp-IHJcXPFpqRKXurs5T2gZAjmqE.r7EgwbRaRypDDfgAupsAWglRb_MsRoBAAvUQ8ao1p6w").build();
	}
	
}
