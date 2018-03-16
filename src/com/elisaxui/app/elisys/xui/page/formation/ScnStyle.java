/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.css.CSSElement;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class ScnStyle extends CSSBuilder {

	static CSSClass cAB = new CSSClass().setId("cAB");
	static CSSClass cABC = new CSSClass().setId("cABC");
	
	void doStyle()
	{
		
	    on(media("screen and (max-width: 640px)"), ()->{
			on(sel("a"), ()->{
				css("display:blocka");
				on(sel(XUIScene.scene.and(XUIScene.cShell).directChildren(ViewOverlay.cBlackOverlay)), 
				()->{
					css("display:blockb");
					css("display:blockc");
				});
			});

			on(keyFrame("nom"), ()->{
				from(()-> css("display:blockd") );
				to(()-> css("display:blocke"));
				prct(10, ()-> css("display:blockf"));
			});
		});
		

	}
	
	void doStyle2()
	{
		CSSElement d = on(sel(cAB, ">", cABC), ()->{
			css("display:blocka");
			css("display:blockb");
			on(sel("&", cAB, "," ,cABC), ()->{ 
				css("display:blockc");
				css("display:blockd");
				on(sel("div"), ()->{
					css("display:blocked");
					css("display:blockfd");
				});
			});
//			on(sel("span"), ()->{
//				css("display:blocke");
//				css("display:blockf");
//			});
		});
		
		
		StringBuilder txtXML = new StringBuilder(1000);
		StringBuilder txtXMLAfter = new StringBuilder(1000);

		XHTMLFile file = new XHTMLFile();
		XUIFactoryXHtml.ThreadLocalXUIFactoryPage.set(file);
		XMLBuilder buf = new XMLBuilder("css", txtXML, txtXMLAfter);
		d.toXML(buf);
		
		System.out.println(txtXML);
	}
	
	public static void main(String [] args)
	{
		new ScnStyle ().doStyle2();
	}
	

}
