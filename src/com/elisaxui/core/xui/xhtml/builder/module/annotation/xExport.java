/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.module.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
public @interface xExport {
	 public Class<?> idClass() default Object.class;   // optionnel
}
