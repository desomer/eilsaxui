package com.elisaxui.core.xui.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.elisaxui.core.xui.xhtml.XUIPageXHtml.HtmlPart;
import com.elisaxui.core.xui.xml.XMLBuilder.Attr;
import com.elisaxui.core.xui.xml.XMLBuilder.Part;
import com.elisaxui.core.xui.xml.annotation.AfterContent;
import com.elisaxui.core.xui.xml.annotation.Comment;
import com.elisaxui.core.xui.xml.annotation.Content;
import com.elisaxui.core.xui.xml.XMLBuilder.Element;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart {
		
	public enum XmlTarget implements ITarget {
		CONTENT, AFTER_CONTENT
	}
	protected HashMap<ITarget, ArrayList<Element>> listPart = new HashMap<ITarget, ArrayList<Element>>();
	
	private final XMLBuilder xmlBuilder = new XMLBuilder("main", null, null);
	private final List<Object> children = new ArrayList<>();
	
	public void addPart(ITarget part, Element value) {
		ArrayList<Element> partData = listPart.get(part);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(part, partData);
		}
		partData.add(value);

	}
	protected ArrayList<Element> getPart(ITarget part) {
		return  listPart.get(part);
	}
	
	
	protected Element getFirstPart(ITarget part) {
		ArrayList<Element> lp =  listPart.get(part);
		if (lp!=null && lp.size()>0)
			return lp.get(0);
		else
			return null;
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
		if (method.getAnnotation(Content.class)!=null)
		{
			try {
				vContent((Element)method.invoke(this, null));
			} catch (IllegalAccessException e) {
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
		
		if (method.getAnnotation(AfterContent.class)!=null)
		{
			try {
				vAfterContent((Element)method.invoke(this, null));
			} catch (IllegalAccessException e) {
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
		Comment comment = this.getClass().getAnnotation(Comment.class);
		if (comment != null) {
			String v = comment.value();
			return (v == null ? "" : v) + " [" + this.getClass().getSimpleName() + "]";
		}
		return null;
	}

	private void initComment() {
		String comment = getComment();
		if (comment != null) {
			if (this.getContent() != null) {
				this.getContent().setComment(comment);
			}
			if (this.getAfter() != null) {
				this.getAfter().setComment("after " + comment);
			}
		}
	}

	/**************************************************************/
	@Deprecated
	public final Element getContent() {
		return getFirstPart(XmlTarget.CONTENT);
	}

	@Deprecated
	public final Element getAfter() {
		return getFirstPart(XmlTarget.AFTER_CONTENT);
	}

	public final List<Object> getChildren() {
		return children;
	}

	public final void vContent(Element part) {
		addPart(XmlTarget.CONTENT, part);
	}

	public final void vAfterContent(Element part) {
		part.setNbInitialTab(1);
		addPart(XmlTarget.AFTER_CONTENT, part);
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
