/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.json;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xml.builder.IXMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;

/**
 * @author gauth
 *
 */
public class JSJson implements IXMLBuilder {

	String unformattedJsonString;

	/**
	 * @return the unformattedJsonString
	 */
	public final String getUnformattedJsonString() {
		return unformattedJsonString;
	}

	@Override
	public String toString() {
		return "JSJson [json=" + unformattedJsonString + "]";
	}

	/**
	 * @param json
	 */
	public JSJson(String json) {
		super();
		this.unformattedJsonString = json;
	}

	@Override
	public XMLBuilder toXML(XMLBuilder buf) {

		if (unformattedJsonString.length() == 2 && unformattedJsonString.equals("{}")) {
			buf.addContentOnTarget(unformattedJsonString);
			
		} else {
			StringBuilder prettyJSONBuilder = new StringBuilder();
			int indentLevel = 0;
			int lastStart = 0;
			int i = 0;
			boolean inQuote = false;
			for (char charFromUnformattedJson : unformattedJsonString.toCharArray()) {
				i++;
				switch (charFromUnformattedJson) {
				case '"':
					// switch the quoting status
					inQuote = !inQuote;
					prettyJSONBuilder.append(charFromUnformattedJson);
					break;
				case ' ':
					// For space: ignore the space if it is not being quoted.
					if (inQuote) {
						prettyJSONBuilder.append(charFromUnformattedJson);
					}
					break;
				case '{':
				case '[':
					// Starting a new block: increase the indent level
					prettyJSONBuilder.append(charFromUnformattedJson);
					indentLevel++;
					lastStart = i;
					appendIndentedNewLine(buf, indentLevel, prettyJSONBuilder);
					break;
				case '}':
				case ']':
					// Ending a new block; decrese the indent level
					indentLevel--;
					if (i - 1 > lastStart)
						appendIndentedNewLine(buf, indentLevel, prettyJSONBuilder);
					prettyJSONBuilder.append(charFromUnformattedJson);
					break;
				case ',':
					// Ending a json item; create a new line after
					prettyJSONBuilder.append(charFromUnformattedJson);
					if (!inQuote) {
						appendIndentedNewLine(buf, indentLevel, prettyJSONBuilder);
					}
					break;
				default:
					prettyJSONBuilder.append(charFromUnformattedJson);
				}
			}

			buf.addContentOnTarget(prettyJSONBuilder.toString());
		}

		return buf;
	}

	/*
	 * Print a new line with indention at the beginning of the new line.
	 * 
	 * @param indentLevel
	 * 
	 * @param stringBuilder
	 */
	private void appendIndentedNewLine(XMLBuilder buf, int indentLevel, StringBuilder stringBuilder) {
		buf.addContentOnTarget(stringBuilder.toString());
		stringBuilder.setLength(0);
		ProxyHandler.getFormatManager().newLine(buf);
		ProxyHandler.getFormatManager().newTabInternal(buf);
		for (int i = 0; i < indentLevel; i++) {
			// Assuming indention using 2 spaces
			stringBuilder.append("  ");
		}
	}

}
