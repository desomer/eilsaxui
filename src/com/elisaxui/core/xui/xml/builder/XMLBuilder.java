package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassImpl;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;

public class XMLBuilder {

	String id; // identifiant du bloc
	StringBuilder content;
	StringBuilder afterContent;
	boolean after = false;
	boolean isJS = false;

	public boolean isJS() {
		return isJS;
	}

	public XMLBuilder setJS(boolean isJS) {
		this.isJS = isJS;
		return this;
	}

	public XMLBuilder(String id, StringBuilder content, StringBuilder afterContent) {
		super();
		this.content = content;
		this.afterContent = afterContent;
		this.id = id;
	}

	public static Element createElement(Object name, Object... inner) {
		Element t = new Element(name, inner);
		return t;
	}

	public static Attr createAttr(Object name, Object value) {
		Attr t = new Attr(name, value);
		return t;
	}

	public static Part createPart(XMLPart part, Object... inner) {
		Part t = new Part(part, inner);
		part.initContent(XUIFactoryXHtml.getXMLRoot());
		return t;
	}

	public static Handle createHandle(String name) {
		Handle t = new Handle(name);
		return t;
	}

	/**
	 * ajoute dans le flux : ici une chaine
	 * 
	 * @param v
	 */
	public void addContent(Object v) {
		(after ? afterContent : content).append(v);
	}

	/**
	 * interface toXML
	 * 
	 * @author Bureau
	 *
	 */
	public interface IXMLBuilder {

		public XMLBuilder toXML(XMLBuilder buf);
	}

	/**
	 * un element XML
	 * 
	 * @author Bureau
	 *
	 */
	public static class Element implements IXMLBuilder {
		private Object name;
		private Object comment;

		public Object getComment() {
			return comment;
		}

		/** commentaire **/
		public Element setComment(Object comment) {
			this.comment = comment;
			return this;
		}

		protected int nbTabInternal = 0;
		protected int nbInitialTab = 0;

		public int getNbInitialTab() {
			return nbInitialTab;
		}

		public Element setNbInitialTab(int nbInitialTab) {
			this.nbInitialTab = nbInitialTab;
			return this;
		}

		private List<Attr> listAttr = new ArrayList<>();
		private List<Object> listInner = new ArrayList<>();

		public Element(Object name, Object... inner) {
			super();
			this.name = name;
			for (Object object : inner) {
				if (object instanceof Attr) {
					listAttr.add((Attr) object);
				} else {
					listInner.add(object);
				}
			}

		}

		public void newLine(XMLBuilder buf) {

			if (buf.isJS()) {
				if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrXMLinJS())
					buf.addContent("'+\n'");
			} else {
				if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCrXML())
					buf.addContent("\n");
			}

			for (int i = 0; i < nbInitialTab; i++) {
				buf.addContent("\t");
			}
		}

		public void newTabulation(XMLBuilder buf) {
			// if (buf.isJS()) return;
			for (int i = 0; i < nbTabInternal; i++) {
				buf.addContent("\t");
			}
		}

		@Override
		public XMLBuilder toXML(XMLBuilder buf) {

			XUIFactoryXHtml.getXMLFile().listParent.add(this);

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

				for (Attr attr : listAttr) {
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
			XUIFactoryXHtml.getXMLFile().listParent.removeLast();
			return buf;
		}

		private int doChild(XMLBuilder buf, int nbChild, Object inner) {

			if (inner instanceof Element) {
				nbChild++;
				Element tag = ((Element) inner);
				tag.nbTabInternal = this.nbTabInternal + 1;
				tag.nbInitialTab = this.nbInitialTab;
				tag.toXML(buf);
			} else if (inner instanceof Part) {
				Part part = ((Part) inner);
				nbChild++;
				for (Element elem : part.part.getListElement(CONTENT.class)) {
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
				LinkedList<Object> listParent = XUIFactoryXHtml.getXMLFile().listParent;
				Object handledObject = null;
				for (Iterator<Object> it = listParent.descendingIterator(); it.hasNext();) {
					Object elm = it.next();
					if (elm instanceof Element) {
						// MgrErrorNotificafion.doError("Handle on Element",
						// null);
					} else if (elm instanceof Part) {
						Object elem = ((Part) elm).part.getProperty(nameHandle);
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
				{
					JSContent part = ((JSContent) inner);
					JSBuilder jsBuilder = part.getJSBuilder();
					jsBuilder.nbTabInternal = this.nbTabInternal + 1;
					jsBuilder.nbInitialTab = this.nbInitialTab;
					part.toXML(buf);
					nbChild++;
				}
			} else
				buf.addContent(inner);

			return nbChild;
		}

	}

	/**
	 * un attribut XML
	 * 
	 * @author Bureau
	 *
	 */
	public static class Attr implements IXMLBuilder {
		private Object name;
		private Object value;

		public Attr(Object name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}

		@Override
		public XMLBuilder toXML(XMLBuilder buf) {
			buf.addContent(name);
			buf.addContent("=");
			buf.addContent(value);
			return buf;
		}
	}

	public static class Part implements IXMLBuilder {
		XMLPart part;

		public Part(XMLPart part, Object... inner) {
			this.part = part;
			this.part.getChildren().addAll(Arrays.asList(inner));
		}

		@Override
		public XMLBuilder toXML(XMLBuilder buf) {
			XUIFactoryXHtml.getXMLFile().listParent.add(this);
			buf.after = false;
			for (Element elem : part.getListElement(CONTENT.class)) {
				elem.toXML(buf);
			}
			buf.after = true;
			for (Element elem : part.getListElement(AFTER_CONTENT.class)) {
				elem.toXML(buf);
			}
			buf.after = false;
			XUIFactoryXHtml.getXMLFile().listParent.removeLast();
			return buf;
		}
	}

	public static class Handle {
		private String name;

		public Handle(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
