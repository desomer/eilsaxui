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

import com.elisaxui.core.xui.xhtml.XHTMLRoot;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLElement;

import javax.ws.rs.core.UriInfo;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

@Path("/page")
public class XUIFactoryXHtml {

	private static final ThreadLocal<XMLFile> ThreadLocalXUIFactoryPage = new ThreadLocal<XMLFile>();

	public static final XMLPart getXMLRoot() {
		return ThreadLocalXUIFactoryPage.get().getRoot();
	}

	public static final XMLFile getXMLFile() {
		return ThreadLocalXUIFactoryPage.get();
	}

	@GET
	@Path("/{pays}/{lang}/id/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("pays") String pays,
			@PathParam("lang") String lang, @PathParam("id") String id) {

		Map<String, Class<? extends XHTMLPart>> mapClass = getMapXHTMLPart();

		Class<? extends XHTMLPart> pageClass = mapClass.get(id);
		if (pageClass != null) {
			XMLFile file = createXMLFile();

			initVariableJS();
			
			file.setRoot(new XHTMLRoot());
			List<Locale> languages = headers.getAcceptableLanguages();
			Locale loc = languages.get(0);

			// premier passe
			initXMLFile(pageClass, file);
			if (ErrorNotificafionMgr.hasErrorMessage()) {
				return Response.status(Status.INTERNAL_SERVER_ERROR) // .type(MediaType.TEXT_HTML)
						.entity(ErrorNotificafionMgr.getBufferErrorMessage().toString()).build();
			}

			// generation page
			StringBuilder buf = new StringBuilder(1000);
			buf.append("<!doctype html>");
			((XHTMLRoot) file.getRoot()).setLang(loc.toLanguageTag());
			file.getRoot().initContent(null);
			ArrayList<XMLElement> rootContent = file.getRoot().getListElement(CONTENT.class);

			for (XMLElement elem : rootContent) {
				elem.toXML(new XMLBuilder("page", buf, null));
			}

//			System.out.println("------------------------------------------");

			return Response.status(Status.OK) // .type(MediaType.TEXT_HTML)
					.entity(buf.toString()).header("XUI", "ok").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("no found " + id).header("a", "b").build();
		}

	}

	private void initVariableJS() {
		List<Class<? extends JSClass>> listJSClass = new ArrayList<>(100);
		new FastClasspathScanner("com.elisaxui.xui").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
		new FastClasspathScanner("com.elisaxui.core.xui.xhtml").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
		for (Class<? extends JSClass> class1 : listJSClass) {
			System.out.println("list JS class "+class1);
			XUIFactoryXHtml.getXMLFile().initVar(XHTMLPart.jsBuilder, class1);
		}
	}

	private void initXMLFile(Class<? extends XHTMLPart> pageClass, XMLFile file) {
		try {
			XHTMLPart page = pageClass.newInstance();
			page.initContent(file.getRoot());
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

	private XMLFile createXMLFile() {
		XMLFile file = new XMLFile();
		ThreadLocalXUIFactoryPage.set(file);
		return file;
	}

	private Map<String, Class<? extends XHTMLPart>> getMapXHTMLPart() {
				
		List<Class<? extends XHTMLPart>> listXHTMLPart = new ArrayList<>(100);
		new FastClasspathScanner("com.elisaxui.xui").matchSubclassesOf(XHTMLPart.class, listXHTMLPart::add).scan();

		Map<String, Class<? extends XHTMLPart>> mapClass = new HashMap<String, Class<? extends XHTMLPart>>(100);
		for (Class<? extends XHTMLPart> pageClass : listXHTMLPart) {
			System.out.println("list XHTMLPart "+pageClass);
			
			xFile annPage = pageClass.getAnnotation(xFile.class);
			if (annPage != null) {
				mapClass.put(annPage.id(), pageClass);
			}
		}
		return mapClass;
	}
}
