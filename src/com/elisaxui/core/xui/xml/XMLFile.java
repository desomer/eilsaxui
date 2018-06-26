package com.elisaxui.core.xui.xml;

import java.util.HashMap;
import java.util.LinkedList;

import com.elisaxui.core.xui.config.ConfigFormat;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapSingletonResource = new HashMap<>();   
	private ConfigFormat configMgr = new ConfigFormat();
	
	/**   la XMLPart principale  (la scene) **/
	private XMLPart mainXMLPart;
	private String coreVersion = "1";
	
	public LinkedList<Object> listTreeXMLParent = new LinkedList<>();   // pour recherche de vProperty
	public HashMap<String, XMLFile> listSubFile = new HashMap<>();

	private String id;
	private String extension;
	
	/**
	 * @return the extension
	 */
	public final String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public final void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the name
	 */
	public final String getID() {
		return id;
	}

	/**
	 * @param name the name to set
	 */
	public final void setID(String name) {
		this.id = name;
	}
	
	/*****************************************************************************/
	/**
	 * @return the coreVersion
	 */
	public final String getCoreVersion() {
		return coreVersion;
	}

	/**
	 * @param coreVersion the coreVersion to set
	 */
	public final void setCoreVersion(String coreVersion) {
		this.coreVersion = coreVersion;
	}
	
	public boolean isXMLPartAlreadyInFile(XMLPart part) {
		return mapSingletonResource.put(part.getClass(), part.getClass()) != null;
	}

	public ConfigFormat getConfigMgr() {
		return configMgr;
	}

	/**
	 * @return the scene
	 */
	public final XMLPart getMainXMLPart() {
		return mainXMLPart;
	}

	/**
	 * @param scene the scene to set
	 */
	public final void setMainXMLPart(XMLPart scene) {
		this.mainXMLPart = scene;
	}

}
