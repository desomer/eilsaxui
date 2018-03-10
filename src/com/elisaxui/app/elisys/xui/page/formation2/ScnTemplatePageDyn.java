/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSXHTMLTemplate;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xStatic;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xFile(id = "ScnTemplatePageDyn")
public class ScnTemplatePageDyn extends XHTMLPart implements IJSDataDriven {

	@xTarget(HEADER.class)
	@xRessource // une seule fois par vue
	public XMLElement xImport() {
		return xList(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(JSXHTMLTemplate.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		vProperties(CmpPage.pContentMain, xPart(new CmpSection()
				.vProperty(CmpSection.pSectionH1, "section static")
				.vProperty(CmpSection.pArticleH1, "article static")
				.vProperty(CmpSection.pContentArticle, "un texte article static")));

		CmpNavVertical nav = new CmpNavVertical();

		vProperties(CmpPage.pContentNav, xPart(nav, nav.xItem("a1"), nav.xItem("a2"), nav.xItem("a3")));

		return xPart(new CmpPage());
	}

	public XMLElement xTemplateDyn(JSArray<JSObject> arr) {

		JSon aRow = JSContent.declareType(JSon.class, "aRow");  //a mettre en injection de dep

		return xListElement(
				vFor(arr, aRow,
						xPart(new CmpSection()
								.vProperty(CmpSection.pSectionH1, aRow.attr("titre"))
								.vProperty(CmpSection.pArticleH1, aRow.attr("article"))
								.vProperty(CmpSection.pContentArticle,
								 vFor(aRow.attr("m"), aRow,
										xDiv(aRow.attr("phrase")))))));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}

	/********************************************************/
	// une class js avec template et datadriven
	/********************************************************/
	public interface JSTest extends JSClass, IXHTMLTemplate {

		@SuppressWarnings("unchecked")
		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {

			JSArray<JSObject> arrSection = let("arr", new JSArray<JSObject>().asLitteral());
			JSArray<JSObject> arrNav = let("arrNav", new JSArray<JSObject>().asLitteral());

			document().querySelector(CmpPage.cPageMain)
					.appendChild(jsTemplate(new ScnTemplatePageDyn().xTemplateDyn(arrSection)));
			
			document().querySelector(CmpNavVertical.cNavContainer)
					.appendChild(jsTemplate(new CmpNavVertical().xTemplateDyn(arrNav)));
			
			/***************************************************/
			JSObject obj = new JSObject().asLitteral();
			obj.attr("titre").set(JSString.value("page A"));
			arrNav.push(obj);
			
			obj = new JSObject().asLitteral();
			obj.attr("titre").set(JSString.value("page B"));
			arrNav.push(obj);
			/***************************************************/
			
			obj = new JSObject().asLitteral();
			obj.attr("titre").set(JSString.value("section A"));
			obj.attr("article").set(JSString.value("un article a ajouter"));
			obj.attr("m").set(new JSArray<JSObject>().asLitteral());
			arrSection.push(obj);

			obj = new JSObject().asLitteral();
			obj.attr("titre").set(JSString.value("section B"));
			obj.attr("article").set(JSString.value("un article 2 a ajouter"));
			obj.attr("m").set(new JSArray<JSObject>().asLitteral());
			arrSection.push(obj);
			/***************************************************/
			JSArray<JSObject> arr2 = let(JSArray.class, "arr2", arrSection.at(0).attr("m"));
			JSArray<JSObject> arr3 = let(JSArray.class, "arr3", arrSection.at(1).attr("m"));

			setTimeout(() -> {
				JSObject phrase = new JSObject().asLitteral();
				phrase.attr("phrase").set(JSString.value("une phrase 1 ajouter"));
				arr2.push(phrase);
				phrase = new JSObject().asLitteral();
				phrase.attr("phrase").set(JSString.value("une phrase 2 ajouter"));
				arr2.push(phrase);
			}, 1000);

			setTimeout(() -> {
				JSObject phrase = new JSObject().asLitteral();
				phrase.attr("phrase").set(JSString.value("une phrase 21 ajouter"));
				arr3.push(phrase);
				phrase = new JSObject().asLitteral();
				phrase.attr("phrase").set(JSString.value("une phrase 22 ajouter"));
				arr3.push(phrase);
				phrase = new JSObject().asLitteral();
				phrase.attr("phrase").set(JSString.value("une phrase 23 ajouter"));
				arr3.push(phrase);
			}, 2000);

			// cast(JSArray.class, obj.attr("m")).push(phrase); //TODO a faire marcher
		}
	}

	/********************************************************/
	// les components
	/********************************************************/
	static class CmpPage extends XHTMLPart {
		static VProperty pContentMain;
		static VProperty pContentNav;
		static XClass cPageContainer;
		static XClass cPageMain;

		@xTarget(HEADER.class)
		@xRessource
		public XMLElement style() {
			return xStyle()
					.path(cPageContainer).add("display:flex")
					.andDirectChildPath(xStyle().path("*").add("flex:1 1 auto"));
		}

		@xTarget(CONTENT.class)
		public XMLElement xBar() {
			return xListElement(
					xHeader(),
					xDiv(cPageContainer,
							xNav(vSearch(pContentNav)),
							xMain(cPageMain, vSearch(pContentMain))),
					xFooter());
		}

	}
	
	/********************************************************/
	static class CmpSection extends XHTMLPart {
		static VProperty pSectionH1;
		static VProperty pArticleH1;
		static VProperty pContentArticle;
		static XClass cSection;

		@xTarget(HEADER.class)
		@xRessource
		public XMLElement style() {
			return xStyle().path(cSection).add("border:1px solid black");
		}

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xSection(cSection,
					vIfExist(pSectionH1,
							xH1(vSearch(pSectionH1))),
					xArticle(vIfExist(pArticleH1,
							xH1(vSearch(pArticleH1))),
							vSearch(pContentArticle)),
					xAside());
		}

	}
	
	/********************************************************/
	static class CmpNavVertical extends XHTMLPart implements IJSDataDriven {
		static XClass cNavContainer;
		static VProperty pContent;

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xUl(cNavContainer, xList(getChildren(), vSearch(pContent)));
		}

		public XMLElement xItem(Object content) {
			return xLi(content);
		}

		public XMLElement xTemplateDyn(JSArray<JSObject> arr) {
			JSon aRow = JSContent.declareType(JSon.class, "aRow");   //a mettre en injection de dep
			
			return xListElement(
					   vFor(arr, aRow, xItem(aRow.attr("titre"))
					));
		}
	}
}
