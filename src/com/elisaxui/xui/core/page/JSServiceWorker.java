/**
 * 
 */
package com.elisaxui.xui.core.page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.elisaxui.core.xui.xhtml.XHTMLAppBuilder;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSMethodInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author Bureau
 *
 */
public final class JSServiceWorker extends XHTMLPart {
	@xTarget(CONTENT.class)
	public XMLElement doJS()
	{
		
		JSon response = new JSon()._setName("response");
		
		JSMethodInterface fctFetch = fct(response)
				 // Check if we received a valid response
	            ._if("!response || response.status !== 200 || response.type !== 'basic'") 
	            //	.consoleDebug(txt("************ ret error from net"), response)
	               ._return(response)  // mauvaise reponse
	            .endif()
	            
	            // mise en cache
	            .var("responseToCache" , "response.clone()")
	            .__("caches.open(CACHE_NAME).then(", fct("cache")
	            		.consoleDebug(txt("add cache"), "cache")
	            		.__("cache.put(event.request, responseToCache)"),")")
	            
	            .consoleDebug(txt("************ ret 200 from net "), response.get("url"))
	            ._return("response");
				;
		
		JSMethodInterface fctCache = fct(response)
				._if(response)
					.consoleDebug(txt("hit cache "), response.get("url"))
			        ._return(response)  // Cache hit - return response
			    .endif()
			    
			    .var("fetchRequest","event.request.clone()")
			    .consoleDebug(txt("******** fetch "), "event")
			        // , mode: 'no-cors'  , { credentials: 'include' }
			    ._return("fetch(fetchRequest).then(", fctFetch ,")")  // 
			    
				;
		  
		JSMethodInterface fctPostMessage = fct("clients")
				._if("clients && clients.length")
					.__("clients[0].postMessage(event.request.url)")
				.endif();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

		LocalDateTime dateBuild = XHTMLAppBuilder.dateBuild;
		
		String formatDateTimeBuild =   dateBuild.format(formatter);
		
		return xListElement(js()
				.var("CACHE_NAME", txt("site-cache-"+formatDateTimeBuild))
				
				.__("self.addEventListener('install',", fct("event")
						
						.__("event.waitUntil(caches.open(CACHE_NAME).then(", fct("cache")
								._return("cache.addAll(['https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'])") ,"))")							
						
						.consoleDebug(txt("************* INSTALL ****************"))
						
						
						,")" )
						
				
				.__("this.addEventListener('activate',", fct("event")
						.__("event.waitUntil(caches.keys().then(", fct("keyList") 
									._return("Promise.all(keyList.map(", fct("key")
											//.consoleDebug(txt("**** test cache "), "key")
											._if("key!=CACHE_NAME")
												//.consoleDebug(txt("**** delete cache "), "key")
												._return("caches.delete(key)")
											.endif()
										,"))")
								,"))")
						, ")")
									
				.__("self.addEventListener('fetch',", fct("event") 
						.consoleDebug(txt("******** event ****"), "event.request.url")
						
						.__("self.clients.matchAll(/* search options */).then(", fctPostMessage,"  )")
						
						.__("event.respondWith(caches.match(event.request).then(", fctCache  ,") )")
						
				,")")
				);				
	}
}