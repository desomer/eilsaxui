package com.elisaxui.core.xui.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLTarget.ITargetRoot;
import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;
import com.elisaxui.core.xui.xml.XMLBuilder.Part;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xTarget;

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
	private final XMLBuilder xmlBuilder = new XMLBuilder("main", null, null);
	
	
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

	public ArrayList<Element> getListElement(Class<? extends XMLTarget> part) {
		return listPart.get(part);
	}

	/**************************************************************/

	public void doContent(XMLPart root) {
	}

	public void doRessource(XMLPart root) {
	}

	/**************************************************************/
	public final void initContent(XMLPart root) {
		doContent(root);

		Method[] listMth = this.getClass().getDeclaredMethods();
		for (Method method : listMth) {
			initAnnotation(method);
		}

		initComment();
	}

	private void initAnnotation(Method method) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {
				Element elem = ((Element) method.invoke(this, new Object[] {}));
				Class<? extends XMLTarget> targetClass = target.value();
		
				if (elem != null && targetClass!=null) {
					int nbTab = targetClass.newInstance().getInitialNbTab();
					if (ITargetRoot.class.isAssignableFrom(targetClass))
						XUIFactoryXHtml.getXMLRoot().addElement(targetClass, elem.setNbInitialTab(nbTab));
					else
						addElement(targetClass, elem.setNbInitialTab(nbTab));
				}
			} 
			catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**************************************************************/
	private String getComment() {
		xComment comment = this.getClass().getAnnotation(xComment.class);
		if (comment != null) {
			String v = comment.value();
			return (v == null ? "" : v) + " [" + this.getClass().getSimpleName() + "]";
		}
		return null;
	}

	private void initComment() {
		String comment = getComment();
		if (comment != null) {
			for (Element elem : getListElement(CONTENT.class)) {
				if (elem != null) {
					elem.setComment(comment);
				}
			}

			for (Element elem : getListElement(AFTER_CONTENT.class)) {
				if (elem != null) {
					elem.setComment("after " + comment);
				}
			}
		}
	}

	/**************************************************************/
	@Deprecated  
	/**
	 * gerer par properties
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
		return xmlBuilder.createPart(part, inner);
	}

	public final Element xElement(String name, Object... inner) {
		Element tag = xmlBuilder.createElement(name, inner);
		return tag;
	}

	public final Element xListElement(Object... array) {
		return xElement(null, array);
	}

	public final Attr xAttr(String name, Object value) {
		Attr attr = xmlBuilder.createAttr(name, value);
		return attr;
	}

	public final Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
