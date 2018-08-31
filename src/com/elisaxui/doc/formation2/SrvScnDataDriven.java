/**
 * 
 */
package com.elisaxui.doc.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

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

import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.IJSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
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

	/********************************************
	 * VIEW
	 ***********************************************/
	@xResource(id = "SrvScnDataDriven")
	public static class ScnDataDriven extends XHTMLPart {

		static CSSClass cMain;

		@xTarget(HEADER.class)
		@xResource // une seule fois par vue
		/** TODO a gerer en automatique if script ou style */
		public XMLElement xImportVue() {
			return xElem(JSDomBuilder.class, TKCom.class);
		}

		@xTarget(HEADER.class)
		@xResource
		public XMLElement xStylePart() {
			return cStyle().path(cMain, " span").set("color:blue");
		}

		@xTarget(CONTENT.class) // la vue App Shell
		public XMLElement xAppShell() {
			return xDiv(xH1("LOGO"), xArticle(cMain));
		}

		@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
		public XMLElement xLoad() {
			return xIncludeJS(JSTestTemplate.class);
		}

		// une class JS
		public interface JSTestTemplate extends JSClass, IJSNodeTemplate {

			@xStatic(autoCall = true) // appel automatique de la methode static
			default void main() {
				ImgType data = declareType(ImgType.class, "data");

				callStatic(TKCom.class).requestUrl(JSString.value(REST_JSON_TEST + "OK"))
						.then(fct(data, () -> document().querySelector(cMain)
								.appendChild(newJS(JSTestTemplate.class).xImageOK(data.name(), data.urlImage()))));
			}

			default JSNodeElement xImageOK(JSAny id, JSString url) {
				return createNodeTemplate(xDiv(xSpan(id), xPicture(url)));
			}

			default JSNodeElement xPicture(JSString url) {
				return createNodeTemplate(xImg(xAttr("src", url)));
			}
		}
	}

	/********************************************
	 * DTO
	 ***********************************************/
	public interface ImgType extends JSType {
		JSString urlImage();

		JSString name();
	}

	/********************************************
	 * SERVICE
	 *************************************************/

	private static final String PHOTO = "https://images.pexels.com/photos/316465/pexels-photo-316465.jpeg?h=350&auto=compress&cs=tinysrgb";
	private static final String REST_JSON_TEST = "/rest/json/test/";

	@GET
	@Path("/test/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {

		ImgType data = newJava(ImgType.class);
		data.name().set("Votre creation " + id);
		data.urlImage().set(PHOTO);

		return Response.status(Status.OK)
				.header("Link", "<https://fonts.googleapis.com/icon?family=Material+Icons>; rel=preload; as=style,"
						+ " <https://fonts.gstatic.com>; rel=preconnect")
				.entity(data._getContent())
				.build();
	}

}
