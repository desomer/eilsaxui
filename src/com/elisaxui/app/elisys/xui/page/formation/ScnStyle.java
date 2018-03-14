/**
 * 
 */
package com.elisaxui.app.elisys.xui.page.formation;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.component.widget.overlay.ViewOverlay;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.builder.css.CSSBuilder;
import com.elisaxui.core.xui.xhtml.builder.css.CSSDomElement;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class ScnStyle extends CSSBuilder {

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
		CSSDomElement d = on(sel("a"), ()->{
			css("display:blocka");
			css("display:blockb");
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
