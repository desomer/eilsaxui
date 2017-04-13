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
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClass;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xRessource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Part;
import com.elisaxui.core.xui.xml.builder.XMLTarget;
import com.elisaxui.core.xui.xml.builder.XMLTarget.ITargetRoot;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart {

	public static final class CONTENT extends XMLTarget {
	};

	public static final class AFTER_CONTENT extends XMLTarget {
		@Override
		public int getInitialNbTab() {
			return 1;
		}
		
	};

	protected HashMap<Class<? extends XMLTarget>, ArrayList<Element>> listPart = new HashMap<Class<? extends XMLTarget>, ArrayList<Element>>();
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
	
	public Element getPropertyElement(Object key)
	{
		return (Element)listProperties.get(key);
	}
	
	@Deprecated
	private final List<Object> children = new ArrayList<>();

	public void addElement(Class<? extends XMLTarget> target, Element value) {
		ArrayList<Element> partData = listPart.get(target);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(target, partData);
		}
		partData.add(value);

	}

	private ArrayList<Element> none = new ArrayList<>();
	public ArrayList<Element> getListElement(Class<? extends XMLTarget> part) {
		ArrayList<Element> list =  listPart.get(part);
		if (list!=null)
			return list;
		else
			return none;
	}

	/**************************************************************/

	public void doContent(XMLPart root) {
	}

	public void doRessource(XMLPart root) {
	}

	/**************************************************************/
	public final void initContent(XMLPart root) {
		
		Field[] lf = this.getClass().getDeclaredFields();
		if (lf!=null)
		{
			for (Field field : lf) {
				if (JSClass.class.isAssignableFrom(field.getType()))
				{
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
			}
		}
		
		doContent(root);
		
		XMLFile file = XUIFactoryXHtml.getXMLFile();
		boolean isfirstInit = !file.isXMLPartAlreadyInFile(this);
		Method[] listMth = this.getClass().getDeclaredMethods();
		for (Method method : listMth) {
			boolean isResource = method.getAnnotation(xRessource.class)!=null;
			if (isfirstInit || !isResource)
			{
				initMethod(method);
			}
		}
		
		// https://github.com/paul-hammant/paranamer
//		Class<?>[] listClass = this.getClass().getDeclaredClasses();
//		for (Class<?> class1 : listClass) {
//			//if (JSClass.class.isAssignableFrom(class1))
//			//{
//				System.out.println("cls="+class1.getSimpleName());
//				Field[] fields = class1.getDeclaredFields();
//				for (Field field : fields) {
//					System.out.println("cls="+class1.getSimpleName());
//				}
//				
//				Method[] mths = class1.getDeclaredMethods();
//				for (Method mth : mths) {
//					System.out.println("mth="+mth.getName());
//					Parameter[] params = mth.getParameters();
//					for (Parameter parameter : params) {
//						System.out.println("param="+parameter.getName());
//					}
//				}
//				
//			//}
//		}
		

				
				
		//initComment();
	}

	private void initMethod(Method method) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {

				Element elem = ((Element) method.invoke(this, new Object[] {}));
				elem.setComment(getComment(method));
				Class<? extends XMLTarget> targetClass = target.value();
				//System.out.println(this.getClass().getSimpleName() + "#" + method.getName() );
				if (elem != null && targetClass!=null ) {
					int nbTab = targetClass.newInstance().getInitialNbTab();
					if (ITargetRoot.class.isAssignableFrom(targetClass))
						XUIFactoryXHtml.getXMLRoot().addElement(targetClass, elem.setNbInitialTab(nbTab));
					else
						addElement(targetClass, elem.setNbInitialTab(nbTab));
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

	public final void vContent(Element part) {
		addElement(CONTENT.class, part);
	}

	public final void vAfterContent(Element part) {
		addElement(AFTER_CONTENT.class, part);
	}

	public final Part xPart(XMLPart part, Object... inner) {
		return XMLBuilder.createPart(part, inner);
	}
	
	public final static Element xElement(String name, Object... inner) {
		Element tag = XMLBuilder.createElement(name, inner);
		return tag;
	}

	public final static Element xListElement(Object... array) {
		return xElement(null, array);
	}

	public final static Attr xAttr(String name, Object value) {
		Attr attr = XMLBuilder.createAttr(name, value);
		return attr;
	}
	
	public final Handle vHandle(String name) {
		Handle attr = XMLBuilder.createHandle(name);
		return attr;
	}

	public final Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
