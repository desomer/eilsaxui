/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.component.toolkit.com.JSCom.xuiCom;
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

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xResource;
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
public class ScnComboDyn implements IJSONBuilder {

	/********************************************
	 * VIEW
	 ***********************************************/
	@xResource(id = "ScnComboDyn")
	public static class CmpComboTelephone extends XHTMLPart implements IJSDataDriven {
		static CSSClass cMain;
		static Telephone aRow;

		/********************************************
		 * IMPORT
		 ********************************************/
		@xTarget(HEADER.class)
		@xResource  
		public XMLElement xImport() {
			return xListNode(
					xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
					xImport(JSDomBuilder.class,
							TKPubSub.class,
							JSDataDriven.class,
							JSDataSet.class,
							TKCom.class));
		}
		/********************************************
		 * STYLE
		 ********************************************/
		@xTarget(HEADER.class)
		@xResource
		public XMLElement xStylePart() {
			return cStyle().path(cMain).set("display:block");
		}

		/********************************************
		 * APP SHELL
		 ********************************************/
		@xTarget(CONTENT.class) // la vue App Shell
		public XMLElement xAppShell() {
			return xUl(cMain);
		}

		/********************************************
		 * TEMPLATE
		 ********************************************/
		public XMLElement xItem(JSAny code, JSAny Libelle) {
			return xLi(code, "- ", Libelle);
		}

		public XMLElement xListItem(JSArray<Telephone> listTelephone) {			
			return xListNode(
					   vFor(listTelephone, aRow, xItem(aRow.nom(), aRow.marque()) 
					));
		}
		
		/********************************************
		 * CONTROLEUR
		 ********************************************/
		@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
		public XMLElement xLoad() {
			return xImport(JSTestTemplate.class);
		}
		// une class JS
		public interface JSTestTemplate extends JSClass, IJSDomTemplate {

			@xStatic(autoCall = true)
			default void main() {
				
				JSArray<Telephone> data = let("data", new JSArray<Telephone>());

				document().querySelector(cMain)
					.appendChildTemplate(new CmpComboTelephone().xListItem(data));
				
				JSArray<Telephone> result = JSContent.declareArray(Telephone.class, "result");
				xuiCom().requestUrl(JSString.value(REST_JSON_TEST+"A"))
						.then(fct(result, () -> data.pushAll(result) ));
			}

		}
	}

	/********************************************
	 * DTO
	 ***********************************************/
	public interface Telephone extends JSType {
		JSString nom();
		JSString marque();
	}

	/********************************************
	 * SERVICE
	 *************************************************/
	private static final String REST_JSON_TEST = "/rest/json/listTelephone/";

	@GET
	@Path("/listTelephone/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHtml(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {

		JSArray<Telephone> result = new JSArray<Telephone>().asLitteral();
		
		Telephone data = newJava(Telephone.class);
		data.nom().set("IPhone " + id);
		data.marque().set("Apple");
		result.push(data);
		
		data = newJava(Telephone.class);
		data.nom().set("Galaxy S7" + id);
		data.marque().set("Samsung");
		result.push(data);

		return Response.status(Status.OK)
				.entity(result.toString())
				.build();
	}

}
