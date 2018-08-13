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

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.com.TKCom;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.IJSMountFactory;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.ICSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.XHTMLPartMount;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.IJSONBuilder;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.IResourceLoader;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 *         https://localhost:9998/rest/page/fr/fra/id/ScnComboDyn
 *
 */
@Path("/json")
public class ScnComboDyn implements IJSONBuilder {

	/********************************************
	 * VIEW
	 ***********************************************/
	@xResource(id = "ScnComboDyn")
	public static class CmpComboTelephone extends XHTMLPartMount implements IJSDataDriven, ICSSBuilder, IResourceLoader, IJSMountFactory {
		static CSSClass cULMain;
		static Telephone aRow;

		/********************************************
		 * IMPORT JS
		 ********************************************/
		@xTarget(HEADER.class)
		@xResource(id = "testCombo.js")
		public XMLElement xImport() {
			return xElem(
					xScriptJS(
							loadResourceFromURL("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js")),
					JSDomBuilder.class,
					TKPubSub.class,
					JSDataDriven.class,
					JSDataSet.class,
					TKCom.class);
		}

		/********************************************
		 * STYLE
		 ********************************************/
		@xTarget(HEADER.class)
		@xResource(id = "testCombo.css", async = true)
		public XMLElement xStylePart() {
			return xElem(xStyle(sMedia("all"), () -> {
				sOn(sSel(cULMain, " li"), () -> {
							css("border-radius: 26px 27px 27px 27px");
							css("border: 1px solid #2394a7");
							css("margin: 9px");
							css("padding: 9px;");
							css("background: #dcf5f5;");
						});
			}));
		}

		/********************************************
		 * APP SHELL
		 ********************************************/
		@xTarget(CONTENT.class) // la vue App Shell
		public XMLElement xAppShell() {
			return xUl(cULMain);
		}

		/********************************************
		 * TEMPLATE
		 ********************************************/
		public XMLElement xItem(JSAny code, JSAny libelle) {
			return xLi(code, "- ", libelle);
		}

		public XMLElement xListItem(JSArray<Telephone> listTelephone) {
			return xElem(vFor(listTelephone, aRow,
					xItem(aRow.nom(), aRow.marque())));
		}

		/********************************************
		 * CONTROLEUR
		 ********************************************/
		@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
		public XMLElement xLoad() {
			return xElem(JSTestTemplate.class);
		}

		// une class JS
		@xCoreVersion("1")
		public interface JSTestTemplate extends JSClass {
			JSArray<Telephone> data = JSClass.declareType();
			JSArray<Telephone> result = JSClass.declareType();
			TKCom tkCom = JSClass.declareTypeClass(TKCom.class);

			@xStatic(autoCall = true)
			default void main() {

				let(data, JSArray.newLitteral());

				document().querySelector(cULMain).appendChild(new CmpComboTelephone().xListItem(data));

				tkCom.requestUrl(JSString.value(REST_JSON_TEST + "unID"))
						.then(fct(result, () -> {
							Telephone sony = newJS(Telephone.class);
							sony.nom().set("Xperia XZ2");
							sony.marque().set("Sony");

							data.push(sony);
							data.pushAll(result);
						}));
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

		JSArray<Telephone> result = JSArray.newLitteral();

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
