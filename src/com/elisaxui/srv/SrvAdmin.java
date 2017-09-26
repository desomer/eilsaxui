/**
 * 
 */
package com.elisaxui.srv;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

import com.elisaxui.core.data.json.JSONBuilder;
import com.elisaxui.xui.core.page.XUIScene;
import com.elisaxui.xui.core.widget.menu.JSONMenu;

/**
 * @author Bureau
 *
 */
@Path("/json")
public class SrvAdmin {
	
	
	class JSONMenuAct1 extends JSONMenu
	{
		@Override
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
		
		if (JSON==null)
		{
			JSONMenuAct1 menu = new JSONMenuAct1();
			JSON =  menu.getJSON().toString();
		}
		
		return Response.status(Status.OK)    //.type(MediaType.APPLICATION_JSON)
				.entity(JSON)
				.header("XUI", "ok").build();
	}
	
    @POST
    @Path("/savemenu")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response myResourceMethod(String content) {
    	if (!content.equals("[]"))
    	{
    		JSON = content;
    		JSON = JSON.replace(",\"_dom_\":{}","");
    	}
    	
    	System.out.println("content="+ content);
    	
    	return Response.status(Status.OK).build();
    }
	
    /***************************************************************************************/
	@GET
	@Path("/manifest.json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManifest(@Context HttpHeaders headers, @Context UriInfo uri) {
		class JSONManifest extends JSONBuilder
		{
			public Object getJSON()
			{
				
			  return obj(v("short_name","Elisys"),
					     v("name","Elisys dyslexie"),
					     v("display","standalone"),
					     v("background_color",XUIScene.bgColorContent),
					     v("theme_color",XUIScene.bgColorTheme),
					     v("start_url","/"),
					     v("description","Elisys, dyslexie pour parent, enfant et professeur"),
					     v("orientation","portrait"),
					     v("icons", arr( 
					    		 obj( v("src","/rest/json/icon-144x144.png"), v("sizes","144x144"), v("type","image/png")),
					    		 obj( v("src","/rest/json/icon-196x196.png"), v("sizes","196x196"), v("type","image/png")),
					    		 obj( v("src","/rest/json/icon-512x512.png"), v("sizes","512x512"), v("type","image/png"))
					    		 ) )
					  ); 
			}
		}
		
		JSONManifest manifest = new JSONManifest();
		
		return Response.status(Status.OK)    //.type(MediaType.APPLICATION_JSON)
				.entity(manifest.getJSON().toString())
				.header("XUI", "ok").build();
	}
	

  
    /**
     * https://stackoverflow.com/questions/9204287/how-to-return-a-png-image-from-jersey-rest-service-method-to-the-browser
     *   
     * @return
     */
	@GET
    @Path("/icon-{h}x{w}.png")
    @Produces("image/png")
    public Response getIcone(@Context HttpHeaders headers, @Context UriInfo uri,  @PathParam("h") int h,  @PathParam("w") int w) {

        byte[] imageData = null;
		try {
			BufferedImage image = ImageIO.read(new File("c:\\java\\brunette_princess1600.png"));
			
			Image resizedImg=image.getScaledInstance(w,h,Image.SCALE_DEFAULT);
		       
	        // Create a BufferedImage object of w,h width and height
	        // and of the bim type
	        BufferedImage rBimg=new BufferedImage(w,h,image.getType());
	       
	        // Create Graphics object
	        Graphics2D g=rBimg.createGraphics();
	       
	        // Draw the resizedImg from 0,0 with no ImageObserver
	        g.drawImage(resizedImg,0,0,null);
	       
	        // Dispose the Graphics object, we no longer need it
	        g.dispose();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(rBimg, "png", baos);
			imageData = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // uncomment line below to send streamed
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }
    
    
//    import org.apache.commons.lang3.time.DateUtils;
//    import org.slf4j.Logger;
//    import org.slf4j.LoggerFactory;
//
//    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
//
//    @GET
//    @Path("16x16")
//    @Produces("image/png")
//    public Response get16x16PNG(@HeaderParam("If-Modified-Since") String modified) {
//        File repositoryFile = new File("c:/temp/myfile.png");
//        return returnFile(repositoryFile, modified);
//    }
//
//    /**
//     * 
//     * Sends the file if modified and "not modified" if not modified
//     * future work may put each file with a unique id in a separate folder in tomcat
//     *   * use that static URL for each file
//     *   * if file is modified, URL of file changes
//     *   * -> client always fetches correct file 
//     * 
//     *     method header for calling method public Response getXY(@HeaderParam("If-Modified-Since") String modified) {
//     * 
//     * @param file to send
//     * @param modified - HeaderField "If-Modified-Since" - may be "null"
//     * @return Response to be sent to the client
//     */
//    public static Response returnFile(File file, String modified) {
//        if (!file.exists()) {
//            return Response.status(Status.NOT_FOUND).build();
//        }
//
//        // do we really need to send the file or can send "not modified"?
//        if (modified != null) {
//            Date modifiedDate = null;
//
//            // we have to switch the locale to ENGLISH as parseDate parses in the default locale
//            Locale old = Locale.getDefault();
//            Locale.setDefault(Locale.ENGLISH);
//            try {
//                modifiedDate = DateUtils.parseDate(modified, org.apache.http.impl.cookie.DateUtils.DEFAULT_PATTERNS);
//            } catch (ParseException e) {
//                logger.error(e.getMessage(), e);
//            }
//            Locale.setDefault(old);
//
//            if (modifiedDate != null) {
//                // modifiedDate does not carry milliseconds, but fileDate does
//                // therefore we have to do a range-based comparison
//                // 1000 milliseconds = 1 second
//                if (file.lastModified()-modifiedDate.getTime() < DateUtils.MILLIS_PER_SECOND) {
//                    return Response.status(Status.NOT_MODIFIED).build();
//                }
//            }
//        }        
//        // we really need to send the file
//
//        try {
//            Date fileDate = new Date(file.lastModified());
//            return Response.ok(new FileInputStream(file)).lastModified(fileDate).build();
//        } catch (FileNotFoundException e) {
//            return Response.status(Status.NOT_FOUND).build();
//        }
//    }
//
//    /*** copied from org.apache.http.impl.cookie.DateUtils, Apache 2.0 License ***/
//
//    /**
//     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
//     */
//    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
//
//    /**
//     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
//     */
//    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
//
//    /**
//     * Date format pattern used to parse HTTP date headers in ANSI C
//     * <code>asctime()</code> format.
//     */
//    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
//
//    public static final String[] DEFAULT_PATTERNS = new String[] {
//        PATTERN_RFC1036,
//        PATTERN_RFC1123,
//        PATTERN_ASCTIME
//    };
}
