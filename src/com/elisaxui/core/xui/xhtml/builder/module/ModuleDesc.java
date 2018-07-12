/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImportList;
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

	public String getExtension() {
		return resourceID.substring(resourceID.indexOf(".") + 1);
	}

	public boolean isResourceCss() {
		return getExtension().equals("css");
	}

	public String getURI() {
		if (XUIFactoryXHtml.getXMLFile().getConfigMgr().getData().isDateFileName())
		{
			long date = XUIFactory.changeMgr.lastOlderFile;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-hhmmss");
			String textdate = formatter.format(new Date(date));
			return textdate + "_" + resourceID;
		}
		else
			return resourceID;
	}

	/**
	 * @param resourceID
	 *            the resourceID to set
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
	 * @param type
	 *            the type to set
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

	public boolean isES6Module() {
		return getListImport() != null;
	}

	/**
	 * @param listImport
	 *            the listImport to set
	 */
	public final void setListImport(ArrayList<ImportDesc> listImport) {
		this.listImport = listImport;
	}

	/**
	 * @return the targetAction
	 */
	public final XMLTarget getTarget() {
		return target;
	}

	/**
	 * @param targetAction
	 *            the targetAction to set
	 */
	public final void setTarget(XMLTarget target) {
		this.target = target;
	}

	public void initES6mport(xImport anImport, xImportList listImport) {
		if (anImport != null) {
			ArrayList<ImportDesc> listImportStr = new ArrayList<>();
			addES6Import(anImport, listImportStr);
			this.setListImport(listImportStr);
		} else if (listImport != null) {

			ArrayList<ImportDesc> listImportStr = new ArrayList<>();
			for (xImport aImport : listImport.value()) {
				addES6Import(aImport, listImportStr);
			}
			this.setListImport(listImportStr);
		}
	}

	/**
	 * @param anImport
	 * @param listImportStr
	 */
	private void addES6Import(xImport anImport, ArrayList<ImportDesc> listImportStr) {
		String e = anImport.export();
		String m = anImport.module();
		Class<?> idc = anImport.idClass();
		
		if (idc!=Object.class)
		{
			e = idc.getSimpleName();
		}
		
		listImportStr.add(new ImportDesc(e, m));
	}
}
