/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ajoute la methode a la class meme si pas d'appel
 * @author gauth
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
public @interface xForceInclude {
}
