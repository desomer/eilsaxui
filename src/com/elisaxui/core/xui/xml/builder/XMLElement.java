/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyle;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSDomElement;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.XHTMLFunction;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * un element XML
 *   TODO mettre un heritage  pour mieux gere doElementModeTemplate et doElementModeXML
 * 
 * @author Bureau
 *
 */
public class XMLElement extends XUIFormatManager implements IXMLBuilder {
	
	public static final String MTH_ADD_ELEM = "e";
	public static final String MTH_ADD_ATTR = "a";
	public static final String MTH_ADD_TEXT = "t";
	
	private static final Object IS_XPART = null;
	
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
		setTabForNewLine(nbInitialTab);
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

			if (listClass != null) {
				String[] arr = new String[listClass.size()];
				listAttr.add(XMLBuilder.createAttr("class", "\"" + String.join("", listClass.toArray(arr)) + "\""));
			}
		}

	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {

		XUIFactoryXHtml.getXHTMLFile().listParent.add(this);

		if (buf.isTemplate) {
			doElementModeTemplate(buf);
		} else
			doElementModeXML(buf);

		nbTabInternal = 0;
		nbTabForNewLine = 0;
		XUIFactoryXHtml.getXHTMLFile().listParent.removeLast();
		return buf;
	}

	/**
	 * @param buf
	 */
	private void doElementModeTemplate(XMLBuilder buf) {
		if (name==IS_XPART)  
			name="xpart";
		
		buf.getJSContent().getListElem().add(MTH_ADD_ELEM+"('" + name + "'");

		buf.getJSContent().getListElem().add(",[");
		int nbAttr = 0;
		for (XMLAttr attr : listAttr) {
			if (nbAttr == 0)
				buf.getJSContent().getListElem().add(MTH_ADD_ATTR+"([");
			else
				buf.getJSContent().getListElem().add(",");

			buf.getJSContent().getListElem().add("\"" + attr.getName() + "\"," + attr.getValue() + "");
			nbAttr++;
		}
		if (nbAttr > 0)
			buf.getJSContent().getListElem().add("])");

		int nbChild = 0;
		for (Object inner : listInner) {
			if (inner != null) {
				if (nbChild > 0 || nbAttr > 0)
					buf.getJSContent().getListElem().add(",");

				nbChild = doChild(buf, nbChild, inner);
			}
		}
		buf.getJSContent().getListElem().add("]");

		buf.getJSContent().getListElem().add(")");
	}

	/**
	 * @param buf
	 */
	private void doElementModeXML(XMLBuilder buf) {
		if (comment != null && XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS()) {
			newLine(buf);
			newTabInternal(buf);
			buf.addContentOnTarget("<!--" + comment + "-->");
		}

		if (name != null) { // && !name.equals(XMLPart.NONAME)
			newLine(buf);
		}

		newTabInternal(buf);
		if (name != null) {
			buf.addContentOnTarget("<" + name);

			for (XMLAttr attr : listAttr) {
				buf.addContentOnTarget(" ");
				attr.toXML(buf);
			}
			buf.addContentOnTarget(">");
		}

		int nbChild = 0;
		for (Object inner : listInner) {
			if (inner != null) {
				this.nbTabInternal = this.nbTabInternal - (this.name == null ? 1 : 0);
				nbChild = doChild(buf, nbChild, inner);
				this.nbTabInternal = this.nbTabInternal + (this.name == null ? 1 : 0);
			}
		}

		if (name != null) {
			if (nbChild > 0) {
				newLine(buf);
				newTabInternal(buf);
			}
			buf.addContentOnTarget("</" + name + ">");
		}

		if (comment != null && XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS()) {
			newLine(buf);
			newTabInternal(buf);
			buf.addContentOnTarget("<!--end of " + comment + "-->");
		}
	}

	private int doChild(XMLBuilder buf, int nbChild, Object inner) {

		if (inner instanceof XMLElement) { // une div
			nbChild++;
			XMLElement tag = ((XMLElement) inner);
			tag.nbTabInternal = this.nbTabInternal + 1;
			tag.nbTabForNewLine = this.nbTabForNewLine;
			tag.toXML(buf);

		} else if (inner instanceof XMLPartElement) { // un XMLPart
			XMLPartElement part = ((XMLPartElement) inner);
			nbChild++;
			// postionne les tabulation
			for (XMLElement elem : part.part.getListElement(CONTENT.class)) {
				elem.nbTabInternal = this.nbTabInternal + 1;
				elem.nbTabForNewLine = this.nbTabForNewLine;
			}
			part.toXML(buf);

		} else if (inner instanceof List) { // une liste
			if (inner instanceof ArrayMethod)
			{				
				XHTMLFunction v = new XHTMLFunction()._setValue(inner);
				nbChild = doChild(buf, nbChild, v);
			}
			else
			{
				List<?> listChild = (List<?>) inner;
				for (Object object : listChild) {
					if (buf.isTemplate && nbChild>0) { buf.getJSContent().getListElem().add(",");}
					
					nbChild = doChild(buf, nbChild, object);
				}
			}

		} else if (inner instanceof Handle) { // un handle
			Handle h = (Handle) inner;
			String nameHandle = h.getName();
			LinkedList<Object> listParent = XUIFactoryXHtml.getXHTMLFile().listParent;
			Object handledObject = null;
			for (Iterator<Object> it = listParent.descendingIterator(); it.hasNext();) {
				Object elm = it.next();
				if (elm instanceof XMLElement) {
					// MgrErrorNotificafion.doError("Handle on Element", null)
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

		} else if (inner instanceof JSClassBuilder) { // cas d'une class js
			JSClassBuilder part = ((JSClassBuilder) inner);
			ProxyHandler.getFormatManager().nbTabInternal = this.nbTabInternal + 1;
			ProxyHandler.getFormatManager().nbTabForNewLine = this.nbTabForNewLine;
			part.toXML(buf);
			nbChild++;

		} else if (inner instanceof JSContent) { // cas d'un js
			JSContent part = ((JSContent) inner);
			ProxyHandler.getFormatManager().nbTabInternal = this.nbTabInternal + 1;
			ProxyHandler.getFormatManager().nbTabForNewLine = this.nbTabForNewLine;
			part.toXML(buf);
			nbChild++;

		} else if (inner instanceof CSSStyle) { // un css
			CSSStyle part = ((CSSStyle) inner);
			part.nbTabInternal = this.nbTabInternal + 1;
			part.nbTabForNewLine = this.nbTabForNewLine;
			part.toXML(buf);
			nbChild++;

		} else {
			if (buf.isTemplate) {
				if (inner instanceof CharSequence) {
					buf.addContentOnTarget("\""+inner+"\"");
				} else if (inner instanceof XHTMLFunction) {
					buf.addContentOnTarget(inner);
				} else if (inner instanceof JSDomElement) {
					buf.addContentOnTarget(inner);
				}
				else {
					buf.addContentOnTarget(MTH_ADD_TEXT+"(");
//					List<String> result = Arrays.asList(inner.toString().split("\\."));
//					StringBuilder buft = new StringBuilder();
//					for (int i = 1; i < result.size(); i++) {
//						if (i>1)
//							buft.append(",");
//						buft.append("'"+result.get(i)+"'");
//					}
					buf.addContentOnTarget(inner);    //+","+result.get(0)+",["+buft+"]");
					buf.addContentOnTarget(")");
				}
				nbChild++;
			} else {
				if (inner instanceof StringBuilder) {
					StringBuilder text = (StringBuilder) inner;
					if (text.indexOf("\n") >= 0)
						nbChild++;
				}
				buf.addContentOnTarget(inner);
			}
		}

		return nbChild;
	}

}