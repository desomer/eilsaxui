package com.elisaxui.core.xui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.elisaxui.component.page.JSServiceWorker;
import com.elisaxui.core.helper.JSExecutorHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.app.CacheManager;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

@Path("/")
public class XUIFactoryXHtml extends XUIFactory {

	private static final String POINT = ".";
	private static final String IF_NONE_MATCH = "if-none-match";
	boolean enableCache = true;
	
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
		
		JSExecutorHelper.setThreadPreprocessor(requestConfig.isEs5());
				
		// cherche les changements
		initChangeManager();
		
		// cherche dans les caches
		CacheManager cache = new CacheManager(id, ""+requestConfig.isEs5(), "html", true);
		cache.initVersion(requestConfig.noCache, requestConfig.version);
				
		if (cache.getResult() == null) {   // si pas en cache
			// recupere la classe de page
			Class<? extends XHTMLPart> xHTMLPartClass = changeMgr.mapClass.get(id);
			if (xHTMLPartClass != null) {
				Response err = createVersion(requestConfig, cache, xHTMLPartClass);		
				if (err!=null)	return err;
			}
		} else {
			CoreLogger.getLogger(1).info(() -> "****** get cache " + cache.getIdCacheDB());
		}
		
		if (cache.getResult()==null)
			return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
				
		StringBuilder dif = new StringBuilder();
		if (cache.getFileComparator().listLineDiff.length()>0)
		{
			dif.append("\n\n<script id='srcdiff' type='application/json'>");
			dif.append(cache.getFileComparator().listLineDiff);
			dif.append("\n</script>");
		}
				
		String response = cache.getResult()+dif;
		
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
	 * @param es5
	 * @param fileXML
	 */
	@Override
	protected void doSubFile(RequestConfig requestConfig, XHTMLFile fileXML) {
		HashMap<String, XMLFile> subFile = fileXML.listSubFile;
		for (Map.Entry<String,XMLFile> entry : subFile.entrySet()) {
			    String name = entry.getKey();
			    XHTMLFile xmlFile = (XHTMLFile)entry.getValue();
				ThreadLocalXUIFactoryPage.set(xmlFile);
				
				xmlFile.setMainXMLPart( fileXML.getMainXMLPart());
				
				xmlFile.getListClassModule().putAll(fileXML.getListClassModule());
			    String contentFile = toXML(xmlFile, null, new XMLBuilder(name, new StringBuilder(1000), null).setModeResource(true));
			    
			    String ext = name.substring(name.indexOf(POINT)+1);
			    
			    if (requestConfig.isEs5() && (ext.equals("js") || ext.equals("mjs")) )
					try {
						contentFile = JSExecutorHelper.doBabel(contentFile);
					} catch (NoSuchMethodException | ScriptException e) {
						CoreLogger.getLogger(2).log(Level.SEVERE, "PB BABEL",  e );
					}
			    
			    name=name.substring(0, name.lastIndexOf('.'));  // nom sans extension
			    
			    
				CacheManager cache = new CacheManager(xmlFile.getID(), ""+requestConfig.isEs5(), xmlFile.getExtension(), true);
				cache.initVersion(requestConfig.noCache, requestConfig.version);
			    
				cache.setResult(contentFile);
				cache.storeResultInDb();
				cache.getVersionDB(0);   // calcul la difference
				
				/*********************************************************************/
				StringBuilder dif = new StringBuilder();
				if (cache.getFileComparator().listLineDiff.length()>0)
				{
					dif.append("\n\n");
					dif.append(cache.getFileComparator().listLineDiff);
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
	
	/*************************************************************************************************/
	
	@GET
	@Path("/js/{name}.js")
	@Produces("application/javascript")
	public Response getJS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		return doJS(headers, name);
	}
	
	@GET
	@Path("/mjs/{name}.mjs")
	@Produces("application/javascript")
	public Response getMJS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		return doJS(headers, name);
	}

	/**
	 * @param headers
	 * @param name
	 * @return
	 */
	private Response doJS(HttpHeaders headers, String name) {
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
//				XMLElement elem = XHTMLPart.xListNodeStatic(XHTMLPart.js().consoleDebug("'PB GET JS "+ name +"'"));
//				rootContent= new ArrayList<>();		
//				rootContent.add(elem);
				System.err.println("PB GET JS "+ name);
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
				.entity("pGKwbQh7uOItAnjq11QNo_-M0itb94zIObB2bpkndgQ.KKB5n0NNvVroXQ1v-TMye1CnAFNSJY4NQsY_o5qSFJg").build();
	}
	
}
