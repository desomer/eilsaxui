package com.elisaxui.xui.core.page;

import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRoot.HEADER;
import com.elisaxui.core.xui.xhtml.js.JSXHTMLPart;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataCtx;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataDriven;
import com.elisaxui.core.xui.xhtml.js.datadriven.JSDataSet;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.xui.admin.page.JSTest2Class;
import com.elisaxui.xui.admin.page.JSTestClass;
import com.elisaxui.xui.admin.page.JSTestDataDriven;
import com.elisaxui.xui.core.widget.ViewBtnBurger;
import com.elisaxui.xui.core.widget.ViewNavBar;
import com.elisaxui.xui.core.widget.ViewOverlay;

@xFile(id = "standard.html")
@xComment("activite standard")
public class ScnStandard extends XHTMLPart {

	public static final int heightNavBar = 53;
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xTitle() {
		return xElement("title", "le standard");
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportAllClass() {
		return xListElement(
				xImport(JSXHTMLPart.class),
				xImport(JSTestDataDriven.class),
				xImport(JSDataDriven.class),
				xImport(JSDataSet.class),
				xImport(JSDataCtx.class)
				);
	}
	
	@xTarget(HEADER.class)
	@xRessource
	public Element xStyle() {

		return xCss()
				.add("html", "font-size: 14px;line-height: 1.5;"
						+ "font-family: 'Roboto', sans-serif;font-weight: normal; color: rgba(0,0,0,0.87);")
				.add("body", "background-color: white;margin: 0")
				// .add("div", "color: black;")

				.add(".content", "position:relative; padding: 8px; padding-top: " + (heightNavBar + 8) + "px")
				.add(".center", "height:100%; display: flex; align-items: center;justify-content: center")
				.add(".logo", "color: inherit; font-size: 2.1rem;")

				.add(".panel", "padding: 15px;"
						+ "margin-bottom: 15px;"
						+ "border-radius: 0;"
						+ "background-color: #FFF;"
						+ "box-shadow: 0 2px 2px 0 rgba(0,0,0,.16), 0 0 2px 0 rgba(0,0,0,.12)")

//				.add(".scene",
//						"position: absolute; width: 100%; height: 100%; overflow-x: hidden; background-color: black;")
				.add(".scene","overflow-x: hidden; background-color: black;")
				
				.add(".activity", "background-color: white; transition:transform 200ms ease-out;")
				.add(".activityLeftMenu", "transform: translate3d(100px,0px,0px);")
				.add("#content", "height:3000px; position:relative")
				;
	}

	@xTarget(HEADER.class)
	@xRessource
	public Element xImportJQUERY() {
		return xElement("/", "<script  src='http://code.jquery.com/jquery-3.2.1.min.js'></script>"
				+ "<script  src='https://cdnjs.cloudflare.com/ajax/libs/fastdom/1.0.5/fastdom.min.js'></script>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css'>"
				+ "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/hamburgers/0.8.1/hamburgers.min.css'>");
	}

	public Element xTest() {
		return xDiv(xAttr("class", "'panel'"),
				"Loin, très loin, au delà des monts Mots, à mille lieues des pays Voyellie et Consonnia, demeurent les Bolos Bolos. Ils vivent en retrait, à Bourg-en-Lettres, sur les côtes de la Sémantique, un vaste océan de langues. Un petit ruisseau, du nom de Larousse, coule en leur lieu et les approvisionne en règlalades nécessaires en tout genre; un pays paradisiagmatique, dans lequel des pans entiers de phrases prémâchées vous volent litéralement tout cuit dans la bouche. Pas même la toute puissante Ponctuation ne régit les Bolos Bolos - une vie on ne peut moins orthodoxographique. Un jour pourtant, une petite ligne de Bolo Bolo du nom de Lorem Ipsum décida de s'aventurer dans la vaste Grammaire. Le grand Oxymore voulut l'en dissuader, le prevenant que là-bas cela fourmillait de vils Virgulos, de sauvages Pointdexclamators et de sournois Semicolons qui l'attendraient pour sûr au prochain paragraphe, mais ces mots ne firent écho dans l'oreille du petit Bolo qui ne se laissa point déconcerter. Il pacqua ses 12 lettrines, glissa son initiale dans sa panse et se mit en route. Alors qu'il avait gravi les premiers flancs de la chaîne des monts Italiques, il jeta un dernier regard sur la skyline de");
	}

	@xTarget(CONTENT.class)
	public Element xContenu() {
		return xDiv(xAttr("class", "'scene'"), 
					xDiv(xAttr("class", "'activity'"),
						xPart(new ViewNavBar(),
								xPart(new ViewBtnBurger()),
								xDiv(xAttr("class", "'center'"), xDiv(xAttr("class", "'logo'"), "Elisa"))),
						xDiv(xAttr("class", "'content'"), xDiv(xAttr("id",txt("content")))
								//, xTest(), xTest(), xTest(), xTest(), xTest()
								, xPart(new ViewOverlay()))
						
				)

		);
	}
	
	//var oRect = oElement.getBoundingClientRect();

	JSTestDataDriven testDataDriven;
	
	@xTarget(AFTER_CONTENT.class)
	public Element xAddJS() {
		return xScriptJS(
				js()
				.__("$.fn.insertAt = function(elements, index){\n"+
						"\tvar children = this.children();\n"+
						"\tif(index >= children.length){\n"+
						"\t\tthis.append(elements);\n"+
						"\t\treturn this;\n"+
						"\t}\n"+
						"\tvar before = children.eq(index);\n"+
						"\t$(elements).insertBefore(before);\n"+
						"\treturn this;\n"+
						"};")
				
				.var(testDataDriven, _new())
				.__(testDataDriven.startTest())
				
				.__("$('.hamburger').on('click',", 
						fct().__("$('.activity').toggleClass('activityLeftMenu')")
						.__("$('.logo').toggleClass('animated shake')")
						
						
						, ")")
				
			);
	}

}
