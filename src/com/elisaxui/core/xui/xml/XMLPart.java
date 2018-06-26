package com.elisaxui.core.xui.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elisaxui.AppConfig;
import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.notification.ErrorNotificafionMgr;
import com.elisaxui.core.xui.XUIFactory;
import com.elisaxui.core.xui.XUIFactoryXHtml;
import com.elisaxui.core.xui.config.XHTMLAppScanner;
import com.elisaxui.core.xui.xhtml.XHTMLFile;
import com.elisaxui.core.xui.xhtml.XHTMLPart;
import com.elisaxui.core.xui.xhtml.XHTMLTemplateResource;
import com.elisaxui.core.xui.xhtml.builder.javascript.lang.value.JSString;
import com.elisaxui.core.xui.xhtml.builder.module.ModuleDesc;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImport;
import com.elisaxui.core.xui.xhtml.builder.module.annotation.xImportList;
import com.elisaxui.core.xui.xhtml.target.HEADER;
import com.elisaxui.core.xui.xml.annotation.xComment;
import com.elisaxui.core.xui.xml.annotation.xPriority;
import com.elisaxui.core.xui.xml.annotation.xResource;
import com.elisaxui.core.xui.xml.annotation.xTarget;
import com.elisaxui.core.xui.xml.builder.VProperty;
import com.elisaxui.core.xui.xml.builder.XMLAttr;
import com.elisaxui.core.xui.xml.builder.XMLBuilder;
import com.elisaxui.core.xui.xml.builder.XMLBuilder.XMLHandle;
import com.elisaxui.core.xui.xml.builder.XMLElement;
import com.elisaxui.core.xui.xml.target.AFTER_CONTENT;
import com.elisaxui.core.xui.xml.target.CONTENT;
import com.elisaxui.core.xui.xml.target.FILE;
import com.elisaxui.core.xui.xml.target.XMLTarget;
import com.elisaxui.core.xui.xml.target.XMLTarget.ITargetRoot;

/**
 * un bloc xml representant une vue
 * 
 * @author Bureau
 *
 */
public class XMLPart  {

	public static final double PRECISION = 100000.0;
	public static final String NONAME = null;

	private static final boolean debug = false;
	
	/**************************************************************************/
	protected HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>> listPart = new HashMap<Class<? extends XMLTarget>, ArrayList<XMLElement>>();
	protected HashMap<Object, Object> listProperties = new HashMap<Object, Object>(); 
	
	public static final String PROP_ID = "PROP_ID";
	public static final String PROP_CHILDREN = "PROP_CHILDREN";

	
	/**************************************************************************/
	public final String getPropertiesPrefix() {
		return vProperty(PROP_ID);
	}

