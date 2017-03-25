package com.elisaxui.core.xui.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;

public class XMLFile {

	private HashMap<Class<?>, Class<?>> mapResource  = new HashMap<Class<?>, Class<?>>();
//	public HashMap<Class<?>, Class<?>> getMapResource() {
//		return mapResource;
//	}
	public LinkedList<Object> listParent = new LinkedList<Object>();
	
	
	public boolean isXMLPartAlreadyInFile(XMLPart part)
	{
		return mapResource.put(part.getClass(), part.getClass())!=null;
	}
	
	private XMLPart root;

	public XMLPart getRoot() {
		return root;
	}

	public void setRoot(XMLPart root) {
		this.root = root;
	} 
	
}
