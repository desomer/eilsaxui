/**
 * 
 */
package com.elisaxui.core.xui.xml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD} )
public @interface xPriority {

	int value();

}
