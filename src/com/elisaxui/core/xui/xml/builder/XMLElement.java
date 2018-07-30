/**
 * 
 */
package com.elisaxui.core.xui.xml.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.elisaxui.component.widget.layout.ViewPageLayout;
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.css.CSSStyleRow;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSContent;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClassBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.dom.JSNodeElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSDomFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.template.JSNodeTemplate;
import com.elisaxui.core.xui.xhtml.builder.module.ImportDesc;
import com.elisaxui.core.xui.xhtml.builder.module.ModuleDesc;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLHandle;
import com.elisaxui.core.xui.xml.target.CONTENT;

/**
 * un element XML TODO mettre un heritage pour mieux gere doElementModeTemplate
 * et doElementModeXML
 * 
 * @author Bureau
 *
 */
public class XMLElement extends XUIFormatManager implements IXMLBuilder {

	private static final Object IS_XPART = null;

	private Object name;

	/**
	 * @return the name
	 */
	public final Object getName() {
		return name;
	}

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
	protected List<Object> listClass = new ArrayList<>();

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

			for (Object object : inner) {
				if (object instanceof XMLAttr) {
					listAttr.add((XMLAttr) object);

				} else if (object instanceof CSSClass) {
					listClass.add(((CSSClass) object).getId());
				} else {

					listInner.add(object);
				}
			}

		}

	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {

		XUIFactoryXHtml.getXMLFile().listTreeXMLParent.add(this);
		if (buf.isTemplate) {
			doElementModeTemplate(buf);
		} else
			doElementModeText(buf);

		nbTabInternal = 0;
		nbTabForNewLine = 0;
		XUIFactoryXHtml.getXMLFile().listTreeXMLParent.removeLast();
		return buf;
	}

	/**
	 * @param buf
	 */
	private void doElementModeTemplate(XMLBuilder buf) {
		int nbAttr = 0;

		if (name == IS_XPART) {
			buf.getJSContent().getListElem().add(JSDomBuilder.MTH_ADD_PART + "(");
			buf.getJSContent().getListElem().add("[");
		} else {
			buf.getJSContent().getListElem().add(JSDomBuilder.MTH_ADD_ELEM + "('" + name + "',");

			buf.getJSContent().getListElem().add("[");

			// recherche un handle de type XMLAttr
			for (Object inner : listInner) {
						
				if (inner instanceof XMLHandle) { // un handle
					XMLHandle h = (XMLHandle) inner;				
					Object handledObject = zzGetProperties(h);
					if (handledObject instanceof CSSClass) {
						listClass.add(((CSSClass)handledObject).getId());
						h.setName(null);
					}
				}
			}

			if (! listClass.isEmpty()) {
				String[] arr = new String[listClass.size()];
				listAttr.add(XMLBuilder.createAttr("class", "\"" + String.join(" ", listClass.toArray(arr)) + "\""));
			}

			for (XMLAttr attr : listAttr) {
				if (nbAttr == 0)
					buf.getJSContent().getListElem().add(JSDomBuilder.MTH_ADD_ATTR + "([");
				else
					buf.getJSContent().getListElem().add(",");

				Object v = attr.getValue();

				if (v instanceof VProperty) {
					v = XMLElement.zzGetProperties(new XMLHandle(((VProperty) v).getName()));
				}

				if (v instanceof String) {
					String vs = ((String) v);
					if (!vs.endsWith("'") && !vs.endsWith("\"") && !vs.startsWith("'") && !vs.startsWith("\""))
						v = "\"" + vs + "\"";
				}

				buf.getJSContent().getListElem().add("\"" + attr.getName() + "\"," + v + "");
				nbAttr++;
			}
			if (nbAttr > 0)
				buf.getJSContent().getListElem().add("])");
		}

		int nbChild = 0;
		for (Object inner : listInner) {
			if (inner != null) {
				if (nbAttr > 0) {
					buf.getJSContent().getListElem().add(",");
					nbAttr = 0;
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
	private void doElementModeText(XMLBuilder buf) {

		boolean isScript = buf.isResource && name != null && name.equals(XHTMLPart.SCRIPT);
		boolean isCss = buf.isResource && name != null && name.equals(XHTMLPart.STYLE);
		boolean isCommentText = buf.isResource && (buf.id.endsWith("css") || buf.id.endsWith("js"));

		if (comment != null && !buf.isModeString()
				&& XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCommentFctJS()) {
			newLine(buf);
			newTabInternal(buf);
			if (isCommentText) {
				buf.addContentOnTarget("/*" + comment + "*/");
			} else {
				buf.addContentOnTarget("<!--" + comment + "-->");
			}
		}

		if (name != null) { // && !name.equals(XMLPart.NONAME)
			newLine(buf);
		}

		if (isScript || isCss) {
			name = null;
		}

		newTabInternal(buf);
		if (name != null) {

			buf.addContentOnTarget("<" + name);

			// recherche un handle de type XMLAttr
			for (Object inner : listInner) {
				if (inner instanceof VProperty) { // un handle
					VProperty p  = (VProperty) inner;
					inner = new XMLHandle(((VProperty) p).getName());		
				}
				
				if (inner instanceof XMLHandle) { // un handle
					XMLHandle h = (XMLHandle) inner;
					Object handledObject = zzGetProperties(h);
					if (handledObject instanceof XMLAttr) {

						XMLAttr attr = (XMLAttr) handledObject;
						Object v = attr.getValue();
						if (v instanceof VProperty) {
							v = XMLElement.zzGetProperties(new XMLHandle(((VProperty) v).getName()));
							listAttr.add(XMLBuilder.createAttr(attr.getName(), v));
						} else
							listAttr.add(attr);
					}
					if (handledObject instanceof CSSClass) {
						h.setName(null);
						listClass.add(((CSSClass)handledObject).getId());
					}
				}
			}

			if (listClass.size() > 0) {
				String[] arr = new String[listClass.size()];
				listAttr.add(XMLBuilder.createAttr("class", "\"" + String.join(" ", listClass.toArray(arr)) + "\""));
			}

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

		if (comment != null && !buf.isModeString()
				&& XUIFactoryXHtml.getXMLFile().getConfigMgr().isEnableCommentFctJS()) {
			newLine(buf);
			newTabInternal(buf);
			if (isCommentText) {
				buf.addContentOnTarget("/* end of " + comment + "*/");
			} else {
				buf.addContentOnTarget("<!--end of " + comment + "-->");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public int doChild(XMLBuilder buf, int nbChild, Object inner) {

		if (inner instanceof XMLElement) { // une div
			nbChild++;
			XMLElement tag = ((XMLElement) inner);
			tag.nbTabInternal = this.nbTabInternal + 1;
			tag.nbTabForNewLine = this.nbTabForNewLine;
			tag.toXML(buf);

		} else if (inner instanceof XMLPartElement) { // un XMLPart
			XMLPartElement part = ((XMLPartElement) inner);
			nbChild++;
			// postionne les tabulation des sous element
			for (XMLElement elem : part.part.getListElementFromTarget(CONTENT.class)) {
				elem.nbTabInternal = this.nbTabInternal + 1;
				elem.nbTabForNewLine = this.nbTabForNewLine;
			}
			part.toXML(buf);

		} else if (inner instanceof List) { // une liste
			if (inner instanceof ArrayMethod) {
				JSDomFunction v = new JSDomFunction()._setValue(inner);
				nbChild = doChild(buf, nbChild, v);
			} else {
				List<?> listChild = (List<?>) inner;
				for (Object object : listChild) {
					if (buf.isTemplate && nbChild > 0) {
						buf.getJSContent().getListElem().add(",");
					}

					nbChild = doChild(buf, nbChild, object);
				}
			}

		} else if (inner instanceof XMLHandle) { // un handle
			XMLHandle h = (XMLHandle) inner;
			nbChild = doProperties(buf, nbChild, h);

		} else if (inner instanceof VProperty) { // un handle
			VProperty h = (VProperty) inner;
			nbChild = doProperties(buf, nbChild, new XMLHandle(h.getName()));

		} else if (inner instanceof JSClassBuilder) { // cas d'une class js
			JSClassBuilder part = ((JSClassBuilder) inner);
			if (!buf.isTemplate())
				ProxyHandler.getFormatManager().setNbTabInternal(this.nbTabInternal + 1);
			ProxyHandler.getFormatManager().setTabForNewLine(this.nbTabForNewLine);
			part.toXML(buf);
			if (!buf.isTemplate())
				ProxyHandler.getFormatManager().setNbTabInternal(this.nbTabInternal);
			nbChild++;

		} else if (inner instanceof JSContent) { // cas d'un js
			JSContent part = ((JSContent) inner);
			if (!buf.isTemplate())
				ProxyHandler.getFormatManager().setNbTabInternal(this.nbTabInternal + 1);
			ProxyHandler.getFormatManager().setTabForNewLine(this.nbTabForNewLine);
			part.toXML(buf);
			if (!buf.isTemplate())
				ProxyHandler.getFormatManager().setNbTabInternal(this.nbTabInternal);
			nbChild++;

		} else if (inner instanceof CSSStyleRow) { // un css
			CSSStyleRow part = ((CSSStyleRow) inner);
			part.nbTabInternal = this.nbTabInternal + part.nbTabInternal + 1;
			part.nbTabForNewLine = this.nbTabForNewLine;
			part.toXML(buf);
			nbChild++;

		} else if (inner instanceof ImportDesc) { // un import
			ImportDesc aImportDesc = (ImportDesc) inner;
			ProxyHandler.getFormatManager().setNbTabInternal(0);
			ProxyHandler.getFormatManager().setTabForNewLine(0);
			aImportDesc.toXML(buf);

		} else if (inner instanceof XMLAttr) { // un attribut
			XMLAttr attr = ((XMLAttr) inner);
			/** TODO a mettre dans le toXML du XMLAttr */
			if (buf.isTemplate) {
				Object v = attr.getValue();
				if (v instanceof VProperty) {
					v = XMLElement.zzGetProperties(new XMLHandle(((VProperty) v).getName()));
				}

				if (v instanceof String) {
					String vs = ((String) v);
					if (!vs.endsWith("'") && !vs.endsWith("\"") && !vs.startsWith("'") &&
							!vs.startsWith("\""))
						v = "\"" + vs + "\"";
				}

				buf.getJSContent().getListElem().add(JSDomBuilder.MTH_ADD_ATTR + "([");
				buf.getJSContent().getListElem().add("\"" + attr.getName() + "\"," + v + "");
				buf.getJSContent().getListElem().add("])" + (nbChild >= 0 ? "," : ""));
			}
		} else if (inner instanceof CSSClass) { // une class en inner
			// ne fait rien
		} else {
			if (buf.isTemplate) {
				if (inner instanceof CharSequence) {
					buf.addContentOnTarget("\"" + inner + "\"");
				} else if (inner instanceof JSDomFunction) {
					buf.addContentOnTarget(inner);
				} else if (inner instanceof JSNodeElement) {
					buf.addContentOnTarget(inner);
				} else {
					buf.addContentOnTarget(JSDomBuilder.MTH_ADD_TEXT + "(");
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

	private int doProperties(XMLBuilder buf, int nbChild, XMLHandle h) {
		Object handledObject = zzGetProperties(h);

//		if (h!=null && h.getName()!=null && h.getName().equals("ViewPageLayout.pWithTabBar"))
//		{
//			h=h;
//		}
		
		if (handledObject != null) {
			nbChild = doChild(buf, nbChild, handledObject);
		}
		return nbChild;
	}

	public static Object zzGetProperties(XMLHandle h) {
		String nameHandle = h.getName();
		if (nameHandle==null)
			return null;
		// recherche dans les parents
		LinkedList<Object> listParent = XUIFactoryXHtml.getXMLFile().listTreeXMLParent;
		Object handledObject = null;
		String firstPrefix = null;
		XMLPart firstPart = null;

		for (Iterator<Object> it = listParent.descendingIterator(); it.hasNext();) {
			Object elm = it.next();
			if (elm instanceof XMLElement) {
				// MgrErrorNotificafion.doError("Handle on Element", null)
			} else if (elm instanceof XMLPartElement) {
				XMLPart part = ((XMLPartElement) elm).part;
				// gestion du prefix
				if (firstPrefix == null) {
					firstPart = part;
					firstPrefix = part.getPropertiesPrefix() == null ? "" : part.getPropertiesPrefix();
				}
				Object elem = part.vProperty(nameHandle + firstPrefix);
				if (elem != null) {
					handledObject = elem;
					break; // element trouve sur un parent
				}
			}
		}

		if (handledObject == null) { // recherche sur la scene
			if (firstPrefix == null)
				firstPrefix = "";
			handledObject = XUIFactoryXHtml.getXMLFile().getMainXMLPart().vProperty(nameHandle + firstPrefix);
		}

		if (firstPart != null && handledObject == null) { // recherche sans le prefix sur le premier part
			handledObject = firstPart.vProperty(nameHandle);
		}

		// gestion du add if exist
		if (h.getIfExistAdd() != null && handledObject != null)
			handledObject = h.getIfExistAdd();

		return handledObject;
	}

}