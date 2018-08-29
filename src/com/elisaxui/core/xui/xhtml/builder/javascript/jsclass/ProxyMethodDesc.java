/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Method;

import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;

/**
 * identifie un appel appel a une meth js 
 * utiliser dans les getListHandleFuntionPrivate
 * @author gauth
 *
 */
public final class ProxyMethodDesc
{
	public ProxyMethodDesc(String idMeth, JSClassBuilder implcl, Object proxy, Method method, Object[] args) {
		super();
		this.implcl = implcl;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.idMeth = idMeth;
	}
	
	String idMeth;
	JSClassBuilder implcl;
	Object proxy; 
	Method method;
	Object[] args;
	
	JSContent content;
	public int lineNoInsered = -1;
	public Object mthNoInserted = null;
	
	/**
	 * @return the proxy
	 */
	public final Object getProxy() {
		return proxy;
	}
	

	/**
	 * @param content
	 */
	public ProxyMethodDesc(JSContent content) {
		super();
		this.content = content;
	}
	
}