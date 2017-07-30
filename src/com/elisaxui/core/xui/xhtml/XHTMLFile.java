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

/**
 * @author Bureau
 *
 */
public class XHTMLFile extends XMLFile {

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
