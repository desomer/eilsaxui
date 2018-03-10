/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation2;

import java.util.ArrayList;
import java.util.List;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xFile(id = "ScnTemplatePage")
public class ScnTemplatePage extends XHTMLPart {

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		
		vProperty(CmpSection.pArticleH1, "article defaut");
		vProperty(CmpSection.pSectionH1, "section defaut");
		vProperty(CmpSection.pSectionH1+"SEC1", "section sec1");
		vProperty(CmpSection.pSectionH1+"SEC3", "section sec3");
		vProperty(CmpSection.pContentArticle+"SEC3", "un article surcharge 3");
		
		/*************************************************/
		/* liste static */
		List<XMLElement> listSection = new ArrayList<>();
		listSection.add(xPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC1")));
		listSection.add(xPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC2")
									.vProperty(CmpSection.pSectionH1, "section test 2")
									.vProperty(CmpSection.pArticleH1, "article test 2")));
		listSection.add(xPart(new CmpSection()
									.vProperty(XMLPart.PROP_ID, "SEC3")
									.vProperty(CmpSection.pArticleH1, "article test 3")
									.vProperty(CmpSection.pContentArticle, "un article 3")
									));
		/***********************************************/
		vProperties(CmpPage.pContentHeader, "HEADER");
		vProperties(CmpPage.pContentMain, listSection);
		vProperties(CmpPage.pContentFooter, "FOOTER");
		
		vProperties(CmpPage.pContentMain, xPart(new CmpSection()
				.vProperty(CmpSection.pArticleH1, "article test 4")
				.vProperty(CmpSection.pContentArticle, "un article 4")
		));
		
		vProperties(CmpPage.pContentMain, xPart(new CmpSection()
				.vProperty(CmpSection.pSectionH1, "section 5")
				.vProperty(CmpSection.pArticleH1, "article test 5")
				.vProperty(CmpSection.pContentArticle, "un article 5 bis")
		));
		
		vProperties(CmpPage.pContentNav, xUl(xLi("a"), xLi("b"), xLi("c")));
		
		return xPart(new CmpPage());
	}

	/********************************************************/
	// les components
	/********************************************************/
	static class CmpPage extends XHTMLPart {
		static VProperty pContentHeader;
		static VProperty pContentMain;
		static VProperty pContentNav;
		static VProperty pContentFooter;
		
		static XClass cPageContainer;
		
		@xTarget(HEADER.class)
		public XMLElement style() {
			return xStyle()
					  .path(cPageContainer).add("display:flex")
					  .andDirectChildPath(xStyle().path("*").add("flex:1 1 auto"));
					
		}
		
		/**TODO retirer le vSearch */
		@xTarget(CONTENT.class)
		public XMLElement xBar() {
			return xListElement(
					xHeader(vSearch(pContentHeader)),
					xDiv( cPageContainer,
							xNav(vSearch(pContentNav)),
							xMain(vSearch(pContentMain))
					),
					xFooter(vSearch(pContentFooter)));
		}

	}
	
	static class CmpSection extends XHTMLPart {
		static VProperty pSectionH1;
		static VProperty pArticleH1;
		static VProperty pContentArticle;
		static XClass cSection;
		
		@xTarget(HEADER.class)
		public XMLElement style() {
			return xStyle().path(cSection).add("border:1px solid black");
		}
		
		@xTarget(CONTENT.class)
		public XMLElement xBloc() {
			return  xSection( cSection,
								vIfExist(pSectionH1,  
										xH1(vSearch(pSectionH1))
								),
								xArticle(vIfExist(pArticleH1, 
										 		xH1(vSearch(pArticleH1))),
										 vSearch(pContentArticle)
										 ),
								xAside()
							);
		}

	}
}