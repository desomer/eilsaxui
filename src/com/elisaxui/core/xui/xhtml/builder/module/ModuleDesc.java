/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.xml.annotation.xImport;
import com.elisaxui.core.xui.xml.annotation.xImportList;
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
	
	public String getExtension()
	{
		return resourceID.substring(resourceID.indexOf(".")+1);
	}
	
	public boolean isResourceCss()
	{
		return getExtension().equals("css");
	}
	
	public String getURI()
	{
		long date = XUIFactory.changeMgr.lastOlderFile;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String textdate = formatter.format(new Date(date));
		return textdate+"_"+resourceID;
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
	
	public boolean isES6Module()
	{
		return getListImport()!=null;
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
	
	public void initES6mport(xImportList listImport) {
		if (listImport==null)
			return;
		
		ArrayList<ImportDesc> listImportStr = new ArrayList<>();
		for (xImport aImport : listImport.value()) {
			listImportStr.add(new ImportDesc(aImport.export(),  aImport.module()));
		}
		this.setListImport(listImportStr);
	}
}
