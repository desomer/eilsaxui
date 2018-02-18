/**
 * 
 */
package com.elisaxui.component.toolkit;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSPromise;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSString;
import com.elisaxui.core.xui.xml.annotation.xStatic;

/**
 * @author gauth
 *
 */
public interface TKCom extends JSClass {

	@xStatic
	default JSPromise requestUrl(JSString url)
	{
		JSObject obj = let(JSObject.class,  "obj",  var("{'url': ",url,"}") );

		JSFunction fct = callback(var("resolve"), var("reject"), ()->{
			
			JSObject xhr = let(JSObject.class, "xhr", "new XMLHttpRequest()");
			xhr.call("open", var(obj,".method || 'GET',",obj,".url"));
			xhr.attr("onload").set(callback(()->{
				_if(xhr.attr("status"), ">=200 &&", xhr.attr("status") ,"<300").then(() -> {
					__("resolve(JSON.parse(", xhr.attr("response"), "))");
				})._else(()->{
					__("reject(", xhr.attr("statusText"), ")");
				});
			}));
			
			xhr.call("send", obj.attr("body"));
			
		});
		
		JSPromise promise = let(JSPromise.class, "promise", "new Promise("+fct+")");
		return promise;
	}
	
	/*
	
	let request = obj => {
	    return new Promise((resolve, reject) => {
	        let xhr = new XMLHttpRequest();
	        xhr.open(obj.method || "GET", obj.url);
	        if (obj.headers) {
	            Object.keys(obj.headers).forEach(key => {
	                xhr.setRequestHeader(key, obj.headers[key]);
	            });
	        }
	        xhr.onload = () => {
	            if (xhr.status >= 200 && xhr.status < 300) {
	                resolve(xhr.response);
	            } else {
	                reject(xhr.statusText);
	            }
	        };
	        xhr.onerror = () => reject(xhr.statusText);
	        xhr.send(obj.body);
	    });
	};
	*/
}
