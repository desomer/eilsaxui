package com.elisaxui.core.xui.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;

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

	public Element getElement(Object name, Object... inner) {
		Element t = new Element(name, inner);
		return t;
	}

	public Attr getAttr(Object name, Object value) {
		Attr t = new Attr(name, value);
		return t;
	}

	public Part getPart(XMLPart part, Object... inner) {
		Part t = new Part(part, inner);
		part.doContent(XUIFactoryXHtml.getXMLRoot());
		return t;
	}

	private void addContent(Object v) {
		(after ? afterContent : content).append(v);
	}

	public static class Element implements IXMLBuilder {
		private Object name;
		private Object comment;

		public Object getComment() {
			return comment;
		}

		public Element setComment(Object comment) {
			this.comment = comment;
			return this;
		}

		private int nbTab = 0;
		private int nbInitialTab = 0;

		public int getNbInitialTab() {
			return nbInitialTab;
		}

		public void setNbInitialTab(int nbInitialTab) {
			this.nbInitialTab = nbInitialTab;
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
			for (int i = 0; i < nbTab; i++) {
				buf.addContent("\t");
			}
		}

		@Override
		public XMLBuilder toXML(XMLBuilder buf) {
			
			if (comment != null) {
				newLine(buf);
				newTabulation(buf);
				buf.addContent("<!--" + comment +"-->");
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
				buf.addContent("<!-- end of " + comment +"-->");
			}
			
			nbTab = 0;
			nbInitialTab = 0;

			return buf;
		}

		private int doChild(XMLBuilder buf, int nbChild, Object inner) {
			if (inner instanceof Element) {
				nbChild++;
				Element tag = ((Element) inner);
				tag.nbTab = this.nbTab + 1;
				tag.nbInitialTab = this.nbInitialTab;
				tag.toXML(buf);
			} else if (inner instanceof Part) {
				Part part = ((Part) inner);
				// Element tag = part.part.getContent();
				if (part != null) {
					nbChild++;
					part.part.getContent().nbTab = this.nbTab + 1;
					part.part.getContent().nbInitialTab = this.nbInitialTab;
					part.toXML(buf);
				}
			} else if (inner instanceof List) {
				List listChild = (List) inner;
				for (Object object : listChild) {
					nbChild = doChild(buf, nbChild, object);
				}
			} else
				buf.addContent(inner);
			return nbChild;
		}

	}

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
			if (part.getContent() != null)
				part.getContent().toXML(buf);
			buf.after = true;
			if (part.getAfter() != null)
				part.getAfter().toXML(buf);
			buf.after = false;
			return buf;
		}
	}

}
