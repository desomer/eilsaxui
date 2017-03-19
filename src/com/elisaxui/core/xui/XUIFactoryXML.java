package com.elisaxui.core.xui;

import java.lang.annotation.Annotation;
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

import com.elisaxui.core.xui.xhtml.XUIFileXHtml;
import com.elisaxui.core.xui.xhtml.XUIViewXHtml;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.annotation.File;

import javax.ws.rs.core.UriInfo;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

@Path("/page")
public class XUIFactoryXML {

	private static final ThreadLocal<XUIFileXHtml> ThreadLocalXUIFactoryPage = new ThreadLocal<XUIFileXHtml>();
	
	public static final XUIFileXHtml getXUIPageBuilder()
	{
		return ThreadLocalXUIFactoryPage.get();
	}
	
	@GET
	@Path("/{pays}/{lang}/id/{id}")
    @Produces(MediaType.TEXT_HTML)
	public Response getXUIScene(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("pays") String pays,
			@PathParam("lang") String lang, @PathParam("id") String id) {

		
		List<Class<? extends XUIViewXHtml>> listClass = new ArrayList<>(100);
		new FastClasspathScanner("com.elisaxui.xui.admin")
		    .matchSubclassesOf(XUIViewXHtml.class, listClass::add)
		    .scan();
		
		Map<String, Class<? extends XUIViewXHtml>> mapClass = new HashMap<String, Class<? extends XUIViewXHtml>>(100);
		for (Class<? extends XUIViewXHtml> pageClass : listClass) {
			File annPage = pageClass.getAnnotation(File.class);
			if (annPage!=null)
			{
				mapClass.put(annPage.id(), pageClass);
			}
		}
		
		Class<? extends XUIViewXHtml> pageClass = mapClass.get(id);
		
		XUIFileXHtml pageFactory = new XUIFileXHtml();
		ThreadLocalXUIFactoryPage.set(pageFactory);

		List<Locale> languages = headers.getAcceptableLanguages();
		Locale loc = languages.get(0);
		
		pageFactory.addPart(XUIFileXHtml.HtmlPart.LANG, loc.toLanguageTag());
		pageFactory.addPart(XUIFileXHtml.HtmlPart.HEADER,"<meta charset=\"utf-8\">\n");
		pageFactory.addPart(XUIFileXHtml.HtmlPart.HEADER,"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n");
	
		if (pageClass!=null)
		{
			try {
				XUIViewXHtml page = pageClass.newInstance();
				XMLFile file = new XMLFile();
				page.doContent(file);
				
			} catch (InstantiationException | IllegalAccessException e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR)   //.type(MediaType.TEXT_HTML)
						.entity(e.toString()).build();
			}
			String html = pageFactory.getPage().toString();
			return Response.status(Status.OK)   //.type(MediaType.TEXT_HTML)
					.entity(html).header("XUI", "ok").build();
		}
		else
		{
			return Response.status(Status.NOT_FOUND)
					.entity("no found "+id).header("a", "b").build();
		}
		


	}
}
