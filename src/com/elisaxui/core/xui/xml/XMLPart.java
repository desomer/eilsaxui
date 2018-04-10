package com.elisaxui.core.xui.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.elisaxui.AppConfig;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLRootResource;
import com.elisaxui.core.xui.xhtml.application.XHTMLAppScanner;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.Handle;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.target.MODULE;
import com.elisaxui.core.xui.xml.target.XMLTarget;
import com.elisaxui.core.xui.xml.target.XMLTarget.ITargetRoot;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart  {
	/**
	 * 
	 */
	public static final double PRECISION = 100000.0;
	public static final String NONAME = null;

	private static final boolean debug = false;
	
	/**************************************************************************/
	protected HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>> listPart = new HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>>();
	protected HashMap<Object, Object> listProperties = new HashMap<Object, Object>(); 
	
	public static final String PROP_ID = "PROP_ID";
	public static final String PROP_CHILDREN = "PROP_CHILDREN";
	/**
	 * @return the propertiesPrefix
	 */
	public final String getPropertiesPrefix() {
		return vProperty(PROP_ID);
	}


	/**************************************************************************/
	public XMLPart vProperty(Object key, Object value)
	{
		listProperties.put(key.toString(), value);
		return this;
	}
	
	/**
	 * ajout de property multiple
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public XMLPart vProp(Object key, Object value)
	{
		Object obj = listProperties.get(key.toString());
		if (obj!=null)
		{
			if (obj instanceof List)
			{
				((List<Object>)obj).add(value);
			}
			else
			{
				List<Object> l = new ArrayList<>();
				l.add(obj);
				listProperties.put(key.toString(), l);
			}
		}
		else
			listProperties.put(key.toString(), value);
		return this;
	}
	
	
	@SuppressWarnings("unchecked")
	public <E extends Object> E  vProperty(Object key)
	{
		return (E) listProperties.get(key.toString());
	}
	
	public XMLElement vPropertyElement(Object key)
	{
		return (XMLElement)listProperties.get(key.toString());
	}
	
	public XMLAttr vPropertyAttr(Object key)
	{
		return (XMLAttr)listProperties.get(key.toString());
	}
	
	/*********************************************************************************/
	public final void addElementOnTarget(Class<? extends XMLTarget> target, XMLElement value) {
		ArrayList<XMLElement> partData = listPart.get(target);
		if (partData == null) {
			partData = new ArrayList<>(100);
			listPart.put(target, partData);
		}
		partData.add(value);

	}
	
	public final void addImportOnTarget(String idResource, Class<? extends XMLTarget> target, XMLElement elem) {
		
		String ext = idResource.substring(idResource.indexOf(".")+1);
		XMLFile file = XUIFactoryXHtml.getXHTMLFile();
		long date = XUIFactoryXHtml.changeMgr.lastOlderFile;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-hhmmss");
		String textdate = formatter.format(new Date(date));
		String fn = null;
		if (target == MODULE.class)
			fn= idResource;
		else
			fn=textdate+"_"+idResource;
		
		final String name = fn;
		
		XMLFile subfile =   file.listSubFile.computeIfAbsent(name, keyResource -> {

			if (ext.equals("css"))
				addElementOnTarget(target, (XMLElement)(XHTMLPart.xLinkCss("/rest/css/"+name).setPriority(elem.getPriority())));
			else
				addElementOnTarget(target, (XMLElement)(XHTMLPart.xScriptSrc("/rest/js/"+name).setPriority(elem.getPriority())));
			
			XHTMLFile f = new XHTMLFile();
			f.setRoot(new XHTMLRootResource());
			return f;
		});
		
		CoreLogger.getLogger(1).fine("Generate file ====>"+ name);;
		
		// ajoute dans le root du ficher
		((XHTMLRootResource)subfile.getRoot()).addElementOnTarget(HEADER.class, elem);
	}

	private ArrayList<XMLElement> none = new ArrayList<>();
	public List<XMLElement> getListElementFromTarget(Class<? extends XMLTarget> target) {
		ArrayList<XMLElement> list =  listPart.get(target);
		if (list!=null)
			return list;
		else
			return none;
	}

	/**************************************************************/

	/**************************************************************/
	public final void doContent(XMLPart root) {
		
		if (debug)
			System.out.println("[XMLPart]--------------- add content of ------------- " + this.getClass() );
				
		XMLFile file = XUIFactoryXHtml.getXHTMLFile();
		boolean isfirstInit = !file.isXMLPartAlreadyInFile(this);
		
		XHTMLAppScanner.initVar(false, this.getClass(), this);
		
		Method[] listMth = getXMLMethod();
		for (Method method : listMth) {
			xResource resource = method.getAnnotation(xResource.class);
			boolean isResource =resource!=null;
			String idResource = isResource? resource.id():null;
			if (idResource!=null && idResource.equals(""))
				idResource=null;
			
			// execute les methode target non ressource
			if (isfirstInit || !isResource)
			{
				addXMLOnTarget(method, idResource);
			}
		}
		
	}
	
	
	private Method[] getXMLMethod()
	{
		ArrayList<Method> alf = new ArrayList<Method>(10);
		Class<?> c = this.getClass();
		while (XMLPart.class.isAssignableFrom(c) && c!= XHTMLPart.class && c!= XMLPart.class) {
			Method[] lf = c.getDeclaredMethods();
			if (lf!=null)
			{
				for (Method field : lf) {
					alf.add(field);
				}
			}
			c=c.getSuperclass();
		}
		
		Method[] a = new Method[alf.size()];
		alf.toArray(a);
		return a;
	}
	



	/**
	 * ajoute les methode avec xTarget
	 * @param method
	 */
	private void addXMLOnTarget(Method method, String idResource) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {
				method.setAccessible(true);
				XMLElement elem = ((XMLElement) method.invoke(this, new Object[] {}));
				if (elem== null) return;
				
				initBlockPriority(method, elem);

				String comment = getComment(method);
				elem.setComment(comment!=null?comment+ " priority "+((int)(elem.getPriority())) : "["+this.getClass().getSimpleName() + "." + method.getName()+ "] priority "+((int)(elem.getPriority())) );
				
				Class<? extends XMLTarget> targetClass = target.value();
				if (debug)
					CoreLogger.getLogger(1).fine(()->"[XMLPart] add Target mth "+ this.getClass().getSimpleName() + " # " + method.getName() + " priority " + elem.getPriority());
			
				if (targetClass!=null ) {
					int nbTab = targetClass.newInstance().getInitialNbTab();
					String idModule = null;
					if (idResource!=null)
						idModule = AppConfig.getModuleJSConfig(idResource);
					
					if (ITargetRoot.class.isAssignableFrom(targetClass))
					{
						// ajoute en root ex BODY, HEADER
						if (idModule==null || XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isSinglefile())
							XUIFactoryXHtml.getXMLRoot().addElementOnTarget(targetClass, elem.getXMLElementTabbed(nbTab));
						else
						{
							XUIFactoryXHtml.getXMLRoot().addImportOnTarget(idModule, targetClass, elem.getXMLElementTabbed(0));
						}
					}
					else
					{
						// ajoute dans un block enfant CONTENT, AFTER_CONTENT
						if (idModule==null|| XUIFactoryXHtml.getXHTMLFile().getConfigMgr().isSinglefile())
							addElementOnTarget(targetClass, elem.getXMLElementTabbed(nbTab));
						else
							addImportOnTarget(idModule, targetClass, elem.getXMLElementTabbed(0));
					}
				}
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				ErrorNotificafionMgr.doError("pb addXMLOnTarget "+method.getName(), e);
			}
		}
	}


	/**
	 * @param method
	 * @param elem
	 */
	private void initBlockPriority(Method method, XMLElement elem) {
		// ajoute le hashcode du nom pour avoir toujours le même tri sur une priorite identique
		long h = method.getName().hashCode()+ this.getClass().getName().hashCode();
		double p = (double)(h)/PRECISION;
		int d = (int)p;
		p=(p-d);
		
		xPriority priority = method.getAnnotation(xPriority.class);
		if (priority != null) {
			p = priority.value()+p;
			elem.setPriority(p);
		}
		else
			elem.setPriority(100.0+p);
	}

	/**************************************************************/
	private String getComment(Method mth) {
		xComment comment = this.getClass().getAnnotation(xComment.class);
		if (comment != null) {
			String v = comment.value();
			return (v == null ? "" : v) + " [" + this.getClass().getSimpleName()+"."+ mth.getName() +"]";
		}
		return null;
	}


	/**************************************************************/
	@SuppressWarnings("unchecked")
	public final List<Object> getChildren() {
		 List<Object> ret =  (List<Object>) listProperties.get(PROP_CHILDREN) ;    
		 if (ret==null)
		 {
			 ret= new ArrayList<>();
			 listProperties.put(PROP_CHILDREN, ret);
		 }
		 return ret;
	}

	public final void vContent(XMLElement part) {
		addElementOnTarget(CONTENT.class, part);
	}

	public final void vAfterContent(XMLElement part) {
		addElementOnTarget(AFTER_CONTENT.class, part);
	}


	/**
	 * xPart( part, children) 
	 * @param part
	 * @param inner
	 * @return
	 */
	public final static XMLElement vPart(XMLPart part, Object... inner) {
		return xNode(null, XMLBuilder.createPart(part, inner));
	}
	
	public final static XMLElement xNode(String name, Object... inner) {
		XMLElement tag = XMLBuilder.createElement(name, inner);
		return tag;
	}

	@Deprecated 
	//xListNode
	public final static XMLElement xListNodeStatic(Object... array) {
		return xNode(NONAME, array);
	}

	public final static XMLAttr xAttr(String name, Object value) {
		XMLAttr attr = XMLBuilder.createAttr(name, value);
		return attr;
	}
	
	public final static XMLAttr xAttr(String name) {
		XMLAttr attr = XMLBuilder.createAttr(name, null);
		return attr;
	}
	
	/**
	 * ajout d'enfant par un .addProperty(name, xxxx) 
	 * @param name
	 * @return
	 */
	@Deprecated  	/*utiliser les vProperty*/
	public static final Handle vSearchProperty(String name) {
		Handle attr = XMLBuilder.createHandle(name);
		return attr;
	}
	
	@Deprecated /* use vIfExist(VProperty */
	public static final Handle vIfExistProperty(String iff, Object then) {
		Handle attr = XMLBuilder.createHandle(iff);
		attr.setIfExistAdd(then);
		return attr;
	}

	public static final Handle vSearch(VProperty name) {
		Handle attr = XMLBuilder.createHandle(name.getName());
		return attr;
	}
	
	public static final Handle vIfExist(VProperty iff, Object then) {
		Handle attr = XMLBuilder.createHandle(iff.getName());
		attr.setIfExistAdd(then);
		return attr;
	}
	
	public final static Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
