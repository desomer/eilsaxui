/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xml.XMLFile;

/**
 * @author Bureau
 *
 * dictionnaire unique des ressource d'un file XHTML
 * 
 */
public class XHTMLFile extends XMLFile {

	private static final boolean debug = false;
	
	private XUIScene scene;
	MultivaluedMap<String, String> param;
	
	/**
	 * @param param the param to set
	 */
	public final void setParam(MultivaluedMap<String, String> param) {
		this.param = param;
	}


	/**
	 * @return the scene
	 */
	public final XUIScene getScene() {
		return scene;
	}

	
	public final List<String> getParam(String key)
	{
		return param.get(key);
	}
	
	public final String getFisrtParam(String key, String def)
	{
		List<String> p =  param.get(key);
		
		return p==null?def:p.get(0);
	}
	
	/**
	 * @param scene the scene to set
	 */
	public final void setScene(XUIScene scene) {
		this.scene = scene;
	}

	private Map<String, JSClassImpl> listClass = new HashMap<String, JSClassImpl>();

	public final JSClassImpl getClassImpl(JSBuilder jsBuilder, Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		JSClassImpl impl = listClass.get(name);
		if (impl == null) {
			if (debug)
				System.out.println("[XHTMLFile] import JSClass " + name);
			impl = jsBuilder.createJSClass();
			impl.setName(cl.getSimpleName());

			listClass.put(name, impl);

			// initialise le constructor
			JSBuilder.initJSConstructor(cl, name);
		}
		return impl;
	}


	
}
