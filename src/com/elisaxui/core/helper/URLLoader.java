/**
 * 
 */
package com.elisaxui.core.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

import com.elisaxui.ResourceLoader;
import com.elisaxui.core.extern.JSMinifier;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;

/**
 * recuperer info d'une url distante
 * 
 * @author Bureau
 *
 */
public class URLLoader {

	public static String loadResourceNearClass(Object obj, String id, boolean optimizeJS) {

		CoreLogger.getLogger(1).info(() -> "read at " + id);
		Class<?> cl = obj.getClass();
		if (obj instanceof Class)
		{
			cl = (Class<?>) obj;
		}

		boolean retJSClass = JSClass.class.isAssignableFrom(cl);
		if (retJSClass) {
			ProxyHandler mh = (ProxyHandler) Proxy.getInvocationHandler(obj);
			cl = mh.getImplementClass();
		}

		URL url = ResourceLoader.getResource(new ResourceLoader(cl, id));

		String str = null;
		try {
			str = new String(Files.readAllBytes(Paths.get(url.toURI())));
		} catch (IOException | URISyntaxException e) {
			CoreLogger.getLogger(1).log(Level.SEVERE, "Pb loadResource " + id, e);
		}

		return optimizeJS ? JSMinifier.optimizeJS(str, false) : str;
	}

	public static final StringBuilder loadTextFromUrl(String u) {
		String home = System.getProperty("user.home");
		String pathdb = home + File.separator + "JTS" + File.separator;

		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		StringBuilder buf = new StringBuilder(10*1024);
		try {

			String f = u.substring(u.lastIndexOf("/") + 1);
			File file = new File(pathdb + f);
			CoreLogger.getLogger(1).info(() -> "read at " + file);
			if (file.exists())
			{
				StringBuilder s = readFile(file);
				return s;
			}
			url = new URL(u);
			CoreLogger.getLogger(1).info(() -> "read retry at " + url);	
			is = url.openStream();
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				buf.append(line);
				buf.append("\n");
			}
			CoreLogger.getLogger(1).info(() -> "read at " + url);
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
			}
		}

		return buf;
	}

	public static StringBuilder readFile(File file) {
		StringBuilder buf = new StringBuilder(10*1024);
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String string : lines) {
				buf.append(string);
				buf.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf;
	}
}
