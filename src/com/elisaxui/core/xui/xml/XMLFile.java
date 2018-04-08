package com.elisaxui.core.xui.xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.elisaxui.core.xui.config.ConfigFormat;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapSingletonResource = new HashMap<Class<?>, Class<?>>();   
	public LinkedList<Object> listTreeXMLParent = new LinkedList<Object>();   // pour recherche de vProperty
	private ConfigFormat configMgr = new ConfigFormat();
	private XMLPart root;
	public HashMap<String, XMLFile> listSubFile = new HashMap<>();
	

	public boolean isXMLPartAlreadyInFile(XMLPart part) {
		return mapSingletonResource.put(part.getClass(), part.getClass()) != null;
	}

	public ConfigFormat getConfigMgr() {
		return configMgr;
	}

	public XMLPart getRoot() {
		return root;
	}

	public void setRoot(XMLPart root) {
		this.root = root;
	}

}
