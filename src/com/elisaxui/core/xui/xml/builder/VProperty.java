/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

/**
 * @author gauth
 *
 */
public class VProperty  {

	String name;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public VProperty(String name) {
		super();
		this.name = name;
	}
	
}
