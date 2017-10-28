/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.HashMap;
import java.util.Map;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.xui.core.page.XUIScene;

/**
 * @author Bureau
 *
 * dictionnaire unique des ressource d'un file XHTML
 * 
 */
public class XHTMLFile extends XMLFile {

	private XUIScene scene;
	
	/**
	 * @return the scene
	 */
	public XUIScene getScene() {
		return scene;
	}

	/**
	 * @param scene the scene to set
	 */
	public void setScene(XUIScene scene) {
		this.scene = scene;
	}

	public Map<String, JSClassImpl> listClass = new HashMap<String, JSClassImpl>();

	public JSClassImpl getClassImpl(JSBuilder jsBuilder, Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		JSClassImpl impl = listClass.get(name);
		if (impl == null) {
			System.out.println("[XHTMLFile] import JSClass " + name);
			impl = jsBuilder.createJSClass();
			impl.setName(cl.getSimpleName());

			listClass.put(name, impl);


			JSBuilder.initJSConstructor(cl, name);
		}
		return impl;
	}


	
}
