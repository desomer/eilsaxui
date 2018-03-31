/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import static com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSDocument.document;

import com.elisaxui.component.toolkit.TKPubSub;
import com.elisaxui.component.toolkit.datadriven.IJSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataDriven;
import com.elisaxui.component.toolkit.datadriven.JSDataSet;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.IJSDomTemplate;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomBuilder;
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

	static Section aSection;
	static Phrase aPhrase;
	
	@xTarget(HEADER.class)
	@xRessource  
	public XMLElement xImport() {
		return xListNode(
				xScriptSrc("https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js"),
				xImport(JSDomBuilder.class,
						TKPubSub.class,
						JSDataDriven.class,
						JSDataSet.class));
	}

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		vProp(CmpPage.pContentMain, vPart(new CmpSection()
				.vProperty(CmpSection.pSectionH1, "section static")
				.vProperty(CmpSection.pArticleH1, "article static")
				.vProperty(CmpSection.pContentArticle, "un texte article static")));

		CmpNavVertical nav = new CmpNavVertical();

		vProp(CmpPage.pContentNav, vPart(nav, nav.xItem("a1"), 
													nav.xItem("a2"), 
													nav.xItem("a3")));

		return vPart(new CmpPage());
	}

	public XMLElement xSessionDyn(JSArray<Section> arr) {
		return xElem(
				vFor(arr, aSection,
						vPart(new CmpSection()
								.vProperty(CmpSection.pSectionH1, aSection.titre())
								.vProperty(CmpSection.pArticleH1, aSection.article())
								.vProperty(CmpSection.pContentArticle,
								 vFor(aSection.phrases(), aPhrase,
										xDiv(aPhrase.text()))))));
	}

	@xTarget(AFTER_CONTENT.class) // le controleur apres chargement du body
	public XMLElement xLoad() {
		return xImport(JSTest.class);
	}

	/********************************************************/
	// une class js avec template et datadriven
	/********************************************************/
	public interface JSTest extends JSClass, IJSDomTemplate {

		@xStatic(autoCall = true) // appel automatique de la methode static
		default void main() {

			JSArray<Section> arrSection = let("arrSection", new JSArray<Section>().asLitteral());
			JSArray<Menu> arrNav = let("arrNav", new JSArray<Menu>().asLitteral());

			document().querySelector(CmpPage.cPageMain)
					.appendChild(createDomTemplate(new ScnTemplatePageDyn().xSessionDyn(arrSection)));
			
			document().querySelector(CmpNavVertical.cNavContainer)
					.appendChild(createDomTemplate(new CmpNavVertical().xNavDyn(arrNav)));
			
			/***************************************************/
			setTimeout(() -> {
				Menu obj = newJS(Menu.class);
				obj.titre().set("page A");
				arrNav.push(obj);
				
				obj = newJS(Menu.class);
				obj.titre().set("page B");
				arrNav.push(obj);
			}, 3000);

			setTimeout(() -> {
				arrNav.pop();
				arrSection.pop();
			}, 4000);
			
			/***************************************************/
			
			Section obj = newJS(Section.class);
			obj.titre().set("section A");
			obj.article().set("un article a ajouter");
			obj.phrases().set(new JSArray<JSObject>().asLitteral());
			arrSection.push(obj);

			obj = newJS(Section.class);
			obj.titre().set("section B");
			obj.article().set("un article 2 a ajouter");
			obj.phrases().set(new JSArray<JSObject>().asLitteral());
			arrSection.push(obj);
						
			/***************************************************/

			setTimeout(() -> {
				Phrase phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 1 ajouter");
				arrSection.at(0).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 2 ajouter");
				arrSection.at(0).phrases().push(phrase);
			}, 1000);

			setTimeout(() -> {
				Phrase phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 21 ajouter");
				arrSection.at(1).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 22 ajouter");
				arrSection.at(1).phrases().push(phrase);
				phrase = newJS(Phrase.class);
				phrase.text().set("une phrase 23 ajouter");
				arrSection.at(1).phrases().push(phrase);
			}, 2000);

		}
	}

	/********************************************************/
	// les dto
	/********************************************************/
	public interface Menu extends JSType {
		JSString titre();
	}
	
	public interface Section extends JSType {
		JSString titre();
		JSString article();
		JSArray<Phrase> phrases();
	}
	
	public interface Phrase extends JSType {
		JSString text();
	}
	
	/********************************************************/
	// les components
	/********************************************************/
	static class CmpPage extends XHTMLPart {
		static VProperty pContentMain;
		static VProperty pContentNav;
		static CSSClass cPageContainer;
		static CSSClass cPageMain;

		@xTarget(HEADER.class)
		@xRessource
		public XMLElement style() {
			return cStyle()
					.path(cPageContainer).set("display:flex")
					.andDirectChild(cStyle().path("*").set("flex:1 1 auto"));
		}

		@xTarget(CONTENT.class)
		public XMLElement xBar() {
			return xListNode(
					xHeader(),
					xDiv(cPageContainer,
							xNav(pContentNav),
							xMain(cPageMain, pContentMain)),
					xFooter());
		}

	}
	
	/********************************************************/
	static class CmpSection extends XHTMLPart {
		static VProperty pSectionH1;
		static VProperty pArticleH1;
		static VProperty pContentArticle;
		static CSSClass cSection;

		@xTarget(HEADER.class)
		@xRessource
		public XMLElement style() {
			return cStyle().path(cSection).set("border:1px solid black");
		}

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xSection(cSection,
						vIfExist(pSectionH1, xH1(pSectionH1)),
						xArticle(vIfExist(pArticleH1, xH1(pArticleH1)),
								pContentArticle),
						xAside()
					);
		}

	}
	
	/********************************************************/
	static class CmpNavVertical extends XHTMLPart implements IJSDataDriven {
		static CSSClass cNavContainer;
		static VProperty pContent;
		static Menu aRow;

		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return xUl(cNavContainer, getChildren(), pContent);
		}

		public XMLElement xItem(Object content) {
			return xLi(content);
		}

		public XMLElement xNavDyn(JSArray<Menu> arr) {			
			return xListNode(
					   vFor(arr, aRow, xItem(aRow.titre()) 
					));
		}
	}
}
