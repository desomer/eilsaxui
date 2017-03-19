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
import javax.ws.rs.core.UriInfo;

import com.elisaxui.core.xui.view.XUIViewPage;
import com.elisaxui.core.xui.view.annotation.Page;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

@Path("/page")
public class XUIFactoryScene {

	private static final ThreadLocal<XUIPageBuilder> ThreadLocalXUIFactoryPage = new ThreadLocal<XUIPageBuilder>();
	
	public static final XUIPageBuilder getXUIPageBuilder()
	{
		return ThreadLocalXUIFactoryPage.get();
	}
	
	@GET
	@Path("/{pays}/{lang}/id/{id}")
    @Produces(MediaType.TEXT_HTML)
	public Response getXUIScene(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("pays") String pays,
			@PathParam("lang") String lang, @PathParam("id") String id) {

		
		List<Class<? extends XUIViewPage>> listClass = new ArrayList<>(100);
		new FastClasspathScanner("com.elisaxui.xui.admin")
		    .matchSubclassesOf(XUIViewPage.class, listClass::add)
		    .scan();
		
		Map<String, Class<? extends XUIViewPage>> mapClass = new HashMap<String, Class<? extends XUIViewPage>>(100);
		for (Class<? extends XUIViewPage> pageClass : listClass) {
			Page annPage = pageClass.getAnnotation(Page.class);
			if (annPage!=null)
			{
				mapClass.put(annPage.id(), pageClass);
			}
		}
		
		Class<? extends XUIViewPage> pageClass = mapClass.get(id);
		
		XUIPageBuilder pageFactory = new XUIPageBuilder();
		ThreadLocalXUIFactoryPage.set(pageFactory);

		List<Locale> languages = headers.getAcceptableLanguages();
		Locale loc = languages.get(0);
		
		pageFactory.addPart(XUIPageBuilder.HtmlPart.LANG, loc.toLanguageTag());
		pageFactory.addPart(XUIPageBuilder.HtmlPart.HEADER,"<meta charset=\"utf-8\">\n");
		pageFactory.addPart(XUIPageBuilder.HtmlPart.HEADER,"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n");
	
		if (pageClass!=null)
		{
			try {
				XUIViewPage page = pageClass.newInstance();
				page.doView();
				
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
