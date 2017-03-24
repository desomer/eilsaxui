package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.XMLPart.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.XMLPart.CONTENT;

public class XMLBuilder {

	String id; // identifiant du bloc
	StringBuilder content;
	StringBuilder afterContent;
	boolean after = false;

	public XMLBuilder(String id, StringBuilder content, StringBuilder afterContent) {
		super();
		this.content = content;
		this.afterContent = afterContent;
		this.id = id;
	}

	public Element createElement(Object name, Object... inner) {
		Element t = new Element(name, inner);
		return t;
	}

	public Attr createAttr(Object name, Object value) {
		Attr t = new Attr(name, value);
		return t;
	}

	public Part createPart(XMLPart part, Object... inner) {
		Part t = new Part(part, inner);
		part.initContent(XUIFactoryXHtml.getXMLRoot());
		return t;
	}

	/**
	 * ajoute dans le flux : ici une chaine
	 * @param v
	 */
	private void addContent(Object v) {
		(after ? afterContent : content).append(v);
	}

	/**
	 * interface toXML 
	 * @author Bureau
	 *
	 */
	public interface IXMLBuilder {

		public XMLBuilder toXML(XMLBuilder buf);
	}
	
	
	/**
	 * un element XML
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

		private int nbTabInternal = 0;
		private int nbInitialTab = 0;

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

		private void newLine(XMLBuilder buf) {
			buf.addContent("\n");
			for (int i = 0; i < nbInitialTab; i++) {
				buf.addContent("\t");
			}
		}

		private void newTabulation(XMLBuilder buf) {
			for (int i = 0; i < nbTabInternal; i++) {
				buf.addContent("\t");
			}
		}

		@Override
		public XMLBuilder toXML(XMLBuilder buf) {

			if (comment != null) {
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

			if (comment != null) {
				newLine(buf);
				newTabulation(buf);
				buf.addContent("<!--end of " + comment + "-->");
			}

			nbTabInternal = 0;
			nbInitialTab = 0;

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
				if (part != null) {
					nbChild++;
					for (Element elem : part.part.getListElement(CONTENT.class)) {
						elem.nbTabInternal = this.nbTabInternal + 1;
						elem.nbInitialTab = this.nbInitialTab;
					}
					part.toXML(buf);
				}
			} else if (inner instanceof List) {
				List<?> listChild = (List<?>) inner;
				for (Object object : listChild) {
					nbChild = doChild(buf, nbChild, object);
				}
			} else
				buf.addContent(inner);
			return nbChild;
		}

	}

	/**
	 * un attribut XML
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
			buf.after = false;
			for (Element elem : part.getListElement(CONTENT.class)) {
				elem.toXML(buf);
			}
			buf.after = true;
			for (Element elem : part.getListElement(AFTER_CONTENT.class)) {
				elem.toXML(buf);
			}
			buf.after = false;
			return buf;
		}
	}

}
