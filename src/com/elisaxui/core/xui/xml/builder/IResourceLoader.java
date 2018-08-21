/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import com.elisaxui.core.extern.JSMinifier;
import com.elisaxui.core.helper.URLLoader;

/**
 * @author gauth
 *
 */
public interface IResourceLoader {

	default String loadResource(String nameFile, boolean optimizeJS) {

		return URLLoader.loadResourceNearClass(this, nameFile, optimizeJS);
	}

	default String loadResourceFromURL(String url, boolean optimizeJS) {
		String str = URLLoader.loadTextFromUrl(url).toString();
		return optimizeJS?minifyJS(str, false):str;
	}

	default String minifyJS(String code, boolean pretty) {
		return JSMinifier.optimizeJS(code, pretty);
	}

}
