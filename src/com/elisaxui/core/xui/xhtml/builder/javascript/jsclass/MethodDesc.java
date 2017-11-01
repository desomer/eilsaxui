/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Method;

/**
 * identifie un appel appel a une meth js 
 * utiliser dans les getListHandleFuntionPrivate
 * @author gauth
 *
 */
public final class MethodDesc
{
	public MethodDesc(JSClassImpl implcl, Object proxy, Method method, Object[] args) {
		super();
		this.implcl = implcl;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}
	
	JSClassImpl implcl;
	Object proxy; 
	Method method;
	Object[] args;
}