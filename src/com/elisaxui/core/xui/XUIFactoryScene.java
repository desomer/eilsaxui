package com.elisaxui.core.xui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/scene")
public class XUIFactoryScene {

	@GET
	@Path("/{pays}/{lang}/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String getXUIScene(@PathParam("pays") String pays, @PathParam("lang") String lang, @PathParam("id") String id) {
		return pays+"-"+lang+"-"+id;
	}
}
