/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.jsclass;

import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author Bureau
 *
 *         class JS d'interface vers un JS externe sans implementation interne
 */
public class JSClassInterface extends JSAny {

	// TODO a retirer et a gerer par le _setContent (voir equal sur JSString)
	ArrayMethod<Object> listContent = new ArrayMethod<>();

	protected static final Object[] addText(Object... classes) {
		if (classes.length == 1) {
			if (classes[0] == null)
				return null;

			Object[] ret = new Object[3];
			if (classes[0] instanceof JSAny) {
				return new Object[] { classes[0] };
			}
			if (classes[0] instanceof Integer) {
				return new Object[] { classes[0] };
			} else {
				ret[0] = "'";
				ret[1] = classes[0];
				ret[2] = "'";
			}
			return ret;
		}

		return new Object[] { "... bug addtext..." };
	}

	@Deprecated // utiliser le parent
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());

		for (Object object : listContent) {
			sb.append(object);
		}

		return sb.toString();
	}

	@Deprecated // utiliser le parent
	@Override
	public final Object _getValueOrName() {
		ArrayMethod<Object> list = new ArrayMethod<Object>();
		Object arr = super._getValueOrName();
		if (arr instanceof ArrayMethod)
			list.addAll((ArrayMethod<?>) arr);
		else
			list.add(arr);

		list.addAll(listContent);
		return list;
	}

	@SuppressWarnings("unchecked")
	protected final <E extends JSClassInterface> E addContent(Object content) {

		if (content instanceof Object[]) {
			Object[] arr = (Object[]) content;
			for (Object object : arr) {

				if (object instanceof XClass) {
					listContent.add(((XClass) object).getId());
				} else if (object instanceof JSFunction) {
					listContent.add(object);
				} else if (object instanceof Object[]) {
					addContent(object);
				} else
					listContent.add(object);
			}
		} else if (content != null)
			listContent.add(content);
		return (E) this;
	}
	

	protected final <E extends JSClassInterface> E _callMth(String mth, Object... classes) {
		E ret = getReturnType();

		ret.addContent("." + mth + "(");
		ret.addContent(classes);
		ret.addContent(")");

		_registerMethod(ret);

		return ret;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final <E extends JSClassInterface> E getReturnType(Class type) {
		E ret = (E) this;
		if (listContent.isEmpty() && this.name != null) {
			// gestion premier appel de variable pour chainage
			try {
				ret = (E) this.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			ret._setName(this._getName());
		}
		return ret;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final <E extends JSClassInterface> E getReturnType() {
		E ret = (E) this;
		if (listContent.isEmpty() && this.name != null) {
			// gestion premier appel de variable pour chainage
			try {
				ret = (E) this.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			ret._setName(this._getName());
		}
		return ret;
	}

	/**********************************************************************/

	/**********************************************************************/
	public final <E extends JSClassInterface> E attrByString(Object attr) {
		E ret = getReturnType();

		ret.addContent("[");
		ret.addContent(addText(attr));
		ret.addContent("]");
		return ret;
	}

	@Deprecated
	/**
	 * use get du json
	 * 
	 * @param att
	 * @return
	 */
	public final <E extends JSClassInterface> E attr(String att) {
		E ret = getReturnType();
		ret.addContent("." + att);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public final <E extends JSAny> E castAttr(JSAny cl, String att) {
		cl._setValue(this._getName() + "." + att);
		return (E) cl;
	}
}