	@Deprecated
	/**
	 * use vProp
	 * @param key
	 * @param value
	 * @return
	 */
	public XMLPart vProperty(Object key, Object value)
	{
		listProperties.put(key.toString(), value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 *  permet d'ajouter plusieur Properties indentique
	 * @param key
	 * @param value
	 * @return
	 */
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
	
	/********************* GET ********************************/
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
	
	public final void addImportOnTarget(ModuleDesc moduleDesc, Class<? extends XMLTarget> target, XMLElement elem) {		
		XMLFile subfile =   addSubFileOnTarget(moduleDesc, target, elem.getPriority());

		if (debug)
			CoreLogger.getLogger(1).fine(elem.getComment()+" add on sub file ====>"+ subfile.getID());
		
		XHTMLTemplateResource rootSubFile= ((XHTMLTemplateResource)((XHTMLFile)subfile).getXHTMLTemplate());
		rootSubFile.addElementOnModule(moduleDesc, elem);
	}


	private XMLFile addSubFileOnTarget(ModuleDesc moduleDesc, Class<? extends XMLTarget> target, double priority) {
		final String name = moduleDesc.getURI();
		XMLFile file = XUIFactoryXHtml.getXMLFile();
				
		return file.listSubFile.computeIfAbsent(name, keyResource -> {
			if (moduleDesc.isResourceCss())
				addElementOnTarget(target, (XMLElement)(XHTMLPart.xLinkCss("/rest/css/"+name).setPriority(priority)));
			else  
			{   //CHANGE TO assert ET non rest
				if (moduleDesc.isES6Module())
					addElementOnTarget(target, (XMLElement)(XHTMLPart.xScriptModule("/rest/js/"+name).setPriority(priority)));
				else	
					addElementOnTarget(target, (XMLElement)(XHTMLPart.xScriptSrc("/rest/js/"+name).setPriority(priority)));
			}
			
			boolean isModule = FILE.class.isAssignableFrom(target) ;
			
			XHTMLFile f = new XHTMLFile();
			f.setXHTMLTemplate(new XHTMLTemplateResource().setModuleES6File(isModule));
			f.setID(moduleDesc.getResourceID());
			f.setExtension(moduleDesc.getExtension());
			return f;
		});
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
	public final void doContent() {
		
		if (debug)
			System.out.println("[XMLPart]--------------- add content of ------------- " + this.getClass() );
				
		XMLFile file = XUIFactoryXHtml.getXMLFile();
		boolean isfirstInit = !file.isXMLPartAlreadyInFile(this);
		
		XHTMLAppScanner.initVar(false, this.getClass(), this);
		
		Method[] listMth = getAllListMethod();
		for (Method method : listMth) {
			xResource resource = method.getAnnotation(xResource.class);
			boolean isResource =resource!=null;
			String idResource = isResource? resource.id():null;
			if (idResource!=null && idResource.equals(""))
				idResource=null;
			
			// execute les methode targetAction non ressource
			if (isfirstInit || !isResource)
			{
				ModuleDesc moduleDesc = new ModuleDesc();
				moduleDesc.setResourceID(idResource);
				moduleDesc.initES6mport(method.getAnnotation(xImport.class), method.getAnnotation(xImportList.class));
				
				addXMLOnTarget(method, moduleDesc);
			}
		}
		
	}

	private Method[] getAllListMethod()
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
	 */
	private void addXMLOnTarget(Method method, ModuleDesc moduleDesc) {
		xTarget target = method.getAnnotation(xTarget.class);
		if (target != null) {
			try {
				method.setAccessible(true);
				XMLElement elem = ((XMLElement) method.invoke(this, new Object[] {}));
				if (elem==null) return;
									
				initBlockPriority(method, elem);

				String comment = getComment(method);
				elem.setComment(comment!=null?comment+ " priority "+((int)(elem.getPriority())) : "["+this.getClass().getSimpleName() + "." + method.getName()+ "] priority "+((int)(elem.getPriority())) );
				
				if (debug)
					CoreLogger.getLogger(1).fine(()->"[XMLPart] add Target mth "+ this.getClass().getSimpleName() + " # " + method.getName() + " priority " + elem.getPriority());
			
				Class<? extends XMLTarget> targetClass = target.value();
				if (targetClass!=null ) {
					
					if (XUIFactoryXHtml.getXMLFile().getConfigMgr().isSinglefile() && targetClass==FILE.class)
					{	// force en single file
						targetClass= HEADER.class;
					}
					
					int nbTab = targetClass.newInstance().getInitialNbTab();

					if (moduleDesc.getResourceID()!=null)
						moduleDesc.setResourceID(AppConfig.getModuleJSConfig(moduleDesc.getResourceID()));
					
					
					if (targetClass==FILE.class)
					{
						Map<String, ModuleDesc> listeClass = XUIFactory.getXHTMLFile().getListClassModule();
						listeClass.forEach((k,v) -> { 
							if (v==null)
							{
								listeClass.put(k, moduleDesc);
								System.out.println(k + ":" + moduleDesc.getResourceID()); 
							}
						});
					}
					
					boolean isInner = (moduleDesc.getResourceID()==null || XUIFactoryXHtml.getXMLFile().getConfigMgr().isSinglefile());
					XMLPart targetPart = this;   //	ajoute dans un block enfant : CONTENT, AFTER_CONTENT
					if (ITargetRoot.class.isAssignableFrom(targetClass))
						targetPart = XUIFactoryXHtml.getXHTMLTemplateRoot();  //sinon ajoute dans un block root : HEADER
				
					if (isInner)
						targetPart.addElementOnTarget(targetClass, elem.getXMLElementTabbed(nbTab));
					else
						targetPart.addImportOnTarget(moduleDesc, targetClass, elem.getXMLElementTabbed(0));

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
	public static final XMLElement vPart(XMLPart part, Object... child) {
		return xNode(NONAME, XMLBuilder.createPart(part, child));
	}
	
	public static final XMLElement xNode(String name, Object... inner) {
		return XMLBuilder.createElement(name, inner);
	}

	@Deprecated 
	//xListNode
	public static final XMLElement xListNodeStatic(Object... array) {
		return xNode(NONAME, array);
	}

	public static final XMLAttr xAttr(String name, Object value) {
		return XMLBuilder.createAttr(name, value);
	}
	
	public static final XMLAttr xAttr(String name) {
		return XMLBuilder.createAttr(name, null);
	}
	
	/**
	 * ajout d'enfant par un .addProperty(name, xxxx) 
	 * @param name
	 * @return
	 */
	@Deprecated  	/*utiliser les vProperty*/
	public static final XMLHandle vSearchProperty(String name) {
		XMLHandle attr = XMLBuilder.createHandle(name);
		return attr;
	}
	
	@Deprecated /* use vIfExist(VProperty */
	public static final XMLHandle vIfExistProperty(String iff, Object then) {
		XMLHandle attr = XMLBuilder.createHandle(iff);
		attr.setIfExistAdd(then);
		return attr;
	}

	@Deprecated  	/*utiliser directement vProperty*/
	public static final XMLHandle vSearch(VProperty name) {
		XMLHandle attr = XMLBuilder.createHandle(name.getName());
		return attr;
	}
	
	public static final XMLHandle vIfExist(VProperty iff, Object then) {
		XMLHandle attr = XMLBuilder.createHandle(iff.getName());
		attr.setIfExistAdd(then);
		return attr;
	}
	
	public static final Object xTxt(Object text) {
		return "\"" + text + "\"";
	}
}
