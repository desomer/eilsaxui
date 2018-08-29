/**
 * 
 */
package com.elisaxui.core.xui.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.elisaxui.core.helper.ClassLoaderHelper;
import com.elisaxui.core.helper.ClassLoaderHelper.FileEntry;
import com.elisaxui.core.helper.ReflectionHelper;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.XUILaucher;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSAny;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.JSArray;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xCoreVersion;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.builder.VProperty;

/**
 * @author Bureau
 * 
 *   a optimiser avec le wathdir
 *   	ajouter les fichier scanner dans le XHTMLChangeManager changeInfo
 *      faire des map avec listXHTMLPart, listJSClass ,  listJSClassMethod pour ajouter le nouveau provenant du watchdir
 */

public class XHTMLAppScanner {

	private static final boolean debug = false;
	private static final ArrayList<String> listURL = new ArrayList<>();

	/**
	 * @return the listurl
	 */
	public static final ArrayList<String> getListurl() {
		return listURL;
	}

	public static synchronized XHTMLChangeManager getMapXHTMLPart(XHTMLChangeManager changeInfo) {

		boolean isInject = changeInfo.mapClass.isEmpty();
		
		CoreLogger.getLogger(1).info("************ INJECTION DEPENDANCE JS");
		
		
		List<Class<? extends XHTMLPart>> listXHTMLPart = new ArrayList<>(100);
		List<Class<? extends JSClass>> listJSClass = new ArrayList<>(100);
		List<Class<? extends JSAny>> listJSClassMethod = new ArrayList<>(100); 

		Date now = new Date();
		Instant instant = now.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		changeInfo.dateInjection = zdt.toLocalDateTime();
		
		changeInfo.listFileChanged.clear();

		long olderFile = 0;
		long last = CacheManager.getLastDate();

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
				
				if (isInject && !fileEntry.file.isDirectory() && path.endsWith(".class")) {
					try {
						int idxs = path.indexOf("com");
						int idxf = path.lastIndexOf(".");
						String classname = path.substring(idxs, idxf);
						classname = classname.replace(File.separatorChar, '.');

						Class c = Thread.currentThread().getContextClassLoader().loadClass(classname);
						if (XHTMLPart.class.isAssignableFrom(c))
							listXHTMLPart.add(c);
						
						else if (c.isInterface() && JSClass.class.isAssignableFrom(c))
							listJSClass.add(c);
						
						else if (JSAny.class.isAssignableFrom(c))
							listJSClassMethod.add(c);
						
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CoreLogger.getLogger(1).info("************ INJECTION DEPENDANCE JS SCAN OK");
		
		if (changeInfo.lastOlderFile != olderFile) {
			changeInfo.lastOlderFile = olderFile;
			changeInfo.nbChangement++;
		}
		CacheManager.setLastDate(changeInfo.lastOlderFile);

		Date input = new Date(olderFile);
		instant = input.toInstant();
		zdt = instant.atZone(ZoneId.systemDefault());
		changeInfo.dateBuild = zdt.toLocalDateTime();

		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* END SCAN FILE ****************************************");

		// new FastClasspathScanner("com.elisaxui").matchSubclassesOf(XHTMLPart.class,
		// listXHTMLPart::add).scan();
//		new FastClasspathScanner("com.elisaxui").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
//		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(JSAny.class, listJSClassMethod::add)
//				.scan();

		listURL.clear();
		doInjectVariable(changeInfo, listXHTMLPart, listJSClass, listJSClassMethod);

		
		CoreLogger.getLogger(1).info("************** FIN INJECTION DEPENDANCE JS ");
		return changeInfo;
	}

	/**
	 * @param changeInfo
	 * @param listXHTMLPart
	 * @param listJSClass
	 * @param listJSClassMethod
	 */
	private static void doInjectVariable(XHTMLChangeManager changeInfo, List<Class<? extends XHTMLPart>> listXHTMLPart,
			List<Class<? extends JSClass>> listJSClass, List<Class<? extends JSAny>> listJSClassMethod) {
		if (debug)
			System.out.println(
					"[XHTMLAppBuilder]********************************************* START SCAN XHTMLPart ************************************");

		for (Class<? extends XHTMLPart> pageClass : listXHTMLPart) {
			xResource annPage = pageClass.getAnnotation(xResource.class);
			if (annPage != null) {
				if (debug) {
					System.out.println(
							"[XHTMLAppBuilder]##############################################################################################");
					System.out.println("[XHTMLAppBuilder]#############################    PAGE   " + annPage.id()
							+ "   ###################################" + annPage.id());
				}
				changeInfo.mapClass.put(annPage.id(), pageClass);
				CoreLogger.getLogger(1).info("Register "+annPage.id());
				listURL.add("https://localhost:9998/rest/page/fr/fra/id/"+annPage.id());
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

		String name = cl.getSimpleName()+"."+field.getName();
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

		xCoreVersion coreVersion = cl.getAnnotation(xCoreVersion.class);
		String version = coreVersion==null?"0":coreVersion.value();
		
		if (JSClass.class.isAssignableFrom(field.getType())) {
			try {
				Object v = ReflectionHelper.getFinalStatic(field, cl);
				if (v == null) {
					// gestion particuliere d'un proxy pour affecter le nom au proxy
					@SuppressWarnings("unchecked")
					JSClass prox = ProxyHandler.getProxy((Class<? extends JSClass>) field.getType());
					setProxyName(field, prox, version);
				    ReflectionHelper.setFinalStatic(field, prox);
				}
			} catch (Exception e1) {
				CoreLogger.getLogger(0).log(Level.SEVERE, "PB on "+cl, e1);
				
			} 
			
		} else if (JSAny.class.isAssignableFrom(field.getType())) {
			try {

				Object v = field.get(cl);

				if (v == null) {
					JSAny var = (JSAny) field.getType().newInstance();
					setVarName(field, var, version);

					ReflectionHelper.setFinalStatic(field, var);
				}
				else if (v instanceof JSArray && !version.equals("0"))
				{	// gestion du type des tableau
					setVarName(field, (JSAny)v, version);   
					ReflectionHelper.setFinalStatic(field, v);
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

	private static void setProxyName(Field field, JSClass prox, String version) {
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		if (name.startsWith("_") || !version.equals("0")) {
			ProxyHandler.setNameOfProxy("", prox, name.substring(!version.equals("0")?0:1));
		} else
			ProxyHandler.setNameOfProxy("this.", prox, name);
	}

	private static void setVarName(Field field, JSAny var, String version) {
		String name = field.getName();
		xComment comment = field.getAnnotation(xComment.class);
		if (comment != null) {
			name = comment.value();
		}
		if (name.startsWith("_")|| !version.equals("0"))
			var._setName(name.substring(!version.equals("0")?0:1));
		else
			var._setName("this." + name);
	}

	

}
