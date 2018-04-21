/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.elisaxui.component.page.XUIScene;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xExport;

/**
 * @author Bureau
 *
 * dictionnaire unique des ressource d'un file XHTML
 * 
 */
public class XHTMLFile extends XMLFile {

	private static final boolean debug = false;
	private Map<String, JSClassBuilder> listClass = new HashMap<String, JSClassBuilder>();
	
	private XMLPart scene;
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
	public final XMLPart getScene() {
		return scene;
	}

	
	public final List<String> getParam(String key)
	{
		return param.get(key);
	}
	
	public final String getFirstQueryParam(String key, String def)
	{
		List<String> p =  param.get(key);
		
		return p==null?def:p.get(0);
	}
	
	/**
	 * @param scene the scene to set
	 */
	public final void setScene(XMLPart scene) {
		this.scene = scene;
	}

	public final JSClassBuilder getClassImpl(Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		JSClassBuilder impl = listClass.get(name);
		if (impl == null) {
			if (debug)
				System.out.println("[XHTMLFile] import JSClass " + name);
			impl = new JSClassBuilder();
			impl.setName(cl.getSimpleName());
			if (! XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isSinglefile())
				impl.setExportable(cl.getAnnotation(xExport.class)!=null);
			
			listClass.put(name, impl);

			// initialise le constructor
			Object auto = JSClassBuilder.initJSConstructor(cl);
			impl.setAutoCallMeth(auto);
		}
		return impl;
	}


	
}
