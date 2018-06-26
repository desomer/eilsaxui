package com.elisaxui.core.xui.xhtml.builder.javascript.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.elisaxui.core.xui.xhtml.builder.javascript.mount.MountFactory;
/**
 * permet de lier une method JS a un attribut de JSON
 *    a utiliser avec vMount
 *   ex : 		 vMount(aPage, aPage.mountArticle()))
 * @author gauth
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD})
public @interface xMount {

	Class<? extends MountFactory> value();

}
