package com.elisaxui.core.xui.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.application.XHTMLAppScanner;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.target.XMLTarget;
import com.elisaxui.core.xui.xml.target.XMLTarget.ITargetRoot;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart  {
	/**
	 * 
	 */
	public static final double PRECISION = 100000.0;
	public static final String NONAME = null;

	private static final boolean debug = false;
	
	/**************************************************************************/
	protected HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>> listPart = new HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>>();
	protected HashMap<Object, Object> listProperties = new HashMap<Object, Object>(); 
	@Deprecated
	private final List<Object> children = new ArrayList<>();
	
	public static final String PROP_ID = "PROP_ID";
	/**
	 * @return the propertiesPrefix
	 */
	public final String getPropertiesPrefix() {
		return vProperty(PROP_ID);
	}


	/**************************************************************************/
	public XMLPart vProperty(Object key, Object value)
	{
		listProperties.put(key.toString(), value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public XMLPart vProperties(Object key, Object value)
	{
		Object obj = listProperties.get(key.toString());
		if (obj!=null)
		{
			if (obj instanceof List)
			{
				((List<Object>)obj).add(value);
			}
			else
			{
				List<Object> l = new ArrayList<>();
				l.add(obj);
				listProperties.put(key.toString(), l);
			}
		}
		else
			listProperties.put(key.toString(), value);
		return this;
	}
	
	
	@SuppressWarnings("unchecked")
	public <E extends Object> E  vProperty(Object key)
	{
		return (E) listProperties.get(key.toString());
	}
	
	public XMLElement vPropertyElement(Object key)
	{
		return (XMLElement)listProperties.get(key.toString());
	}
	
	public XMLAttr vPropertyAttr(Object key)
	{
		return (XMLAttr)listProperties.get(key.toString());
	}
	
	/*********************************************************************************/
	public final void addElementOnTarget(Class<? extends XMLTarget> target, XMLElement value) {
		ArrayList<XMLElement> partData = listPart.get(target);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(target, partData);
		}
		partData.add(value);

	}

	private ArrayList<XMLElement> none = new ArrayList<>();
	public List<XMLElement> getListElementFromTarget(Class<? extends XMLTarget> target) {
		ArrayList<XMLElement> list =  listPart.get(target);
		if (list!=null)
			return list;
		else
			return none;
	}

	/**************************************************************/

	/**************************************************************/
	public final void doContent(XMLPart root) {
		
		if (debug)
			System.out.println("[XMLPart]--------------- add content of ------------- " + this.getClass() );
				
		XMLFile file = XUIFactoryXHtml.getXHTMLFile();
		boolean isfirstInit = !file.isXMLPartAlreadyInFile(this);
		
		XHTMLAppScanner.initVar(false, this.getClass(), this);
		
		Method[] listMth = getXMLMethod();
		for (Method method : listMth) {
			boolean isResource = method.getAnnotation(xRessource.class)!=null;
			// execute les methode target non ressource
			if (isfirstInit || !isResource)
			{
				initMethod(method);
			}
		}
		
	}
	
	
	private Method[] getXMLMethod()
	{
		ArrayList<Method> alf = new ArrayList<Method>(10);
	//	Map<String, Method> dicoMeth = 
		Class<?> c = this.getClass();
		while (XMLPart.class.isAssignableFrom(c) && c!= XHTMLPart.class && c!= XMLPart.class) {
			Method[] lf = c.getDeclaredMethods();
			if (lf!=null)
			{
				for (Method field : lf) {
					alf.add(field);
				}
			}
			c=c.getSuperclass();
		}
		
		Method[] a = new Method[alf.size()];
		alf.toArray(a);
		return a;
	}
	



	/**
	 * ajoute les methode avec xTarget
	 * @param method
	 */
	private void initMethod(Method method) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {
				method.setAccessible(true);
				XMLElement elem = ((XMLElement) method.invoke(this, new Object[] {}));
				if (elem== null) return;
				
				// ajoute le hashcode du nom pour avoir toujours le même tri sur une priorite identique
				int h = method.getName().hashCode();
				double p = (double)(h)/PRECISION;
				int d = (int)p;
				p=(p-d);
				
				xPriority priority = method.getAnnotation(xPriority.class);
				if (priority != null) {
					p = priority.value()+p;
					elem.setPriority(p);
				}
				else
					elem.setPriority(100.0+p);

				String comment = getComment(method);
				elem.setComment(comment!=null?comment+ " priority "+((int)(elem.getPriority())) : null );
				
				Class<? extends XMLTarget> targetClass = target.value();
				if (debug)
					System.out.println("[XMLPart] add Target mth "+ this.getClass().getSimpleName() + " # " + method.getName() + " priority " + elem.getPriority() );
				if (elem != null && targetClass!=null ) {
					int nbTab = targetClass.newInstance().getInitialNbTab();
					if (ITargetRoot.class.isAssignableFrom(targetClass))
						XUIFactoryXHtml.getXMLRoot().addElementOnTarget(targetClass, elem.getXMLElementTabbed(nbTab));
					else
						addElementOnTarget(targetClass, elem.getXMLElementTabbed(nbTab));
				}
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				ErrorNotificafionMgr.doError("pb initMethod "+method.getName(), e);
			}
		}
	}

	/**************************************************************/
	private String getComment(Method mth) {
		xComment comment = this.getClass().getAnnotation(xComment.class);
		if (comment != null) {
			String v = comment.value();
			return (v == null ? "" : v) + " [" + this.getClass().getSimpleName()+"."+ mth.getName() +"]";
		}
		return null;
	}

