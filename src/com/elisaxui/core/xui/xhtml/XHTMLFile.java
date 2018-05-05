/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xml.XMLFile;
import com.elisaxui.core.xui.xml.annotation.xExport;

/**
 * @author Bureau
 *
 * dictionnaire unique des ressource d'un file XHTML
 * 
 */
public class XHTMLFile extends XMLFile {

	private static final boolean DEBUG = false;
	/** liste des classes du fichier */
	private Map<String, JSClassBuilder> listClass = new HashMap<String, JSClassBuilder>();
	/**   les parametres des la request */
	MultivaluedMap<String, String> param;
	private XHTMLTemplate root;
	
	
	/**
	 * @param param the param to set
	 */
	public final void setParam(MultivaluedMap<String, String> param) {
		this.param = param;
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
	
	public final JSClassBuilder getClassImpl(Class<? extends JSClass> cl) {
		String name = cl.getSimpleName();
		JSClassBuilder impl = listClass.get(name);
		if (impl == null) {
			if (DEBUG)
				System.out.println("[XHTMLFile] import JSClass " + name);
			impl = new JSClassBuilder();
			impl.setName(cl.getSimpleName());
			impl.setExportable(cl.getAnnotation(xExport.class)!=null);
			
			listClass.put(name, impl);

			// initialise le constructor
			Object auto = JSClassBuilder.initJSConstructor(cl);
			impl.setAutoCallMeth(auto);
		}
		return impl;
	}


	public XHTMLTemplate getXHTMLTemplate() {
		return root;
	}


	public void setXHTMLTemplate(XHTMLTemplate root) {
		this.root = root;
	}


	
}
