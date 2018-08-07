/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.XUILaucher;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class ImportDesc implements IXMLBuilder {
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
	 * @param export
	 *            the export to set
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
	 * @param module
	 *            the module to set
	 */
	public final void setModule(String module) {
		this.module = module;
	}

	public String getURI() {
		long date = XUIFactory.changeMgr.lastOlderFile;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String textdate = formatter.format(new Date(date));
		return textdate + "_" + module;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {
		ModuleDesc module = XUIFactory.getXHTMLFile().getListClassModule().get(getExport());
		// System.out.println(getExport()+" --- "+module.getResourceID());

		if (module == null)
			CoreLogger.getLogger(1).severe("****** Le module " + getExport() + " n'existe pas. Use xExport , xModule(**.Class) et  xImport(FILE.class)");
		else {
			ProxyHandler.getFormatManager().newLine(buf);
			buf.addContentOnTarget(
					"import {" + getExport() + "} from '" + XUILaucher.PATH_ASSET + "/mjs/" + module.getURI() + "';");
		}

		return buf;
	}

}
