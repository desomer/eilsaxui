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
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.builder.XMLPartElement;
import com.elisaxui.core.xui.xml.builder.XMLTarget;
import com.elisaxui.core.xui.xml.builder.XMLTarget.ITargetRoot;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart  {
	private static final boolean debug = false;
	
	/**************************************************************************/
	protected HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>> listPart = new HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>>();
	protected HashMap<Object, Object> listProperties = new HashMap<Object, Object>(); 
	
	
	public XMLPart addProperty(Object key, Object value)
	{
		listProperties.put(key, value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Object> E  getProperty(Object key)
	{
		return (E) listProperties.get(key);
	}
	
	public XMLElement getPropertyElement(Object key)
	{
		return (XMLElement)listProperties.get(key);
	}
	
	public XMLAttr getPropertyXID(Object key)
	{
		return (XMLAttr)listProperties.get(key);
	}
	
	@Deprecated
	private final List<Object> children = new ArrayList<>();

	public void addElement(Class<? extends XMLTarget> target, XMLElement value) {
		ArrayList<XMLElement> partData = listPart.get(target);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(target, partData);
		}
		partData.add(value);

	}

	private ArrayList<XMLElement> none = new ArrayList<>();
	public ArrayList<XMLElement> getListElement(Class<? extends XMLTarget> part) {
		ArrayList<XMLElement> list =  listPart.get(part);
		if (list!=null)
			return list;
		else
			return none;
	}

	/**************************************************************/

//	public void doContent(XMLPart root) {
//	}
//
//	public void doRessource(XMLPart root) {
//	}

	/**************************************************************/
	public final void doContent(XMLPart root) {
		
		
		if (debug)
			System.out.println("[XMLPart]--------------- add content of ------------- " + this.getClass() );
				
		XMLFile file = XUIFactoryXHtml.getXHTMLFile();
		boolean isfirstInit = !file.isXMLPartAlreadyInFile(this);
		
		initVar();
		
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
	
	private Field[] getXMLField()
	{
		ArrayList<Field> alf = new ArrayList<Field>(10);
		Class c = this.getClass();
		while (XMLPart.class.isAssignableFrom(c)) {
			Field[] lf = c.getDeclaredFields();
			if (lf!=null)
			{
				for (Field field : lf) {
					alf.add(field);
				}
			}
			c=c.getSuperclass();
		}
		
		Field[] a = new Field[alf.size()];
		alf.toArray(a);
		return a;
	}
	
	public void initVar() {
		Field[] lf = getXMLField();
		if (lf!=null)
		{
			for (Field field : lf) {
				
				boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
				if (!isStatic && JSClass.class.isAssignableFrom(field.getType()))
				{
					if (debug)
						System.out.println("[XMLPart] init var JSClass on " + this.getClass() + " name "+ field.getName() );
					field.setAccessible(true);
					@SuppressWarnings("unchecked")
					JSClass inst = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) field.getType());
					XHTMLPart.jsBuilder.setNameOfProxy("", inst, field.getName());
					try {
						field.set(this, inst);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (!isStatic && XClass.class.isAssignableFrom(field.getType()))
				{
					if (debug)
						System.out.println("[XMLPart] init var CSSClass on " + this.getClass() + " name "+ field.getName() );
					XClass classCss = new XClass();
					String name = field.getName();
					xComment comment = field.getAnnotation(xComment.class);
					if (comment != null) {
						name = comment.value();
					}
					classCss.setId(name);
					field.setAccessible(true);
					try {
						field.set(this,classCss);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				else if (!isStatic && JSVariable.class.isAssignableFrom(field.getType()))
				{
					if (debug)
						System.out.println("[XMLPart] init var JSVariable on " + this.getClass() + " name "+ field.getName() );
					JSVariable variablejs=null;
					try {
						variablejs = (JSVariable) field.getType().newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String name = field.getName();
					xComment comment = field.getAnnotation(xComment.class);
					if (comment != null) {
						name = comment.value();
					}
					variablejs._setName(name);
					field.setAccessible(true);
					try {
						field.set(this,variablejs);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		}
	}


	/**
	 * ajoute les methode avec xTarget
	 * @param method
	 */
	private void initMethod(Method method) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {
				XMLElement elem = ((XMLElement) method.invoke(this, new Object[] {}));
				xPriority priority = method.getAnnotation(xPriority.class);
				if (priority != null) {
					elem.setPriority(priority.value());
				}

				String comment = getComment(method);
				elem.setComment(comment!=null?comment+ " priority "+elem.getPriority() : null );
				
				Class<? extends XMLTarget> targetClass = target.value();
				if (debug)
					System.out.println("[XMLPart] add Target mth "+ this.getClass().getSimpleName() + " # " + method.getName() + " priority " + elem.getPriority() );
				if (elem != null && targetClass!=null ) {
					int nbTab = targetClass.newInstance().getInitialNbTab();
					if (ITargetRoot.class.isAssignableFrom(targetClass))
						XUIFactoryXHtml.getXMLRoot().addElement(targetClass, elem.getXMLElementTabbed(nbTab));
					else
						addElement(targetClass, elem.getXMLElementTabbed(nbTab));
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
		addElement(CONTENT.class, part);
	}

	public final void vAfterContent(XMLElement part) {
		addElement(AFTER_CONTENT.class, part);
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
		return xElement(null, array);
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
	public final static Handle vHandle(String name) {
		Handle attr = XMLBuilder.createHandle(name);
		return attr;
	}

	public final static Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
