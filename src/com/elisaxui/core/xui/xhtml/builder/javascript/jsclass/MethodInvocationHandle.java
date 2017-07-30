/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import java.lang.reflect.Method;

public final class MethodInvocationHandle
{
	public MethodInvocationHandle(JSClassImpl implcl, Object proxy, Method method, Object[] args) {
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