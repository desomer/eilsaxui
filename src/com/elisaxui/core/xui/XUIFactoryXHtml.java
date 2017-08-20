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
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLElement;

@Path("/page")
public class XUIFactoryXHtml {

	private static final ThreadLocal<XHTMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<XHTMLFile>();

	private static final HashMap<String, String> pageCache = new HashMap<String, String>();

	public static final XMLPart getXMLRoot() {
		return ThreadLocalXUIFactoryPage.get().getRoot();
	}

	public static final XHTMLFile getXHTMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	
	boolean enableCache = true;
	
	@SuppressWarnings("deprecation")
	@GET
	@Path("/{pays}/{lang}/id/{id}")
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
			//	pageCache.put(id, html);

				// System.out.println("------------------------------------------");

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

}
