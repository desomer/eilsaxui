/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module;

/**
 * @author gauth
 *
 */
public class ImportDesc {
	String export;	
	String module;	
	
	/**
	 * @param export
	 * @param module
	 */
	public ImportDesc(String export, String module) {
		super();
		this.export = export;
		this.module = module;
	}

	/**
	 * @return the export
	 */
	public final String getExport() {
		return export;
	}

	/**
	 * @param export the export to set
	 */
	public final void setExport(String export) {
		this.export = export;
	}

	/**
	 * @return the module
	 */
	public final String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public final void setModule(String module) {
		this.module = module;
	}
	
	

}
