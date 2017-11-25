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
	
	JSContent content;
	public int currentLine = -1;
	public Object currentMthNoInserted = null;
	
	/**
	 * @return the currentLine
	 */
	public int getCurrentLine() {
		return currentLine;
	}

	/**
	 * @param currentLine the currentLine to set
	 */
	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}

	/**
	 * @return the currentMthNoInserted
	 */
	public Object getCurrentMthNoInserted() {
		return currentMthNoInserted;
	}

	/**
	 * @param currentMthNoInserted the currentMthNoInserted to set
	 */
	public void setCurrentMthNoInserted(String currentMthNoInserted) {
		this.currentMthNoInserted = currentMthNoInserted;
	}

	/**
	 * @param content
	 */
	public MethodDesc(JSContent content) {
		super();
		this.content = content;
	}
	
}