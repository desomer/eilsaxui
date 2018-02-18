/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

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

import com.elisaxui.component.toolkit.TKCom;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSONType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@Path("/json")
public class SrvScnDataDriven implements IJSONBuilder {

	@xFile(id = "ScnSingle")
	public static class ScnDataDriven extends XHTMLPart {
		
		static XClass cMain;

		@xTarget(HEADER.class)
		@xRessource // une seule fois par vue
		public XMLElement xImportVue() {
			return xImport(JSXHTMLTemplate.class, TKCom.class);
		}

		@xTarget(HEADER.class)
		@xRessource
		public XMLElement xStylePart() {
			return xStyle(cMain, " span").add("color:blue");
		}
		
		@xTarget(CONTENT.class) // la vue App Shell
		public XMLElement xAppShell() {
			return xDiv(xH1("LOGO"), xArticle(cMain) );
		}

		@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
		public XMLElement xLoad() {
			return xImport(JSTestDataDriven.class);
		}

		// une class JS
		public interface JSTestDataDriven extends JSClass, IXHTMLTemplate {

			@xStatic(autoCall = true) // appel automatique de la methode static
			default void main() {	
				ImgType data = declareType(ImgType.class, "data");
				TKCom tkcom = declareType(TKCom.class, "TKCom");
				
				tkcom.requestUrl(JSString.value(REST_JSON_TEST_55)).then(callback(data, ()->
					document().querySelector(cMain).appendChild(newInst(JSTestDataDriven.class)
							.xImageOK(data.name(), data.urlImage()))
				));
			}

			default Object xImageOK(JSAny id, JSString url) {
				return xTemplateJS(xDiv(xSpan(id), xPicture(url)));
			}

			default Object xPicture(JSString url) {
				return xTemplateJS(xImg(xAttr("src", url)));
			}
		}
	}
	
	/****************************************************************************************************/
	/**
	 *  un type json
	 */
	public interface ImgType extends JSONType {
		JSString urlImage();
		JSString name();
	}
	/***************************************************************************************************/
	
	private static final String PHOTO = "https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb";
	private static final String REST_JSON_TEST_55 = "/rest/json/test/ok";
	
	@GET
	@Path("/test/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {

		ImgType data =  newJava(ImgType.class);
		data.name().set("Votre creation "+id);
		data.urlImage().set(PHOTO);
		
		return Response.status(Status.OK)    
				.entity(data._getContent())
				.build();
	}

}
