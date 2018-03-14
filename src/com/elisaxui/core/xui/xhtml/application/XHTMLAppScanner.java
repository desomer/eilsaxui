/**
 * 
 */
package com.elisaxui.core.xui.xhtml.application;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.elisaxui.core.helper.ClassLoaderHelper;
import com.elisaxui.core.helper.ClassLoaderHelper.FileEntry;
import com.elisaxui.core.helper.ReflectionHelper;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.XUILaucher;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.IJSClassInterface;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;
import com.elisaxui.core.xui.xml.builder.VProperty;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * @author Bureau
 *
 */
public class XHTMLAppScanner {

	private static final boolean debug = false;

	// TODO mettre en cache
	public static synchronized XHTMLChangeManager getMapXHTMLPart(XHTMLChangeManager changeInfo) {

		List<Class<? extends XHTMLPart>> listXHTMLPart = new ArrayList<>(100);
		List<Class<? extends JSClass>> listJSClass = new ArrayList<>(100);
		List<Class<? extends JSAny>> listJSClassMethod = new ArrayList<>(100); /**TODO a retirer */

		changeInfo.listFileChanged.clear();

		long olderFile = 0;
		long last = XUIFactoryXHtml.getLastDate();

		try {
			Iterable<FileEntry> list = ClassLoaderHelper.listFilesRelativeToClass(XUILaucher.class, "com");
			for (FileEntry fileEntry : list) {
				long lm = fileEntry.file.lastModified();
				if (debug)
					System.out.println("[XHTMLAppBuilder]file scan for date " + fileEntry.file);
				if (lm > olderFile)
					olderFile = lm;

				if (lm > last)
					changeInfo.listFileChanged.add(fileEntry.file);

				String path = fileEntry.file.getPath();
				
				if (!fileEntry.file.isDirectory() && path.endsWith(".class")) {
					try {
						int idxs = path.indexOf("com");
						int idxf = path.lastIndexOf(".");
						String classname = path.substring(idxs, idxf);
						classname = classname.replace(File.separatorChar, '.');

						Class c = Thread.currentThread().getContextClassLoader().loadClass(classname);
						if (XHTMLPart.class.isAssignableFrom(c))
							listXHTMLPart.add(c);
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (changeInfo.lastOlderFile != olderFile) {
			changeInfo.lastOlderFile = olderFile;
			changeInfo.nbChangement++;
		}
		XUIFactoryXHtml.setLastDate(changeInfo.lastOlderFile);

		Date input = new Date(olderFile);
		Instant instant = input.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		changeInfo.dateBuild = zdt.toLocalDateTime();

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* END SCAN FILE ****************************************");

		/** TODO a changer car scan deja au dessus pour calcul dateBuild **/
		// new FastClasspathScanner("com.elisaxui").matchSubclassesOf(XHTMLPart.class,
		// listXHTMLPart::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(JSAny.class, listJSClassMethod::add)
				.scan();

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* START SCAN XHTMLPart ************************************");

		for (Class<? extends XHTMLPart> pageClass : listXHTMLPart) {
			xFile annPage = pageClass.getAnnotation(xFile.class);
			if (annPage != null) {
				if (debug) {
					System.out.println(
							"[XHTMLAppBuilder]##############################################################################################");
					System.out.println("[XHTMLAppBuilder]#############################    PAGE   " + annPage.id()
							+ "   ###################################" + annPage.id());
				}
				changeInfo.mapClass.put(annPage.id(), pageClass);
			}
			initXMLPartVarStatic(pageClass);
		}

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* START SCAN JSClassMethod ************************************");

		for (Class<? extends JSAny> class1 : listJSClassMethod) {
			if (debug)
				System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClassInterface -----" + class1);
			initJSClassVar(class1);
		}

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* START SCAN JSClass ************************************");

		for (Class<? extends JSClass> class1 : listJSClass) {
			if (debug)
				System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClass -----" + class1);
			initJSClassVar(class1);
		}

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* END SCAN JSClass ****************************************");

		return changeInfo;
	}

	private static void initXMLPartVarStatic(Class<? extends XMLPart> cl) {
		if (debug)
			System.out
					.println("[XHTMLAppBuilder]---------START SCAN FIELD OF XHTMLPart  [JSClass, CSSClass]---- " + cl);

		initVar(true, cl, cl);
	}

	
	private static final Field[] getXMLField(Class<?> c)
	{
		ArrayList<Field> alf = new ArrayList<Field>(10);

		while (XMLPart.class.isAssignableFrom(c)) {
			Field[] lf = c.getDeclaredFields();
			if (lf!=null)
			{
				for (Field field : lf) {
					alf.add(field);
				}
			}
			c=c.getSuperclass();
		}
		
		Field[] a = new Field[alf.size()];
		alf.toArray(a);
		return a;
	}
	
	public static final void initVar(boolean doStatic, Class<? extends XMLPart> cl, Object obj) {
		Field[] lf = getXMLField(cl);
		for (Field field : lf) {
			
			boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
			if (doStatic==isStatic && JSClass.class.isAssignableFrom(field.getType()))
			{
				doVarJSClass(cl, obj, field);
			}
			else if (doStatic==isStatic && CSSClass.class.isAssignableFrom(field.getType()))
			{
				doVarXClass(cl, obj, field); 
			}
			else if (doStatic==isStatic && JSAny.class.isAssignableFrom(field.getType()))
			{
				doVarJSAny(cl, obj, field); 
			}
			else if (doStatic==isStatic && VProperty.class.isAssignableFrom(field.getType()))
			{
				doVarProperties(cl, obj, field); 
			}
		}
	}

	/**
	 * @param cl
	 * @param obj
	 * @param field
	 */
	private static void doVarJSClass(Class<? extends XMLPart> cl, Object obj, Field field) {
		if (debug)
			System.out.println("[XMLPart] init var JSClass on " + cl + " name "+ field.getName() );
		field.setAccessible(true);
		@SuppressWarnings("unchecked")
		JSClass inst = ProxyHandler.getProxy((Class<? extends JSClass>) field.getType());
		ProxyHandler.setNameOfProxy("", inst, field.getName());
		try {
			field.set(obj, inst);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param cl
	 * @param obj
	 * @param field
	 */
	private static void doVarJSAny(Class<? extends XMLPart> cl, Object obj, Field field) {
		if (debug)
			System.out.println("[XMLPart] init var JSVariable on " + cl + " name "+ field.getName() );
		JSAny variablejs=null;
		try {
			variablejs = (JSAny) field.getType().newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		variablejs._setName(name);
		field.setAccessible(true);
		try {
			field.set(obj,variablejs);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param cl
	 * @param obj
	 * @param field
	 */
	private static void doVarXClass(Class<? extends XMLPart> cl, Object obj, Field field) {
		if (debug)
			System.out.println("[XMLPart] init var CSSClass on " + cl + " name "+ field.getName() );
		CSSClass classCss = new CSSClass();
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		classCss.setId(name);
		field.setAccessible(true);
		try {
			field.set(obj,classCss);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static void doVarProperties(Class<? extends XMLPart> cl, Object obj, Field field) {
		if (debug)
			System.out.println("[XMLPart] init var VProperty on " + cl + " name "+ field.getName() );

		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		VProperty v = new VProperty(name);
		field.setAccessible(true);
		try {
			field.set(obj,v);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void initJSClassVar(Class<?> cl) {
		// init field => chaque attribut contient le nom js de son champs
		Field[] listField = cl.getDeclaredFields();
		if (listField != null) {
			for (Field field : listField) {
				initJSClassField(cl, field);
			}

		}
	}

	private static void initJSClassField(Class<?> cl, Field field) {

		// int mod = field.getModifiers();
		// boolean isTransient = Modifier.isTransient(mod);
		// boolean isFinal = Modifier.isFinal(mod);
		// boolean isNative = Modifier.isNative(mod);

		if (debug)
			System.out.print("[XHTMLAppBuilder]init JSClass/JSClassMethod var " + field.getName() + " de type "
					+ field.getType());

		if (JSClass.class.isAssignableFrom(field.getType())) {
			// gestion particuliere d'un proxy pour affecter le nom
			// au proxy
			@SuppressWarnings("unchecked")

			JSClass prox = ProxyHandler.getProxy((Class<? extends JSClass>) field.getType());
			setProxyName(field, prox);

			try {
				ReflectionHelper.setFinalStatic(field, prox);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (JSAny.class.isAssignableFrom(field.getType())) {
			try {

				Object v = field.get(cl);

				if (v == null) {
					JSAny var = (JSAny) field.getType().newInstance();
					setVarName(field, var);

					ReflectionHelper.setFinalStatic(field, var);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (debug)
				System.out.print(" ************* NO JAVASCRIPT ************* ");
			// // affecte le nom de la variable
			// try {
			// if (field.getName().startsWith("_"))
			// ReflectionHelper.setFinalStatic(field, field.getName().substring(1));
			// else
			// ReflectionHelper.setFinalStatic(field, "this." + field.getName());
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		if (debug)
			System.out.println("");
	}

	private static void setProxyName(Field field, JSClass prox) {
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		if (name.startsWith("_")) {
			ProxyHandler.setNameOfProxy("", prox, name.substring(1));
		} else
			ProxyHandler.setNameOfProxy("this.", prox, name);
	}

	private static void setVarName(Field field, JSAny var) {
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		if (name.startsWith("_"))
			var._setName(name.substring(1));
		else
			var._setName("this." + name);
	}

	

}
