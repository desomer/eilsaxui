/**
 * 
 */
package com.elisaxui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.elisaxui.core.notification.ErrorNotificafionMgr;

public class ResourceLoader {

	String uri;
	
	public ResourceLoader(Class ResourceHandler, String id)
	{
		uri = ResourceHandler.getCanonicalName();
		uri = uri.substring(0, uri.lastIndexOf('.'));
		uri = uri.replace('.', '/')+'/'+id;
	}
	
	
	public static URL getResource(ResourceLoader resource) {
		return getResource("src/"+resource.uri);
	}
	
    public static URL getResource(String resource) {
    	
        final List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
        classLoaders.add(Thread.currentThread().getContextClassLoader());
        classLoaders.add(ResourceLoader.class.getClassLoader());

        for (ClassLoader classLoader : classLoaders) {
            final URL url = getResourceWith(classLoader, resource);
            if (url != null) {
                return url;
            }
        }

        final URL systemResource = ClassLoader.getSystemResource(resource);
        if (systemResource != null) {
            return systemResource;
        } else {
            try {
                return new File(resource).toURI().toURL();
            } catch (MalformedURLException e) {
            	ErrorNotificafionMgr.doError("ResourceLoader "+resource, e);
                return null;
            }
        }
    }

    private static URL getResourceWith(ClassLoader classLoader, String resource) {
        if (classLoader != null) {
            return classLoader.getResource(resource);
        }
        return null;
    }

}