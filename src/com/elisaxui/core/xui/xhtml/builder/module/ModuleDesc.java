/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module;

import java.util.ArrayList;

import com.elisaxui.core.xui.xml.target.XMLTarget;

/**
 * @author gauth
 *
 */
public class ModuleDesc {
	String resourceID;
	String type;
	XMLTarget target;
	ArrayList<ImportDesc> listImport;
	
	/**
	 * @return the resourceID
	 */
	public final String getResourceID() {
		return resourceID;
	}
	/**
	 * @param resourceID the resourceID to set
	 */
	public final void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the listImport
	 */
	public final ArrayList<ImportDesc> getListImport() {
		return listImport;
	}
	/**
	 * @param listImport the listImport to set
	 */
	public final void setListImport(ArrayList<ImportDesc> listImport) {
		this.listImport = listImport;
	}
	/**
	 * @return the target
	 */
	public final XMLTarget getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public final void setTarget(XMLTarget target) {
		this.target = target;
	}
	
	
}
