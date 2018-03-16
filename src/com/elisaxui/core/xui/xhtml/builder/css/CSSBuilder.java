/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.css;

import java.util.ArrayList;
import java.util.LinkedList;

import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;

/**
 * @author gauth
 *
 */
public class CSSBuilder {

	LinkedList<CSSElement> list = new LinkedList<>();

	protected CSSElement on(CSSElement parent, CssBlock css) {
		css.run();

		ArrayList<CSSElement> listChildCSSElement = new ArrayList<CSSElement>();
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
			for (CSSStyleRow row : parent.getListStyle()) {
				String[] or = row.path.toString().split(",");
				for (int j = 0; j < or.length; j++) {
					String parentpath = or[j];
					LinkedList<CSSStyleRow> pathchild = child.getListStyle();

					for (CSSStyleRow cssStyle : pathchild) {
						String path = cssStyle.path.toString();
						
						if (path.startsWith("&")) {
							listStyleToAdd.add(new CSSStyleRow(
									CSSSelector.onPath(parentpath, path.substring(1)),
									cssStyle.content));
						} else {
							listStyleToAdd.add(new CSSStyleRow(
									CSSSelector.onPath(parentpath, " ", path), cssStyle.content));
						}
					}
				}
			}

			// String[] or = path.split(",");
			//
			// for (int j = 0; j < or.length; j++) {
			// String subpath = or[j];
			//
			// if (subpath.startsWith("&"))
			// {
			// CSSElement childbis = new CSSElement();
			//
			// for (CSSStyleRow row : child.getListStyle()) {
			// childbis.getListStyle().add(new CSSStyleRow(subpath.substring(1),
			// row.content));
			// }
			//
			// selector.and(childbis);
			// }
			// else
			// {
			// CSSElement childbis = new CSSElement();
			//
			// for (CSSStyleRow row : child.getListStyle()) {
			// childbis.getListStyle().add(new CSSStyleRow(subpath, row.content));
			// }
			// selector.andChild(childbis);
			// }
			// }

		}
		for (CSSStyleRow cssStyleRow : listStyleToAdd) {
			parent.getListStyle().add(cssStyleRow);
		}

		return parent;
	}

	protected CSSElement sel(Object... selector) {
		CSSElement dom = new CSSElement();
		list.add(dom);
		return dom.path(selector);
	}

	protected void css(Object... selector) {
		String content = selector[0].toString();
		list.getLast().set(content);

	}

	protected CSSElement media(Object... selector) {
		return null;
	}

	protected CSSElement keyFrame(Object selector) {
		return null;
	}

	protected void from(CssBlock css) {

	}

	protected void to(CssBlock css) {

	}

	protected void prct(Object value, CssBlock css) {

	}

	public interface CssBlock extends Runnable {

	}

}
