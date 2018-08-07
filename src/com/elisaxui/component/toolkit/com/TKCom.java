/**
 * 
 */
package com.elisaxui.component.toolkit.com;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.annotation.xStatic;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSObject;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSon;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.es6.JSPromise;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.json.JSType;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xExport;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;

/**
 * @author gauth
 *
 */
@xExport
@xCoreVersion("1")
public interface TKCom extends JSClass {
	
	JSon jsonUrl = JSClass.declareType();
	JSObject xhr = JSClass.declareType();
	
	@xStatic
	default JSPromise requestUrl(JSString url)
	{
		let(jsonUrl,  var("{'url': ",url,"}") );

		JSFunction fct = fct(var("resolve"), var("reject"), ()->{
			let(xhr, "new XMLHttpRequest()");
			xhr.callMth("open", var(jsonUrl,".method || 'GET',", jsonUrl ,".url"));
			xhr.attr("onload").set(fct(()->{
				_if(xhr.attr("status"), ">=200 &&", xhr.attr("status") ,"<300").then(() -> {
					__("resolve(JSON.parse(", xhr.attr("response"), "))");
				})._else(()->{
					__("reject(", xhr.attr("statusText"), ")");
				});
			}));
			
			xhr.callMth("send", jsonUrl.attr("body"));
			
		});
		
		return cast(JSPromise.class, var("new Promise(", fct ,")"));
	}
	
	@xStatic
	default JSPromise postUrl(JSString url, JSElement data)
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
	
	
	interface TCallURIInfo extends JSType
	{
		JSString url();
		JSString method();
		JSString body();
		JSString mode();
		JSString redirect();
		JSon header();
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
