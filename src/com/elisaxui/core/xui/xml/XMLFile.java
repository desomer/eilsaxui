package com.elisaxui.core.xui.xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.elisaxui.core.xui.config.ConfigMgr;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapResource = new HashMap<Class<?>, Class<?>>();
	public LinkedList<Object> listParent = new LinkedList<Object>();
	private ConfigMgr configMgr = new ConfigMgr();



	public boolean isXMLPartAlreadyInFile(XMLPart part) {
		return mapResource.put(part.getClass(), part.getClass()) != null;
	}

	public ConfigMgr getConfigMgr() {
		return configMgr;
	}

	private XMLPart root;

	public XMLPart getRoot() {
		return root;
	}

	public void setRoot(XMLPart root) {
		this.root = root;
	}

}
