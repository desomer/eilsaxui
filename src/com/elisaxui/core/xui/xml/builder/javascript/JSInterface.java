package com.elisaxui.core.xui.xml.builder.javascript;

public interface JSInterface {

	JSInterface __(Object... content);

	JSInterface set(Object name, Object... content);

	JSInterface var(Object name, Object... content);

	Object _new(Object... param);
}