//	private void initComment() {
//		String comment = getComment();
//		if (comment != null) {
//			
//			for (Entry<Class<? extends XMLTarget>, ArrayList<Element>> entryListElem : listPart.entrySet()) {
//				entryListElem.getKey();
//				/**todo get prefixe block */
//				for (ArrayList<Element> listElem : listPart.values()) {
//					for (Element elem : listElem) {
//						if (elem != null) {
//							elem.setComment(comment);
//						}
//					}
//				}
//			}
//		}
//	}

	/**************************************************************/
	/**
	 * gerer par properties ?????????????
	 * @return
	 */
	public final List<Object> getChildren() {
		return children;
	}

	public final void vContent(XMLElement part) {
		addElementOnTarget(CONTENT.class, part);
	}

	public final void vAfterContent(XMLElement part) {
		addElementOnTarget(AFTER_CONTENT.class, part);
	}


	/**
	 * xPart( part, children) 
	 * @param part
	 * @param inner
	 * @return
	 */
	public final static XMLElement xPart(XMLPart part, Object... inner) {
		return xElement(null, XMLBuilder.createPart(part, inner));
	}
	
	public final static XMLElement xElement(String name, Object... inner) {
		XMLElement tag = XMLBuilder.createElement(name, inner);
		return tag;
	}

	public final static XMLElement xListElement(Object... array) {
		return xElement(NONAME, array);
	}

	public final static XMLAttr xAttr(String name, Object value) {
		XMLAttr attr = XMLBuilder.createAttr(name, value);
		return attr;
	}
	
	public final static XMLAttr xAttr(String name) {
		XMLAttr attr = XMLBuilder.createAttr(name, null);
		return attr;
	}
	
	/**
	 * ajout d'enfant par un .addProperty(name, xxxx) 
	 * @param name
	 * @return
	 */
	@Deprecated
	public static final Handle vSearchProperty(String name) {
		Handle attr = XMLBuilder.createHandle(name);
		return attr;
	}
	
	@Deprecated
	public static final Handle vIfExistProperty(String iff, Object then) {
		Handle attr = XMLBuilder.createHandle(iff);
		attr.setIfExistAdd(then);
		return attr;
	}

	public static final Handle vSearch(VProperty name) {
		Handle attr = XMLBuilder.createHandle(name.getName());
		return attr;
	}
	
	public static final Handle vIfExist(VProperty iff, Object then) {
		Handle attr = XMLBuilder.createHandle(iff.getName());
		attr.setIfExistAdd(then);
		return attr;
	}
	
	public final static Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
