/**
 * 
 */
package com.elisaxui.core.xui.xhtml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elisaxui.core.helper.ClassLoaderHelper;
import com.elisaxui.core.helper.ReflectionHelper;
import com.elisaxui.core.helper.ClassLoaderHelper.FileEntry;
import com.elisaxui.core.xui.XUILaucher;
import com.elisaxui.core.xui.xhtml.builder.html.XClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSBuilder;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassInterface;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSVariable;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xml.XMLPart;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xFile;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * @author Bureau
 *
 */
public class XHTMLAppBuilder {

	public static int nbChangement = 0;
	public static long lastOlderFile = 0;
	public static LocalDateTime dateBuild = null;
	
	private static final boolean debug = false;
	
	// TODO mettre en cache
	public static synchronized Map<String, Class<? extends XHTMLPart>>  getMapXHTMLPart() {
				
		List<Class<? extends XHTMLPart>> listXHTMLPart = new ArrayList<>(100);
		List<Class<? extends JSClass>> listJSClass = new ArrayList<>(100);
		List<Class<? extends JSClassInterface>> listJSClassMethod = new ArrayList<>(100);
		
		long olderFile = 0;
		try {
			Iterable<FileEntry> list = ClassLoaderHelper.listFilesRelativeToClass(XUILaucher.class,	"com"); ///elisaxui/core/xui/xml
			for (FileEntry fileEntry : list) {
				long lm = fileEntry.file.lastModified();
				if (debug)
					System.out.println("[XHTMLAppBuilder]file scan for date " +  fileEntry.file);
				if (lm > olderFile)
					olderFile = lm;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lastOlderFile!=olderFile)
		{
			lastOlderFile=olderFile;
			nbChangement++;
		}
		
		Date input = new Date(olderFile);
		Instant instant = input.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		dateBuild = zdt.toLocalDateTime();
		if (debug)
			System.out.println("[XHTMLAppBuilder]********************************************* END SCAN FILE ****************************************");

		/** TODO a changer car scan deja au dessus pour calcul dateBuild **/ 
		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(XHTMLPart.class, listXHTMLPart::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(JSClassInterface.class, listJSClassMethod::add).scan();
		
		if (debug)
			System.out.println("[XHTMLAppBuilder]********************************************* START SCAN XHTMLPart ************************************");

		Map<String, Class<? extends XHTMLPart>> mapClass = new HashMap<String, Class<? extends XHTMLPart>>(100);
		for (Class<? extends XHTMLPart> pageClass : listXHTMLPart) {
			xFile annPage = pageClass.getAnnotation(xFile.class);
			if (annPage != null) { 
				if (debug) {
					System.out.println("[XHTMLAppBuilder]##############################################################################################");
					System.out.println("[XHTMLAppBuilder]#############################    PAGE   "+annPage.id()+"   ###################################"+ annPage.id());
				}
				mapClass.put(annPage.id(), pageClass);
			}
			initXMLPartVarStatic(pageClass);
		}
		
		if (debug)
			System.out.println("[XHTMLAppBuilder]********************************************* START SCAN JSClassMethod ************************************");

		for (Class<? extends JSClassInterface> class1 : listJSClassMethod) {
			if (debug)
				System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClassMethod -----"+ class1);
			initJSClassVar(XHTMLPart.jsBuilder, class1);
		}
		
		if (debug)
			System.out.println("[XHTMLAppBuilder]********************************************* START SCAN JSClass ************************************");

		for (Class<? extends JSClass> class1 : listJSClass) {
			if (debug)
				System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClass -----"+ class1);
			initJSClassVar(XHTMLPart.jsBuilder, class1);
		}
		
		if (debug)
			System.out.println("[XHTMLAppBuilder]********************************************* END SCAN JSClass ****************************************");
		
		return mapClass;
	}
	
	
	private static void initXMLPartVarStatic(Class<? extends XMLPart>  cl) {
		if (debug)
			System.out.println("[XHTMLAppBuilder]---------START SCAN FIELD OF XHTMLPart  [JSClass, CSSClass]---- "+ cl);
		
		Field[] lf = cl.getDeclaredFields();
		if (lf!=null)
		{
			for (Field field : lf) {
				boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
				if (isStatic && JSClass.class.isAssignableFrom(field.getType()))
				{
					if (debug)
						System.out.println("[XHTMLAppBuilder] init XMLPart var static <JSClass> name "+ field.getName() );
					field.setAccessible(true);
					@SuppressWarnings("unchecked")
					JSClass inst = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) field.getType());
					XHTMLPart.jsBuilder.setNameOfProxy("", inst, field.getName());
					try {
						field.set(cl, inst);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if (isStatic && XClass.class.isAssignableFrom(field.getType()))
				{
					if (debug)
						System.out.println("[XHTMLAppBuilder] init XMLPart var static <CSSClass> name "+ field.getName() );
					XClass classCss = new XClass();
					String name = field.getName();
					xComment comment = field.getAnnotation(xComment.class);
					if (comment != null) {
						name = comment.value();
					}
					classCss.setId(name);
					field.setAccessible(true);
					try {
						field.set(cl,classCss);
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						System.out.println("**** ERROR on "+ field.getName() + " of type " + field.getType().getName() + " on class "+cl.getName());
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
	
	public static void initJSClassVar(JSBuilder jsBuilder, Class<?> cl) {
	//	String name = cl.getSimpleName();
		// init field => chaque attribut contient le nom js de son champs
		Field[] listField = cl.getDeclaredFields();
		if (listField != null) {
			for (Field field : listField) {
				initJSClassField(cl, field);
			}
			
		}
	}
	

	private static void initJSClassField(Class<?> cl, Field field) {
		
//		int mod = field.getModifiers();
//		boolean isTransient =  Modifier.isTransient(mod);
//		boolean isFinal =  Modifier.isFinal(mod);
//		boolean isNative =  Modifier.isNative(mod);
		
		if (debug)
			System.out.print("[XHTMLAppBuilder]init JSClass/JSClassMethod var "+ field.getName() + " de type "+ field.getType());
		
		if (JSClass.class.isAssignableFrom(field.getType())) {
			// gestion particuliere d'un proxy pour affecter le nom
			// au proxy
			@SuppressWarnings("unchecked")
			
			JSClass prox = XHTMLPart.jsBuilder.getProxy((Class<? extends JSClass>) field.getType());
			setProxyName(field, prox);
			
			try {
				ReflectionHelper.setFinalStatic(field, prox);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (JSVariable.class.isAssignableFrom(field.getType())) {
			try {
				
				Object v = field.get(cl);
				
				if (v==null)
				{
					JSVariable var =(JSVariable) field.getType().newInstance();
					setVarName(field, var);
					  
					ReflectionHelper.setFinalStatic(field, var);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			if (debug)
				System.out.print(" ************* NO JAVASCRIPT ************* ");
//			// affecte le nom de la variable
//			try {
//				if (field.getName().startsWith("_"))
//					ReflectionHelper.setFinalStatic(field, field.getName().substring(1));
//				else
//					ReflectionHelper.setFinalStatic(field, "this." + field.getName());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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
			XHTMLPart.jsBuilder.setNameOfProxy("", prox, name.substring(1));
		} else
			XHTMLPart.jsBuilder.setNameOfProxy("this.", prox, name);
	}


	private static void setVarName(Field field, JSVariable var) {
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
