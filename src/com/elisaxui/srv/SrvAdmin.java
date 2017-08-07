/**
 * 
 */
package com.elisaxui.srv;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

/**
 * @author Bureau
 *
 */
@Path("/json")
public class SrvAdmin {
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {
		return Response.status(Status.OK)    //.type(MediaType.APPLICATION_JSON)
				.entity("{\"a\":1}")
				.header("XUI", "ok").build();
	}

}
