/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.ArrayList;
import java.util.LinkedList;

import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSMedia;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;

/**
 * @author gauth
 *
 */
public interface ICSSBuilder {

	LinkedList<CSSElement> list = new LinkedList<>();  //TODO a changer  ThreadLocal
	
	default CSSElement sOn(CSSElement parent, CssBlock css) {
		return xStyle(parent, css);
	}
	
	default CSSElement xStyle(CSSElement parent, CssBlock css) {
		css.run();
		
		ArrayList<CSSElement> listChildCSSElement = new ArrayList<>();
		while (true) {
			CSSElement s = list.getLast();
			if (s == parent)
				break;
			list.removeLast();
			listChildCSSElement.add(s);
		}
		
		LinkedList<CSSStyleRow> listStyleToAdd = new LinkedList<>();

		for (int i = listChildCSSElement.size() - 1; i >= 0; i--) {
			CSSElement child = listChildCSSElement.get(i);
			
			if (parent instanceof CSSMedia)
			{
				LinkedList<CSSStyleRow> pathchild = child.getListStyle();

				for (CSSStyleRow rowChild : pathchild) {
					listStyleToAdd.add(rowChild);
				}
			}
			else
			{
			doSelector(parent, listStyleToAdd, child);
		  }
		}
		
		for (CSSStyleRow cssStyleRow : listStyleToAdd) {
			parent.getListStyle().add(cssStyleRow);
		}

		return parent;
	}

	/**
	 * @param parent
	 * @param listStyleToAdd
	 * @param child
	 */
	default void doSelector(CSSElement parent, LinkedList<CSSStyleRow> listStyleToAdd, CSSElement child) {
		for (CSSStyleRow rowParent : parent.getListStyle()) {
			String[] or = rowParent.path.toString().split(",");
			for (int j = 0; j < or.length; j++) {
				String parentpath = or[j];

				LinkedList<CSSStyleRow> pathchild = child.getListStyle();

				for (CSSStyleRow rowChild : pathchild) {
					String childpath = rowChild.path.toString();
					String[] orChild = childpath.split(",");
					int nb = 0;
					
					StringBuilder strb = new StringBuilder();
					for (String porChild : orChild) {
						if (nb > 0)
							strb.append(",");
						strb.append(parentpath);
						if (porChild.startsWith("&")) {
							strb.append(porChild.substring(1));
						} else {
							strb.append(" ");
							strb.append(porChild);
						}
						nb++;
					}

					listStyleToAdd.add(new CSSStyleRow(CSSSelector.onPath(strb.toString()), rowChild.content));

				}
			}
		}
	}
	
	default CSSElement sSel(Object... selector) {
		CSSElement dom = new CSSElement();
		list.add(dom);
		return dom.path(selector);
	}

	default void css(Object... selector) {
		String content = selector[0].toString();
		list.getLast().set(content);

	}

	default CSSElement sMedia(Object... selector) {
		CSSMedia dom = new CSSMedia();
		list.add(dom);
		return dom.path("@media "+ selector[0]);
	}

	default CSSElement sKeyFrame(Object... selector) {
		CSSMedia dom = new CSSMedia();
		list.add(dom);
		return dom.path("@keyframes "+ selector[0]);
	}

	default void from(CssBlock css) {

	}

	default void to(CssBlock css) {

	}

	default void prct(Object value, CssBlock css) {

	}

	interface CssBlock extends Runnable {

	}
	
}
