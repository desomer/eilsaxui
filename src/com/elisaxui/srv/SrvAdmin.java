/**
 * 
 */
package com.elisaxui.srv;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import javax.ws.rs.core.UriInfo;

import com.elisaxui.xui.core.widget.menu.JSONMenu;

/**
 * @author Bureau
 *
 */
@Path("/json")
public class SrvAdmin {
	
	
	class JSONMenuAct1 extends JSONMenu
	{
		public Object getJSON()
		{
		  return arr(item("Paramètres", "settings", "setting"),
				  	 item("Configuration","build","config"),
				  	 divider(),
				  	 item("Aide", "help_outline", "help")
				  ) ; 
		}
	}
	
	
	private static String JSON = null; 
	
	@GET
	@Path("/menu/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {
		
		JSONMenuAct1 menu = new JSONMenuAct1();
		
		if (JSON==null)
			JSON =  menu.getJSON().toString();
		
		JSON = JSON.replace(",\"_dom_\":{}","");
		
		return Response.status(Status.OK)    //.type(MediaType.APPLICATION_JSON)
				.entity(JSON)
				.header("XUI", "ok").build();
	}
	
	
    @POST
    @Path("/save")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response myResourceMethod(String content) {
    	if (!content.equals("[]"))
    		JSON = content;
    	
    	System.out.println("content="+ content);
    	
    	return Response.status(Status.OK).build();
    }

}
