/**
 * 
 */
package com.elisaxui.core.xui.xhtml.builder.javascript.lang;

import javax.json.Json;

import com.elisaxui.core.helper.log.CoreLogger;
import com.elisaxui.core.xui.xhtml.builder.css.selector.CSSSelector;
import com.elisaxui.core.xui.xhtml.builder.html.CSSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSElement;
import com.elisaxui.core.xui.xhtml.builder.javascript.JSFunction;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ArrayMethod;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyHandler;
import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.ProxyMethodDesc;
import com.elisaxui.core.xui.xhtml.builder.json.JsonNumberImpl;

/**
 * @author Bureau
 *
 */
public class JSAny implements JSElement {
	
	private static final String SEP = ",";
	private static Object NULL = null;
	
	private Object name;
	protected Object value = null;
	private ArrayMethod<Object> listContent = new ArrayMethod<>();
	
	protected Object parentLitteral;

	/**
	 * @return the parentLitteral
	 */
	public final Object zzGetParentLitteral() {
		return parentLitteral;
	}

	/***************************************************************/
	public final <E extends JSAny> E attrByString(Object attr) {
		E ret = getReturnType();

		ret.addContent("[");
		ret.addContent(addParamMth(attr));
		ret.addContent("]");
		return ret;
	}

	public <E extends JSAny> E attr(String att) {
		E ret = getReturnType();
		ret.addContent("." + att);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public final <E extends JSAny> E castAttr(JSAny cl, String att) {
		Object name = this._getName();
		
		if (name!=null)
		  cl._setValue(name + "." + att);
		else
		{
			ArrayMethod<Object> arr = new ArrayMethod<>();
			
			Object content =_getValueOrName();
			
			if (content instanceof ArrayMethod)
				arr.addAll((ArrayMethod<?>) content);
			else
				arr.add(content);
	
			arr.add("." +att);
	
			cl._setValue(arr);
		}
		
		return (E) cl;
	}
	
	/**************************************************************/
	public final JSBool equalsJS(Object obj)
	{
		JSBool ret = new JSBool();
		doOperator(ret, "==", obj);
		return ret;
	}
	
	public final JSBool isNotEqual(Object obj)
	{
		JSBool ret = new JSBool();
		doOperator(ret, "!=", obj);
		return ret;
	}
	
	public final JSAny set(Object... objs)
	{
		
		JSAny ret = declareTypeAny();
		boolean isLitteral = false;
		
		if (this.parentLitteral instanceof ProxyHandler)
		{
			ProxyHandler mh = (ProxyHandler)this.parentLitteral;
			isLitteral = mh.getJsonBuilder()!=null;
			
			if (isLitteral || ProxyHandler.isModeJava()  ) {
				if (mh.getJsonBuilder()==null)
					mh.setJsonBuilder(Json.createObjectBuilder());
				
				String attr = ""+this.name;
				attr = attr.substring(attr.lastIndexOf('.')+1);
				
				if (objs[0] instanceof String && this instanceof JSValue)
					objs[0] = "\"" + objs[0] + "\"";
				
				mh.getJsonBuilder().add(attr, new JsonNumberImpl(objs[0].toString()));
			}
		}
		else if (this.parentLitteral instanceof JSObject)
		{
			JSObject mh = (JSObject)this.parentLitteral;
			isLitteral = mh.jsonBuilder!=null;
			String attr = ""+this.name;
			
			if (objs[0] instanceof String && this instanceof JSValue)
				objs[0] = "\"" + objs[0] + "\"";
			
			mh.jsonBuilder.add(attr, new JsonNumberImpl(objs[0].toString()));
		}
		
		if (!isLitteral)
			doOperator(ret, "=", objs);
		
		return ret;
	}
	
	/***********************************************************************************/
	public final <E extends JSAny> E callMth(String mth, Object... objs)
	{
		return callTyped(null, mth, objs );
	}
		
	public final <E extends JSAny> E callTyped(E type, String mth, Object... objs)
	{
		objs = addParamMth(objs);
		
		if (type == null) {
			type = getReturnType();
		}
		
		if (ProxyHandler.isModeJava()) {
			return type;
		} else {

			ArrayMethod<Object> arr = new ArrayMethod<>();
			
			Object content =_getValueOrName();
			
			if (content instanceof ArrayMethod)
				arr.addAll((ArrayMethod<?>) content);
			else
				arr.add(content);

			arr.add("." + mth + "(");
			addContent(arr, objs);
			arr.add(")");

			type._setValue(arr);
			
			zzRegisterMethod(type);
			return type;
		}
	}
			
	
	public final <E extends JSAny> E apply(Object... classes) {
		return callMth("apply", classes);
	}
	
	/***************************************************************************************/
	@SuppressWarnings("unchecked")
	protected final <E extends JSAny> E getReturnType() {
		E ret = (E) this;
		if (this.name != null && listContent.isEmpty()) {  
			// gestion premier appel de variable pour chainage
			ret = (E) ret.declareTypeAny();
			ret._setName(this._getName());
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	protected final <E extends JSAny> E addContent(Object content) {		
		addContent(listContent, content);
		return (E) this;
	}
	
	private final void addContent(ArrayMethod<Object> inner, Object content) {
		
		if ( content instanceof Object[])
		{
			Object[] arr = (Object[])content;
			for (Object object : arr) {
				
				if (object instanceof CSSClass)
					inner.add( new JSAny()._setName( ((CSSClass)object).getId()) );
				
				else if (object instanceof CSSSelector)
					inner.add( new JSAny()._setName("'" + ((CSSSelector)object).toString()) +"'" );
				
				else if (object instanceof JSFunction)
				{
					inner.add(object);
				}
				else if ( object instanceof Object[])
				{
					addContent(inner, object);
				}
				else if ( object instanceof String && object != SEP )
					inner.add(JSString.value(object.toString()));
				else if ( object instanceof ArrayMethod)
				{
					inner.addAll((ArrayMethod<?>)object);
				}
				else
				{				
					inner.add(object);
				}
			}
		}
		else if (content!=null)
			inner.add(content);
	}
	
	/*******************************************************************************/
	protected final void doOperator(JSAny ret, String operator,  Object... objs) {
		
		if (ProxyHandler.isModeJava()) {
			
		} else {
			ArrayMethod<Object> arr = new ArrayMethod<>();
			Object content = _getValueOrName();
			if (content instanceof ArrayMethod)
				arr.addAll((ArrayMethod<?>) content);
			else
				arr.add(content);

			arr.add(operator);

			if (objs != null) {
				int i = objs.length;

				for (Object obj : objs) {

					if (i == 1 && obj instanceof String && this instanceof JSValue)
						obj = "\"" + obj + "\"";

					if (obj instanceof ArrayMethod)
						arr.addAll((ArrayMethod<?>) obj);
					else
						arr.add(obj);
				}
			} else {
				arr.add(null);
			}

			ret._setValue(arr);
			zzRegisterMethod(ret);
		}
	}
	
	protected final JSAny declareTypeAny() {
		JSAny ret =  null;
		try {
			ret = this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			CoreLogger.getLogger(1).severe("pb declareTypeAny");
			return this;
		}
		return ret;
	}	
	
	protected static final Object[] addParamMth(Object... classes) {
	
		if (classes==null)
			return null;
		
		Object[] ret = classes;
		if (ret!=null && ret.length>1)
		{
			ret = new Object[((ret.length-1)*2)+1];
		}
		
		for (int i = 0; i < classes.length; i++) {
			int j = i*2;
			
			if (classes[i] instanceof String )
				ret[j] = JSString.value(classes[i].toString());
			else if ( classes[i] instanceof CSSClass)
				ret[j] = JSString.value(((CSSClass)classes[i]).getId());
			else
				ret[j] = classes[i];
			
			if (i>0)
				ret[j-1] = SEP;
		}

		return ret;

	}
	/********************************************************************************/
	public final Object _getName() {
		return name;
	}

	public final <E extends JSAny> E _setName(Object name) {
		this.name = name;
		return (E)this;
	}
	
	/*******************************************************************************/
	public boolean isLitteral() {
		return false;
	}
	
	public final void _setParentLitteral(Object parent) {
		this.parentLitteral = parent;
	}

	/*******************************************************************************/
	public Object _getValue() { 
		return value;
	}
	
	public Object _getDirectValue() { 
		return value;
	}
	
	@Deprecated
	public final <E extends JSAny> E _setValue(Object v) {
		this.value = v;
		listContent.clear();
		return (E)this;
	}
	
	public final Object _getValueOrName() {
		
		if (this instanceof IJSClassInterface) {
			return getValueOrNameContent();
		}
		
		return getValueOrNameAny();
	}

	private Object getValueOrNameAny() {
		Object v =_getValue();
		if (v==null)
			return name==null?"":_getName();
		else
			return v;
	}

	private Object getValueOrNameContent() {
		ArrayMethod<Object> list = new ArrayMethod<>();
		Object arr = getValueOrNameAny();
		if (arr instanceof ArrayMethod)
			list.addAll((ArrayMethod<?>) arr);
		else if (arr !=null)
			list.add(arr);

		list.addAll(listContent);
		return list;
	}


	/*************************************************************/
	protected static final void zzRegisterMethod(Object obj ) {
			 ProxyMethodDesc currentMethodDesc = ProxyHandler.ThreadLocalMethodDesc.get();
			 try {
				ProxyHandler.doLastSourceLineInsered( false);
			} catch (ClassNotFoundException e1) {
				CoreLogger.getLogger(1).severe("error");
			}
			 
			 StackTraceElement[]  stack = Thread.currentThread().getStackTrace();
			
			 int numLigne = -1;
			 for (StackTraceElement stackTraceElement : stack) {
				 try {
						if (JSClass.class.isAssignableFrom(Class.forName(stackTraceElement.getClassName()))  && stackTraceElement.getLineNumber()!=-1 )
						{
							numLigne = stackTraceElement.getLineNumber();
							currentMethodDesc.lastLineNoInsered = numLigne;
							currentMethodDesc.lastMthNoInserted = obj;
							break;
						}
				} catch (ClassNotFoundException e) {
					CoreLogger.getLogger(1).severe("error");
				}
			 }
	}
	
	
	public String zzGetJSClassType() {
		return this.getClass().getSimpleName();
	}
	/*************************************************************/
	@Override
	public String toString() {
		
		if (this instanceof IJSClassInterface)
		{
			
			StringBuilder sb = new StringBuilder();
			
			Object arr = getValueOrNameAny();
			if (arr instanceof ArrayMethod)
				for (Object object : (ArrayMethod<?>)arr) {
					sb.append(object);
				}
			else
				sb.append(arr);
	
			for (Object object : listContent) {
				sb.append(object);
			}

			return sb.toString();
		}

		
		return getValueOrNameAny().toString();
	}
}
