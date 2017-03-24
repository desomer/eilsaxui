package com.elisaxui.core.xui.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.elisaxui.core.xui.xml.builder.XMLTarget;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD, ElementType.METHOD} )
public @interface xTarget {
	 public Class<? extends XMLTarget> value() default XMLTarget.class;
}
