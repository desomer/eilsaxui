package com.elisaxui.core.xui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.XHTMLAppBuilder;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.value.JSon;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

@Path("/")
public class XUIFactoryXHtml {

	/**
	 * @author Bureau
	 *
	 */
	public final class XHTMLJSFile extends XHTMLPart {
		@xTarget(CONTENT.class)
		public XMLElement doJS()
		{
			
			JSon response = new JSon().setName("response");
			
			JSMethodInterface fctFetch = fct(response)
					 // Check if we received a valid response
		            ._if("!response || response.status !== 200 || response.type !== 'basic'") 
		            	.consoleDebug(txt("ret error from net"), response)
		               ._return(response)  // mauvaise reponse
		            .endif()
		            
		            // mise en cache
		            .var("responseToCache" , "response.clone()")
		            .__("caches.open(CACHE_NAME).then(", fct("cache")
		            		.consoleDebug(txt("add cache"), "cache")
		            		.__("cache.put(event.request, responseToCache)"),")")
		            
		            .consoleDebug(txt("ret 200 from net "), response.get("url"))
		            ._return("response");
					;
			
			JSMethodInterface fctCache = fct(response)
					._if(response)
						.consoleDebug(txt("hit cache "), response.get("url"))
				        ._return(response)  // Cache hit - return response
				    .endif()
				    
				    .var("fetchRequest","event.request.clone()")
				    
				    ._return("fetch(fetchRequest).then(", fctFetch ,")")
				    
					;
			  
			JSMethodInterface fctPostMessage = fct("clients")
					._if("clients && clients.length")
						.__("clients[0].postMessage(event.request.url)")
					.endif();
			
			return xListElement(js()
					.var("CACHE_NAME", txt("site-cache-v1"))
					
					
					.__("self.addEventListener('fetch',", fct("event") 
							.consoleDebug(txt("event"), "event")
							//.__("fetch(event.request)")
							
							.__("self.clients.matchAll(/* search options */).then(", fctPostMessage,"  )")
//							    if (clients && clients.length) {
//							        // you need to decide which clients you want to send the message to..
//							        const client = clients[0];
//							        client.postMessage("your message");
//							    }
							
							.__("event.respondWith(caches.match(event.request).then(", fctCache  ,") )")
							
					,")")
					);				
		}
	}


	private static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<XHTMLFile>();

	private static final HashMap<String, String> pageCache = new HashMap<String, String>();

	public static final XMLPart getXMLRoot() {
		return ThreadLocalXUIFactoryPage.get().getRoot();
	}

	public static final XHTMLFile getXHTMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	
	boolean enableCache = false;
	
	@SuppressWarnings("deprecation")
	@GET
	@Path("/page/{pays}/{lang}/id/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("pays") String pays,
			@PathParam("lang") String lang, @PathParam("id") String id) {

		String htmlInCache=null;
		if (enableCache)
			htmlInCache = pageCache.get(id);
			
		if (htmlInCache == null) {
			Map<String, Class<? extends XHTMLPart>> mapClass = XHTMLAppBuilder.getMapXHTMLPart();

			Class<? extends XHTMLPart> pageClass = mapClass.get(id);
			if (pageClass != null) {
				XMLFile fileXML = createXHTMLFile();

				fileXML.setRoot(new XHTMLRoot());
				List<Locale> languages = headers.getAcceptableLanguages();
				Locale loc = languages.get(0);

				// premier passe
				initXMLFile(pageClass, fileXML);

				if (ErrorNotificafionMgr.hasErrorMessage()) {
					return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
							.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
				}

				// generation page
				System.out.println("[XUIFactoryXHtml]");
				System.out
						.println("[XUIFactoryXHtml]********************************************* GENERATE XML File of "
								+ pageClass + "  ****************************************");

				StringBuilder buf = new StringBuilder(1000);
				buf.append("<!doctype html>");
				((XHTMLRoot) fileXML.getRoot()).setLang(loc.toLanguageTag());
				fileXML.getRoot().doContent(null);
				ArrayList<XMLElement> rootContent = fileXML.getRoot().getListElement(CONTENT.class);

				for (XMLElement elem : rootContent) {
					elem.toXML(new XMLBuilder("page", buf, null));
				}

				String html = buf.toString();
				pageCache.put(id, html);


				return Response.status(Status.OK) // .type(MediaType.TEXT_HTML)
						.entity(html).header("XUI", "ok").build();
			} else {
				return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
			}
		} else {
			return Response.status(Status.OK) // .type(MediaType.TEXT_HTML)
					.entity(htmlInCache).header("XUI", "ok").build();
		}

	}

	private void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file) {
		try {
			System.out.println("[XUIFactoryXHtml]");
			System.out.println("[XUIFactoryXHtml]********************************************* INIT XML File of "
					+ pageClass + "  ****************************************");

			XHTMLPart page = pageClass.newInstance();
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

	private XHTMLFile createXHTMLFile() {
		XHTMLFile file = new XHTMLFile();
		ThreadLocalXUIFactoryPage.set(file);
		return file;
	}

	
	@GET
	@Path("/page/{name}.js")
	@Produces("application/javascript")
	public Response getJS(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("name") String name) {
		
		String ReponseInCache=null;
		if (enableCache)
			ReponseInCache = pageCache.get(name);
		
		if (ReponseInCache==null) {
			XMLFile fileXML = createXHTMLFile();
			
			XHTMLPart part = new XHTMLJSFile();
			
			fileXML.setRoot(part);
			
			StringBuilder buf = new StringBuilder(1000);
			part.doContent(null);
			ArrayList<XMLElement> rootContent = part.getListElement(CONTENT.class);
	
			for (XMLElement elem : rootContent) {
				elem.toXML(new XMLBuilder("page", buf, null));
			}
			
			ReponseInCache = buf.toString();
			pageCache.put(name, ReponseInCache);
		}
		
		return Response.status(Status.OK)
				.entity(ReponseInCache).header("content-type","application/javascript").header("XUI", "ok").build();
	}
	
	@GET
	@Path("/page/challenge/{token}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getChallenge(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("token") String token) {
		System.out.println("token ="+token);
		return Response.status(Status.OK)
				.entity("myNV6mWL8RiK2hwEAlcjy43GsJOady4q4kQy-BKsPCU.r7EgwbRaRypDDfgAupsAWglRb_MsRoBAAvUQ8ao1p6w").build();
	}
	
}
