package com.elisaxui.core.xui.xml;

import java.util.ArrayList;
import java.util.List;

public class XMLBuilder {

	String id;     // identifiant du bloc
	StringBuilder buf;

	public XMLBuilder(String id, StringBuilder buf) {
		super();
		this.buf = buf;
		this.id = id;
	}

	public Tag getTag(Object name, Object... inner) {
		Tag t = new Tag(name, inner);
		return t;
	}

	public Attr getAttr(Object name, Object value) {
		Attr t = new Attr(name, value);
		return t;
	}

	private void append(Object v) {
		buf.append(v);
	}

	public static class Tag implements IXMLBuilder {
		private Object name;
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

		public Tag(Object name, Object... inner) {
			super();
			this.name = name;
			for (Object object : inner) {
				if (object instanceof Attr) {
					listAttr.add((Attr) object);
				}
				else
				{
					listInner.add(object);
				}
			}

		}

		
		private void newLine(XMLBuilder buf)
		{
			buf.append("\n");
			for (int i = 0; i < nbInitialTab; i++) {
				buf.append("\t");
			}
		}
		
		@Override
		public XMLBuilder toHtml(XMLBuilder buf) {
			newLine(buf);
			
			for (int i = 0; i < nbTab; i++) {
				buf.append("\t");
			}
			buf.append("<" + name);

			for (Attr attr : listAttr) {
				buf.append(" ");
				attr.toHtml(buf);
			}
			buf.append(">");
			int nbChild = 0;
			for (Object inner : listInner) {
				if (inner instanceof Tag)
				{
					nbChild++;
					Tag tag = ((Tag) inner);
					tag.nbTab = this.nbTab+1;
					tag.nbInitialTab = this.nbInitialTab;
					tag.toHtml(buf);
				}
				else
					buf.append(inner);
			}
			if (nbChild>0) {
				newLine(buf);
				for (int i = 0; i < nbTab; i++) {
					buf.append("\t");
				}
			}	
			buf.append("</" + name + ">");

			nbTab = 0;
			nbInitialTab=0;
			
			return buf;
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
		public XMLBuilder toHtml(XMLBuilder buf) {
			buf.append(name);
			buf.append("=");
			buf.append(value);
			return buf;
		}

	}

}
