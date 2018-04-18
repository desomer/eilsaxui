/**
 * 
 */
package com.elisaxui.component.toolkit.com;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.es6.JSPromise;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
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

		JSFunction fct = fct(var("resolve"), var("reject"), ()->{
			
			JSObject xhr = let(JSObject.class, "xhr", "new XMLHttpRequest()");
			xhr.callMth("open", var(obj,".method || 'GET',",obj,".url"));
			xhr.attr("onload").set(fct(()->{
				_if(xhr.attr("status"), ">=200 &&", xhr.attr("status") ,"<300")._then(() -> {
					__("resolve(JSON.parse(", xhr.attr("response"), "))");
				})._else(()->{
					__("reject(", xhr.attr("statusText"), ")");
				});
			}));
			
			xhr.callMth("send", obj.attr("body"));
			
		});
		
		return cast(JSPromise.class, "new Promise("+fct+")");
	}
	
	@xStatic
	default JSPromise postUrl(JSString url, JSon data)
	{
		JSon param = let(JSon.class, "param", "{}");
		param.attr("method").set("'post'");
		param.attr("mode").set("'cors'");   // same origin
		param.attr("redirect").set("'follow'");
		param.attr("body").set("JSON.stringify(", data ,")");
		param.attr("headers").set("{'Accept': 'application/json', 'Content-Type': 'application/json'}");
		
		JSAny request = let(JSAny.class, "request", "new Request(",url,",", param, ")");
		
		return cast(JSPromise.class, var("fetch(",request,")") );
	}
	

	/*
	 
	 const api = function(method, url, data, headers = {}){
		  return fetch(url, {
		    method: method.toUpperCase(),
		    body: JSON.stringify(data),  // send it as stringified json
		    credentials: api.credentials,  // to keep the session on the request
		    headers: Object.assign({}, api.headers, headers)  // extend the headers
		  }).then(res => res.ok ? res.json() : Promise.reject(res));
		};
		
		// Defaults that can be globally overwritten
		api.credentials = 'include';
		api.headers = {
		  'csrf-token': window.csrf || '',    // only if globally set, otherwise ignored
		  'Accept': 'application/json',       // receive json
		  'Content-Type': 'application/json'  // send json
		};
		
		// Convenient methods
		['get', 'post', 'put', 'delete'].forEach(method => {
		  api[method] = api.bind(null, method);
		});
			 
	 */
	
	/*
	 $('.like').on('click', async e => {
  const id = 123;  // Get it however it is better suited

  await api.put(`/like/${id}`, { like: true });

  // Whatever:
  $(e.target).addClass('active dislike').removeClass('like');
});
	 */
	
	
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
