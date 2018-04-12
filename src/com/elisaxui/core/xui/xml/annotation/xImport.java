/**
 * 
 */
package com.elisaxui.core.xui.xml.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
@Repeatable(xImportList.class)
public @interface xImport {
	 public String export();
	 public String module();
}