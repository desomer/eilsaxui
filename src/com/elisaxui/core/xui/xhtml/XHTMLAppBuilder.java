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
import com.elisaxui.core.xui.xhtml.builder.javascript.JSClassMethod;
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
	
	public static Map<String, Class<? extends XHTMLPart>> getMapXHTMLPart() {
		
		List<Class<? extends XHTMLPart>> listXHTMLPart = new ArrayList<>(100);
		List<Class<? extends JSClass>> listJSClass = new ArrayList<>(100);
		List<Class<? extends JSClassMethod>> listJSClassMethod = new ArrayList<>(100);
		
		long olderFile = 0;
		try {
			Iterable<FileEntry> list = ClassLoaderHelper.listFilesRelativeToClass(XUILaucher.class,	"com"); ///elisaxui/core/xui/xml
			for (FileEntry fileEntry : list) {
				long lm = fileEntry.file.lastModified();
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
		
		System.out.println("[XHTMLAppBuilder]********************************************* END SCAN FILE ****************************************");

		
		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(XHTMLPart.class, listXHTMLPart::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubinterfacesOf(JSClass.class, listJSClass::add).scan();
		new FastClasspathScanner("com.elisaxui").matchSubclassesOf(JSClassMethod.class, listJSClassMethod::add).scan();
		
		System.out.println("[XHTMLAppBuilder]********************************************* START SCAN XHTMLPart ************************************");

		Map<String, Class<? extends XHTMLPart>> mapClass = new HashMap<String, Class<? extends XHTMLPart>>(100);
		for (Class<? extends XHTMLPart> pageClass : listXHTMLPart) {
			
			initXMLPartVarStatic(pageClass);
			
			xFile annPage = pageClass.getAnnotation(xFile.class);
			if (annPage != null) {
				mapClass.put(annPage.id(), pageClass);
			}
		}
		
		System.out.println("[XHTMLAppBuilder]********************************************* START SCAN JSClassMethod ************************************");

		for (Class<? extends JSClassMethod> class1 : listJSClassMethod) {
			System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClassMethod -----"+ class1);
			initJSClassVar(XHTMLPart.jsBuilder, class1);
		}
		
		System.out.println("[XHTMLAppBuilder]********************************************* START SCAN JSClass ************************************");

		for (Class<? extends JSClass> class1 : listJSClass) {
			System.out.println("[XHTMLAppBuilder]------------ START SCAN FIELD OF JSClass -----"+ class1);
			initJSClassVar(XHTMLPart.jsBuilder, class1);
		}

		System.out.println("[XHTMLAppBuilder]********************************************* END SCAN JSClass ****************************************");
		return mapClass;
	}
	
	
	private static void initXMLPartVarStatic(Class<? extends XMLPart>  cl) {
		System.out.println("[XHTMLAppBuilder]---------START SCAN FIELD OF XHTMLPart  [JSClass, CSSClass]---- "+ cl);
		
		Field[] lf = cl.getDeclaredFields();
		if (lf!=null)
		{
			for (Field field : lf) {
				boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
				if (isStatic && JSClass.class.isAssignableFrom(field.getType()))
				{
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
				initJSClassField(field);
			}
			
		}
	}
	

	private static void initJSClassField(Field field) {
		
//		int mod = field.getModifiers();
//		boolean isTransient =  Modifier.isTransient(mod);
//		boolean isFinal =  Modifier.isFinal(mod);
//		boolean isNative =  Modifier.isNative(mod);
		
		
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
				JSVariable var =(JSVariable) field.getType().newInstance();
				setVarName(field, var);
					  
			    ReflectionHelper.setFinalStatic(field, var);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
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
			var.setName(name.substring(1));
		else
			var.setName("this." + name);
	}
	
}
