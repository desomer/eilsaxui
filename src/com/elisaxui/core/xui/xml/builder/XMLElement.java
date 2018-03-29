/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyleRow;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.xtemplate.JSDomFuntion;
import com.elisaxui.core.xui.xml.XMLPart;
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

	/**
	 * @return the listInner
	 */
	public final List<Object> getListInner() {
		return listInner;
	}

	public XMLElement(Object name, Object... inner) {
		super();
		this.name = name;

		if (inner != null) {
			List<String> listClass = null;

			for (Object object : inner) {
				if (object instanceof XMLAttr) {
					listAttr.add((XMLAttr) object);

				} else if (object instanceof CSSClass) {
					if (listClass == null)
						listClass = new ArrayList<>();
					else
						listClass.add(" ");
					listClass.add(((CSSClass) object).getId());
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
				if (nbAttr > 0)
				{
					buf.getJSContent().getListElem().add(",");
					nbAttr=0;
				}
				
				if (nbChild > 0)
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
		if (comment != null && !buf.isModeString() && XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS()) {
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
			
			// recherche un handle de type XMLAttr
			for (Object inner : listInner) {
				if (inner != null) {
					if (inner instanceof Handle) { // un handle
						Handle h = (Handle) inner;
						Object handledObject = zzGetProperties(h);
						if (handledObject instanceof XMLAttr)
						{
							buf.addContentOnTarget(" ");
							((XMLAttr)handledObject).toXML(buf);
						}
					}
				}
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

		if (comment != null && !buf.isModeString() && XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isEnableCommentFctJS()) {
			newLine(buf);
			newTabInternal(buf);
			buf.addContentOnTarget("<!--end of " + comment + "-->");
		}
	}

	@SuppressWarnings("deprecation")
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
			for (XMLElement elem : part.part.getListElementFromTarget(CONTENT.class)) {
				elem.nbTabInternal = this.nbTabInternal + 1;
				elem.nbTabForNewLine = this.nbTabForNewLine;
			}
			part.toXML(buf);

		} else if (inner instanceof List) { // une liste
			if (inner instanceof ArrayMethod)
			{				
				JSDomFuntion v = new JSDomFuntion()._setValue(inner);
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
			nbChild = doProperties(buf, nbChild, h);

		} else if (inner instanceof VProperty) { // un handle
			VProperty h = (VProperty) inner;
			nbChild = doProperties(buf, nbChild, new Handle(h.getName()));

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

		} else if (inner instanceof CSSStyleRow) { // un css
			CSSStyleRow part = ((CSSStyleRow) inner);
			part.nbTabInternal = this.nbTabInternal + part.nbTabInternal+ 1;
			part.nbTabForNewLine = this.nbTabForNewLine;
			part.toXML(buf);
			nbChild++;

		} else if (inner instanceof XMLAttr) { // un attribut
			XMLAttr attr = ((XMLAttr) inner);
			if (buf.isTemplate) {
				Object v = attr.getValue();
				if (v instanceof VProperty)
				{
					v = XMLElement.zzGetProperties(new Handle(((VProperty)v).getName()));
				}
				buf.getJSContent().getListElem().add(MTH_ADD_ATTR+"([");
				buf.getJSContent().getListElem().add("\"" + attr.getName() + "\"," + v + "");
				buf.getJSContent().getListElem().add("]),");
			}
			//else
			//	attr.toXML(buf);   // pas en children
			
		} else {
			if (buf.isTemplate) {
				if (inner instanceof CharSequence) {
					buf.addContentOnTarget("\""+inner+"\"");
				} else if (inner instanceof JSDomFuntion) {
					buf.addContentOnTarget(inner);
				} else if (inner instanceof JSNodeElement) {
					buf.addContentOnTarget(inner);
				} else {
					buf.addContentOnTarget(MTH_ADD_TEXT+"(");
					buf.addContentOnTarget(inner);  
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

	private int doProperties(XMLBuilder buf, int nbChild, Handle h) {
		Object handledObject = zzGetProperties(h);
		
		if (handledObject != null) {
			nbChild = doChild(buf, nbChild, handledObject);
		}
		return nbChild;
	}
	

	public static Object zzGetProperties(Handle h) {
		String nameHandle = h.getName();
		// recherche dans les parents
		LinkedList<Object> listParent = XUIFactoryXHtml.getXHTMLFile().listParent;
		Object handledObject = null;
		String firstPrefix = null;
		XMLPart firstPart = null;
		
		for (Iterator<Object> it = listParent.descendingIterator(); it.hasNext();) {
			Object elm = it.next();
			if (elm instanceof XMLElement) {
				// MgrErrorNotificafion.doError("Handle on Element", null)
			} else if (elm instanceof XMLPartElement) {
				XMLPart part = ((XMLPartElement) elm).part;
				// getsion du prefix
				if (firstPrefix==null)
				{
					firstPart = part;
					firstPrefix = part.getPropertiesPrefix()==null?"":part.getPropertiesPrefix();
				}
				Object elem = part.vProperty(nameHandle+firstPrefix);
				if (elem != null) {
					handledObject = elem;
					break;   // element trouve sur un parent
				}
			}
		}
		
		if (handledObject==null)
		{   // recherche sur la scene
			handledObject = XUIFactoryXHtml.getXHTMLFile().getScene().vProperty(nameHandle+firstPrefix);
		}
		
		if (firstPart!=null && handledObject==null)
		{   // recherche sans le prefix sur le premier part
			handledObject = firstPart.vProperty(nameHandle);
		}
		
		if (h.getIfExistAdd()!=null && handledObject!=null)
			handledObject = h.getIfExistAdd();
		
		return handledObject;
	}

}