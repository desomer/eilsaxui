/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyle;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassImpl;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * un element XML
 * 
 * @author Bureau
 *
 */
public class XMLElement extends XUIFormatManager implements IXMLBuilder {
	private Object name;
	private Object comment;

	public Object getComment() {
		return comment;
	}

	/** commentaire **/
	public XMLElement setComment(Object comment) {
		this.comment = comment;
		return this;
	}
	
	public XMLElement getXMLElementTabbed(int nbInitialTab) {
		setNbInitialTab(nbInitialTab);
		return this;
	}

	protected List<XMLAttr> listAttr = new ArrayList<>();
	protected List<Object> listInner = new ArrayList<>();

	public XMLElement(Object name, Object... inner) {
		super();
		this.name = name;

		if (inner != null) {
			List<String> listClass = null;
			
			for (Object object : inner) {
				if (object instanceof XMLAttr) {
					listAttr.add((XMLAttr) object);

				} else if (object instanceof XClass) {
					if (listClass == null)
						listClass = new ArrayList<>();
					else
						listClass.add(" ");
					listClass.add(((XClass) object).getId());
				} else {

					listInner.add(object);
				}
			}
			
			if (listClass!=null)
			{
				String[] arr = new String[listClass.size()];
				listAttr.add(XMLBuilder.createAttr("class", "\""+ String.join("", listClass.toArray(arr)) +"\""));
			}
		}

	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {

		XUIFactoryXHtml.getXHTMLFile().listParent.add(this);

		if (comment != null /* && !buf.isJS() */) {
			newLine(buf);
			newTabulation(buf);
			buf.addContent("<!--" + comment + "-->");
		}

		if (name != null) {
			newLine(buf);
		}

		newTabulation(buf);
		if (name != null) {
			buf.addContent("<" + name);

			for (XMLAttr attr : listAttr) {
				buf.addContent(" ");
				attr.toXML(buf);
			}
			buf.addContent(">");
		}
		int nbChild = 0;
		for (Object inner : listInner) {
			if (inner != null)
				nbChild = doChild(buf, nbChild, inner);
		}
		if (nbChild > 0) {
			newLine(buf);
			newTabulation(buf);
		}
		if (name != null) {
			buf.addContent("</" + name + ">");
		}

		if (comment != null /* && !buf.isJS() */) {
			newLine(buf);
			newTabulation(buf);
			buf.addContent("<!--end of " + comment + "-->");
		}

		nbTabInternal = 0;
		nbInitialTab = 0;
		XUIFactoryXHtml.getXHTMLFile().listParent.removeLast();
		return buf;
	}

	private int doChild(XMLBuilder buf, int nbChild, Object inner) {

		if (inner instanceof XMLElement) {
			nbChild++;
			XMLElement tag = ((XMLElement) inner);
			tag.nbTabInternal = this.nbTabInternal + 1;
			tag.nbInitialTab = this.nbInitialTab;
			tag.toXML(buf);
			
		} else if (inner instanceof XMLPartElement) {
			XMLPartElement part = ((XMLPartElement) inner);
			nbChild++;
			for (XMLElement elem : part.part.getListElement(CONTENT.class)) {
				elem.nbTabInternal = this.nbTabInternal + 1;
				elem.nbInitialTab = this.nbInitialTab;
			}
			part.toXML(buf);
		} else if (inner instanceof List) {
			List<?> listChild = (List<?>) inner;
			for (Object object : listChild) {
				nbChild = doChild(buf, nbChild, object);
			}
		} else if (inner instanceof Handle) {
			Handle h = (Handle) inner;
			String nameHandle = h.getName();
			LinkedList<Object> listParent = XUIFactoryXHtml.getXHTMLFile().listParent;
			Object handledObject = null;
			for (Iterator<Object> it = listParent.descendingIterator(); it.hasNext();) {
				Object elm = it.next();
				if (elm instanceof XMLElement) {
					// MgrErrorNotificafion.doError("Handle on Element",
					// null);
				} else if (elm instanceof XMLPartElement) {
					Object elem = ((XMLPartElement) elm).part.getProperty(nameHandle);
					if (elem != null) {
						handledObject = elem;
					}
				}
			}
			if (handledObject != null) {
				nbChild = doChild(buf, nbChild, handledObject);
			}

		} else if (inner instanceof JSClassImpl) {
			JSClassImpl part = ((JSClassImpl) inner);
			JSBuilder jsBuilder = part.getJSBuilder();
			jsBuilder.nbTabInternal = this.nbTabInternal + 1;
			jsBuilder.nbInitialTab = this.nbInitialTab;
			part.toXML(buf);
			nbChild++;
		} else if (inner instanceof JSContent) {
			JSContent part = ((JSContent) inner);
			JSBuilder jsBuilder = part.getJSBuilder();
			jsBuilder.nbTabInternal = this.nbTabInternal + 1;
			jsBuilder.nbInitialTab = this.nbInitialTab;
			part.toXML(buf);
			nbChild++;
		}else if (inner instanceof CSSStyle) {
			
			CSSStyle part = ((CSSStyle) inner);
			part.nbTabInternal = this.nbTabInternal + 1;
			part.nbInitialTab = this.nbInitialTab;
			part.toXML(buf);
			nbChild++;
			
		} else
			buf.addContent(inner);

		return nbChild;
	}

}