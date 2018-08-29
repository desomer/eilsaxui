/**
 * 
 */
package com.elisaxui.app.core.admin;

import java.util.ArrayList;

import com.elisaxui.core.xui.app.XHTMLAppScanner;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * @author gauth
 *
 */
@xResource(id = "ScnListPage")
public class ScnListPage extends XHTMLPart {

	@xTarget(CONTENT.class) // la vue App Shell
	public XMLElement xAppShell() {
		
		ArrayList list = new ArrayList();
		
		ArrayList<String> listUrl = XHTMLAppScanner.getListurl();
		
		for (String string : listUrl) {
			list.add(xLi(xA(xAttr("href", string), string.substring(string.lastIndexOf('/')))));
		}
		
		return xUl(list);
	}
}
