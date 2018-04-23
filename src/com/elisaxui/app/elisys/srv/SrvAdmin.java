/**
 * 
 */
package com.elisaxui.app.elisys.srv;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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

import com.elisaxui.app.core.admin.ScnAdmin.JSAppConfiguration;
import com.elisaxui.core.xui.config.ConfigFormat;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;

/**
 * @author Bureau
 *
 */
@Path("/app")
public class SrvAdmin implements IJSONBuilder {

	@GET
	@Path("/config/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getConfig(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {

		JSAppConfiguration config = newJava(JSAppConfiguration.class);

		config.minify().set(ConfigFormat.getData().isMinifyOnStart());
		config.disableComment().set(!ConfigFormat.getData().isEnableCommentFctJS());
		config.es5().set(ConfigFormat.getData().isEs5());
		config.singleFile().set(ConfigFormat.getData().isSinglefile());
		
		config.timeGenerated().set(ConfigFormat.getData().isTimeGenerated());
		config.fileChanged().set(ConfigFormat.getData().isFileChanged());
		config.patchChanges().set(ConfigFormat.getData().isPatchChanges());

		return config.getStringJSON();
	}

	@POST
	@Path("/config/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConfig(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id,
			String content) {

		JsonReader rdr = Json.createReader(new StringReader(content));
		JsonObject obj = rdr.readObject();
		boolean minify = obj.getBoolean("minify");
		boolean disableComment = obj.getBoolean("disableComment");
		boolean singleFile = obj.getBoolean("singleFile");
		boolean es5 = obj.getBoolean("es5");
		boolean timeGenerated = obj.getBoolean("timeGenerated");
		boolean fileChanged = obj.getBoolean("fileChanged");
		boolean patchChanges = obj.getBoolean("patchChanges");
		rdr.close();

		if (ConfigFormat.getData().isMinifyOnStart() != minify)
			ConfigFormat.getData().setMinify(minify);
		else {
			ConfigFormat.getData().setEnableCommentFctJS(!disableComment);
			ConfigFormat.getData().setTimeGenerated(timeGenerated);
			ConfigFormat.getData().setFileChanged(fileChanged);
			ConfigFormat.getData().setPatchChanges(patchChanges);	
		}
		
		
		
		ConfigFormat.getData().setSinglefile(singleFile);
		ConfigFormat.getData().setEs5(es5);
		if (es5)
			ConfigFormat.getData().setSinglefile(true);
		

		ConfigFormat.getData().setReload(true);

		return Response.status(Status.OK).build();
	}

	/*
	 * // JsonParser parser = Json.createParser(new StringReader(content)); // //
	 * while (parser.hasNext()) { // Event event = parser.next(); // if (event ==
	 * JsonParser.Event.KEY_NAME ) { // String key = parser.getString(); // event =
	 * parser.next(); // if (key.equals("phoneNumber")) { // String value =
	 * parser.getString(); // } // } // }
	 */
}
